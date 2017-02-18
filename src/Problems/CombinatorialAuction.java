package Problems;

import java.awt.Label;
import java.lang.reflect.Array;
import java.rmi.dgc.Lease;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Model.Bidder;

public class CombinatorialAuction {

	private int totalGoods;
	private int totalBidders;
	private int chance;
	private int amountRangeOfEachGood;
	private int[] goodStore;
	private int[] goodStoreOriginal;
	private int[][] conflictionMatrix;
	List<Bidder> bidders;

	public CombinatorialAuction(int size, int totalGoods, int weightBaseOfGoods, int weightRangeOfGoods, int chance, int amountRangeOfEachGood) {
		this.totalBidders = size;
		this.totalGoods = totalGoods;
		this.chance = 100/chance;
		this.amountRangeOfEachGood = amountRangeOfEachGood;
		goodStore = new int[totalGoods];
		goodStoreOriginal = new int[totalGoods];
		conflictionMatrix = new int[size][size];
		initBidders();
		initGoodStore();
		initBundlesAndConflictionMatrix();
		initBidderWeight(weightBaseOfGoods,weightRangeOfGoods);
		randomizeBidderID();
	}
	
/*getters and setters*/
	public void setConflictionMatrix(int[][] martrix) {
		conflictionMatrix = martrix;
	}

	public List<Bidder> getBidders() {
		return bidders;
	}
	
	public int[] getGoodStoreOriginal(){
		return goodStoreOriginal;
	}
	
	public void setGoodStore(int[] goodStore){
		this.goodStore = goodStore;
	}
	
	public void setGoodStoreOriginal(){
		for(int i=0;i<totalGoods;i++){
			goodStoreOriginal[i] = goodStore[i];
		}
	}
	
	public double getDensityOfGraph(){
		int edges=0;
		for(int i=0;i<totalBidders;i++){
			for(int j=0;j<totalBidders;j++){
				if(conflictionMatrix[i][j]==1)
					edges += 1;
			}
		}
		edges = edges/2;
		return (double)edges/(totalBidders*(totalBidders-1)/2);
	}
/*getters and setters*/

	public void start(String winnerDeterminationAlgo, String type) {
	
		// reset good store
		for(int i=0;i<totalGoods;i++){
			goodStore[i] = goodStoreOriginal[i];
		}
		// reset bidder's choice
		for(Bidder bidder:bidders){
			bidder.setChoice(0);
		}
		
		switch(type){		
		case "game":
			Game_async(winnerDeterminationAlgo);
			break;
		case "centralized":
			//	Centralized_aysnc(winnerDeterminationAlgo);		
			break;
		default:
			System.out.println("the execution type must be \"game\" or \"centralized\"");
			return;
		}
	}

	private void initBidders() {
		bidders = new ArrayList<>();
		for (int i = 0; i < totalBidders; i++) {
			Bidder bidder = new Bidder(i, totalBidders, totalGoods);
			bidders.add(bidder);
		}
	}
	
	private void initGoodStore() {
		if(amountRangeOfEachGood > 1) {
			Random ran = new Random();
			for(int i=0;i<totalGoods;i++){
				int amount = ran.nextInt(amountRangeOfEachGood) + 1;
				goodStore[i] = amount;
				goodStoreOriginal[i] = amount;
			}
		} else {
			for(int i=0;i<totalGoods;i++){
				goodStore[i] = 1;
				goodStoreOriginal[i] = 1;
			}
		}
	}

	private void initBundlesAndConflictionMatrix() {
		Random ran = new Random();
		
		// for each bidder, random choose to take or not take a good. 
		for (int i = 0; i < totalBidders; i++) {
			Bidder bidder = bidders.get(i);
			for (int j = 0; j < totalGoods; j++) {
				if (ran.nextInt(chance) % chance == 0) {
					int amountToTake = ran.nextInt(goodStore[j])+1;
					bidder.setBundle(j, amountToTake);
				}
			}
		}

		// make a adjacency matrix represent the confliction of every bidder
		for (int i = 0; i < totalBidders; i++) {
			Bidder bidder = bidders.get(i);
			for (int j = i + 1; j < totalBidders; j++) {
				Bidder bidder2 = bidders.get(j);
				for (int k = 0; k < totalGoods; k++) {
					if (bidder.getBundle()[k] > 0 && bidder2.getBundle()[k] > 0) {
						conflictionMatrix[i][j] = 1;
						bidder.setNeighbors(j, 1);
						conflictionMatrix[j][i] = 1;
						bidder2.setNeighbors(i, 1);
						break;
					}
				}
			}
		}
	}
	
