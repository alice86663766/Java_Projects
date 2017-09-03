
/**
 * The Ice class, used to represent the ice ingredient.
 * @author Chen Hung-Yu
 *
 */
public class Ice extends Ingredient{
	/**
	 * Construct an Ice ingredient with the input volume.
	 * @param volume The volume of ice
	 */
	public Ice(int volume) {
		super(volume);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Ice";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Ice(0);
	}
}
