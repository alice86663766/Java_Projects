import java.util.ArrayList;
import java.util.Random;

/**
 * The Feline class, used to represent Feline category of animals.
 * @author Chen, Hung-Yu
 *
 */
public class Feline extends Animal{
	/**
	 * Get the category of the animal.
	 * @return Category of the animal.
	 */
	public String getType() {
		return "Feline";
	}
	
	/**
	 * Check if the animal can win in attack.
	 * @param b The animal being attacked.
	 * @return If the attacking animal wins.
	 */
	public boolean attackWin(Animal b) {
		if (b.getName().equals("Turtle")) {
			Random rand = new Random();
			int num = rand.nextInt(5);
			if (num == 0) {
				return true;
			}
		}
		if (b.getType().equals("Canine")) {
			return true;
		}
		return false;
	}
	/**
	 * Generate all possible relative movements.
	 * @return all possible relative movements.
	 */
	public ArrayList<int[]> movements() {
		ArrayList <int[]> movement = new ArrayList<int[]>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <=1; j++) {
				if (i != 0 || j != 0) {
					int[] arr = {this.location[0]+i, this.location[1]+j};
					movement.add(arr);
				}
			}
		}
		return movement;
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
		
		ArrayList <int[]> movement = this.movements();
		
		boolean valid = false;
		while (!valid) {
			Random rand = new Random();
			int num = rand.nextInt(movement.size());
			int x = movement.get(num)[0];
			int y = movement.get(num)[1];
			if (x >= 0 && x < row && y >= 0 && y < col) {
				int[] arr = new int[2];
				arr[0] = x;
				arr[1] = y;
				path.add(arr);
				valid = true;
			}
		}
		return path;
	}
}
