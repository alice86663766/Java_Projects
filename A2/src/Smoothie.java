import java.util.ArrayList;
/**
 * The Smoothie class, used to represent a smoothie.
 * @author Chen Hung-Yu
 *
 */
public class Smoothie {
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	private ArrayList<Container> containers = new ArrayList<Container>();
	
	/**
	 * Construct a smoothie and add plastic cup as one of the container choices.
	 */
	public Smoothie() {
		PlasticCup cup = new PlasticCup();
		this.containers.add(cup);
	}
	/**
	 * Add one ingredient to the smoothie.
	 * @param ingr The ingredient to be added.
	 */
	public void addIngrs(Ingredient ingr) {
		this.ingredients.add(ingr);
		if (ingr instanceof Container) {
			this.containers.add((Container)ingr);
		}
	}
	/**
	 * Get all ingredients in this smoothie.
	 * @return all ingredients in this smoothie.
	 */
	public ArrayList<Ingredient> getIngrs() {
		return this.ingredients;
	}
	/**
	 * Get all container choices for this smoothie order.
	 * @return all container choices for this smoothie order.
	 */
	public ArrayList<Container> getContainers() {
		return this.containers;
	}
	/**
	 * Calculate remaining volume after all ingredients ordered by customer are added.
	 * @param c The container that customer chose to hold this smoothie.
	 * @return the remaining volume of the smoothie where ice can be added.
	 */
	public int remaining(Container c) {
		int total = 0;
		for (int i = 0; i < ingredients.size(); i++) {
			total = total + ingredients.get(i).getVolume();
		}
		return (c.getCapacity() - total);
	}
	/**
	 * Add ice to the smoothie.
	 * @param volume The volume of ice to be added in this smoothie.
	 */
	public void addIce(int volume) {
		Ice ice = new Ice(volume);
		this.ingredients.add(ice);
	}
	/**
	 * Make the smoothie with the chosen container and ingredients.
	 * @param index Index of the chosen container in containers ArrayList.
	 */
	public void make(int index) {
		Container con = this.getContainers().get(index);
		int remain = this.remaining(con);
		if (remain >= 0) {
			this.addIce(remain);
		}
		System.out.println("Smoothie ingredients:");
		for (int i = 0; i < this.getIngrs().size(); i++) {
			Ingredient ingr = this.getIngrs().get(i);
			System.out.println(ingr.getName() + " (" + ingr.getVolume() + "ml)");
		}
		System.out.println("Container: " + con.getConName() + " (" + con.getCapacity() + "ml)");
		if (remain < 0) {
			System.out.println("Wasted " + remain*(-1) + "ml!");
		}
	}
}
