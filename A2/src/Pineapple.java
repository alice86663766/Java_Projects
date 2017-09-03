/**
 * The Pineapple class, used to represent the pineapple ingredient.
 * @author Chen Hung-Yu
 */
public class Pineapple extends Ingredient implements Container {
	/**
	 * Construct a Pineapple ingredient with random volume between 75 and 90.
	 */
	public Pineapple() {
		super(75, 90);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Pineapple";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Pineapple();
	}
	/* (non-Javadoc)
	 * @see Container#getCapacity()
	 */
	public int getCapacity() {
		return this.getVolume()*2;
	}
	/* (non-Javadoc)
	 * @see Container#getConName()
	 */
	public String getConName() {
		return "Pineapple";
	}
}
