package ProcessMethod;

import java.util.List;
import java.util.Map;

import Environment.CombinatorialAuction;
import Model.Bidder;
import Model.Good;
import Utils.Calculator;

public class Game {
	private List<Bidder> bidders;
	private List<Good> wareHouse;
	
	public Game(CombinatorialAuction auction) {
		bidders = auction.getBidders();
		wareHouse = auction.getWareHouse();
	}

	public boolean start(String winnerDeterminationAlgo) {
	
		/*reset data*/
		for (Bidder bidder : bidders) {
			bidder.setDecision(false);
			bidder.setPriority(Calculator.calculatePriority(bidder,winnerDeterminationAlgo));
		}
		for (Good good : wareHouse) {
			good.resetLeft();
		}
		
		/*run auction*/
		boolean newDecision;
		boolean done = false;
		int step = 0;
		while(!done) {
			if (step > 10000) {
				return false; // time out.
			}
			done = true;
			for (Bidder bidder : bidders) {
				Map<Integer, Integer> bundle = bidder.getWholeBundle();
				newDecision = Calculator.makeNewDecision(bidder, bidders, wareHouse);
				if (bidder.getDecision() != newDecision) {
					if (newDecision == false) {
						for (Map.Entry<Integer, Integer> entry : bundle.entrySet()) {
							wareHouse.get(entry.getKey()).put(entry.getValue());
						}
					} else {
						for (Map.Entry<Integer, Integer> entry : bundle.entrySet()) {
							wareHouse.get(entry.getKey()).take(entry.getValue());
						}
					}					
					bidder.setDecision(newDecision);
					done = false;
					break;
				}
			}
			step ++ ;
		}
		
		/*auction finished. calculate each bidder's CV&payment*/
		for (Bidder bidder : bidders) {
			double criticalValue = Calculator.calculateCriticalValue(bidder, bidders, wareHouse, winnerDeterminationAlgo);
			bidder.setCriticalValue(criticalValue);
			
			if (bidder.getDecision() == true) {
				bidder.setPayment(criticalValue);
			} else {
				bidder.setPayment(0);
			}
		}
		
		return true;
	}
}
