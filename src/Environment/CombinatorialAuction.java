package Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import Model.Bidder;
import Model.Good;

public class CombinatorialAuction {
	Random ran = new Random();
	
	private int amountOfBidder;
	private int amountOfGoodType;
	private double chance; // chance of whether a bidder want a good.
	private int instanceRangeOfGood;
	private List<Good> wareHouse;
	private List<Bidder> bidders;
	
	public CombinatorialAuction(int amountOfBidder, int amountOfGoodType, double chance, int instanceRangeOfGood) {
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
	
	/*getters and setters*/
	public int getAmountOfBidder() {
		return amountOfBidder;
	}

	public int getAmountOfGoodType() {
		return amountOfGoodType;
	}

	public List<Good> getWareHouse() {
		return wareHouse;
	}
	
	public List<Bidder> getBidders() {
		return bidders;
	}

	public void setBidders(List<Bidder> bidders) {
		this.bidders = bidders;
	}
	
	public double getCompetitiveIntensity() {
		int fullCompetition = amountOfBidder*(amountOfBidder-1)/2;
		int realCompetition = 0;
		for (Bidder bidder : bidders) {
			Map<Integer, Boolean> competitors = bidder.getAllCompetitor();
			for (Map.Entry<Integer, Boolean> entry : competitors.entrySet()) {
				if (entry.getValue() == true) {
					realCompetition ++ ;
				}
			}
		}
		realCompetition = realCompetition/2;
		
		return (double)realCompetition/fullCompetition*100;
	}
	
	/*initializers*/
	private void createBidder() {
		for (int i=0;i<amountOfBidder;i++) {
			Bidder bidder = new Bidder(i);
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
		for (Bidder bidder : bidders) {
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
		for (Bidder bidder : bidders) {
			double bid = 0;
			Map<Integer, Integer> bundle = bidder.getWholeBundle();
			for (Map.Entry<Integer, Integer> entry : bundle.entrySet()) {
				if (entry.getValue() > 0) {
					double valuationOriginal = wareHouse.get(entry.getKey()).getValuation();					
					double valuationBidder = valuationOriginal;

					// every bidder has his own valuation of each type of good
					// but the difference won't be too huge 
					// so we model it as a normal distribution
					// mean=original valuation, standard deviation=(original valuation)/5
					do {
						valuationBidder = ran.nextGaussian() * (valuationOriginal / 5) + valuationOriginal;
					} while (valuationBidder <= 0); // drop 0 or negative value
					
					valuationBidder = valuationBidder*entry.getValue()*(1 + (entry.getValue()-1)*0.02); // want more, bid more.
					
					bid += valuationBidder;
				}
			}
			bidder.setBid(bid);
		}
	}
	
	private void letBidderKnowTheirCompetitors() {
		for (int i=0; i<amountOfBidder; i++) {
			Bidder bidder = bidders.get(i);
			Map<Integer, Integer> bundle = bidder.getWholeBundle();
			for (int j=i+1; j<amountOfBidder; j++) {
				Bidder anotherBidder = bidders.get(j);
				Map<Integer, Integer> anotherBundle = anotherBidder.getWholeBundle();
				for (int k=0; k<amountOfGoodType; k++) {
					if (bundle.containsKey(k) == true && anotherBundle.containsKey(k) == true) {
						bidder.setCompetitor(anotherBidder.getID(), true);
						anotherBidder.setCompetitor(bidder.getID(), true);
						break;
					}
				}
			}
		}
	}
}
