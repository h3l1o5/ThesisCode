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
	private String winnerDeteminationAlgo;
	private String type;
	private int totalGoods;
	private int totalBidders;
	private int[][] conflictionMatrix;
	int[] degreeCountsOfEachBidder;
	public int[] bundleCountsOfEachBidder;
	List<Bidder> bidders;

	public CombinatorialAuction(int size, int totalGoods) {
		this.totalBidders = size;
		this.totalGoods = totalGoods;
		conflictionMatrix = new int[size][size];
		bundleCountsOfEachBidder = new int[size];
		degreeCountsOfEachBidder = new int[size];
		initBidders();
		initBundlesAndConflictionMatrix();
		initBidderWeight(100);
		randomizeBidderID();

		for (int i = 0; i < size; i++) {
			bidders.get(i).setPriorityByDegreeAndSquare((double) bidders.get(i).getWeight() / (degreeCountsOfEachBidder[i]+1));
			bidders.get(i).setPriorityByGoodsAndSquare((double) bidders.get(i).getWeight() / (bundleCountsOfEachBidder[i]));
		}
	}
	
	public List<Bidder> getBidders() {
		return bidders;
	}

	public void start(String winnerDeterminationAlgo, String type) {
		this.type = type;
		this.winnerDeteminationAlgo = winnerDeterminationAlgo;
		if (type.equals("game")) {
			Game_async();
		} else {
			Centralized_aysnc();
		}
	}

	private void initBidders() {
		bidders = new ArrayList<>();
		for (int i = 0; i < totalBidders; i++) {
			Bidder bidder = new Bidder(i, totalBidders, 1, totalGoods);
			bidders.add(bidder);
		}
	}

	private void initBundlesAndConflictionMatrix() {
		Random ran = new Random();
		
		// for each bidder, random choose to take or not take a good. 
		for (int i = 0; i < totalBidders; i++) {
			Bidder bidder = bidders.get(i);
			for (int j = 0; j < totalGoods; j++) {
				if (ran.nextInt(25) % 25 == 0) {
					bidder.setBundle(j, 1);
					bundleCountsOfEachBidder[i] ++ ;
				}
			}
		}

		// make a adjacency matrix represent of the confliction of every bidder
		for (int i = 0; i < totalBidders; i++) {
			for (int j = i + 1; j < totalBidders; j++) {
				for (int k = 0; k < totalGoods; k++) {
					if (bidders.get(i).getBundle()[k] == 1 && bidders.get(j).getBundle()[k] == 1) {
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
	
	public void initBidderWeight(int goodWeightRange) {
		Random ran = new Random();
		int bidderWeight = 0;
		int[] weightOfGood = new int[totalGoods];
		for (int i = 0; i < totalGoods; i++) {
			weightOfGood[i] = ran.nextInt(goodWeightRange) + 1;
		}
		for (int i = 0; i < totalBidders; i++) {
			for (int j = 0; j < totalGoods; j++) {
				if (bidders.get(i).getBundle()[j] == 1) {
					double normalRandomNumber = 0;
					while (normalRandomNumber <= 0) {
						normalRandomNumber = ran.nextGaussian() * (weightOfGood[j] / 5) + weightOfGood[j];
					}
					bidderWeight += normalRandomNumber;
				}
			}
			bidders.get(i).setWeight(bidderWeight);
			bidderWeight = 0;
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
	
	private void Centralized_aysnc() {
		if (winnerDeteminationAlgo.equals("ours")) {

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
		if (winnerDeteminationAlgo.equals("ours")) {
			for (int step = 1;; step++) {
				flag = 0;
				for (int i = 0; i < totalBidders; i++) {
					newDecision = ourWinnerDetermination(i);
					if (bidders.get(i).getChoice() != newDecision) {
						bidders.get(i).setChoice(newDecision);
						flag = 1;
						break;
					}
				}
				if (flag == 0)
					break;
			}
			for (int i = 0; i < totalBidders; i++) {
				if (bidders.get(i).getChoice() == 1) {
					double temp = 0;
					for (int j = 0; j < totalBidders; j++) {
						if (conflictionMatrix[i][j] == 1) {
							if (bidders.get(j).getPriorityByDegreeAndSquare() > temp)
								temp = bidders.get(j).getPriorityByDegreeAndSquare();
						}
					}
					bidders.get(i).setPaymentByDegree(temp * (degreeCountsOfEachBidder[i]+1));
				} else {
					bidders.get(i).setPaymentByDegree(0);
				}
			}
		} else { // LOS02
			for (int step = 1;; step++) {
				flag = 0;
				for (int i = 0; i < totalBidders; i++) {
					newDecision = LOS02WinnerDetermination(i);
					if (bidders.get(i).getChoice() != newDecision) {
						bidders.get(i).setChoice(newDecision);
						flag = 1;
						break;
					}
				}
				if (flag == 0)
					break;
			}
			for (int i = 0; i < totalBidders; i++) {
				if (bidders.get(i).getChoice() == 1) {
					double temp = 0;
					for (int j = 0; j < totalBidders; j++) {
						if (conflictionMatrix[i][j] == 1) {
							if (bidders.get(j).getPriorityByGoodsAndSquare() > temp)
								temp = bidders.get(j).getPriorityByGoodsAndSquare();
						}
					}
					bidders.get(i).setPaymentByGoods(temp * (bundleCountsOfEachBidder[i]));
				} else {
					bidders.get(i).setPaymentByGoods(0);
				}
			}
		}
		checkResult();
	}

	private void checkResult() {
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
				System.out.println("不是independent");
			} else if (flag == 2) {
				System.out.println("不是dominating");
			}
		}
	}
	
	private void calculateCriticalValue() {
		for(int i=0; i<totalBidders; i++){
			// winner
			if (bidders.get(i).getChoice() == 1) {
				
			} else {  // loser
				double temp = 999999999;
				for(int j=0; j<totalBidders; j++){
					if (conflictionMatrix[i][j] == 1 && bidders.get(j).getChoice() == 1){
						if (bidders.get(j).getPriorityByDegreeAndSquare() < temp)
							temp = bidders.get(j).getPriorityByDegreeAndSquare();
					}
				}
				bidders.get(i).setCriticalValue(temp * Math.sqrt(degreeCountsOfEachBidder[i]+1));
			}
		}
	}

	private int ourWinnerDetermination(int currentBidder) {
		double m_current, m_compare;
		int[] degree = new int[totalBidders];

		for (int i = 0; i < totalBidders; i++) {
			for (int j = 0; j < totalBidders; j++) {
				if (conflictionMatrix[i][j] == 1)
					degree[i]++;
			}
		}

		m_current = (double) bidders.get(currentBidder).getWeight() / (degree[currentBidder] + 1);

		for (int i = 0; i < totalBidders; i++) {
			if (conflictionMatrix[currentBidder][i] == 1 && bidders.get(i).getChoice() == 1) {
				m_compare = (double) bidders.get(i).getWeight() / (degree[i] + 1);
				if (m_compare > m_current)
					return 0;
				if (m_compare == m_current && bidders.get(i).getID() < bidders.get(currentBidder).getID())
					return 0;
			}
		}
		return 1;
	}

	private int LOS02WinnerDetermination(int currentBidder) {
		double m_current, m_compare;
		int[] goods = new int[totalBidders];

		for (int i = 0; i < totalBidders; i++) {
			for (int j = 0; j < totalGoods; j++) {
				if (bidders.get(i).getBundle()[j] == 1)
					goods[i]++;
			}
		}

		m_current = (double) bidders.get(currentBidder).getWeight() / goods[currentBidder];

		for (int i = 0; i < totalBidders; i++) {
			if (conflictionMatrix[currentBidder][i] == 1 && bidders.get(i).getChoice() == 1) {
				m_compare = (double) bidders.get(i).getWeight() / goods[i];
				if (m_compare > m_current)
					return 0;
				if (m_compare == m_current && bidders.get(i).getID() < bidders.get(currentBidder).getID())
					return 0;
			}
		}
		return 1;
	}
}
