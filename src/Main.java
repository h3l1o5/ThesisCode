import java.util.List;

import Games.CombinatorialAuction;
import Games.FindMaximalIndepentSet;
import Model.Bidder;
import Model.Player;
import java.io.IOException;

public class Main {
	private static final String FIND_MAXIMAL_INDEPENDENT_SET = "find maximal independent set";
	private static final String SYNC = "synchronize";
	private static final String ASYNC = "asynchronize";
	private static final String GWMIN = "gwmin";
	private static final String GWMIN2 = "gwmin2";
	private static final int TOTAL_PLAYER = 30;
	private static final int TOTAL_GOODS = 300;
	private static final int TOTAL_FILES = 2000;
	private static final int CHANCE = 5;
	private static final int WEIGHT_BASE_OF_GOODS = 1000;
	private static final int WEIGHT_RANGE_OF_GOODS = 1000;
	private static final int AMOUNT_RANGE_OF_EACH_GOOD = 2;

	public static void main(String[] args) throws IOException {

		int Result_Of_Ours_Game = 0;
		int Result_Of_LOS02_Game = 0;
		int sell_Of_Ours_Game = 0;
		int sell_Of_LOS02_Game = 0;
		int winner_Of_Ours_Game = 0;
		int winner_Of_LOS02_Game = 0;
		int good_amount = 0;

		for (int file = 1; file <= TOTAL_FILES; file++) {
			double totalPaymentDegree;
			double totalPaymentGoods;
			List<Bidder> bidders;
			int[] goodStore;
			CombinatorialAuction auctionGame = new CombinatorialAuction(TOTAL_PLAYER, TOTAL_GOODS, WEIGHT_BASE_OF_GOODS, WEIGHT_RANGE_OF_GOODS, CHANCE, AMOUNT_RANGE_OF_EACH_GOOD);
			goodStore = auctionGame.getGoodStoreOriginal();
			for(int i=0;i<TOTAL_GOODS;i++){
				good_amount += goodStore[i];
			}
			
// Our heuristic and game method
			totalPaymentDegree = 0;
			auctionGame.start("ours", "game");
			bidders = auctionGame.getBidders();

			totalPaymentDegree = 0;
			for (Bidder bidder : bidders) {
				if (bidder.getChoice() == 1) {
					totalPaymentDegree += bidder.getPaymentByDegree();
				}
			}
			Result_Of_Ours_Game += totalPaymentDegree;
			
			// print winner
//			System.out.print("winner: ");
//			for(Bidder bidder:bidders){
//				System.out.print(bidder.getChoice() + " ");
//			}
//			System.out.println();
//			
//			for(int i=0;i<TOTAL_PLAYER;i++) {
//				if(bidders.get(i).getChoice()==1){
//					System.out.print("bidder"+i+": ");
//					for(int j=0;j<TOTAL_GOODS;j++){
//						if(bidders.get(i).getBundle()[j]==1) System.out.print(j+",");
//					}
//					System.out.println();
//				}
//			}
			
			// print each winner's payment
//			System.out.println("payment: ");
//			for(Bidder bidder:bidders) {
//				if(bidder.getChoice() == 1)	System.out.print((int)bidder.getPaymentByDegree() + " ");
//			}
//			System.out.println();
			
			// total sell
			for(int i=0;i<TOTAL_PLAYER;i++){
				if(bidders.get(i).getChoice() == 1) sell_Of_Ours_Game += auctionGame.bundleCountsOfEachBidder[i];
			}
			
			// total winner
			for(int i=0;i<TOTAL_PLAYER; i++){
				if(bidders.get(i).getChoice() == 1) winner_Of_Ours_Game += 1;
			}
			
			// print good store
//			goodStore = auctionGame.getGoodStore();
//			System.out.print("good store: ");
//			for(int i=0;i<TOTAL_GOODS;i++){
//				System.out.print(goodStore[i]+" ");
//			}
//			System.out.println();	

// LOS02 heuristic and game method
			totalPaymentGoods = 0;
			auctionGame.start("LOS02", "game");
			bidders = auctionGame.getBidders();
			for (Bidder bidder : bidders) {
				if (bidder.getChoice() == 1) {
					totalPaymentGoods += bidder.getPaymentByGoods();
				}
			}
			Result_Of_LOS02_Game += totalPaymentGoods;
			
			// print winner
//			System.out.print("winner: ");
//			for(Bidder bidder:bidders){
//				System.out.print(bidder.getChoice() + " ");
//			}
//			System.out.println();
//			
//			for(int i=0;i<TOTAL_PLAYER;i++) {
//				if(bidders.get(i).getChoice()==1){
//					System.out.print("bidder"+i+": ");
//					for(int j=0;j<TOTAL_GOODS;j++){
//						if(bidders.get(i).getBundle()[j]==1) System.out.print(j+",");
//					}
//					System.out.println();
//				}
//			}
			
			// print each winner's payment
//			System.out.println("payment: ");
//			for(Bidder bidder:bidders) {
//				if(bidder.getChoice()==1) System.out.print((int)bidder.getPaymentByGoods() + " ");
//			}
//			System.out.println();
			
			// print total sell
			for(int i=0;i<TOTAL_PLAYER;i++){
				if(bidders.get(i).getChoice() == 1) sell_Of_LOS02_Game += auctionGame.bundleCountsOfEachBidder[i];
			}
			
			// total winner
			for(int i=0;i<TOTAL_PLAYER; i++){
				if(bidders.get(i).getChoice() == 1) winner_Of_LOS02_Game += 1;
			}
			
			// print good store
//			goodStore = auctionGame.getGoodStore();
//			System.out.print("good store: ");
//			for(int i=0;i<TOTAL_GOODS;i++){
//				System.out.print(goodStore[i]+" ");
//			}
//			System.out.println();	
		}

		
		System.out.println("GAME SETTING : ");
		System.out.println("bidders = " + TOTAL_PLAYER);
		System.out.println("chance = " + CHANCE + "%");
		System.out.println("good species = " + TOTAL_GOODS);
		System.out.println("amount range of each good = " + AMOUNT_RANGE_OF_EACH_GOOD);
		System.out.println("total goods = " + good_amount / TOTAL_FILES);
		System.out.println("weight base of each good species = " + WEIGHT_BASE_OF_GOODS);
		System.out.println("weight range of each good species = " + WEIGHT_RANGE_OF_GOODS);
		System.out.println();
		System.out.println("-----ours_game-----");
		System.out.println("benifit: " + Result_Of_Ours_Game / TOTAL_FILES);
		System.out.println("sell: " + sell_Of_Ours_Game / TOTAL_FILES);
		System.out.println("winner: " + winner_Of_Ours_Game / TOTAL_FILES);
		System.out.println("-----LOS02_game-----");
		System.out.println("benifit: " + Result_Of_LOS02_Game / TOTAL_FILES);
		System.out.println("sell: " + sell_Of_LOS02_Game / TOTAL_FILES);
		System.out.println("winner: " + winner_Of_LOS02_Game / TOTAL_FILES);
		
	}
}
