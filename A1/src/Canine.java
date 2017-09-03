import java.util.ArrayList;
import java.util.Random;

/**
 * The Canine class, used to represent Canine category of animals.
 * @author Chen, Hung-Yu
 *
 */
public class Canine extends Animal{
	/**
	 * Get the category of the animal.
	 * @return Category of the animal.
	 */
	public String getType() {
		return "Canine";
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
		if (b.getType().equals("Feline")) {
			Random rand = new Random();
			int num = rand.nextInt(2);
			if (num == 0) {
				System.out.println("num = 0");
				return true;
			}
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
		
		boolean valid = false;
		while (!valid) {
			Random rand = new Random();
			int num = rand.nextInt(movement.size());
			int num2 = rand.nextInt(2);
			int x = movement.get(num)[0] + a[0];
			int y = movement.get(num)[1] + a[1];
			if (x >= 0 && x < row && y >= 0 && y < col) {
				int[] arr = new int[2];
				arr[0] = x;
				arr[1] = y;
				path.add(arr);
				
				int[] arr2 = new int[2];
				arr2[0] = x + movement.get(num)[0];
				arr2[1] = y + movement.get(num)[1];
				if (num2 == 1 && arr2[0] >= 0 && arr2[0] < row && arr2[1] >= 0 && arr2[1] < col) {
					path.add(arr2);
				}
				valid = true;
			}
		}
		return path;
	}
}
