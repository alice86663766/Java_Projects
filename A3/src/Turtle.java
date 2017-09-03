import java.util.ArrayList;
import java.util.Random;

/**
 * The Turtle class, used to represent a turtle.
 * @author Chen, Hung-Yu
 */
public class Turtle extends Animal {
	/**
	 * Get name of the animal.
	 * @return Name of the animal.
	 */
	public String getName() {
		return "Turtle";
	}
	/**
	 * Get symbol of the animal.
	 * @return Symbol of the animal.
	 */
	public char getAlias() {
		return 'u';
	}
	/**
	 * Check if the animal can win in attack.
	 * @param b The animal being attacked.
	 * @return If the attacking animal wins.
	 */
	public boolean attackWin(Animal b) {
		Random rand = new Random();
		int num = rand.nextInt(2);
		if (num == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Move the animal.
	 * @param row Number of rows in the forest. 
	 * @param col Number of columns in the forest.
	 * @return The path of the animal movement in one round.
	 */
	public ArrayList<int[]> move(int row, int col) {
		ArrayList <int[]> path = new ArrayList<int[]>();
		int a[] = {this.location[0], this.location[1]};
		path.add(a);
		
		ArrayList <int[]> movement = new ArrayList<int[]>();
		movement.add(new int[] {0,1});
		movement.add(new int[] {0,-1});
		movement.add(new int[] {1,0});
		movement.add(new int[] {-1,0});
		
		Random rand2 = new Random();
		int num2 = rand2.nextInt(2);
		
		if (num2 == 0) {
			path.add(a);
		}
		
		else {
			boolean valid = false;
			while (!valid) {
				Random rand = new Random();
				int num = rand.nextInt(movement.size());
				int x = movement.get(num)[0] + a[0];
				int y = movement.get(num)[1] + a[1];
				if (x >= 0 && x < row && y >= 0 && y < col) {
					int[] arr = new int[2];
					arr[0] = x;
					arr[1] = y;
					path.add(arr);
					valid = true;
				}
			}
		}
		return path;
	}
}
