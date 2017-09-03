
/**
 * The Coconut class, used to represent the coconut ingredient.
 * It can also be used as a coconut container.
 * @author Chen Hung-Yu
 */
public class Coconut extends Ingredient implements Container{
	/**
	 * Construct a Coconut ingredient with random volume between 60 and 80.
	 */
	public Coconut() {
		super(60, 80);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Coconut";
	}
	/* (non-Javadoc)
	 * @see Container#getCapacity()
	 */
	public int getCapacity() {
		return this.getVolume()*5;
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Coconut();
	}
	/* (non-Javadoc)
	 * @see Container#getConName()
	 */
	public String getConName() {
		return "Coconut";
	}
}
