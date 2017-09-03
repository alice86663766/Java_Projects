import java.util.Random;
/**
 * The Ingredient abstract class, which is the superclass of all ingredients.
 * @author Chen Hung-Yu
 *
 */
public abstract class Ingredient {
	private int volume = 0;
	
	/**
	 * Get the display name of the ingredient.
	 * @return display name of the ingredient.
	 */
	public abstract String getName();
	/**
	 * Create a new ingredient object.
	 * @return the ingredient object created.
	 */
	public abstract Ingredient getNew();
	/**
	 * The Ingredient constructor with 1 parameter only, used to construct an ingredient of fixed volume.
	 * @param fixed The fixed volume of the ingredient.
	 */
	public Ingredient(int fixed) {
		this.volume = fixed;
	}
	/**
	 * The Ingredient constructor with 2 parameters, used to construct an ingredient of random volume.
	 * @param lower The minimum volume of this ingredient.
	 * @param upper The maximum volume of this ingredient.
	 */
	public Ingredient(int lower, int upper) {
		Random rand = new Random();
		this.volume = lower + rand.nextInt(upper - lower + 1);
	}
	/**
	 * Get volume of the ingredient.
	 * @return volume of the ingredient.
	 */
	public int getVolume() {
		return this.volume;
	}
}
