package Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import Model.Bidder;
import Model.Good;

public class CombinatorialAuction {
	Random ran = new Random();
	
	private int numberOfBidders;
	private int numberOfGoodTypes;
	private int probability; // chance of whether a bidder desire a good.
	private int maxAvailableUnit;
	private List<Good> wareHouse;
	private List<Bidder> bidders;
	
	public CombinatorialAuction(int m, int n, int p, int lambda, boolean SA) {
		this.numberOfBidders = m;
		this.numberOfGoodTypes = n;
		this.probability = p;
		this.maxAvailableUnit = lambda;
		wareHouse = new ArrayList<>();
		bidders = new ArrayList<>();
		createBidder();
		createGood();
		letBidderChooseGood();
		letBidderDecideTheirBid(SA);
		letBidderKnowTheirCompetitors();
	}
	
	/*getters and setters*/
	public int getAmountOfBidder() {
		return numberOfBidders;
	}

	public int getAmountOfGoodType() {
		return numberOfGoodTypes;
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
		int fullCompetition = numberOfBidders*(numberOfBidders-1)/2;
		int realCompetition = 0;
		for (Bidder bidder : bidders) {
			Map<Bidder, Integer> competitors = bidder.getAllCompetitor();
			for (Map.Entry<Bidder, Integer> entry : competitors.entrySet()) {
				if (entry.getValue() > 0) {
					realCompetition ++ ;
				}
			}
		}
		realCompetition = realCompetition/2;
		
		return (double)realCompetition/fullCompetition*100;
	}
	
	/*initializers*/
	private void createBidder() {
		for (int i=0;i<numberOfBidders;i++) {
			Bidder bidder = new Bidder(i);
			bidders.add(bidder);
		}
	}
	
	private void createGood() {
		for (int i=0;i<numberOfGoodTypes;i++) {
			Good good = new Good(i, maxAvailableUnit);
			wareHouse.add(good);
		}
	}
	
	private void letBidderChooseGood() {
		for (Bidder bidder : bidders) {
			boolean emptyBundle = true;
			while (emptyBundle) {
				for (Good good : wareHouse) {
					if (ran.nextInt(1000) < probability*10) { // want this one
						int instanceToTake = ran.nextInt(good.getInstanceCount())+1;
						bidder.setBundle(good.getType(), instanceToTake);
					} else {
						bidder.setBundle(good.getType(), 0);
					}
				}
				if (bidder.getBundleInstanceCount() != 0) {
					emptyBundle = false;
				}
			}
		}
	}
	
	private void letBidderDecideTheirBid(boolean SA) {
		for (Bidder bidder : bidders) {
			double bid;
			
			if (SA) {
				bid = 0;
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
					
						valuationBidder = valuationBidder*entry.getValue(); 
						bid += valuationBidder;
					}
				}
			} else {
				bid = ran.nextDouble()*300+200;
			}
			
			bidder.setBid(bid);
		}
	}
	
	private void letBidderKnowTheirCompetitors() {
		for (int i=0; i<numberOfBidders; i++) {
			Bidder bidder = bidders.get(i);
			Map<Integer, Integer> bundle = bidder.getWholeBundle();
			
			for (int j=i+1; j<numberOfBidders; j++) {		
				Bidder anotherBidder = bidders.get(j);
				Map<Integer, Integer> anotherBundle = anotherBidder.getWholeBundle();
				int numberOfConflict = 0;
				
				for (int k=0; k<numberOfGoodTypes; k++) {
					numberOfConflict += bundle.get(k)*anotherBundle.get(k);
				}
				
				bidder.setCompetitor(anotherBidder, numberOfConflict);
				anotherBidder.setCompetitor(bidder, numberOfConflict);
			}
		}
	}
}
