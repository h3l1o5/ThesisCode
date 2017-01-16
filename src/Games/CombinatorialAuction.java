package Games;

import java.awt.Label;
import java.lang.reflect.Array;
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
	private String winnerDeterminationAlgo;
	private String type;
	private int totalGoods;
	private int totalBidders;
	private int chance;
	private boolean isMultiUnit;
	private int amountRangeOfEachGood;
	private int[] goodStore;
	private int[] goodStoreOriginal;
	private int[][] conflictionMatrix;
	int[] degreeCountsOfEachBidder;
	public int[] bundleCountsOfEachBidder;
	List<Bidder> bidders;

	public CombinatorialAuction(int size, int totalGoods, int weightBaseOfGoods, int weightRangeOfGoods, int chance, int amountRangeOfEachGood) {
		this.totalBidders = size;
		this.totalGoods = totalGoods;
		this.chance = 100/chance;
		this.amountRangeOfEachGood = amountRangeOfEachGood;
		goodStore = new int[totalGoods];
		goodStoreOriginal = new int[totalGoods];
		conflictionMatrix = new int[size][size];
		bundleCountsOfEachBidder = new int[size];
		degreeCountsOfEachBidder = new int[size];
		initBidders();
		initGoodStore(isMultiUnit);
		initBundlesAndConflictionMatrix();
		initBidderWeight(weightBaseOfGoods,weightRangeOfGoods);
		randomizeBidderID();
	}
	