	public void initBidderWeight(int base, int range) {
		Random ran = new Random();
		int bidderWeight;
		int[] weightOfGood = new int[totalGoods];
		for (int i = 0; i < totalGoods; i++) {
			weightOfGood[i] = ran.nextInt(range) + base;
		}
		for (int i = 0; i < totalBidders; i++) {
			Bidder bidder = bidders.get(i);
			bidderWeight = 0;
			for (int j = 0; j < totalGoods; j++) {
				if (bidder.getBundle()[j] > 0) {
					double normalRandomNumber = 0;
					while (normalRandomNumber <= 0) {
						normalRandomNumber = ran.nextGaussian() * (weightOfGood[j] / 5) + weightOfGood[j];
					}
					bidderWeight += (normalRandomNumber*bidder.getBundle()[j]);
				}
			}
			bidder.setWeight(bidderWeight);
		}
	}

	public void randomizeBidderID() {
		Random ran = new Random();
		int target;
		int temp;
		for (int i = 0; i < totalBidders; i++) {
			temp = bidders.get(i).getID();
			target = ran.nextInt(totalBidders);
			bidders.get(i).setID(bidders.get(target).getID());
			bidders.get(target).setID(temp);
		}
	}
	

	private void Game_async(String winnerDeterminationAlgo) {
		int flag;
		int newDecision;

		calculatePriority(winnerDeterminationAlgo);
		
		for (int step = 1;; step++) {
			flag = 0;
			for (int i = 0; i < totalBidders; i++) {
				Bidder bidder = bidders.get(i);
				newDecision = makeNewDecition(i);
				if (bidder.getChoice() != newDecision) {
					if(newDecision == 0){
						for(int j=0;j<totalGoods;j++){
							goodStore[j] += bidder.getBundle()[j];
						}
					}
					if(newDecision == 1){
						for(int j=0;j<totalGoods;j++){
							goodStore[j] -= bidder.getBundle()[j];
						}
					}		
					bidders.get(i).setChoice(newDecision);
					flag = 1;
					break;
				}
			}
			if (flag == 0) break;
		}
		
		calculateCriticalValue(winnerDeterminationAlgo);

		if(amountRangeOfEachGood == 1) checkIndependentAndDomination();
		else checkGoodStore();
	
	}

	private void checkIndependentAndDomination() {
		int flag = 0;

		for (int i = 0; i < totalBidders; i++) {
			if (bidders.get(i).getChoice() == 1) {
				for (int j = 0; j < totalBidders; j++) {
					if (conflictionMatrix[i][j] == 1 && bidders.get(j).getChoice() == 1) {
						flag = 1;
						break;
					}
				}
			} else {
				flag = 2;
				for (int j = 0; j < totalBidders; j++) {
					if (conflictionMatrix[i][j] == 1 && bidders.get(j).getChoice() == 1) {
						flag = 0;
						break;
					}
				}
			}
			if (flag == 1) {
				System.out.println("not independent");
			} else if (flag == 2) {
				System.out.println("not dominating");
			}
		}
	}
	
	private void checkGoodStore(){
		for(int i=0;i<totalGoods;i++){
			if(goodStore[i]<0){
				System.out.println("some goods <0");
				break;
			}
		}
	}
	
	private void calculatePriority(String winnerDeterminationAlgo) {
		switch(winnerDeterminationAlgo){
		case "LOS02":
			for (int i = 0; i < totalBidders; i++) {
				bidders.get(i).setPriority((double) bidders.get(i).getWeight() / Math.sqrt(bidders.get(i).getBundleCount()));
			}
			break;
		case "ours":
			for (int i = 0; i < totalBidders; i++){
				Bidder bidder = bidders.get(i);
				bidder.setPriority((double) bidder.getWeight() / Math.sqrt(bidder.getBundleCount()*(bidder.getNeighborCount()+1)));
			}
			break;
		default:
			return;
		}
	}
	
