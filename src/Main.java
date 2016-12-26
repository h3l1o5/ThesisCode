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

	public static void main(String[] args) throws IOException {

		int Result_Of_Ours_Game = 0;
		int Result_Of_LOS02_Game = 0;
		int Result_Of_Ours_Centralized = 0;
		int Result_Of_LOS02_Centralized = 0;

		for (int file = 1; file <= TOTAL_FILES; file++) {
			double totalPaymentDegree;
			double totalPaymentGoods;
			List<Bidder> bidders;

			CombinatorialAuction auctionGame = new CombinatorialAuction(TOTAL_PLAYER, TOTAL_GOODS);

			// Our heuristic and centralized method
			totalPaymentDegree = 0;
			auctionGame.start("ours", "centralized");
			bidders = auctionGame.getBidders();
			for (Bidder bidder : bidders) {
				if (bidder.getChoice() == 1) {
					totalPaymentDegree += bidder.getPaymentByDegree();
				}
			}
			Result_Of_Ours_Centralized += totalPaymentDegree;

			// LOS02 heuristic and centralized method
			totalPaymentGoods = 0;
			auctionGame.start("LOS02", "centralized");
			bidders = auctionGame.getBidders();
			for (Bidder bidder : bidders) {
				if (bidder.getChoice() == 1) {
					totalPaymentGoods += bidder.getPaymentByGoods();
				}
			}
			Result_Of_LOS02_Centralized += totalPaymentGoods;

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

			// /*environment setup*/
			// informationMatrix = FileUtils.readfile(TOPOLOGY_MODEL,
			// TOTAL_PLAYER, 1, file);
			// FindMaximalIndepentSet MISgame = new FindMaximalIndepentSet(SYNC,
			// GWMIN2, TOTAL_PLAYER, informationMatrix);
			//
			// /*players setup*/
			// MISgame.randomizePlayerID();
			//
			// /*play*/
			// MISgame.start();
			//
			// /*print result*/
			// players = MISgame.getPlayers();
			// int total=0;
			// for(int i = 0;i < TOTAL_PLAYER;i++){
			// System.out.print(players.get(i).getChoice()+" ");
			// if(players.get(i).getChoice() == 1) total++;
			// }
			// System.out.println("total:"+total);

		}

		System.out.println("ours_centralized: " + Result_Of_Ours_Centralized / TOTAL_FILES);
		System.out.println("LOS02_centralized: " + Result_Of_LOS02_Centralized / TOTAL_FILES);
		System.out.println("ours_game: " + Result_Of_Ours_Game / TOTAL_FILES);
		System.out.println("LOS02_game: " + Result_Of_LOS02_Game / TOTAL_FILES);
	}
}
