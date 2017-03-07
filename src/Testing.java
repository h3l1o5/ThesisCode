import java.util.List;

import Model.Bidder;
import Model.BidderTemp;
import Problem.CombinatorialAuctionTemp;

public class Testing {
	public static void main(String[] args) {
		CombinatorialAuctionTemp auction = new CombinatorialAuctionTemp(10, 10, 10, 10);
		List<BidderTemp> bidders = auction.getBidders();
		for (BidderTemp bidder : bidders) {
			System.out.println("ID:" + bidder.getID() + " bundle size:" + bidder.getBundleInstanceCount() + ", compe:" + bidder.getAllCompetitor() + ", bid:" + bidder.getBid());
		}
	}
}
