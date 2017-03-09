import java.util.List;

import Environment.CombinatorialAuction;
import Model.Bidder;

import java.io.IOException;

public class Main {
	private static final int TOTAL_PLAYER = 5;
	private static final int TOTAL_GOODS = 100;
	private static final int TOTAL_FILES = 10000;
	private static final int CHANCE = 5;
	private static final int WEIGHT_BASE_OF_GOODS = 10;
	private static final int WEIGHT_RANGE_OF_GOODS = 20;
	private static final int AMOUNT_RANGE_OF_EACH_GOOD = 10;

	public static void main(String[] args) throws IOException {
		
		// test
//		Test test = new Test();
//		test.startSingleUnitTest();
//		System.out.println("-----------------------------------------------------------------------------");
//		test.startMultiUnitTest();
//		System.out.println("-----------------------------------------------------------------------------");


		double Result_Of_Ours_Game = 0;
		double Result_Of_LOS02_Game = 0;
		double sell_Of_Ours_Game = 0;
		double sell_Of_LOS02_Game = 0;
		double winner_Of_Ours_Game = 0;
		double winner_Of_LOS02_Game = 0;
		int good_amount = 0;
		double neighborOfWinner_Ours = 0;
		double neighborOfWinner_LOS02 = 0;
		double density_Of_Graph = 0;
		
		long s = System.currentTimeMillis();
		for (int file = 1; file <= TOTAL_FILES; file++) {
	
			double totalPaymentDegree;
			double totalPaymentGoods;
			int neighborCount;
			int winner;
			List<Bidder> bidders;
			int[] goodStore;
			CombinatorialAuction auctionGame = new CombinatorialAuction(TOTAL_PLAYER, TOTAL_GOODS, WEIGHT_BASE_OF_GOODS, WEIGHT_RANGE_OF_GOODS, CHANCE, AMOUNT_RANGE_OF_EACH_GOOD);
			goodStore = auctionGame.getGoodStoreOriginal();
			density_Of_Graph += auctionGame.getDensityOfGraph();
			for(int i=0;i<TOTAL_GOODS;i++){
				good_amount += goodStore[i];
			}

// Our heuristic and game method
			totalPaymentDegree = 0;
			auctionGame.start("ours", "game");
			bidders = auctionGame.getBidders();

			// print benefit
			totalPaymentDegree = 0;
			for (Bidder bidder : bidders) {
				if (bidder.getChoice() == 1) {
					totalPaymentDegree += bidder.getPayment();
				}
			}
			Result_Of_Ours_Game += totalPaymentDegree;
			
			// print winner's neighbor count
			neighborCount = 0;
			winner = 0;
			for(Bidder bidder:bidders){
				if(bidder.getChoice()==1){
					winner ++ ;
					int[] neighbors = bidder.getNeighbors();
					for(int n=0;n<TOTAL_PLAYER;n++){
						if(neighbors[n]==1)
							neighborCount ++;
					}
				}
			}
			neighborOfWinner_Ours += neighborCount / winner;
			
			
			// total sell
			for(int i=0;i<TOTAL_PLAYER;i++){
				if(bidders.get(i).getChoice() == 1) sell_Of_Ours_Game += bidders.get(i).getBundleCount();
			}
			
			// total winner
			for(int i=0;i<TOTAL_PLAYER; i++){
				if(bidders.get(i).getChoice() == 1) winner_Of_Ours_Game += 1;
			}


// LOS02 heuristic and game method
			totalPaymentGoods = 0;
			auctionGame.start("LOS02", "game");
			for (Bidder bidder : bidders) {
				if (bidder.getChoice() == 1) {
					totalPaymentGoods += bidder.getPayment();
				}
			}
			Result_Of_LOS02_Game += totalPaymentGoods;
			
			// print winner's neighbor count
			neighborCount = 0;
			winner = 0;
			for(Bidder bidder:bidders){
				if(bidder.getChoice()==1){
					winner ++ ;
					int[] neighbors = bidder.getNeighbors();
					for(int n=0;n<TOTAL_PLAYER;n++){
						if(neighbors[n]==1)
							neighborCount ++;
					}
				}
			}
			neighborOfWinner_LOS02 += neighborCount / winner;
			
			// print total sell
			for(int i=0;i<TOTAL_PLAYER;i++){
				if(bidders.get(i).getChoice() == 1) sell_Of_LOS02_Game += bidders.get(i).getBundleCount();
			}
			
			// total winner
			for(int i=0;i<TOTAL_PLAYER; i++){
				if(bidders.get(i).getChoice() == 1) winner_Of_LOS02_Game += 1;
			}
			System.out.println("iteration: " + file + "...");
		}
		long e = System.currentTimeMillis();
		System.out.println("time: " + (e-s));

		
		System.out.println("GAME SETTING : ");
		System.out.println("bidders = " + TOTAL_PLAYER);
		System.out.println("chance = " + CHANCE + "%");
		System.out.println("good species = " + TOTAL_GOODS);
		System.out.println("amount range of each good = " + AMOUNT_RANGE_OF_EACH_GOOD);
		System.out.println("total goods = " + good_amount / TOTAL_FILES);
		System.out.println("weight base of each good species = " + WEIGHT_BASE_OF_GOODS);
		System.out.println("weight range of each good species = " + WEIGHT_RANGE_OF_GOODS);
		System.out.println("density = " + (density_Of_Graph/TOTAL_FILES)*100 + "%");
		System.out.println();
		System.out.println("-----ours_game-----");
		System.out.println("benifit: " + Result_Of_Ours_Game / TOTAL_FILES);
		System.out.println("sell: " + sell_Of_Ours_Game / TOTAL_FILES);
		System.out.println("winner: " + winner_Of_Ours_Game / TOTAL_FILES);
		System.out.println("neighborCountOfWinners: " + neighborOfWinner_Ours / TOTAL_FILES);
		System.out.println("-----LOS02_game-----");
		System.out.println("benifit: " + Result_Of_LOS02_Game / TOTAL_FILES);
		System.out.println("sell: " + sell_Of_LOS02_Game / TOTAL_FILES);
		System.out.println("winner: " + winner_Of_LOS02_Game / TOTAL_FILES);
		System.out.println("neighborCountOfWinners: " + neighborOfWinner_LOS02 / TOTAL_FILES );
	}
}
