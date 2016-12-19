import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class FileUtils {
	private FileUtils(){};
	
	public static int[][] readfile(String topologyModel, int size,float P,int file) throws IOException {
		int[][] admatrix = new int[size][size];
		if (topologyModel == "ER") {
			FileReader fr = new FileReader("../Topology/" + topologyModel + "/30/P=" + P + "/30_" + file + ".txt");
			BufferedReader br = new BufferedReader(fr);

			int i = 0, j = 0;
			while (br.ready()) {
				admatrix[i][j] = Integer.parseInt(br.readLine());
				j++;
				if (j >= size) {
					i++;
					j = 0;
				}
			}
			fr.close();
			return admatrix;
		}
		else if (topologyModel == "BA") {
			FileReader fr = new FileReader("../Topology/" + topologyModel + "/30/m=" + (int) P + "/30_" + file + ".txt");
			BufferedReader br = new BufferedReader(fr);

			int i = 0, j = 0;
			while (br.ready()) {
				admatrix[i][j] = Integer.parseInt(br.readLine());
				j++;
				if (j >= size) {
					i++;
					j = 0;
				}
			}
			fr.close();
			return admatrix;
		}
		else if (topologyModel == "UDG") {
			FileReader fr = new FileReader("../Topology/" + topologyModel + "/n" + (int) P + "/" + (int) P + "_" + file + ".txt");
			BufferedReader br = new BufferedReader(fr);

			int i = 0, j = 0;
			while (br.ready()) {
				admatrix[i][j] = Integer.parseInt(br.readLine());
				j++;
				if (j >= size) {
					i++;
					j = 0;
				}
			}
			fr.close();
			return admatrix;
		}
		else {
			FileReader fr = new FileReader("../Topology/" + topologyModel + "/30/P=" + P + "/30_" + file + ".txt");
			BufferedReader br = new BufferedReader(fr);

			int i = 0, j = 0;
			while (br.ready()) {
				admatrix[i][j] = Integer.parseInt(br.readLine());
				j++;
				if (j >= size) {
					i++;
					j = 0;
				}
			}
			fr.close();
			return admatrix;
		}
	}
}