	private void calculateCriticalValue(String winnerDeterminationAlgo) {
		for(int i=0; i<totalBidders; i++){
			// calculation for winners
			if (bidders.get(i).getChoice() == 1) {
				Bidder winner = bidders.get(i);
				double temp=-1;
				
				// find all neighbors who will become winner if current winner was not win.
				winner.setChoice(0);
				for(int j=0; j<totalBidders; j++){
					if(conflictionMatrix[i][j]==1 && bidders.get(j).getChoice()==0) {
						if(makeNewDecition(j) == 1 && bidders.get(j).getPriority()>temp) {
							temp = bidders.get(j).getPriority();
						}
					}
				}
				winner.setChoice(1);
				
				if(temp==-1){ // no such neighbor, don't have to pay anything.
					winner.setPayment(0);
					winner.setCriticalValue(0);
				} else {
					switch(winnerDeterminationAlgo){
					case "LOS02":
						winner.setPayment(temp * Math.sqrt(winner.getBundleCount()));
						winner.setCriticalValue(temp * Math.sqrt(winner.getBundleCount()));
						break;
					case "ours":
						winner.setPayment(temp * Math.sqrt(winner.getBundleCount()*(winner.getNeighborCount()+1)));
						winner.setCriticalValue(temp * Math.sqrt(winner.getBundleCount()*(winner.getNeighborCount()+1)));
						break;
					default:
						return;
					}
				}
			} else {  // calculation for losers
				Bidder loser = bidders.get(i);
				double temp = 999999999;
				
				// find the neighbor who is winner and has highest priority.
				for(int j=0; j<totalBidders; j++){
					if (conflictionMatrix[i][j] == 1 && bidders.get(j).getChoice() == 1){
						bidders.get(j).setChoice(0);
						if(makeNewDecition(i)==1){
							if(bidders.get(j).getPriority()<temp)
								temp = bidders.get(j).getPriority();
						}
						bidders.get(j).setChoice(1);
					}
				}
				
				switch (winnerDeterminationAlgo) {
				case "LOS02":
					loser.setCriticalValue(temp * Math.sqrt(loser.getBundleCount()));					
					break;
				case "ours":
					loser.setCriticalValue(temp * Math.sqrt(loser.getBundleCount()*(loser.getNeighborCount()+1)));					
				default:
					break;
				}
				
				// loser don't have to pay.
				loser.setPayment(0);	
			}
		}	
	}
	
	// incorrect one, left here, can use to compare with another one.
	private int makeNewDecition2(int serialNumberOfCurrentBidder) {
		Bidder currentBidder = bidders.get(serialNumberOfCurrentBidder);
		for (int i = 0; i < totalBidders; i++) {
			if (conflictionMatrix[serialNumberOfCurrentBidder][i] == 1 && bidders.get(i).getChoice() == 1) {
				Bidder competitor = bidders.get(i);
				if (competitor.getPriority() > currentBidder.getPriority() || (competitor.getPriority() == currentBidder.getPriority() && competitor.getID() < currentBidder.getID())){
					if (currentBidder.getChoice()==1){
						for(int j=0;j<totalGoods;j++){
							if(currentBidder.getBundle()[j]>0 && competitor.getBundle()[j]>0 && goodStore[j]<0){
								return 0;
							}
						}
					} else {
						for(int j=0;j<totalGoods;j++){
							if(currentBidder.getBundle()[j]>0 && competitor.getBundle()[j]>0 && goodStore[j]-currentBidder.getBundle()[j]<0)
								return 0;
						}
					}
				}
			}
		}
		return 1;
	}
	
