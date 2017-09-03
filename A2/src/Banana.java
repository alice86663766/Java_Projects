
/**
 * The Banana class, used to represent the banana ingredient.
 * @author Chen Hung-Yu
 *
 */
public class Banana extends Ingredient{
	/**
	 * Construct a Banana ingredient with random volume between 35 and 40.
	 */
	public Banana() {
		super(35, 40);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Banana";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Banana();
	}
}
