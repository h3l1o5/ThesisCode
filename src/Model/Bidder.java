package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bidder {
	private int ID;
	private boolean decision;
	private double bid;
	private double priority;
	private int totalConflictGood;
	private double criticalValue;
	private double payment;
	private Map<Bidder, Integer> competitors;
	private Map<Integer, Integer> bundle;
	
	public Bidder(int ID) {
		this.ID = ID;
		decision = false;
		bid = 0;
		priority = 0;
		totalConflictGood = 0;
		criticalValue = 0;
		payment = 0;
		competitors = new HashMap<>();
		bundle = new HashMap<>();
	}
	
	@Override
	public String toString() {
		return Integer.toString(ID);
	}

	public int getID() {
		return ID;
	}

	public boolean getDecision() {
		return decision;
	}

	public void setDecision(boolean decision) {
		this.decision = decision;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public double getPriority() {
		return priority;
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}

	public double getCriticalValue() {
		return criticalValue;
	}

	public void setCriticalValue(double criticalValue) {
		this.criticalValue = criticalValue;
	}
	
	public double getPayment() {
		return payment;
	}
	
	public void setPayment(double payment) {
		this.payment = payment;
	}
	
	/**
	 * 
	 * @return complete competitor list of this bidder
	 * 
	 */
	public Map<Bidder, Integer> getAllCompetitor() {
		return competitors;
	}

	/**
	 * 
	 * @param ID id of some bidder
	 * @return whether this bidder is a competitor
	 * 
	 * */
	public int getCompetitor(int ID) throws NullPointerException{
		try {
			return competitors.get(ID);
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
	public int getCompetitorCount() {
		int count = 0;
		for (Map.Entry<Bidder, Integer> entry : competitors.entrySet()) {
			if (entry.getValue() > 0) {
				count ++;
			}
		}
		return count;
	}
	
	public void setCompetitor(Bidder bidder, int numberOfConflict) {
		competitors.put(bidder, numberOfConflict);
	}
	
	public Map<Integer, Integer> getWholeBundle() {
		return bundle;
	}
	
	
	/**
	 * 
	 * @param goodType type of good
	 * @return instance count of this type of good in bundle
	 * 
	 * */
	public int getBundle(int goodType) throws NullPointerException{
		try {
			return bundle.get(goodType);
		} catch(NullPointerException e) {
			return 0;
		}
	}
	
	public int getBundleInstanceCount() {
		int count = 0;
		for (Map.Entry<Integer, Integer> entry : bundle.entrySet()) {
			count += entry.getValue();
		}
		return count;
	}
	
	public void setBundle(int goodType, int amount) {
		bundle.put(goodType, amount);
	}
	
	public int getTotalConflictGood() {
		return totalConflictGood;
	}
	
	public void setTotalConflictGood(int totalConflictGood) {
		this.totalConflictGood = totalConflictGood;
	}
}


