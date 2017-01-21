import java.util.List;

import Model.Bidder;
import Problems.CombinatorialAuction;

public class Test {
	private List<Bidder> bidders;
		
	public void startSingleUnitTest(){
		CombinatorialAuction gameForTest = new CombinatorialAuction(5, 5, 1, 1, 1, 1);
		bidders = gameForTest.getBidders();
		prepareBundleSingle();
		prepareWeight();
		prepareNeighbors();
		gameForTest.setConflictionMatrix(prepareConflictionMatrix());
		gameForTest.setGoodStore(prepareGoodsSingle());
		gameForTest.setGoodStoreOriginal();
		
		gameForTest.start("ours", "game");
		System.out.println("/Single-Unit test result of ours algo/");
		for(int i=0;i<5;i++){
			Bidder bidder = bidders.get(i);
			System.out.print("bidder"+i+": ");
			if(bidder.getChoice()==1){
				System.out.print("winner, ");
			} else {
				System.out.print("loser, ");
			}
			System.out.print("Critical Value = " + bidder.getCriticalValue());
			System.out.println();
		}
		System.out.println();

		gameForTest.start("LOS02", "game");
		System.out.println("/Single-Unit test result of LOS02 algo/");
		for(int i=0;i<5;i++){
			Bidder bidder = bidders.get(i);
			System.out.print("bidder"+i+": ");
			if(bidder.getChoice()==1){
				System.out.print("winner, ");
			} else {
				System.out.print("loser, ");
			}
			System.out.print("Critical Value = " + bidder.getCriticalValue());
			System.out.println();
		}
		System.out.println();
	}
	
	public void startMultiUnitTest(){
		CombinatorialAuction gameForTest = new CombinatorialAuction(5, 5, 1, 1, 1, 5);
		bidders = gameForTest.getBidders();
		prepareBundleMulti();
		prepareWeight();
		prepareNeighbors();
		gameForTest.setConflictionMatrix(prepareConflictionMatrix());
		gameForTest.setGoodStore(prepareGoodsMulti());
		gameForTest.setGoodStoreOriginal();
		
		gameForTest.start("ours", "game");
		System.out.println("/Multi-Unit test result of ours algo/");
		for(int i=0;i<5;i++){
			Bidder bidder = bidders.get(i);
			System.out.print("bidder"+i+": ");
			if(bidder.getChoice()==1){
				System.out.print("winner, ");
			} else {
				System.out.print("loser, ");
			}
			System.out.print("Critical Value = " + bidder.getCriticalValue());
			System.out.println();
		}
		System.out.println();

		gameForTest.start("LOS02", "game");
		System.out.println("/Multi-Unit test result of LOS02 algo/");
		for(int i=0;i<5;i++){
			Bidder bidder = bidders.get(i);
			System.out.print("bidder"+i+": ");
			if(bidder.getChoice()==1){
				System.out.print("winner, ");
			} else {
				System.out.print("loser, ");
			}
			System.out.print("Critical Value = " + bidder.getCriticalValue());
			System.out.println();
		}
		System.out.println();
	}
	
	private void prepareBundleSingle(){
		bidders.get(0).setBundle(0, 1);
		bidders.get(0).setBundle(1, 0);
		bidders.get(0).setBundle(2, 1);
		bidders.get(0).setBundle(3, 1);
		bidders.get(0).setBundle(4, 0);
		
		bidders.get(1).setBundle(0, 1);
		bidders.get(1).setBundle(1, 1);
		bidders.get(1).setBundle(2, 1);
		bidders.get(1).setBundle(3, 0);
		bidders.get(1).setBundle(4, 0);
		
		bidders.get(2).setBundle(0, 0);
		bidders.get(2).setBundle(1, 1);
		bidders.get(2).setBundle(2, 0);
		bidders.get(2).setBundle(3, 1);
		bidders.get(2).setBundle(4, 1);
		
		bidders.get(3).setBundle(0, 0);
		bidders.get(3).setBundle(1, 0);
		bidders.get(3).setBundle(2, 0);
		bidders.get(3).setBundle(3, 1);
		bidders.get(3).setBundle(4, 1);
		
		bidders.get(4).setBundle(0, 1);
		bidders.get(4).setBundle(1, 0);
		bidders.get(4).setBundle(2, 1);
		bidders.get(4).setBundle(3, 0);
		bidders.get(4).setBundle(4, 0);
	}
	
