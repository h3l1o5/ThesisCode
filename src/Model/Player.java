package Model;

public class Player {
	private int ID;
	private double weight;
	private int choice;
	private int[] neighbors;
	
	public Player(int ID,int size,double weight){
		this.ID = ID;
		this.weight = weight;
		neighbors = new int[size];
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}

	public int[] getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(int position,int isNeighbor) {
		neighbors[position] = isNeighbor;
	}
}
