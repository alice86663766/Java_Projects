import java.awt.Checkbox;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The SetupMenu class. Used to represent the setup menu.
 * @author Chen, Hung-Yu
 *
 */
public class SetupMenu {
	private JFrame frame = new JFrame("Startup Menu"); // title of the frame
	private Animal[] animals = new Animal[8];
	private Checkbox[] boxes = new Checkbox[animals.length];
	private String[] paths = new String[animals.length];
	private ArrayList<String> selectedIcons = new ArrayList<String>();
	private ArrayList<Animal> selectedAnimals = new ArrayList<Animal>();
	private JButton start = new JButton("Start");
	/**
	 * The SetupMenu constructor. Used to construct a SetupMenu object.
	 */
	public SetupMenu() {
		this.addAnimals();
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		frame.add(listPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for (int i = 0; i < animals.length; i++) {
			int index = i;
			JButton changeIcon = new JButton("Pick an alternative icon");
			boxes[i] = new Checkbox(animals[i].getName());
			boxes[i].setState(true);
			paths[i] = "Icons/" + animals[i].getName() + ".png";
			
			JPanel p = new JPanel();
			p.add(boxes[i]);
			p.add(changeIcon);
			listPane.add(p);
			
			changeIcon.addActionListener(new ActionListener() {
				/* (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					JFileChooser file = new JFileChooser();
					file.showOpenDialog(null);
					if (file.getSelectedFile() != null) {
						paths[index] = file.getSelectedFile().getAbsolutePath();			
					}
				}
			});
		}
		
		listPane.add(start);
		start.addActionListener(new ActionListener() {
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < animals.length; i++) {
					if (boxes[i].getState() == true) {
						selectedIcons.add(paths[i]);
						selectedAnimals.add(animals[i]);
					}
				}
				frame.setVisible(false);
				AnimalGUI animalGUI = new AnimalGUI(selectedAnimals, selectedIcons);
				animalGUI.start();
			}
		});
	}
	/**
	 * Add animal options to setup menu.
	 */
	public void addAnimals() {
		animals[0] = new Cat();
		animals[1] = new Dog();
		animals[2] = new Fox();
		animals[3] = new Hippo();
		animals[4] = new Lion();
		animals[5] = new Tiger();
		animals[6] = new Turtle();
		animals[7] = new Wolf();
	}
	/**
	 * Generate the setup menu GUI.
	 */
	public void start() {
		frame.setSize(300, 500);
		frame.setVisible(true);
	}
}
