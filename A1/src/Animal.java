import java.util.ArrayList;
import java.util.Random;

/**
 * The Animal class, used to represent an animal.
 * @author Chen, Hung-Yu
 */
public class Animal {
	private boolean alive = true;
	protected int[] location = new int[2];
	/**
	 * Check if the animal is alive.
	 * @return True if the animal is alive.
	 * 		   False if the animal is dead.
	 */
	public boolean alive () {
		return this.alive;
	}
	
	/**
	 * Set location of the animal.
	 * @param r Row number of the location.
	 * @param c Column number of the location.
	 */
	public void setLocation(int r, int c) {
		this.location[0] = r;
		this.location[1] = c;
	}
	
	/**
	 * Get location of the animal.
	 * @return Location of the animal.
	 */
	public int[] getLocation() {
		return this.location;
	}
	
	/**
	 * Get name of the animal.
	 * @return Name of the animal.
	 */
	public String getName() {
		return "0";
	}
	
	/**
	 * Get symbol of the animal.
	 * @return Symbol of the animal.
	 */
	public char getAlias() {
		return '0';
	}
	
	/**
	 * Get the category of the animal.
	 * @return Category of the animal. If the animal doesn't belong to any category, return default.
	 */
	public String getType() {
		return "Default";
	}
	
	
	/**
	 * Set the status of animal to dead.
	 */
	public void setDead() {
		this.alive = false;
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
		return path;
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
		return false;
	}
}
