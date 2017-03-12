package Utils;

import java.util.List;
import java.util.Map;

import Model.Bidder;
import Model.Good;

public class Calculator {
	
	public static boolean makeNewDecision(Bidder currentBidder, List<Bidder> bidders, List<Good> wareHouse) {
		Map<Integer,Integer> bundle = currentBidder.getWholeBundle();
		Map<Integer, Boolean> competitors = currentBidder.getAllCompetitor();
		for (Map.Entry<Integer, Integer> goodEntry : bundle.entrySet()) {
			int instanceCountOfThisGood = wareHouse.get(goodEntry.getKey()).getInstanceCount();
			int totalNeededOfThisGood = goodEntry.getValue();
			
			for (Map.Entry<Integer, Boolean> competitorEntry : competitors.entrySet()) {
				if (bidders.get(competitorEntry.getKey()).getDecision() == true) {
					Bidder competitor = bidders.get(competitorEntry.getKey());
					if (competitor.getPriority() > currentBidder.getPriority()) {
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
	
	
	public static double calculatePriority(Bidder bidder, String winnerDeterminationAlgo) {
		double result = 0;
		switch (winnerDeterminationAlgo) {
		case "LOS02":
			result = bidder.getBid() / Math.sqrt(bidder.getBundleInstanceCount());
			break;
		case "ours":
			result = bidder.getBid() / Math.sqrt(bidder.getBundleInstanceCount()*(bidder.getCompetitorCount()+1));
			break;
		default:
			System.err.println("Error: no such winner determination algo");
			System.exit(2);
		}
		return result;
	}
	
	
	public static double calculateCriticalValue(Bidder bidder, List<Bidder> bidders,List<Good> wareHouse, String winnerDeterminationAlgo) {
		Map<Integer, Boolean> competitors = bidder.getAllCompetitor();
		double temp;
		if (bidder.getDecision() == true) {
			// this bidder is winner.
			temp = Double.MIN_VALUE;
			
			// find all competitors who will become winner if this winner was not win.
			bidder.setDecision(false);
			for (Map.Entry<Integer, Boolean> entry : competitors.entrySet()) {
				Bidder targetBidder = bidders.get(entry.getKey());
				if (targetBidder.getDecision() == false){
					if (makeNewDecision(targetBidder, bidders, wareHouse) == true && targetBidder.getPriority() > temp) {
						temp = targetBidder.getPriority();
					}					
				}
			}
			bidder.setDecision(true);
			if(temp == Double.MIN_VALUE) {
				return 0;
			}
		} else {
			// this bidder is loser.
			temp = Double.MAX_VALUE;
			
			// find all winner competitors that while he lose, I win.  
			for (Map.Entry<Integer, Boolean> entry : competitors.entrySet()) {
				Bidder targetBidder = bidders.get(entry.getKey());
				if (targetBidder.getDecision() == true) {
					targetBidder.setDecision(false);
					if(makeNewDecision(bidder, bidders, wareHouse) == true && targetBidder.getPriority() < temp) {
						temp = targetBidder.getPriority();
					}
					targetBidder.setDecision(true);
				}
			}

		}
		
		switch (winnerDeterminationAlgo) {
		case "LOS02":
			temp = temp * Math.sqrt(bidder.getBundleInstanceCount());
			break;
		case "ours":
			temp = temp * Math.sqrt(bidder.getBundleInstanceCount()*(bidder.getCompetitorCount()+1));
			break;
		default:
			System.err.println("Error: no such winner determination algo");
			System.exit(2);;
		}
		
		return temp;
	}
}
