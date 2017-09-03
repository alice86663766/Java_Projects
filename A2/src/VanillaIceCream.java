
/**
 * The VanillaIceCream class, used to represent the vanilla ice cream ingredient.
 * @author Chen Hung-Yu
 *
 */
public class VanillaIceCream extends Ingredient {
	/**
	 * Construct a VanillaIceCream ingredient with random volume between 40 and 45.
	 */
	public VanillaIceCream() {
		super(40, 45);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Vanilla ice cream";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new VanillaIceCream();
	}
}
