
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Environment.CombinatorialAuction;
import Model.Bidder;
import ProcessMethod.Centralize;
import ProcessMethod.Game;

public class Main {
	private static final String OUTPUT_DIR = "C:/Users/Martian/Desktop/results/";
	private static final int TOTAL_ITERATION = 200;
	private static final boolean WITH_SUPERADDITIVE_PROPERTY = true;

	public static void main(String[] args) throws IOException {
		
		// standard parameter settings
		int numberOfBidders = 200;
		int numberOfGoodTypes = 300;
		int probability = 5; // percentage
		int maxAvailableUnitOfEachGoodType = 5;
		int[] parameters = { numberOfBidders, numberOfGoodTypes, probability, maxAvailableUnitOfEachGoodType };

		// changing every parameter
		for (int parameter = 3; parameter <= 3; parameter++) {
			String parameterType = "";
			int start = 0;
			int end = 0;
			int gap = 0;

			// setting start/end/gap value for current parameter
			switch (parameter) {
			case 0:
				parameterType = "m";
				start = 100;
				end = 300;
				gap = 20;
				break;
			case 1:
				parameterType = "n";
				start = 50;
				end = 950;
				gap = 100;
				break;
			case 2:
				parameterType = "p";
				start = 2;
				end = 20;
				gap = 2;
				break;
			case 3:
				parameterType = "lambda";
				start = 1;
				end = 10;
				gap = 1;
				break;
			default:
				break;
			}

			System.out.println("Current Parameter: " + parameterType);
			while (end >= start) {
				parameters[parameter] = start;
				double competitiveIntensity = 0;
				double bundleInstacesAmount = 0;
				int winner_HABPC = 0;
				int winner_ABPG = 0;
				int winner_ABPC = 0;
				int winner_ABPCG = 0;
				int bid_HABPC = 0;
				int bid_ABPC = 0;
				int bid_ABPG = 0;
				int bid_ABPCG = 0;
				int round_HABPC = 0;
				int round_ABPG = 0;
				int round_ABPC = 0;
				int round_ABPCG = 0;
				int centralized_winner_HABPC = 0;
				int centralized_winner_ABPG = 0;
				int centralized_winner_ABPC = 0;
				int centralized_winner_ABPCG = 0;
				int centralized_bid_HABPC = 0;
				int centralized_bid_ABPC = 0;
				int centralized_bid_ABPG = 0;
				int centralized_bid_ABPCG = 0;
				List<Bidder> bidders;

				System.out.println("value=" + start);
				// for every settings, run *TOTAL_ITERATION* time trials
				for (int i = 0; i < TOTAL_ITERATION; i++) {		
					
					// create an auction
					CombinatorialAuction auction = new CombinatorialAuction(parameters[0], parameters[1], parameters[2], parameters[3], WITH_SUPERADDITIVE_PROPERTY);
					competitiveIntensity += auction.getCompetitiveIntensity();
					bidders = auction.getBidders();
					for (Bidder bidder : bidders) {
						bundleInstacesAmount += bidder.getBundleInstanceCount();
					}
					
					// create procedures
//					Game game = new Game(auction);
					Centralize centralize = new Centralize(auction);

//					round_HABPC += game.start("HABPC");
//					bidders = auction.getBidders();
//					for (Bidder bidder : bidders) {
//						if (bidder.getDecision()) {
//							winner_HABPC++;
//							bid_HABPC += bidder.getBid();
//						}
//					}
//					round_ABPG += game.start("ABPG");
//					bidders = auction.getBidders();
//					for (Bidder bidder : bidders) {
//						if (bidder.getDecision()) {
//							winner_ABPG++;
//							bid_ABPG += bidder.getBid();
//						}
//					}
//					round_ABPC += game.start("ABPC");
//					bidders = auction.getBidders();
//					for (Bidder bidder : bidders) {
//						if (bidder.getDecision()) {
//							winner_ABPC++;
//							bid_ABPC += bidder.getBid();
//						}
//					}
//					round_ABPCG += game.start("ABPCG");
//					bidders = auction.getBidders();
//					for (Bidder bidder : bidders) {
//						if (bidder.getDecision()) {
//							winner_ABPCG++;
//							bid_ABPCG += bidder.getBid();
//						}
//					}

//					centralize.start("ABPC");
//					bidders = auction.getBidders();
//					for (Bidder bidder : bidders) {
//						if (bidder.getDecision()) {
//							centralized_winner_ABPC++;
//							centralized_bid_ABPC += bidder.getBid();
//						}
//					}
//					centralize.start("ABPG");
//					bidders = auction.getBidders();
//					for (Bidder bidder : bidders) {
//						if (bidder.getDecision()) {
//							centralized_winner_ABPG++;
//							centralized_bid_ABPG += bidder.getBid();
//						}
//					}
//					centralize.start("HABPC");
//					bidders = auction.getBidders();
//					for (Bidder bidder : bidders) {
//						if (bidder.getDecision()) {
//							centralized_winner_HABPC++;
//							centralized_bid_HABPC += bidder.getBid();
//						}
//					}
					centralize.start("ABPCG");
					bidders = auction.getBidders();
					for (Bidder bidder : bidders) {
						if (bidder.getDecision()) {
							centralized_winner_ABPCG++;
							centralized_bid_ABPCG += bidder.getBid();
						}
					}
				}

				// writing results
				FileWriter fw = new FileWriter(OUTPUT_DIR + parameterType + ".txt", true);
				fw.write("////////////////////////////////////////////////////////////\r\r\n");
				fw.write("m: " + parameters[0] + "\r\r\n");
				fw.write("n: " + parameters[1] + "\r\r\n");
				fw.write("p: " + parameters[2] + "\r\r\n");
				fw.write("lambda: " + parameters[3] + "\r\r\n");
				fw.write("Average competitive intensity: " + competitiveIntensity / TOTAL_ITERATION + "%" + "\r\r\n");
				fw.write("Average bundle instances amount: " + bundleInstacesAmount / parameters[0] / TOTAL_ITERATION + "\r\r\n");
				fw.write("\r\r\n");
//				fw.write("////////////////GAME////////////////" + "\r\r\n");
//				fw.write("-----HABPC-----" + "\r\r\n");
//				fw.write("Social Welfare: " + (double) bid_HABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("Average bid: " + (double) bid_HABPC / winner_HABPC + "\r\r\n");
//				fw.write("winner: " + (double) winner_HABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("rounds: " + (double) round_HABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("-----ABPG-----" + "\r\r\n");
//				fw.write("Social Welfare: " + (double) bid_ABPG / TOTAL_ITERATION + "\r\r\n");
//				fw.write("Average bid: " + (double) bid_ABPG / winner_ABPG + "\r\r\n");
//				fw.write("winner: " + (double) winner_ABPG / TOTAL_ITERATION + "\r\r\n");
//				fw.write("rounds: " + (double) round_ABPG / TOTAL_ITERATION + "\r\r\n");
//				fw.write("-----ABPC-----" + "\r\r\n");
//				fw.write("Social Welfare: " + (double) bid_ABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("Average bid: " + (double) bid_ABPC / winner_ABPC + "\r\r\n");
//				fw.write("winner: " + (double) winner_ABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("rounds: " + (double) round_ABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("-----ABPCG-----" + "\r\r\n");
//				fw.write("Social Welfare: " + (double) bid_ABPCG / TOTAL_ITERATION + "\r\r\n");
//				fw.write("Average bid: " + (double) bid_ABPCG / winner_ABPCG + "\r\r\n");
//				fw.write("winner: " + (double) winner_ABPCG / TOTAL_ITERATION + "\r\r\n");
//				fw.write("rounds: " + (double) round_ABPCG / TOTAL_ITERATION + "\r\r\n");
//				fw.write("\r\r\n");
				fw.write("////////////////CENTRALIZE////////////////" + "\r\r\n");
//				fw.write("-----HABPC-----" + "\r\r\n");
//				fw.write("Social Welfare: " + (double) centralized_bid_HABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("Average bid: " + (double) centralized_bid_HABPC / centralized_winner_HABPC + "\r\r\n");
//				fw.write("winner: " + (double) centralized_winner_HABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("-----ABPG-----" + "\r\r\n");
//				fw.write("Social Welfare: " + (double) centralized_bid_ABPG / TOTAL_ITERATION + "\r\r\n");
//				fw.write("Average bid: " + (double) centralized_bid_ABPG / centralized_winner_ABPG + "\r\r\n");
//				fw.write("winner: " + (double) centralized_winner_ABPG / TOTAL_ITERATION + "\r\r\n");
//				fw.write("-----ABPC-----" + "\r\r\n");
//				fw.write("Social Welfare: " + (double) centralized_bid_ABPC / TOTAL_ITERATION + "\r\r\n");
//				fw.write("Average bid: " + (double) centralized_bid_ABPC / centralized_winner_ABPC + "\r\r\n");
//				fw.write("winner: " + (double) centralized_winner_ABPC / TOTAL_ITERATION + "\r\r\n");
				fw.write("-----ABPCG-----" + "\r\r\n");
				fw.write("Social Welfare: " + (double) centralized_bid_ABPCG / TOTAL_ITERATION + "\r\r\n");
				fw.write("Average bid: " + (double) centralized_bid_ABPCG / centralized_winner_ABPCG + "\r\r\n");
				fw.write("winner: " + (double) centralized_winner_ABPCG / TOTAL_ITERATION + "\r\r\n");
				fw.write("////////////////////////////////////////////////////////////\r\r\n");
				fw.close();
				
				// changing current parameter's value
				start += gap;
			}
		}
	}
}
