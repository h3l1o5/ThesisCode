package Games;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Model.Player;

public class FindMaximalIndepentSet {
	private String gameType;
	private String heuristic;
	private int size;
	private int[][] admatrix;
	List<Player> players;
	
	public FindMaximalIndepentSet(String gameType,String heuristic,int size,int[][] admatrix){
		this.gameType = gameType;
		this.heuristic = heuristic;
		this.size = size;
		this.admatrix = admatrix;
		
		players = new ArrayList<>();
		for(int i = 0;i<size;i++){
			Player player = new Player(i,size,1);
			for(int j = 0;j<size;j++){
				player.setNeighbors(j, admatrix[i][j]);
			}
			players.add(player);
		}
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public void randomizePlayerID(){
		Random ran = new Random();
		int target;
		int temp;
		for(int i = 0; i < size; i++){
			temp = players.get(i).getID();
			target = ran.nextInt(size);
			players.get(i).setID(players.get(target).getID());
			players.get(target).setID(temp);
		}
	}
	
	public void randomizePlayerWeight(int range){
		Random ran = new Random();
		for(int i = 0; i < size; i++){
			players.get(i).setWeight(ran.nextInt(range));
		}
	}

	public void start(){
		if(gameType.equals("asynchronize")) playGame_async(heuristic);
		else if(gameType.equals("synchronize")) playGame_sync(heuristic);
		else System.out.println("Can't find " + gameType + " game type");
	}
	
	private void playGame_async(String heuristic){
		Random ran = new Random();
		
		for(int i = 0; i<size; i++){
			players.get(i).setChoice(ran.nextInt(2));
		}
		
		int flag;
		int newDecision;
		if(heuristic.equals("gwmin")){
			for(int step = 1;;step++){
				flag=0;
				for(int i = 0;i<size;i++){
					newDecision = gwmin(i);
					if(players.get(i).getChoice() != newDecision){
						players.get(i).setChoice(newDecision);
						flag = 1;
						break;
					}
				}
				if (flag == 0) break;
			}
		}
		else{
			for(int step = 1;;step++){
				flag=0;
				for(int i = 0;i<size;i++){
					newDecision = gwmin2(i);
					if(players.get(i).getChoice() != newDecision){
						players.get(i).setChoice(newDecision);
						flag = 1;
						break;
					}
				}
				if (flag == 0) break;
			}
		}
		checkResult();
	}
	
	private void playGame_sync(String heuristic){
		Random ran = new Random();
		int[] tempChoice = new int[size];
		
		for(int i = 0; i<size; i++){
			int randomChoice = ran.nextInt(2);
			players.get(i).setChoice(randomChoice);
			tempChoice[i] = randomChoice;
		}
		
		int flag;
		if(heuristic.equals("gwmin")){
			for(int step = 1;;step++){
				flag=0;
				for(int i = 0;i < size;i++){
					if(players.get(i).getChoice() != gwmin(i)){
						flag = 1;
						break;
					}
				}
				if (flag == 0) break;
				
				for (int i = 0; i < size; i++) 
					tempChoice[i] = gwmin(i);
				
				for (int i = 0; i < size; i++)
					players.get(i).setChoice(tempChoice[i]);
			}
		}
		else{
			for(int step = 1;;step++){
				flag=0;
				for(int i = 0;i < size;i++){
					if(players.get(i).getChoice() != gwmin2(i)){
						flag = 1;
						break;
					}
				}
				if (flag == 0) break;
				
				for (int i = 0; i < size; i++) 
					tempChoice[i] = gwmin2(i);
				
				for (int i = 0; i < size; i++)
					players.get(i).setChoice(tempChoice[i]);
			}
		}
		checkResult();
	}
	
	private void checkResult(){
		int flag = 0;
		
		for(int i=0;i<size;i++){
			if (players.get(i).getChoice() == 1) {
				for (int j = 0; j < size; j++) {
					if (admatrix[i][j] == 1 && players.get(j).getChoice() == 1) {
						flag = 1;
						break;
					}
				}
			} else {
				flag = 2;
				for (int j = 0; j < size; j++) {
					if (admatrix[i][j] == 1 && players.get(j).getChoice() == 1) {
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
	
	private int gwmin(int currentPlayer){
		double m_current, m_compare;
		int[] degree = new int[size];
		
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(admatrix[i][j] == 1) degree[i]++;
			}
		}
		
		m_current = (double) players.get(currentPlayer).getWeight() / (degree[currentPlayer] + 1);
		
		for(int i = 0;i<size;i++){
			if(admatrix[currentPlayer][i] == 1 && players.get(i).getChoice() == 1){
				m_compare = (double) players.get(i).getWeight() / (degree[i] + 1);
				if(m_compare < m_current) return 0;
				if(m_compare == m_current && players.get(i).getID() < players.get(currentPlayer).getID()) return 0;
			}
		}
		return 1;
	}
	
	private int gwmin2(int currentPlayer){
		double m_current, m_compare, sum_current, sum_compare;

		sum_current = players.get(currentPlayer).getWeight();
		
		for(int i=0;i<size;i++){
			if(admatrix[currentPlayer][i]==1) sum_current += players.get(i).getWeight();
		}
		
		m_current = (double) players.get(currentPlayer).getWeight() / sum_current;
		
		for(int i =0;i<size;i++){
			if(admatrix[currentPlayer][i]==1&&players.get(i).getChoice()==1){
				sum_compare = players.get(i).getWeight();
				
				for(int j = 0;j< size; j++){
					if(admatrix[i][j]==1) sum_compare += players.get(j).getWeight();
				}
				
				m_compare = (double) players.get(i).getWeight() / sum_compare;
				if (m_compare < m_current)
					return 0;
				if(m_compare == m_current && players.get(i).getID() < players.get(currentPlayer).getID()) 
					return 0;
			}
		}
		return 1;
	}
}
