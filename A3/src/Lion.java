import java.util.Random;

/**
 * The Lion class, used to represent a lion.
 * @author Chen, Hung-Yu
 */
public class Lion extends Feline{
	/**
	 * Get name of the animal.
	 * @return Name of the animal.
	 */
	public String getName() {
		return "Lion";
	}
	/**
	 * Get symbol of the animal.
	 * @return Symbol of the animal.
	 */
	public char getAlias() {
		return 'l';
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
		if (b.getName().equals("Hippo")) {
			return true;
		}
		if (b.getType().equals("Canine")) {
			return true;
		}
		return false;
	}
}
