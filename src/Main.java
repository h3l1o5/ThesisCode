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
	private static final int TOTAL_FILES = 1;

	public static void main(String[] args) throws IOException {

		int Result_Of_Ours_Game = 0;
		int Result_Of_LOS02_Game = 0;
		int sell_Of_Ours_Game = 0;
		int sell_Of_LOS02_Game = 0;
		int winner_Of_Ours_Game = 0;
		int winner_Of_LOS02_Game = 0;


		for (int file = 1; file <= TOTAL_FILES; file++) {
			double totalPaymentDegree;
			double totalPaymentGoods;
			List<Bidder> bidders;
			CombinatorialAuction auctionGame = new CombinatorialAuction(TOTAL_PLAYER, TOTAL_GOODS);

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
			
			// print payment
			System.out.print("payment: ");
			for(int i=0;i<TOTAL_PLAYER;i++){
				if(bidders.get(i).getChoice() == 1) {
					System.out.print((int)(bidders.get(i).getWeight()-bidders.get(i).getPaymentByDegree()) + " ");
				}
			}
			System.out.println();
			
			// total sell
			for(int i=0;i<TOTAL_PLAYER;i++){
				if(bidders.get(i).getChoice() == 1) sell_Of_Ours_Game += auctionGame.bundleCountsOfEachBidder[i];
			}
			
			// total winner
			for(int i=0;i<TOTAL_PLAYER; i++){
				if(bidders.get(i).getChoice() == 1) winner_Of_Ours_Game += 1;
			}

			

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
			
			// print payment
			System.out.print("payment: ");
			for(int i=0;i<TOTAL_PLAYER;i++){
				if(bidders.get(i).getChoice() == 1) {
					System.out.print((int)(bidders.get(i).getWeight()-bidders.get(i).getPaymentByGoods()) + " ");
				}
			}
			System.out.println();
			
			// print total sell
			for(int i=0;i<TOTAL_PLAYER;i++){
				if(bidders.get(i).getChoice() == 1) sell_Of_LOS02_Game += auctionGame.bundleCountsOfEachBidder[i];
			}
			
			// total winner
			for(int i=0;i<TOTAL_PLAYER; i++){
				if(bidders.get(i).getChoice() == 1) winner_Of_LOS02_Game += 1;
			}
		}

		System.out.println("ours_game: " + Result_Of_Ours_Game / TOTAL_FILES);
//		System.out.println("sell: " + sell_Of_Ours_Game / TOTAL_FILES);
//		System.out.println("winner: " + winner_Of_Ours_Game / TOTAL_FILES);
		System.out.println("LOS02_game: " + Result_Of_LOS02_Game / TOTAL_FILES);
//		System.out.println("sell: " + sell_Of_LOS02_Game / TOTAL_FILES);
//		System.out.println("winner: " + winner_Of_LOS02_Game / TOTAL_FILES);
		
	}
}
