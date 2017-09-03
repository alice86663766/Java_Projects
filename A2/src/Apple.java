/**
 * The Apple class, used to represent the apple ingredient.
 * @author Chen Hung-Yu
 *
 */
public class Apple extends Ingredient {
	/**
	 * Construct an Apple ingredient with random volume between 40 and 50.
	 */
	public Apple() {
		super(40, 50);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Apple";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Apple();
	}
}
