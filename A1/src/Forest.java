import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The Forest class, used to generate a forest with animals.
 * @author Chen, Hung-Yu
 */
public class Forest {
	private int row = 12;
	private int col = 12;
	private char[][] map = new char[row][col];
	private ArrayList<Animal> animals = new ArrayList<Animal>();
	private Scanner keyboard = new Scanner(System.in);
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Forest forest = new Forest();
		forest.start();
	}
	
	/**
	 * Start the simulation of a forest.
	 */
	public void start() {
		this.initializeMap();
		this.drawMap();
		
		String line = "";
		System.out.print("Press enter to iterate, type 'exit' to quit: ");
		line = keyboard.nextLine();
		while(!line.equals("exit")) {
			for (int i = 0; i < animals.size(); i++) {
				Animal a = animals.get(i);
				if (a.alive()) {
					ArrayList <int[]> path = a.move(this.row, this.col);
					boolean stop = false;
					int j = 1;
					
					while (j < path.size() && !stop) {
						int x = path.get(j)[0];
						int y = path.get(j)[1];
						if (map[x][y] != '.' && map[x][y] != a.getAlias()) {
							// if encounter dead, stop moving in this round
							if (Character.isLowerCase(map[x][y])) {
								int index = this.findAttacked(a, x, y);
								Animal b = animals.get(index);
								System.out.print(a.getName() + " from " + path.get(0)[0] + "," + path.get(0)[1] + " attacks " + b.getName() + " at " + path.get(j)[0] + "," + path.get(j)[1] + " and ");
								boolean attackWin = a.attackWin(b);
								this.removeAnimal(path.get(j-1)[0], path.get(j-1)[1]);
								this.setAnimal(a, x, y);
								if (attackWin) {
									System.out.println("wins.");
									b.setDead();
									this.moveDead(b);
								}
								else {
									System.out.println("loses.");
									this.setAnimal(b, x, y);
									a.setDead();
									this.moveDead(a);
									stop = true;
								}
							}
							else {
								stop = true;
								System.out.println(a.getName() + " moved from " + path.get(0)[0]  + "," + path.get(0)[1]  + " to " + a.getLocation()[0] + "," + a.getLocation()[1]);
							}
						}
						else {
							this.removeAnimal(path.get(j-1)[0], path.get(j-1)[1]);
							this.setAnimal(a, x, y);
						}
						j++;
					}
					if (!stop) {
						System.out.println(a.getName() + " moved from " + path.get(0)[0]  + "," + path.get(0)[1]  + " to " + a.getLocation()[0] + "," + a.getLocation()[1]);
					}
				}
			}
			//this.drawMap();
			System.out.print("Press enter to iterate, type 'exit' to quit:");
			line = keyboard.nextLine();
		}
		this.printStatus();
	}
	
	/**
	 * Initialize map with animals (represented by different characters) and empty spaces (represented by dots).
	 */
	public void initializeMap() {
		for (int i = 0; i < this.row; i ++) {
			for (int j = 0; j < this.col; j++) {
				map[i][j] = '.';
			}
		}
		Animal c = new Cat();
		Animal d = new Dog();
		Animal f = new Fox();
		Animal h = new Hippo();
		Animal l = new Lion();
		Animal t = new Tiger();
		Animal u = new Turtle();
		Animal w = new Wolf();
		animals.add(c);
		animals.add(d);
		animals.add(f);
		animals.add(h);
		animals.add(l);
		animals.add(t);
		animals.add(u);
		animals.add(w);
		for (int i = 0; i < animals.size(); i ++) {
			Animal a = animals.get(i);
			int arr[] = this.generateLocation(a);
			
			setAnimal(a, arr[0], arr[1]);
		}
	}
	
	/**
	 * Randomly generate location for each animal.
	 * @param a The animal to be placed in the forest.
	 * @return an integer array of the animal's position.
	 */
	public int[] generateLocation(Animal a) {
		boolean repeat = true;
		int[] location = new int[2];
		while (repeat) {
			Random rand = new Random();
			int x = rand.nextInt(this.row);
			int y = rand.nextInt(this.col);
			
			if (map[x][y] == '.') {
				map[x][y] = a.getAlias();
				location[0] = x;
				location[1] = y;
				repeat = false;
			}
		}
		return location;
	}
	
	/**
	 * Print the forest on screen.
	 */
	public void drawMap() {
		for (int i = 0; i < this.row; i ++) {
			for (int j = 0; j < this.col; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Set an animal location in the forest.
	 * @param a The animal to be set in the forest.
	 * @param r Row number of the animal's position.
	 * @param c Column number of the animal's position.
	 */
	public void setAnimal(Animal a, int r, int c) {
		a.setLocation(r, c);
		if (a.alive()) {
			map[r][c] = a.getAlias();
		}
		else {
			map[r][c] = Character.toUpperCase(a.getAlias());
		}
	}
	
	/**
	 * Remove an animal from the forest.
	 * @param r Row number of the animal's position.
	 * @param c Column number of the animal's position.
	 */
	public void removeAnimal(int r, int c) {
		this.map[r][c] = '.';
	}
	
	/**
	 * Find the animal that will be attacked.
	 * @param a The attacking animal.
	 * @param r Row number of the animal being attacked.
	 * @param c Column number of the animal being attacked.
	 * @return The index of the animal to be attacked in the animal list.
	 */
	public int findAttacked (Animal a, int r, int c) {
		int i = 0;
		boolean found = false;
		Animal b = animals.get(i);
		while (!found && i != animals.size()) {
			b = animals.get(i);
			int arr[] = b.getLocation();
			char alias = b.getAlias();
			if (arr[0] == r && arr[1] == c && alias != a.getAlias()) {
				found = true;
				return i;
			}
			i++;
		}
		return i-1;
	}
	
	/**
	 * Move the dead animal body to a nearby location.
	 * @param d The animal object to be moved.
	 */
	public void moveDead (Animal d) {
		int r = d.getLocation()[0];
		int c = d.getLocation()[1];
		ArrayList <int[]> movement = new ArrayList<int[]>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <=1; j++) {
				if ((i != 0 || j != 0) && (r+i >= 0) && (c+j >= 0) && (r+i < this.row) && (c+j < this.col) && map[r+i][c+j] == '.' ) {
					int[] arr = new int[2];
					arr[0] = r + i;
					arr[1] = c + j;
					movement.add(arr);
				}
			}
		}
		Random rand = new Random();
		int index = rand.nextInt(movement.size());
		this.setAnimal(d, movement.get(index)[0], movement.get(index)[1]);
		System.out.println(d.getName() + " dies at " + movement.get(index)[0] + "," + movement.get(index)[1]);
	}
	
	/**
	 * Print status of all animals in the forest.
	 */
	public void printStatus() {
		for (int i = 0; i < this.animals.size(); i ++) {
			Animal a = this.animals.get(i);
			System.out.print(a.getName() + " is ");
			if (a.alive()) {
				System.out.print("alive");
			}
			else {
				System.out.print("dead");
			}
			System.out.print(" at " + a.getLocation()[0] + "," + a.getLocation()[1]);
			System.out.println();
		}
	}

}
