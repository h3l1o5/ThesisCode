package ProcessMethod;

import java.util.Collections;
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

	public int start(String PD) {
	
		/*reset data*/
		for (Bidder bidder : bidders) {
			bidder.setDecision(false);
			bidder.setPriority(Calculator.calculatePriority(bidder,PD));
		}
		for (Good good : wareHouse) {
			good.resetLeft();
		}
		
		/*random ordered bidder list*/
		Collections.shuffle(bidders);

		/*run auction*/
		boolean newDecision;
		boolean done = false;
		int rounds = 0;
		while(!done) {	
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
				}
			}
			rounds ++;
		}
		
		Calculator.checkWarehouse(wareHouse);
		
		/*auction finished. calculate each bidder's CV&payment*/
		for (Bidder bidder : bidders) {
			double criticalValue = Calculator.calculateCriticalValue(bidder, bidders, wareHouse, PD);
			bidder.setCriticalValue(criticalValue);
			
			if (bidder.getDecision() == true) {
				bidder.setPayment(criticalValue);
			} else {
				bidder.setPayment(0);
			}
		}
		
		return rounds;
	}
}
