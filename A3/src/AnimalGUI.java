import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The AnimalGUI class. Used to represent the forest interface.
 * @author Chen, Hung-Yu
 *
 */
public class AnimalGUI {
	private int row = 15;
	private int col = 15;
	private int width = 40;
	private int height = 40;
	private int line = 2;
	private ArrayList<Animal> animals = new ArrayList<Animal>();
	private ArrayList<String> icons = new ArrayList<String>();
	private JFrame forest = new JFrame("Forest");
	private JPanel forest_p = new DrawForest();
	private JButton mode_button = new JButton("Auto Mode");
	private JPanel p_b = new JPanel();
	private MouseListener mouseL = new MouseL();
	private MouseMotionListener mouseM = new MouseM();
	private int[] mousePos = new int[] {-1, -1};
	private Animal[][] map = new Animal[row][col];
	private int mode = 0; //0: manual, 1:auto
	private Animal attacker = null;
	private Animal attacked = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SetupMenu menu = new SetupMenu();
		menu.start();
	}
	
	/**
	 * The AnimalGUI constructor. Used to construct an AnimalGUI object.
	 * @param animals The selected animals.
	 * @param icons The icons of the selected animals.
	 */
	public AnimalGUI(ArrayList<Animal> animals, ArrayList<String> icons) {
		this.initializeMap(animals, icons);
		this.drawMap();
	}
	
	/**
	 * Identify mode and start.
	 */
	public void start() {
		System.out.println("--------START--------");
		this.forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
		this.p_b.paintImmediately(0, 0, 650, 650);
		if (mode == 0) {
			this.manual();
		}
		else if (mode == 1) {
			this.auto();
		}
	}
	
	/**
	 * Reset the map before restarting.
	 */
	public void reset() {
		mode = 0;
		mode_button.setText("Auto Mode");
		for (int i = 0; i < this.animals.size(); i++) {
			if (this.animals.get(i).alive()) {
				System.out.println("The survivor is " + this.animals.get(i).getName() + " at " + this.animals.get(i).getLocation()[0] + "," + this.animals.get(i).getLocation()[1]);
			}
			this.animals.get(i).setDead(false);
		}
		
		System.out.println("--------END--------");
		
		for (int i = 0; i < this.row; i ++) {
			for (int j = 0; j < this.col; j++) {
				map[i][j] = null;
			}
		}
		for (int i = 0; i < this.animals.size(); i++) {
			Animal a = this.animals.get(i);
			int arr[] = this.generateLocation(i);
			setAnimal(a, arr[0], arr[1]);
			System.out.println(a.getName() + " initialized at " + arr[0] + "," + arr[1]);
			this.forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
			this.p_b.paintImmediately(0, 0, 650, 650);
		}
	}
	
	/**
	 * Automode of the game.
	 */
	public void auto() {
		this.forest.removeMouseListener(mouseL);
		this.forest.removeMouseMotionListener(mouseM);
		this.forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
		this.p_b.paintImmediately(0, 0, 650, 650);
		while(mode == 1 && this.numOfAlive() > 1) {
			for (int i = 0; i < animals.size(); i++) {
				Animal a = animals.get(i);
				if (a.alive()) {
					ArrayList <int[]> path = a.move(this.row, this.col);
					boolean stop = false;
					int j = 1;
					
					while (j < path.size() && !stop) {
						int x = path.get(j)[0];
						int y = path.get(j)[1];
						if (map[x][y] != null && map[x][y].getAlias() != a.getAlias()) {
							// if encounter dead, stop moving in this round
							if (map[x][y].alive()) {
								int index = this.findAttacked(a, x, y);
								Animal b = animals.get(index);
								System.out.print(a.getName() + " from " + path.get(0)[0] + "," + path.get(0)[1] + " attacks " + b.getName() + " at " + path.get(j)[0] + "," + path.get(j)[1] + " and ");
								boolean attackWin = a.attackWin(b);
								this.removeAnimal(path.get(j-1)[0], path.get(j-1)[1]);
								this.setAnimal(a, x, y);
								if (attackWin) {
									System.out.println("wins.");
									b.setDead(true);
									this.moveDead(b);
								}
								else {
									System.out.println("loses.");
									this.setAnimal(b, x, y);
									a.setDead(true);
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
				/*
				this.forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
				try {
					//TimeUnit.SECONDS.sleep(1);
					 Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			this.forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
			this.p_b.paintImmediately(0, 0, 650, 650);
			
		}
		this.reset();
		this.start();
	}
	
	/**
	 * Manual mode of the game.
	 */
	public void manual() {
		this.forest.addMouseListener(mouseL);
		this.forest.addMouseMotionListener(mouseM);
	}
	
	/**
	 * Draw the GUI of the map.
	 */
	public void drawMap() {
		p_b.add(mode_button);
		
		mode_button.addActionListener(new ActionListener() {
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e5) {
				if (mode == 0) {
					mode_button.setText("Manual Mode");
					mode = 1;
					
				}
				else if (mode == 1) {
					mode_button.setText("Auto Mode");
					mode = 0;
				}
				start();
			}
		});
		this.forest.getContentPane().add(BorderLayout.CENTER, forest_p);	
		this.forest.getContentPane().add(BorderLayout.SOUTH, p_b);
		
		this.forest.setSize(new Dimension(640, 700));
		this.forest.setVisible(true);
		this.forest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
	}
	
	/**
	 * Initialize map with animals randomly located at different locations.
	 * @param inputA An ArrayList of animals to be initialized.
	 * @param inputI An ArrayList of animal icons to be attaced to animals.
	 */
	public void initializeMap(ArrayList<Animal> inputA, ArrayList<String> inputI) {
		for (int i = 0; i < this.row; i ++) {
			for (int j = 0; j < this.col; j++) {
				map[i][j] = null;
			}
		}
		for (int i = 0; i < inputA.size(); i++) {
			Animal a = inputA.get(i);
			String icon = inputI.get(i);
			this.animals.add(a);
			this.icons.add(icon);
			a.setIcon(icon);
			int arr[] = this.generateLocation(i);
			setAnimal(a, arr[0], arr[1]);
			System.out.println(a.getName() + " initialized at " + arr[0] + "," + arr[1]);
		}
	}
	
	/**
	 * Randomly generate location for each animal.
	 * @param a The animal to be placed in the forest.
	 * @return an integer array of the animal's position.
	 */
	public int[] generateLocation(int index) {
		boolean repeat = true;
		int[] location = new int[2];
		while (repeat) {
			Random rand = new Random();
			int x = rand.nextInt(this.row);
			int y = rand.nextInt(this.col);
			
			if (map[x][y] == null) {
				map[x][y] = this.animals.get(index);
				location[0] = x;
				location[1] = y;
				repeat = false;
			}
		}
		return location;
	}
	/**
	 * Set an animal location in the forest.
	 * @param a The animal to be set in the forest.
	 * @param r Row number of the animal's position.
	 * @param c Column number of the animal's position.
	 */
	public void setAnimal(Animal a, int r, int c) {
		a.setLocation(r, c);
		map[r][c] = a;
		if (!a.alive()) {
			map[r][c].setDead(true);
		}
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
				if ((i != 0 || j != 0) && (r+i >= 0) && (c+j >= 0) && (r+i < this.row) && (c+j < this.col) && map[r+i][c+j] == null ) {
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
		//map[movement.get(index)[0]][movement.get(index)[1]] = d;
		System.out.println(d.getName() + " dies at " + movement.get(index)[0] + "," + movement.get(index)[1]);
	}
	
	/**
	 * Remove an animal from the forest.
	 * @param r Row number of the animal's position.
	 * @param c Column number of the animal's position.
	 */
	public void removeAnimal(int r, int c) {
		this.map[r][c] = null;
	}
	
	
	/**
	 * Calculate number of animals that are alive.
	 * @return number of alive animals.
	 */
	public int numOfAlive() {
		int count = 0;
		for (int i = 0; i < this.animals.size(); i++) {
			if (this.animals.get(i).alive()) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Check if the position is valid for animal to move. 
	 * A position is invalid if there's a dead body on the way and will remain white when the moving animal is clicked.
	 * A position is also invalid if there's a dead body lying on the position, which will be represented with a smaller icon on yellow background.
	 * @param r Row index of the position.
	 * @param c Column index of the position.
	 * @return true if valid. False if not a valid position to move.
	 */
	public boolean canDrag(int r, int c) {
		if (attacker == null || (map[r][c] != null && !map[r][c].alive())) {
			return false;
		}
		boolean found = false;
		int i = 0;
		while(!found && i != attacker.movements().size()) {
			if (attacker.movements().get(i)[0] == r && attacker.movements().get(i)[1] == c) {
				if ((r+attacker.getLocation()[0])%2 == 0 &&(c+attacker.getLocation()[1])%2 == 0 && map[(r+attacker.getLocation()[0])/2][(c+attacker.getLocation()[1])/2] != null && !map[(r+attacker.getLocation()[0])/2][(c+attacker.getLocation()[1])/2].alive()) {
					return false;
				}
				found = true;
			}
			i++;
		}
		return found;
	}
	
	/**
	 * The class that modifies paintComponent method of JPanel to paint on the map.
	 * @author Chen, Hung-Yu
	 *
	 */
	class DrawForest extends JPanel {
	    /* (non-Javadoc)
	     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	     */
	    @Override public void paintComponent(Graphics g) { 
	    	g.setColor(Color.black);
	    	g.fillRect(0, 0, width*col + line*(col+1), height*row + line*(row+1));
	    	for (int i = 0; i < row; i++) {
	    		for (int j = 0; j < col; j++) {
	    			g.setColor(Color.white);
	    			g.fillRect(line*(i+1)+i*width, line*(j+1)+j*height, width, height);
	    		}
	    	}
	    	
	    	for (int i = 0; i < animals.size(); i++) {
	    		Animal a = animals.get(i);
	    		int x = 0;
	    		int y = 0;
	    		x = a.getLocation()[0];
	    		y = a.getLocation()[1];
	    		if (a.alive()) {
	    			g.drawImage(a.getIcon(), line*(y+1)+y*width, line*(x+1)+x*height, width, height, this);
	    		}
	    		else {
	    			g.setColor(Color.yellow);
	    			g.fillRect(line*(y+1)+y*width, line*(x+1)+x*height, width, height);
	    			g.drawImage(a.getIcon(), line*(y+1)+y*width, line*(x+1)+x*height, width/2, height/2, this);
	    		}
	    	}
	    	
	    	if (attacker != null) {
	    		for (int i = 0; i < attacker.movements().size(); i++) {
	    			int x = attacker.movements().get(i)[0];
					int y = attacker.movements().get(i)[1];
					if (x >= 0 && x < row && y >= 0 && y < col) {
						if (canDrag(x, y)) {
							if (map[x][y] == null) {
								g.setColor(Color.orange);
								g.fillRect(line*(y+1)+y*width, line*(x+1)+x*height, width, height);
							}
							else if (map[x][y].alive()) {
								g.setColor(Color.red);
								g.fillRect(line*(y+1)+y*width, line*(x+1)+x*height, width, height);
							}
							else {
								g.setColor(Color.yellow);
							}
						}
		    			//g.fillRect(line*(y+1)+y*width, line*(x+1)+x*height, width, height);
					}
	    		}
	    	}
	    	
	    	if (mousePos[0] != -1 && canDrag(mousePos[0], mousePos[1])) {
	    		int x = mousePos[0];
	    		int y = mousePos[1];
	    		g.setColor(Color.blue);
	    		g.fillRect(line*(y+1)+y*width, line*(x+1)+x*height, width, height);
	    	}
	    	
	    }
	}
	
	/**
	 * The MouseL class. Used to detect mousePressed and mouseReleased.
	 * @author Chen, Hung-Yu
	 *
	 */
	class MouseL extends MouseAdapter {
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY() - 25;
			if (x >= 0 && x <= line*(col+1)+col*width && y >= 0 && y <= line*(row+1)+row*height) {
				 int r = (int) Math.floor(y/42);
				 int c = (int) Math.floor(x/42);
				 Animal a = map[r][c];
				 if (a != null && a.alive()) {
					 attacker = a;
				 }
				 else {
					 attacker = null;
				 }
				 forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
			}
	    }
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override public void mouseReleased(MouseEvent e) {
			int r = mousePos[0];
			int c = mousePos[1];
			if (canDrag(r, c)) {
				ArrayList <int[]> path = new ArrayList<int[]>();
				int arr1[] = {attacker.getLocation()[0], attacker.getLocation()[1]};
				path.add(arr1);
				if (Math.abs(attacker.getLocation()[0] - mousePos[0]) == 2 ||Math.abs(attacker.getLocation()[1] - mousePos[1]) == 2) {
					int arr2[] = {(attacker.getLocation()[0] + mousePos[0])/2, (attacker.getLocation()[1] + mousePos[1])/2};
					path.add(arr2);
				}
				path.add(new int[] {mousePos[0], mousePos[1]});
				
				boolean stop = false;
				int j = 1;
				
				while (j < path.size() && !stop) {
					int x = path.get(j)[0];
					int y = path.get(j)[1];
					if (map[x][y] != null && map[x][y].getAlias() != attacker.getAlias()) {
						// if encounter dead, stop moving in this round
						if (map[x][y].alive()) {
							int index = findAttacked(attacker, x, y);
							Animal b = animals.get(index);
							System.out.print(attacker.getName() + " from " + path.get(0)[0] + "," + path.get(0)[1] + " attacks " + b.getName() + " at " + path.get(j)[0] + "," + path.get(j)[1] + " and ");
							boolean attackWin = attacker.attackWin(b);
							removeAnimal(path.get(j-1)[0], path.get(j-1)[1]);
							setAnimal(attacker, x, y);
							if (attackWin) {
								System.out.println("wins.");
								b.setDead(true);
								moveDead(b);
							}
							else {
								System.out.println("loses.");
								setAnimal(b, x, y);
								attacker.setDead(true);
								moveDead(attacker);
								stop = true;
							}
						}
						else {
							stop = true;
							System.out.println(attacker.getName() + " moved from " + path.get(0)[0]  + "," + path.get(0)[1]  + " to " + attacker.getLocation()[0] + "," + attacker.getLocation()[1]);
						}
					}
					else {
						removeAnimal(path.get(j-1)[0], path.get(j-1)[1]);
						setAnimal(attacker, x, y);
					}
					j++;
				}
				if (!stop) {
					System.out.println(attacker.getName() + " moved from " + path.get(0)[0]  + "," + path.get(0)[1]  + " to " + attacker.getLocation()[0] + "," + attacker.getLocation()[1]);
				}
			}
			attacker = null;
			mousePos[0] = -1;
			mousePos[1] = -1;
			forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
			if (numOfAlive() == 1) {
				reset();
				start();
			}
		}
		
	}
	
	/**
	 * The MouseM class. Used to detect mouseDragged.
	 * @author Chen, Hung-Yu
	 *
	 */
	class MouseM extends MouseAdapter {
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
		 */
		@Override public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY() - 25;
			if (attacker != null && x >= 0 && x <= line*(col+1)+col*width && y >= 0 && y <= line*(row+1)+row*height) {
				 mousePos[0] = (int) Math.floor(y/42);
				 mousePos[1] = (int) Math.floor(x/42);
				 forest_p.paintImmediately(0, 0, width*col + line*(col+1), height*row + line*(row+1));
			}
			else {
				mousePos[0] = -1;
				mousePos[1] = -1;
			}
		}
	}
}
