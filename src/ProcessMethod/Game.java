package ProcessMethod;

import java.util.List;
import java.util.Map;

import Environment.CombinatorialAuctionTemp;
import Model.BidderTemp;
import Model.Good;
import Utils.Calculator;

public class Game {
	CombinatorialAuctionTemp auction;
	private List<BidderTemp> bidders;
	private List<Good> wareHouse;
	
	public Game(CombinatorialAuctionTemp auction) {
		this.auction = auction;
		bidders = auction.getBidders();
		wareHouse = auction.getWareHouse();
	}

	public void start(String winnerDeterminationAlgo) {
	
		/*reset data*/
		for (BidderTemp bidder : bidders) {
			bidder.setDecision(false);
			bidder.setPriority(Calculator.calculatePriority(bidder,winnerDeterminationAlgo));
		}
		for (Good good : wareHouse) {
			good.resetLeft();
		}
		
		boolean newDecision;
		boolean done = false;

		while(!done) {
			done = true;
			for (BidderTemp bidder : bidders) {
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
		}
		
		for (BidderTemp bidder : bidders) {
			double criticalValue = Calculator.calculateCriticalValue(bidder, bidders, wareHouse, winnerDeterminationAlgo);
			bidder.setCriticalValue(criticalValue);
			
			if (bidder.getDecision() == true) {
				bidder.setPayment(criticalValue);
			} else {
				bidder.setPayment(0);
			}
		}
		
		auction.setBidders(bidders);
	}
}
