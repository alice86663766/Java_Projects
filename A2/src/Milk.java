
/**
 * The Milk class, used to represent the milk ingredient.
 * @author Chen Hung-Yu
 *
 */
public class Milk extends Ingredient{
	/**
	 * Construct a Milk ingredient with fixed volume of 100.
	 */
	public Milk() {
		super(100);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Milk";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Milk();
	}
}
