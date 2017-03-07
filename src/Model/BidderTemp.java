package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BidderTemp {
	private UUID ID;
	private boolean decision;
	private double bid;
	private double priority;
	private double criticalValue;
	private double payment;
	private Map<UUID, Boolean> competitors;
	private Map<Integer, Integer> bundle;
	
	public BidderTemp() {
		ID = UUID.randomUUID();
		decision = false;
		bid = 0;
		priority = 0;
		criticalValue = 0;
		payment = 0;
		competitors = new HashMap<>();
		bundle = new HashMap<>();
	}

	public UUID getID() {
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
	public Map<UUID, Boolean> getAllCompetitor() {
		return competitors;
	}

	/**
	 * 
	 * @param uuid id of some bidder
	 * @return whether this bidder is a competitor
	 * 
	 * */
	public boolean getCompetitor(UUID uuid) {
		return competitors.get(uuid);
	}
	
	public int getCompetitorCount() {
		int count = 0;
		for (Map.Entry<UUID, Boolean> entry : competitors.entrySet()) {
			if (entry.getValue() == true) {
				count ++;
			}
		}
		return count;
	}
	
	public void setCompetitor(UUID uuid, boolean isCompetitor) {
		competitors.put(uuid, isCompetitor);
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
	public int getBundle(int goodType) {
		return bundle.get(goodType);
	}
	
	public int getBundleInstanceCount() {
		int count = 0;
		for (Map.Entry<Integer, Integer> entry : bundle.entrySet()) {
			count += entry.getValue();
		}
		return count;
	}
}


