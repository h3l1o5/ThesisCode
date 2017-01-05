package Model;

import org.omg.PortableServer.AdapterActivator;

public class Bidder extends Player {
	private double priorityByGoodsAndSquare;
	private double priorityByDegreeAndSquare;
	private double paymentByDegree;
	private double paymentByGoods;
	private double criticalValue;

	private int[] bundle;
	private int totalGoods;
	
	public Bidder(int ID,int size,double weight,int totalGoods){
		super(ID, size,weight);
		this.totalGoods = totalGoods;
		bundle = new int[totalGoods];
	}
	
	public double getCriticalValue() {
		return criticalValue;
	}
	public void setCriticalValue(double criticalValue) {
		this.criticalValue = criticalValue;
	}

	public double getPriorityByGoodsAndSquare() {
		return priorityByGoodsAndSquare;
	}

	public void setPriorityByGoodsAndSquare(double PriorityByGoodsAndSquare) {
		this.priorityByGoodsAndSquare = PriorityByGoodsAndSquare;
	}

	public double getPriorityByDegreeAndSquare() {
		return priorityByDegreeAndSquare;
	}

	public void setPriorityByDegreeAndSquare(double PriorityByDegreeAndSquare) {
		this.priorityByDegreeAndSquare = PriorityByDegreeAndSquare;
	}

	public double getPaymentByDegree() {
		return paymentByDegree;
	}

	public void setPaymentByDegree(double payment) {
		paymentByDegree = payment;
	}

	public double getPaymentByGoods() {
		return paymentByGoods;
	}

	public void setPaymentByGoods(double paymentByGoods) {
		this.paymentByGoods = paymentByGoods;
	}

	public int getBundleCount() {
		int count=0;
		for(int i=0;i<totalGoods;i++){
			if(bundle[i]==1) count++;
		}
		return count;
	}

	public int[] getBundle() {
		return bundle;
	}

	public void setBundle(int position, int choose) {
		bundle[position] = choose;
	}
}
