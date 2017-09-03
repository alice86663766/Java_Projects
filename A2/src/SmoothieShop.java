import java.util.ArrayList;
import java.util.Scanner;
/**
 * The SmoothieShop class, used to represent the smoothie shop.
 * @author Chen Hung-Yu
 *
 */
public class SmoothieShop {
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	private Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SmoothieShop shop = new SmoothieShop();
		shop.importIngr();
		shop.startOrder();
	}
	/**
	 * Import all ingredient options.
	 */
	public void importIngr() {
		Apple apple = new Apple();
		Orange orange = new Orange();
		Banana banana = new Banana();
		Melon melon = new Melon();
		Coconut coconut = new Coconut();
		ChocolateChips chocolate = new ChocolateChips();
		Milk milk = new Milk();
		VanillaIceCream vanilla = new VanillaIceCream();
		WaterMelon wMelon = new WaterMelon();
		ingredients.add(apple);
		ingredients.add(orange);
		ingredients.add(banana);
		ingredients.add(melon);
		ingredients.add(coconut);
		ingredients.add(chocolate);
		ingredients.add(milk);
		ingredients.add(vanilla);
		ingredients.add(wMelon);
	}
	/**
	 * Allow user input to choose ingredients and container for one smoothie order.
	 */
	public void startOrder() {
		Smoothie smoothie = new Smoothie();
		this.printMenu();
		System.out.println("Please enter your choice (1-" + ingredients.size() + ", or 0 to finish the order):");
		int input;
		input = keyboard.nextInt();
		while(input != 0) {
			if (input > 0 && input < ingredients.size()) {
				Ingredient ingr = ingredients.get(input-1).getNew();
				smoothie.addIngrs(ingr);
			}
			else {
				System.out.println("Please enter your choice (1-" + ingredients.size() + ", or 0 to finish the order):");
			}
			input = keyboard.nextInt();
		}
		this.printConMenu(smoothie);
		System.out.println("Please enter your choice (1-" + smoothie.getContainers().size() + "):");
		int inputCon;
		inputCon = keyboard.nextInt();
		while (inputCon <= 0 || inputCon > smoothie.getContainers().size()) {
			System.out.println("Please enter your choice (1-" + smoothie.getContainers().size() + "):");
			inputCon = keyboard.nextInt();
		}
		smoothie.make(inputCon-1);
	}
	/**
	 * Print ingredient menu.
	 */
	public void printMenu() {
		for (int i = 0; i < ingredients.size(); i++) {
			System.out.println(i+1 + ". " + ingredients.get(i).getName());
		}
		System.out.println("What would you like to add to your smoothie?");
	}
	/**
	 * Print container menu.
	 * @param smoothie The smoothie ordered by customer, which holds ingredient information.
	 */
	public void printConMenu(Smoothie smoothie) {
		for (int i = 0; i < smoothie.getContainers().size(); i++) {
			Container con = smoothie.getContainers().get(i);
			System.out.println(i+1 + ". " + con.getConName() + " (" + con.getCapacity() + "ml)");
		}
		System.out.println("What would you like to use to hold your smoothie?");
	}
}
