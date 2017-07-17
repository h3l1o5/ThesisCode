package Utils;

import java.util.List;
import java.util.Map;

import Environment.CombinatorialAuction;
import Model.Bidder;
import Model.Good;

public class Calculator {
	
	public static boolean makeNewDecision(Bidder currentBidder, List<Bidder> bidders, List<Good> wareHouse) {
		Map<Integer,Integer> bundle = currentBidder.getWholeBundle();
		Map<Bidder, Integer> competitors = currentBidder.getAllCompetitor();
		for (Map.Entry<Integer, Integer> goodEntry : bundle.entrySet()) {
			int instanceCountOfThisGood = wareHouse.get(goodEntry.getKey()).getInstanceCount();
			int totalNeededOfThisGood = goodEntry.getValue();
			
			for (Map.Entry<Bidder, Integer> competitorEntry : competitors.entrySet()) {
				if (competitorEntry.getValue() > 0 && competitorEntry.getKey().getDecision() == true) {
					Bidder competitor = competitorEntry.getKey();
					if (competitor.getPriority() > currentBidder.getPriority()) {
						totalNeededOfThisGood += competitor.getBundle(goodEntry.getKey());
					}
					// same priority, compare bidder ID
					if (competitor.getPriority() == currentBidder.getPriority() && competitor.getID() < currentBidder.getID()) {
						totalNeededOfThisGood += competitor.getBundle(goodEntry.getKey());
					}
				}
			}
			
			if (totalNeededOfThisGood > instanceCountOfThisGood) {
				return false;
			}
		}
		return true;
	}
	
	
	public static double calculatePriority(Bidder bidder, String PD) {
		double result = 0;
		switch (PD) {
		case "ABPG":
			result = bidder.getBid() / Math.sqrt(bidder.getBundleInstanceCount());
			break;
		case "HABPC":
			result = bidder.getBid() / Math.sqrt(bidder.getBundleInstanceCount()*(bidder.getCompetitorCount()+1));
			break;
		case "ABPC":
			result = bidder.getBid() / Math.sqrt(bidder.getCompetitorCount()+1);
			break;
		case "ABPCG":
			int totalConflictGood = 0;
			Map<Bidder, Integer> competitors = bidder.getAllCompetitor();
			for (Map.Entry<Bidder, Integer> entry : competitors.entrySet()) {
				totalConflictGood += entry.getValue();
			}
			bidder.setTotalConflictGood(totalConflictGood);
			result = bidder.getBid() / Math.sqrt(totalConflictGood+1);
			break;
		default:
			System.err.println("Error: no such winner determination algo");
			System.exit(2);
		}
		return result;
	}
	
	
	public static double calculateCriticalValue(Bidder bidder, List<Bidder> bidders,List<Good> wareHouse, String PD) {
		Map<Bidder, Integer> competitors = bidder.getAllCompetitor();
		double temp;
		if (bidder.getDecision() == true) {
			// this bidder is winner.
			temp = Double.MIN_VALUE;
			
			// find all competitors who will become winner if this winner was not win.
			bidder.setDecision(false);
			for (Map.Entry<Bidder, Integer> entry : competitors.entrySet()) {
				if (entry.getValue() > 0 && entry.getKey().getDecision() == false) {
				Bidder targetBidder = entry.getKey();
					if (makeNewDecision(targetBidder, bidders, wareHouse) == true && targetBidder.getPriority() > temp) {
						temp = targetBidder.getPriority();
					}					
				}			}
			bidder.setDecision(true);
			if(temp == Double.MIN_VALUE) {
				return 0;
			}
		} else {
			// this bidder is loser.
			temp = Double.MAX_VALUE;
			
			// find all winner competitors that while he lose, I win.  
			for (Map.Entry<Bidder, Integer> entry : competitors.entrySet()) {
				if (entry.getValue() > 0 && entry.getKey().getDecision() == true) {
					Bidder targetBidder = entry.getKey();
					targetBidder.setDecision(false);
					if(makeNewDecision(bidder, bidders, wareHouse) == true && targetBidder.getPriority() < temp) {
						temp = targetBidder.getPriority();
					}
					targetBidder.setDecision(true);
				}
			}

		}
		
		switch (PD) {
		case "ABPG":
			temp = temp * Math.sqrt(bidder.getBundleInstanceCount());
			break;
		case "HABPC":
			temp = temp * Math.sqrt(bidder.getBundleInstanceCount()*(bidder.getCompetitorCount()+1));
			break;
		case "ABPC":
			temp = temp * Math.sqrt(bidder.getCompetitorCount()+1);
			break;
		case "ABPCG":
			temp = temp * Math.sqrt(bidder.getTotalConflictGood()+1);
			break;
		default:
			System.err.println("Error: no such winner determination algo");
			System.exit(2);;
		}
		
		return temp;
	}
	
	public static void checkWarehouse(List<Good> wareHouse) {
		List<Good> goods = wareHouse;
		for (Good good : goods) {
			if (good.getLeft() < 0) {
				System.err.println("Some good's instances is less than 0");
				break;
			}
		}
	}
}
