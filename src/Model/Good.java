package Model;

import java.util.Random;

public class Good {
	private int type;
	private double valuation;
	private int instanceCount;
	private int left;

	public Good(int type, int rangeOfInstanceCount) {
		Random ran = new Random();
		this.type = type;
		valuation = ran.nextDouble()*10 + 10;
		switch (rangeOfInstanceCount) {
		case 0:
			System.out.println("Error: range of instance count must greater than 0");
			System.exit(2);
		case 1:
			instanceCount = 1;
			break;
		default:
			instanceCount = ran.nextInt(rangeOfInstanceCount) + 1;
			break;
		}
		resetLeft();
	}

	public int getType() {
		return type;
	}

	public double getValuation() {
		return valuation;
	}

	public int getInstanceCount() {
		return instanceCount;
	}
	
	public int getLeft() {
		return left;
	}
	public void take(int amountToTake) {
		left -= amountToTake;
	}
	
	public void put(int amountToPut) {
		left += amountToPut;
	}
		
	public void resetLeft() {
		left = instanceCount;
	}
	
}