	private void prepareBundleMulti(){
		bidders.get(0).setBundle(0, 1);
		bidders.get(0).setBundle(1, 0);
		bidders.get(0).setBundle(2, 3);
		bidders.get(0).setBundle(3, 2);
		bidders.get(0).setBundle(4, 0);
		
		bidders.get(1).setBundle(0, 2);
		bidders.get(1).setBundle(1, 1);
		bidders.get(1).setBundle(2, 1);
		bidders.get(1).setBundle(3, 0);
		bidders.get(1).setBundle(4, 0);
		
		bidders.get(2).setBundle(0, 0);
		bidders.get(2).setBundle(1, 3);
		bidders.get(2).setBundle(2, 0);
		bidders.get(2).setBundle(3, 2);
		bidders.get(2).setBundle(4, 1);
		
		bidders.get(3).setBundle(0, 0);
		bidders.get(3).setBundle(1, 0);
		bidders.get(3).setBundle(2, 0);
		bidders.get(3).setBundle(3, 2);
		bidders.get(3).setBundle(4, 1);
		
		bidders.get(4).setBundle(0, 1);
		bidders.get(4).setBundle(1, 0);
		bidders.get(4).setBundle(2, 3);
		bidders.get(4).setBundle(3, 0);
		bidders.get(4).setBundle(4, 0);
	}
	
	private void prepareWeight() {
		bidders.get(0).setWeight(63);
		bidders.get(1).setWeight(54);
		bidders.get(2).setWeight(93);
		bidders.get(3).setWeight(70);
		bidders.get(4).setWeight(17);
	}
	
	private void prepareNeighbors() {
		bidders.get(0).setNeighbors(0, 0);
		bidders.get(0).setNeighbors(1, 1);
		bidders.get(0).setNeighbors(2, 1);
		bidders.get(0).setNeighbors(3, 1);
		bidders.get(0).setNeighbors(4, 1);
		
		bidders.get(1).setNeighbors(0, 1);
		bidders.get(1).setNeighbors(1, 0);
		bidders.get(1).setNeighbors(2, 1);
		bidders.get(1).setNeighbors(3, 0);
		bidders.get(1).setNeighbors(4, 1);
		
		bidders.get(2).setNeighbors(0, 1);
		bidders.get(2).setNeighbors(1, 1);
		bidders.get(2).setNeighbors(2, 0);
		bidders.get(2).setNeighbors(3, 1);
		bidders.get(2).setNeighbors(4, 0);
		
		bidders.get(3).setNeighbors(0, 1);
		bidders.get(3).setNeighbors(1, 0);
		bidders.get(3).setNeighbors(2, 1);
		bidders.get(3).setNeighbors(3, 0);
		bidders.get(3).setNeighbors(4, 0);
		
		bidders.get(4).setNeighbors(0, 1);
		bidders.get(4).setNeighbors(1, 1);
		bidders.get(4).setNeighbors(2, 0);
		bidders.get(4).setNeighbors(3, 0);
		bidders.get(4).setNeighbors(4, 0);		
	}
	
	private int[][] prepareConflictionMatrix(){
		int[][] confMatrix = new int[5][5];
		confMatrix[0][0] = 0;
		confMatrix[0][1] = 1;
		confMatrix[0][2] = 1;
		confMatrix[0][3] = 1;
		confMatrix[0][4] = 1;
		
		confMatrix[1][0] = 1;
		confMatrix[1][1] = 0;
		confMatrix[1][2] = 1;
		confMatrix[1][3] = 0;
		confMatrix[1][4] = 1;

		
		confMatrix[2][0] = 1;
		confMatrix[2][1] = 1;
		confMatrix[2][2] = 0;
		confMatrix[2][3] = 1;
		confMatrix[2][4] = 0;

		
		confMatrix[3][0] = 1;
		confMatrix[3][1] = 0;
		confMatrix[3][2] = 1;
		confMatrix[3][3] = 0;
		confMatrix[3][4] = 0;

		
		confMatrix[4][0] = 1;
		confMatrix[4][1] = 1;
		confMatrix[4][2] = 0;
		confMatrix[4][3] = 0;
		confMatrix[4][4] = 0;

		
		return confMatrix;
	}
	
	private int[] prepareGoodsSingle() {
		int[] goods = new int[5];
		goods[0] = 1;
		goods[1] = 1;
		goods[2] = 1;
		goods[3] = 1;
		goods[4] = 1;
		
		return goods;		
	}
	
	private int[] prepareGoodsMulti(){
		int[] goods = new int[5];
		goods[0] = 3;
		goods[1] = 4;
		goods[2] = 5;
		goods[3] = 4;
		goods[4] = 3;
		
		return goods;
	}
}
