package Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import Model.BidderTemp;
import Model.Good;

public class CombinatorialAuctionTemp {
	Random ran = new Random();
	
	private int amountOfBidder;
	private int amountOfGoodType;
	private double chance; // chance of whether a bidder want a good.
	private int instanceRangeOfGood;
	private List<Good> wareHouse;
	private List<BidderTemp> bidders;
	
	public CombinatorialAuctionTemp(int amountOfBidder, int amountOfGoodType, double chance, int instanceRangeOfGood) {
		this.amountOfBidder = amountOfBidder;
		this.amountOfGoodType = amountOfGoodType;
		this.chance = chance;
		this.instanceRangeOfGood = instanceRangeOfGood;
		wareHouse = new ArrayList<>();
		bidders = new ArrayList<>();
		createBidder();
		createGood();
		letBidderChooseGood();
		letBidderDecideTheirBid();
		letBidderKnowTheirCompetitors();
	}
	
	public void start(String processMethod, String winnerDeterminationAlgo) {
		for (Good good : wareHouse) {
			good.resetLeft();
		}		
		for (BidderTemp bidder : bidders) {
			bidder.setDecision(false);
		}
		
		switch (processMethod) {
		case "game":
			processInGameMethod();
			break;
		case "centralize":
			processInCentralizeMethod();
			break;
		default:
			System.out.println("Error: no such process method");
			System.exit(2);
		}
	}
	
	public List<BidderTemp> getBidders() {
		return bidders;
	}

	/*initializers*/
	private void createBidder() {
		for (int i=0;i<amountOfBidder;i++) {
			BidderTemp bidder = new BidderTemp(i);
			bidders.add(bidder);
		}
	}
	
	private void createGood() {
		for (int i=0;i<amountOfGoodType;i++) {
			Good good = new Good(i, instanceRangeOfGood);
			wareHouse.add(good);
		}
	}
	
	private void letBidderChooseGood() {
		for (BidderTemp bidder : bidders) {
			boolean emptyBundle = true;
			while (emptyBundle) {
				for (Good good : wareHouse) {
					if (ran.nextInt(1000) < chance*10) { // want this one
						int instanceToTake = ran.nextInt(good.getInstanceCount())+1;
						bidder.setBundle(good.getType(), instanceToTake);
					}
				}
				if (bidder.getBundleInstanceCount() != 0) {
					emptyBundle = false;
				}
			}
		}
	}
	
	private void letBidderDecideTheirBid() {
		for (BidderTemp bidder : bidders) {
			double bid = 0;
			Map<Integer, Integer> bundle = bidder.getWholeBundle();
			for (Map.Entry<Integer, Integer> entry : bundle.entrySet()) {
				if (entry.getValue() > 0) {
					double valuation = wareHouse.get(entry.getKey()).getValuation();
					
					do {
						valuation = ran.nextGaussian() * (valuation / 5) + valuation; // normal distribution.
					} while (valuation <= 0);
					
					valuation = valuation*entry.getValue()*(1 + (entry.getValue()-1)*0.02); // want more, bid more.
					
					bid += valuation;
				}
			}
			bidder.setBid(bid);
		}
	}
	
	private void letBidderKnowTheirCompetitors() {
		for (int i=0; i<amountOfBidder; i++) {
			BidderTemp bidder = bidders.get(i);
			for (int j=i+1; j<amountOfBidder; j++) {
				BidderTemp anotherBidder = bidders.get(j);
				for (int k=0; k<amountOfGoodType; k++) {
					if (bidder.getBundle(k)>0 && anotherBidder.getBundle(k)>0) {
						bidder.setCompetitor(anotherBidder.getID(), true);
						anotherBidder.setCompetitor(bidder.getID(), true);
						break;
					}
				}
			}
		}
	}
	
	/*process methods*/
	private void processInGameMethod() {
		
	}
	
	private void processInCentralizeMethod() {
		
	}
	
	/*calculators*/
	
	
	/*checkers*/
}
