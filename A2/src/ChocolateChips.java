
/**
 * The ChocolateChips class, used to represent the chocolate chips ingredient.
 * @author Chen Hung-Yu
 *
 */
public class ChocolateChips extends Ingredient {
	/**
	 * Construct a ChocolateChips ingredient with random volume between 30 and 40.
	 */
	public ChocolateChips() {
		super(30, 40);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Chocolate chips";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new ChocolateChips();
	}
}
