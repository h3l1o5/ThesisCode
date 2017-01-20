package Model;

public class Bidder {
	private int ID;
	private int choice;
	private int[] neighbors;
	private int[] bundle;
	private int totalGoods;
	private int totalBidders;
	private double weight;
	private double priority;
	private double payment;
	private double criticalValue;
	
	public Bidder(int ID, int totalBidders, int totalGoods){
		this.ID = ID;
		this.totalGoods = totalGoods;
		this.totalBidders = totalBidders;
		bundle = new int[totalGoods];
		neighbors = new int[totalBidders];
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getPriority() {
		return priority;
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public double getCriticalValue() {
		return criticalValue;
	}
	public void setCriticalValue(double criticalValue) {
		this.criticalValue = criticalValue;
	}

	public int getBundleCount() {
		int count=0;
		for(int i=0;i<totalGoods;i++){
			if(bundle[i]>=1) count += bundle[i];
		}
		return count;
	}

	public int[] getBundle() {
		return bundle;
	}

	public void setBundle(int position, int choose) {
		bundle[position] = choose;
	}
	
	public int getNeighborCount() {
		int count=0;
		for(int i=0;i<totalBidders;i++){
			if(neighbors[i]==1) count++;
		}
		return count;
	}
	
	public int[] getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(int position,int isNeighbor) {
		neighbors[position] = isNeighbor;
	}
}
