
/**
 * The WaterMelon class, used to represent the water melon ingredient.
 * It can also be used as a water melon container.
 * @author Chen Hung-Yu
 *
 */
public class WaterMelon extends Ingredient implements Container{
	/**
	 * Construct a WaterMelon ingredient with fixed volume of 150.
	 */
	public WaterMelon() {
		super(150);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Water melon";
	}
	/* (non-Javadoc)
	 * @see Container#getCapacity()
	 */
	public int getCapacity() {
		int volume = 350;
		return volume;
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new WaterMelon();
	}
	/* (non-Javadoc)
	 * @see Container#getConName()
	 */
	public String getConName() {
		return "Water melon";
	}
}
