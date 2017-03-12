
import java.util.List;


import Environment.CombinatorialAuction;
import Model.Bidder;
import ProcessMethod.Game;

public class Main {
	private static final int TOTAL_BIDDER = 100;
	private static final int TOTAL_GOOD = 100;
	private static final double CHANCE = 8;
	private static final int AMOUNT_OF_GOOD = 5;
	private static final int TOTAL_ITERATION = 500;
	
	public static void main(String[] args) {
		int timeOutCount = 0;
		double competitiveIntensity = 0;
		double revenue_ours = 0;
		double revenue_LOS02 = 0;
		int winner_ours = 0;
		int winner_LOS02 = 0;
		int sell_ours = 0;
		int sell_LOS02 = 0;
		List<Bidder> bidders;
		
		long s = System.currentTimeMillis();
		for (int i = 0 ; i < TOTAL_ITERATION ; i ++) {
			System.out.print("iteration " + i + "... ");
			CombinatorialAuction auction = new CombinatorialAuction(TOTAL_BIDDER, TOTAL_GOOD, CHANCE, AMOUNT_OF_GOOD);
			Game game = new Game(auction);
			competitiveIntensity += auction.getCompetitiveIntensity();
						
			if (game.start("ours")) {
				bidders = auction.getBidders();	
				for (Bidder bidder : bidders) {
					revenue_ours += bidder.getPayment();
					if (bidder.getDecision()) {
						winner_ours ++;
						sell_ours += bidder.getBundleInstanceCount();
					}
				}
			} else {
				System.out.println("time out");
				timeOutCount ++ ;
				continue;
			}
			
			if (game.start("LOS02")) {
				bidders = auction.getBidders();
				for (Bidder bidder : bidders) {
					revenue_LOS02 += bidder.getPayment();
					if (bidder.getDecision()) {
						winner_LOS02 ++;
						sell_LOS02 += bidder.getBundleInstanceCount();
					}
				}
			} else {
				System.out.println("time out");
				timeOutCount ++ ;
				continue;
			}
			System.out.println();
		}		
		long e = System.currentTimeMillis();
		
		System.out.println();
		System.out.println();
		System.out.println("Total running time: " + (e - s)/1000 + "s");		
		System.out.println("Average competitive intensity: " + competitiveIntensity / (TOTAL_ITERATION-timeOutCount) + "%");
		System.out.println("-----ours-----");
		System.out.println("Revenue: " + revenue_ours / (TOTAL_ITERATION-timeOutCount));
		System.out.println("Sell: " + (double)sell_ours / (TOTAL_ITERATION-timeOutCount));
		System.out.println("winner: " + (double)winner_ours / (TOTAL_ITERATION-timeOutCount));
		System.out.println("-----LOS02-----");
		System.out.println("Revenue: " + revenue_LOS02 / (TOTAL_ITERATION-timeOutCount));
		System.out.println("Sell: " + (double)sell_LOS02 / (TOTAL_ITERATION-timeOutCount));
		System.out.println("winner: " + (double)winner_LOS02 / (TOTAL_ITERATION-timeOutCount));
	}
}
