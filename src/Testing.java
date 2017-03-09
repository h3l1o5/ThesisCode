
import java.util.List;


import Environment.CombinatorialAuctionTemp;
import Model.BidderTemp;
import ProcessMethod.Game;

public class Testing {
	private static final int TOTAL_BIDDER = 5;
	private static final int TOTAL_GOOD = 100;
	private static final double CHANCE = 5;
	private static final int AMOUNT_OF_GOOD = 10;
	private static final int TOTAL_ITERATION = 10000;
	
	public static void main(String[] args) {
		double revenue_ours = 0;
		double revenue_LOS02 = 0;
		int winner_ours = 0;
		int winner_LOS02 = 0;
		int sell_ours = 0;
		int sell_LOS02 = 0;
		List<BidderTemp> bidders;
		
		long s = System.currentTimeMillis();
		for (int i = 0 ; i < TOTAL_ITERATION ; i ++) {
			CombinatorialAuctionTemp auction = new CombinatorialAuctionTemp(TOTAL_BIDDER, TOTAL_GOOD, CHANCE, AMOUNT_OF_GOOD);
			Game game = new Game(auction);
			game.start("ours");
			bidders = auction.getBidders();	
			for (BidderTemp bidder : bidders) {
				revenue_ours += bidder.getPayment();
				if (bidder.getDecision()) {
					winner_ours ++;
					sell_ours += bidder.getBundleInstanceCount();
				}
			}
			game.start("LOS02");
			bidders = auction.getBidders();
			for (BidderTemp bidder : bidders) {
				revenue_LOS02 += bidder.getPayment();
				if (bidder.getDecision()) {
					winner_LOS02 ++;
					sell_LOS02 += bidder.getBundleInstanceCount();
				}
			}
			System.out.println("iteration " + i + "...");
		}
		
		long e = System.currentTimeMillis();
		System.out.println("time: " + (e - s));
		
		System.out.println("-----ours-----");
		System.out.println("Revenue: " + revenue_ours / TOTAL_ITERATION);
		System.out.println("Sell: " + (double)sell_ours / TOTAL_ITERATION);
		System.out.println("winner: " + (double)winner_ours / TOTAL_ITERATION);
		System.out.println("-----LOS02-----");
		System.out.println("Revenue: " + revenue_LOS02 / TOTAL_ITERATION);
		System.out.println("Sell: " + (double)sell_LOS02 / TOTAL_ITERATION);
		System.out.println("winner: " + (double)winner_LOS02 / TOTAL_ITERATION);
	}
}
