package ProcessMethod;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import Environment.CombinatorialAuction;
import Model.Bidder;
import Model.Good;
import Utils.Calculator;

public class Centralize {
	private List<Bidder> bidders;
	private List<Good> wareHouse;
	
	public Centralize(CombinatorialAuction auction) {
		bidders = auction.getBidders();
		wareHouse = auction.getWareHouse();
	}
	
	public boolean start(String PD) {
		/*reset data*/
		for (Bidder bidder : bidders) {
			bidder.setDecision(false);
			bidder.setPriority(Calculator.calculatePriority(bidder,PD));
//			bidder.setPriority(500);
		}
		for (Good good : wareHouse) {
			good.resetLeft();
		}
		
		// sort bidders by their priority
		Collections.sort(bidders, new Comparator<Bidder>() {
			public int compare(Bidder bidder1, Bidder bidder2) {
				double result = bidder2.getPriority() - bidder1.getPriority();
				if (result < 0) {
					return -1;
				} else if (result > 0) {
					return 1;
				} else {
					// same priority, compare bidder ID (i,e, ID=1 > ID=2) 
					return (bidder1.getID() - bidder2.getID());
				}
			}
		});

		// winner determination
		for (Bidder bidder : bidders) {
			Map<Integer, Integer> bundle = bidder.getWholeBundle();
			boolean enoughGood = true;
			for (Map.Entry<Integer, Integer> entry : bundle.entrySet()) {
				if (entry.getValue() > wareHouse.get(entry.getKey()).getLeft()) {
					enoughGood = false;
					break;
				}
			}
			
			if (enoughGood) {
				bidder.setDecision(true);
				for (Map.Entry<Integer, Integer> entry : bundle.entrySet()) {
					wareHouse.get(entry.getKey()).take(entry.getValue());
				}
			}
		}
		
		Calculator.checkWarehouse(wareHouse);
		
		// payment determination
		for (Bidder bidder : bidders) {
			bidder.setCriticalValue(Calculator.calculateCriticalValue(bidder, bidders, wareHouse, PD));
			if (bidder.getDecision()) {
				bidder.setPayment(bidder.getCriticalValue());
			} else {
				bidder.setPayment(0);
			}
		}
		
		return true;
	}
}
