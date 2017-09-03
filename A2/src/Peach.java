/**
 * The Peach class, used to represent the peach ingredient.
 * @author Chen Hung-Yu
 */
public class Peach extends Ingredient {
	/**
	 * Construct a Peach ingredient with random volume between 30 and 35.
	 */
	public Peach() {
		super(30, 35);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Peach";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Peach();
	}
}
