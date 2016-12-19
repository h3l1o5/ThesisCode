package Model;

import org.omg.PortableServer.AdapterActivator;

public class Bidder extends Player {
	private double magicNumberByGoods;
	private double magicNumberByDegree;
	private double paymentByDegree;
	private double paymentByGoods;
	private int[] bundle;
	private int totalGoods;
	
	public Bidder(int ID,int size,double weight,int totalGoods){
		super(ID, size,weight);
		this.totalGoods = totalGoods;
		bundle = new int[totalGoods];
	}

	public double getMagicNumberByGoods() {
		return magicNumberByGoods;
	}

	public void setMagicNumberByGoods(double magicNumberByGoods) {
		this.magicNumberByGoods = magicNumberByGoods;
	}

	public double getMagicNumberByDegree() {
		return magicNumberByDegree;
	}

	public void setMagicNumberByDegree(double magicNumberByDegree) {
		this.magicNumberByDegree = magicNumberByDegree;
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
