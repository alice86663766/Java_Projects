/**
 * The Cantaloupe class, used to represent the cantaloupe ingredient.
 * @author Chen Hung-Yu
 */
public class Cantaloupe extends Ingredient implements Container{
	/**
	 * Construct a Cantaloupe ingredient with random volume between 80 and 100.
	 */
	public Cantaloupe() {
		super(80, 100);
	}
	/* (non-Javadoc)
	 * @see Ingredient#getName()
	 */
	public String getName() {
		return "Cantaloupe";
	}
	/* (non-Javadoc)
	 * @see Ingredient#getNew()
	 */
	public Ingredient getNew() {
		return new Cantaloupe();
	}
	/* (non-Javadoc)
	 * @see Container#getCapacity()
	 */
	public int getCapacity() {
		return 250;
	}
	/* (non-Javadoc)
	 * @see Container#getConName()
	 */
	public String getConName() {
		return "Cantaloupe";
	}
}