	private int makeNewDecition(int serialNumberOfCurrentBidder){
		Bidder currentBidder = bidders.get(serialNumberOfCurrentBidder);
		for(int i=0;i<totalGoods;i++){
			if (currentBidder.getBundle()[i]>0){
				int instanceCountOfThisGood = goodStoreOriginal[i];
				int totalNeededOfThisGood = currentBidder.getBundle()[i];
				
				for(int j=0;j<totalBidders;j++){
					if(currentBidder.getNeighbors()[j] == 1 && bidders.get(j).getChoice() == 1){
						Bidder competitor = bidders.get(j);
						if (competitor.getPriority() > currentBidder.getPriority() || (competitor.getPriority() == currentBidder.getPriority() && competitor.getID() < currentBidder.getID())){
							totalNeededOfThisGood += competitor.getBundle()[i];
						}
					}
				}
				
				if (totalNeededOfThisGood > instanceCountOfThisGood){
					return 0;
				}
			}
		}
		return 1;
	}

	
	// TODO: repair this
/*	private void Centralized_aysnc(String winnerDeterminationAlgo) {
		if (winnerDeterminationAlgo.equals("ours")) {
			
			// mapping bidders and their weight
			HashMap<Integer, Double> map = new HashMap<>();
			for (int i = 0; i < totalBidders; i++) {
				map.put(i, bidders.get(i).getPriority());
			}
			
			// sorting by weight/square(degree)+1
			List<Map.Entry<Integer, Double>> list = new ArrayList<>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
				public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
					return (entry1.getValue().compareTo(entry2.getValue()));
				}
			});
			
			// begin
			int[] hasGoodBeenTaken = new int[totalGoods];
			label: for (Map.Entry<Integer, Double> entry : list) {
				Bidder currentBidder = bidders.get(entry.getKey());
				// loser
				for (int i = 0; i < totalGoods; i++) {
					if (currentBidder.getBundle()[i] == 1 && hasGoodBeenTaken[i] == 1) {
						currentBidder.setChoice(0);
						currentBidder.setPayment(0);
						continue label;
					}
				}
				// winner
				for (int i = 0; i < totalGoods; i++) {
					if (currentBidder.getBundle()[i] == 1) {
						hasGoodBeenTaken[i] = currentBidder.getBundle()[i];
					}
				}
				// second price payment
				double temp = 0;
				for (int i = 0; i < totalBidders; i++) {
					if (conflictionMatrix[entry.getKey()][i] == 1) {
						if (bidders.get(i).getPriority() > temp) {
							temp = bidders.get(i).getPriority();
						}
					}
				}
				currentBidder.setChoice(1);
				currentBidder.setPayment(temp * Math.sqrt(degreeCountsOfEachBidder[entry.getKey()]+1));
			}
		} else { // LOS02
			
			// mapping bidders and their weight
			HashMap<Integer, Double> map = new HashMap<>();
			for (int i = 0; i < totalBidders; i++) {
				map.put(i, bidders.get(i).getPriority());
			}
			
			// sorting by weight/square(degree)+1
			List<Map.Entry<Integer, Double>> list = new ArrayList<>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
				public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
					return (entry1.getValue().compareTo(entry2.getValue()));
				}
			});
			
			// begin
			int[] hasGoodBeenTaken = new int[totalGoods];
			label: for (Map.Entry<Integer, Double> entry : list) {
				Bidder currentBidder = bidders.get(entry.getKey());
				// loser
				for (int i = 0; i < totalGoods; i++) {
					if (currentBidder.getBundle()[i] == 1 && hasGoodBeenTaken[i] == 1) {
						currentBidder.setChoice(0);
						currentBidder.setPayment(0);
						continue label;
					}
				}
				// winner
				for (int i = 0; i < totalGoods; i++) {
					if (currentBidder.getBundle()[i] == 1) {
						hasGoodBeenTaken[i] = currentBidder.getBundle()[i];
					}
				}
				// second price payment
				double temp = 0;
				for (int i = 0; i < totalBidders; i++) {
					if (conflictionMatrix[entry.getKey()][i] == 1) {
						if (bidders.get(i).getPriority() > temp) {
							temp = bidders.get(i).getPriority();
						}
					}
				}
				currentBidder.setChoice(1);
				currentBidder.setPayment(temp * Math.sqrt(bundleCountsOfEachBidder[entry.getKey()]));
			}
		}
	}*/
}
