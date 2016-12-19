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
	private static final int TOTAL_FILES = 2000;
	private static final String TOPOLOGY_MODEL = "BA";
	
	private static List<Player> players;
	private static int[][] informationMatrix;
	
	
	public static void main(String[] args) throws IOException{
		
		int totalResultOfHeuristic = 0;
		int totalResultOfHeuristic2 = 0;
		for(int file = 1;file <= TOTAL_FILES;file++){
			
			CombinatorialAuction auctionGame = new CombinatorialAuction("", "", TOTAL_PLAYER,300);
			auctionGame.start("heuristic");
			List<Bidder> bidders = auctionGame.getBidders();
			double totalPaymentDegree=0;
			for(Bidder bidder:bidders){
				if(bidder.getChoice()==1){
					totalPaymentDegree += bidder.getPaymentByDegree();
				}
			}
			
			auctionGame.start("heuristic2");
			bidders = auctionGame.getBidders();
			double totalPaymentGoods=0;
			for(Bidder bidder:bidders){
				if(bidder.getChoice()==1){
					totalPaymentGoods += bidder.getPaymentByGoods();
				}
			}

			totalResultOfHeuristic += totalPaymentDegree;
			totalResultOfHeuristic2 += totalPaymentGoods;
			
			
//			/*environment setup*/
//			informationMatrix = FileUtils.readfile(TOPOLOGY_MODEL, TOTAL_PLAYER, 1, file);
//			FindMaximalIndepentSet MISgame = new FindMaximalIndepentSet(SYNC, GWMIN2, TOTAL_PLAYER, informationMatrix);
//		
//			/*players setup*/
//			MISgame.randomizePlayerID();
//			
//			/*play*/
//			MISgame.start();
//
//			/*print result*/
//			players = MISgame.getPlayers();
//			int total=0;
//			for(int i = 0;i < TOTAL_PLAYER;i++){
//				System.out.print(players.get(i).getChoice()+" ");
//				if(players.get(i).getChoice() == 1) total++;
//			}
//			System.out.println("total:"+total);
			
		}
		
		System.out.println("heuristic of degree: "+totalResultOfHeuristic/TOTAL_FILES);
		System.out.println("heuristic of good: "+totalResultOfHeuristic2/TOTAL_FILES);
	}
}