/*getters and setters*/
	public void setConlictionMatrix(int[][] martrix) {
		conflictionMatrix = martrix;
	}
	
	public void setBundleCountsOfEachBidder(int[] bundleCount) {
		bundleCountsOfEachBidder = bundleCount;
	}
	
	public void setDegreeCountsOfEachBidder(int[] degreeCount) {
		degreeCountsOfEachBidder = degreeCount;
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
/*getters and setters*/

	public void start(String winnerDeterminationAlgo, String type) {
		for (int i = 0; i < totalBidders; i++) {
			bidders.get(i).setPriorityByDegreeAndSquare((double) bidders.get(i).getWeight() / Math.sqrt(degreeCountsOfEachBidder[i]+1));
			bidders.get(i).setPriorityByGoodsAndSquare((double) bidders.get(i).getWeight() / Math.sqrt(bundleCountsOfEachBidder[i]));
		}
		
		this.type = type;
		this.winnerDeterminationAlgo = winnerDeterminationAlgo;
		
		// reset good store
		for(int i=0;i<totalGoods;i++){
			goodStore[i] = goodStoreOriginal[i];
		}
		// reset bidder's choice
		for(Bidder bidder:bidders){
			bidder.setChoice(0);
		}
		
		if (type.equals("game")) {
			Game_async();
		} else {
			Centralized_aysnc();
		}
	}

	private void initBidders() {
		bidders = new ArrayList<>();
		for (int i = 0; i < totalBidders; i++) {
			Bidder bidder = new Bidder(i, totalBidders, totalGoods);
			bidders.add(bidder);
		}
	}
	
	private void initGoodStore(boolean isMultiUnit) {
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
					bundleCountsOfEachBidder[i] += amountToTake;
				}
			}
		}

		// make a adjacency matrix represent of the confliction of every bidder
		for (int i = 0; i < totalBidders; i++) {
			Bidder bidder = bidders.get(i);
			for (int j = i + 1; j < totalBidders; j++) {
				Bidder bidder2 = bidders.get(j);
				for (int k = 0; k < totalGoods; k++) {
					if (bidder.getBundle()[k] > 0 && bidder2.getBundle()[k] > 0) {
						conflictionMatrix[i][j] = 1;
						conflictionMatrix[j][i] = 1;
						degreeCountsOfEachBidder[i] ++ ;
						degreeCountsOfEachBidder[j] ++ ;
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
	
	// TODO: repair this
	private void Centralized_aysnc() {
		if (winnerDeterminationAlgo.equals("ours")) {

			// mapping bidders and their weight
			HashMap<Integer, Double> map = new HashMap<>();
			for (int i = 0; i < totalBidders; i++) {
				map.put(i, bidders.get(i).getPriorityByDegreeAndSquare());
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
						currentBidder.setPaymentByDegree(0);
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
						if (bidders.get(i).getPriorityByDegreeAndSquare() > temp) {
							temp = bidders.get(i).getPriorityByDegreeAndSquare();
						}
					}
				}
				currentBidder.setChoice(1);
				currentBidder.setPaymentByDegree(temp * Math.sqrt(degreeCountsOfEachBidder[entry.getKey()]+1));
			}
		} else { // LOS02
			
			// mapping bidders and their weight
			HashMap<Integer, Double> map = new HashMap<>();
			for (int i = 0; i < totalBidders; i++) {
				map.put(i, bidders.get(i).getPriorityByGoodsAndSquare());
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
						currentBidder.setPaymentByGoods(0);
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
						if (bidders.get(i).getPriorityByGoodsAndSquare() > temp) {
							temp = bidders.get(i).getPriorityByGoodsAndSquare();
						}
					}
				}
				currentBidder.setChoice(1);
				currentBidder.setPaymentByGoods(temp * Math.sqrt(bundleCountsOfEachBidder[entry.getKey()]));
			}
		}
	}

	private void Game_async() {
		int flag;
		int newDecision;
		if (winnerDeterminationAlgo.equals("ours")) {
			for (int step = 1;; step++) {
				flag = 0;
				for (int i = 0; i < totalBidders; i++) {
					Bidder bidder = bidders.get(i);
					newDecision = ourWinnerDetermination(i);
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
				if (flag == 0)
					break;
			}
			calculateCriticalValue();
		} else { // LOS02
			for (int step = 1;; step++) {
				flag = 0;
				for (int i = 0; i < totalBidders; i++) {
					Bidder bidder = bidders.get(i);
					newDecision = LOS02WinnerDetermination(i);
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
				if (flag == 0)
					break;
			}
			calculateCriticalValue();
		}
		if(amountRangeOfEachGood == 1){
			checkIndependentAndDomination();
		} else {
			checkGoodStore();
		}
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
	
	private void calculateCriticalValue() {
		if(winnerDeterminationAlgo.equals("ours")){
			for(int i=0; i<totalBidders; i++){
				// winner
				if (bidders.get(i).getChoice() == 1) {
					double temp=-1;
					bidders.get(i).setChoice(0);
					for(int j=0; j<totalBidders; j++){
						if(conflictionMatrix[i][j]==1 && bidders.get(j).getChoice()==0) {
							if(ourWinnerDetermination(j) == 1 && bidders.get(j).getPriorityByDegreeAndSquare()>temp) {
								temp = bidders.get(j).getPriorityByDegreeAndSquare();
							}
						}
					}
					bidders.get(i).setChoice(1);
					if(temp==-1){
						bidders.get(i).setPaymentByDegree(0);
						bidders.get(i).setCriticalValue(0);
					} else {
						bidders.get(i).setPaymentByDegree(temp * Math.sqrt(degreeCountsOfEachBidder[i]+1));
						bidders.get(i).setCriticalValue(temp * Math.sqrt(degreeCountsOfEachBidder[i]+1));
					}
				} else {  // loser
					double temp = 999999999;
					for(int j=0; j<totalBidders; j++){
						if (conflictionMatrix[i][j] == 1 && bidders.get(j).getChoice() == 1){
							bidders.get(j).setChoice(0);
							if(ourWinnerDetermination(i)==1){
								if(bidders.get(j).getPriorityByDegreeAndSquare()<temp)
									temp = bidders.get(j).getPriorityByDegreeAndSquare();
							}
							bidders.get(j).setChoice(1);
						}
					}
					bidders.get(i).setCriticalValue(temp * Math.sqrt(degreeCountsOfEachBidder[i]+1));
					bidders.get(i).setPaymentByDegree(0);
				}
			}
		} else {
			for(int i=0; i<totalBidders; i++){
				// winner
				if (bidders.get(i).getChoice() == 1) {
					double temp=-1;
					bidders.get(i).setChoice(0);
					for(int j=0; j<totalBidders; j++){
						if(conflictionMatrix[i][j]==1 && bidders.get(j).getChoice()==0) {
							if(LOS02WinnerDetermination(j) == 1 && bidders.get(j).getPriorityByGoodsAndSquare()>temp) {
								temp = bidders.get(j).getPriorityByGoodsAndSquare();
							}
						}
					}
					bidders.get(i).setChoice(1);
					if(temp==-1){
						bidders.get(i).setPaymentByGoods(0);
						bidders.get(i).setCriticalValue(0);
					} else {
						bidders.get(i).setPaymentByGoods(temp * Math.sqrt(bundleCountsOfEachBidder[i]));
						bidders.get(i).setCriticalValue(temp * Math.sqrt(bundleCountsOfEachBidder[i]));
					}
				} else {  // loser
					double temp = 999999999;
					for(int j=0; j<totalBidders; j++){
						if (conflictionMatrix[i][j] == 1 && bidders.get(j).getChoice() == 1){
							bidders.get(j).setChoice(0);
							if(LOS02WinnerDetermination(i)==1){
								if(bidders.get(j).getPriorityByGoodsAndSquare()<temp)
									temp = bidders.get(j).getPriorityByGoodsAndSquare();
							}
							bidders.get(j).setChoice(1);
						}
					}
					bidders.get(i).setCriticalValue(temp * Math.sqrt(bundleCountsOfEachBidder[i]));
					bidders.get(i).setPaymentByGoods(0);
				}
			}			
		}
	}

	private int ourWinnerDetermination(int currentBidder) {
		
		for (int i = 0; i < totalBidders; i++) {
			if (conflictionMatrix[currentBidder][i] == 1 && bidders.get(i).getChoice() == 1) {
				if (bidders.get(i).getPriorityByDegreeAndSquare() > bidders.get(currentBidder).getPriorityByDegreeAndSquare()){
					if (bidders.get(currentBidder).getChoice()==1){
						for(int j=0;j<totalGoods;j++){
							if(bidders.get(currentBidder).getBundle()[j]>0 && bidders.get(i).getBundle()[j]>0 && goodStore[j]<0){
								return 0;
							}
						}
					} else {
						for(int j=0;j<totalGoods;j++){
							if(bidders.get(currentBidder).getBundle()[j]>0 && bidders.get(i).getBundle()[j]>0 && goodStore[j]-bidders.get(currentBidder).getBundle()[j]<0)
								return 0;
						}
					}
				}
				if (bidders.get(i).getPriorityByDegreeAndSquare() == bidders.get(currentBidder).getPriorityByDegreeAndSquare() && bidders.get(i).getID() < bidders.get(currentBidder).getID()){
					if (bidders.get(currentBidder).getChoice()==1){
						for(int j=0;j<totalGoods;j++){
							if(bidders.get(currentBidder).getBundle()[j]>0 && bidders.get(i).getBundle()[j]>0 && goodStore[j]<0){
								return 0;
							}
						}
					} else {
						for(int j=0;j<totalGoods;j++){
							if(bidders.get(currentBidder).getBundle()[j]>0 && bidders.get(i).getBundle()[j]>0 && goodStore[j]-bidders.get(currentBidder).getBundle()[j]<0)
								return 0;
						}
					}					
				}
			}
		}
		return 1;
	}

	private int LOS02WinnerDetermination(int currentBidder) {		
		for (int i = 0; i < totalBidders; i++) {
			if (conflictionMatrix[currentBidder][i] == 1 && bidders.get(i).getChoice() == 1) {
				if (bidders.get(i).getPriorityByGoodsAndSquare() > bidders.get(currentBidder).getPriorityByGoodsAndSquare()){
					if (bidders.get(currentBidder).getChoice()==1){
						for(int j=0;j<totalGoods;j++){
							if(bidders.get(currentBidder).getBundle()[j]>0 && bidders.get(i).getBundle()[j]>0 && goodStore[j]<0){
								return 0;
							}
						}
					} else {
						for(int j=0;j<totalGoods;j++){
							if(bidders.get(currentBidder).getBundle()[j]>0 && bidders.get(i).getBundle()[j]>0 && goodStore[j]-bidders.get(currentBidder).getBundle()[j]<0)
								return 0;
						}
					}
				}
				if (bidders.get(i).getPriorityByGoodsAndSquare() == bidders.get(currentBidder).getPriorityByGoodsAndSquare() && bidders.get(i).getID() < bidders.get(currentBidder).getID()){
					if (bidders.get(currentBidder).getChoice()==1){
						for(int j=0;j<totalGoods;j++){
							if(bidders.get(currentBidder).getBundle()[j]>0 && bidders.get(i).getBundle()[j]>0 && goodStore[j]<0){
								return 0;
							}
						}
					} else {
						for(int j=0;j<totalGoods;j++){
							if(bidders.get(currentBidder).getBundle()[j]>0 && bidders.get(i).getBundle()[j]>0 && goodStore[j]-bidders.get(currentBidder).getBundle()[j]<0)
								return 0;
						}
					}					
				}
			}
		}
		return 1;
	}
}
