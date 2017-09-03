import java.util.Random;

/**
 * The Fox class, used to represent a fox.
 * @author Chen, Hung-Yu
 */
public class Fox extends Canine{
	/**
	 * Get name of the animal.
	 * @return Name of the animal.
	 */
	public String getName() {
		return "Fox";
	}
	/**
	 * Get symbol of the animal.
	 * @return Symbol of the animal.
	 */
	public char getAlias() {
		return 'f';
	}
	
	/**
	 * Check if the animal can win in attack.
	 * @param b The animal being attacked.
	 * @return If the attacking animal wins.
	 */
	public boolean attackWin(Animal b) {
		if (b.getName().equals("Turtle")) {
			Random rand = new Random();
			int num = rand.nextInt(5);
			if (num == 0) {
				return true;
			}
		}
		if (b.getName().equals("Cat")) {
			return true;
		}
		if (b.getType().equals("Feline")) {
			Random rand = new Random();
			int num = rand.nextInt(2);
			if (num == 0) {
				return true;
			}
		}
		return false;
	}
}
