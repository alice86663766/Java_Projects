
/**
 * The Orange class, used to represent the orange ingredient.
 * @author Chen Hung-Yu
 */
public class Orange extends Ingredient{
	/**
	 * Construct an Orange ingredient with random volume between 50 and 60.
	 */
	public Orange() {
		super(50, 60);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Orange";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Orange();
	}
}
