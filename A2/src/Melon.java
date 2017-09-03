
/**
 * The Melon class, used to represent the melon ingredient.
 * It can also be used as a melon container.
 * @author Chen Hung-Yu
 *
 */
public class Melon extends Ingredient implements Container{
	/**
	 * Construct a Melon ingredient with random volume between 50 and 70.
	 */
	public Melon() {
		super(50, 70);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Melon";
	}
	/* (non-Javadoc)
	 * @see Container#getCapacity()
	 */
	public int getCapacity() {
		return this.getVolume()*4;
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Melon();
	}
	/* (non-Javadoc)
	 * @see Container#getConName()
	 */
	public String getConName() {
		return "Melon";
	}
}
