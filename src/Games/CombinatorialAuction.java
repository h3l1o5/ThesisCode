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
	private String heuristic;
	private String type;
	private int totalGoods;
	private int size;
	private int[][] admatrix;
	int[] degree;
	int[] goods; // each bidder's goods count
	List<Bidder> bidders;

	public CombinatorialAuction(int size, int totalGoods) {
		this.size = size;
		this.totalGoods = totalGoods;
		admatrix = new int[size][size];
		initBidders();
		initBundlesAndMatrix();
		initBidderWeight(100);
		randomizeBidderID();

		degree = new int[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (admatrix[i][j] == 1)
					degree[i]++;
			}
		}

		goods = new int[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < totalGoods; j++) {
				if (bidders.get(i).getBundle()[j] == 1)
					goods[i]++;
			}
		}
		for (int i = 0; i < size; i++) {
			bidders.get(i).setMagicNumberByDegree((double) bidders.get(i).getWeight() / Math.sqrt(degree[i]) + 1);
			bidders.get(i).setMagicNumberByGoods((double) bidders.get(i).getWeight() / Math.sqrt(goods[i]));
		}
	}

	public void start(String heuristic, String type) {
		this.type = type;
		this.heuristic = heuristic;
		if (type.equals("game")) {
			Game_async();
		} else {
			Centralized_aysnc();
		}
	}

	private void initBidders() {
		bidders = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			Bidder bidder = new Bidder(i, size, 1, totalGoods);
			bidders.add(bidder);
		}
	}

	private void initBundlesAndMatrix() {
		Random ran = new Random();
		for (int i = 0; i < size; i++) {
			Bidder bidder = bidders.get(i);
			for (int j = 0; j < totalGoods; j++) {
				if (ran.nextInt(25) % 25 == 0)
					bidder.getBundle()[j] = 1;
			}
		}

		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				for (int k = 0; k < totalGoods; k++) {
					if (bidders.get(i).getBundle()[k] == 1 && bidders.get(j).getBundle()[k] == 1) {
						admatrix[i][j] = 1;
						admatrix[j][i] = 1;
						break;
					}
				}
			}
		}
	}

	private void Centralized_aysnc() {
		if (heuristic.equals("ours")) {

			// mapping bidders and their weight
			HashMap<Integer, Double> map = new HashMap<>();
			for (int i = 0; i < size; i++) {
				map.put(i, bidders.get(i).getMagicNumberByDegree());
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
				for (int i = 0; i < size; i++) {
					if (admatrix[entry.getKey()][i] == 1) {
						if (bidders.get(i).getMagicNumberByDegree() > temp) {
							temp = bidders.get(i).getMagicNumberByDegree();
						}
					}
				}
				currentBidder.setChoice(1);
				currentBidder.setPaymentByDegree(temp * Math.sqrt(degree[entry.getKey()]) + 1);
			}
		} else { // LOS02
			
			// mapping bidders and their weight
			HashMap<Integer, Double> map = new HashMap<>();
			for (int i = 0; i < size; i++) {
				map.put(i, bidders.get(i).getMagicNumberByGoods());
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
				for (int i = 0; i < size; i++) {
					if (admatrix[entry.getKey()][i] == 1) {
						if (bidders.get(i).getMagicNumberByGoods() > temp) {
							temp = bidders.get(i).getMagicNumberByGoods();
						}
					}
				}
				currentBidder.setChoice(1);
				currentBidder.setPaymentByGoods(temp * Math.sqrt(goods[entry.getKey()]));
			}
		}
	}

	private void Game_async() {
		int flag;
		int newDecision;
		if (heuristic.equals("ours")) {
			for (int step = 1;; step++) {
				flag = 0;
				for (int i = 0; i < size; i++) {
					newDecision = heuristic(i);
					if (bidders.get(i).getChoice() != newDecision) {
						bidders.get(i).setChoice(newDecision);
						flag = 1;
						break;
					}
				}
				if (flag == 0)
					break;
			}
			for (int i = 0; i < size; i++) {
				if (bidders.get(i).getChoice() == 1) {
					double temp = 0;
					for (int j = 0; j < size; j++) {
						if (admatrix[i][j] == 1) {
							if (bidders.get(j).getMagicNumberByDegree() > temp)
								temp = bidders.get(j).getMagicNumberByDegree();
						}
					}
					bidders.get(i).setPaymentByDegree(temp * Math.sqrt(degree[i]) + 1);
				} else {
					bidders.get(i).setPaymentByDegree(0);
				}
			}
		} else { // LOS02
			for (int step = 1;; step++) {
				flag = 0;
				for (int i = 0; i < size; i++) {
					newDecision = heuristic2(i);
					if (bidders.get(i).getChoice() != newDecision) {
						bidders.get(i).setChoice(newDecision);
						flag = 1;
						break;
					}
				}
				if (flag == 0)
					break;
			}
			for (int i = 0; i < size; i++) {
				if (bidders.get(i).getChoice() == 1) {
					double temp = 0;
					for (int j = 0; j < size; j++) {
						if (admatrix[i][j] == 1) {
							if (bidders.get(j).getMagicNumberByGoods() > temp)
								temp = bidders.get(j).getMagicNumberByGoods();
						}
					}
					bidders.get(i).setPaymentByGoods(temp * Math.sqrt(goods[i]));
				} else {
					bidders.get(i).setPaymentByGoods(0);
				}
			}
		}
		checkResult();
	}

	private void checkResult() {
		int flag = 0;

		for (int i = 0; i < size; i++) {
			if (bidders.get(i).getChoice() == 1) {
				for (int j = 0; j < size; j++) {
					if (admatrix[i][j] == 1 && bidders.get(j).getChoice() == 1) {
						flag = 1;
						break;
					}
				}
			} else {
				flag = 2;
				for (int j = 0; j < size; j++) {
					if (admatrix[i][j] == 1 && bidders.get(j).getChoice() == 1) {
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

	private int heuristic(int currentBidder) {
		double m_current, m_compare;
		int[] degree = new int[size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (admatrix[i][j] == 1)
					degree[i]++;
			}
		}

		m_current = (double) bidders.get(currentBidder).getWeight() / (degree[currentBidder] + 1);

		for (int i = 0; i < size; i++) {
			if (admatrix[currentBidder][i] == 1 && bidders.get(i).getChoice() == 1) {
				m_compare = (double) bidders.get(i).getWeight() / (degree[i] + 1);
				if (m_compare > m_current)
					return 0;
				if (m_compare == m_current && bidders.get(i).getID() < bidders.get(currentBidder).getID())
					return 0;
			}
		}
		return 1;
	}

	private int heuristic2(int currentBidder) {
		double m_current, m_compare;
		int[] goods = new int[size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < totalGoods; j++) {
				if (bidders.get(i).getBundle()[j] == 1)
					goods[i]++;
			}
		}

		m_current = (double) bidders.get(currentBidder).getWeight() / goods[currentBidder];

		for (int i = 0; i < size; i++) {
			if (admatrix[currentBidder][i] == 1 && bidders.get(i).getChoice() == 1) {
				m_compare = (double) bidders.get(i).getWeight() / goods[i];
				if (m_compare > m_current)
					return 0;
				if (m_compare == m_current && bidders.get(i).getID() < bidders.get(currentBidder).getID())
					return 0;
			}
		}
		return 1;
	}

	public List<Bidder> getBidders() {
		return bidders;
	}

	public void randomizeBidderID() {
		Random ran = new Random();
		int target;
		int temp;
		for (int i = 0; i < size; i++) {
			temp = bidders.get(i).getID();
			target = ran.nextInt(size);
			bidders.get(i).setID(bidders.get(target).getID());
			bidders.get(target).setID(temp);
		}
	}

	public void initBidderWeight(int goodWeightRange) {
		Random ran = new Random();
		int bidderWeight = 0;
		int[] weightOfGood = new int[totalGoods];
		for (int i = 0; i < totalGoods; i++) {
			weightOfGood[i] = ran.nextInt(goodWeightRange) + 1;
		}
		for (int i = 0; i < size; i++) {
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
}
