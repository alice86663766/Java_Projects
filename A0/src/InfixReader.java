import java.io.*;

/**
 * The InfixReader class, used to convert infix to postfix.
 * @author Chen Hung-Yu
 */
public class InfixReader {

	public static void main(String[] args) {
		InfixReader myAnswer = new InfixReader();
		myAnswer.doConversion();
	}
	
	/**
	 * Compare precedency of input operator with the operator on the top of the stack.
	 * @param s The stack of operators
	 * @param op The operator that is now pointed to
	 * @return True if the input operator has a higher or equal precedency than the operator on the top of the stack.
	 * 		   False if the input operator has a lower precedency or the input operator is parenthesis.
	 */
	public boolean opLarge(Stack s, String op) {
		if (s.size() == 0) {
			return true;
		}
		String top = s.top();
		if (top.equals(op)) {
			return false;
		}
		if (top.equals("^") || op.equals("+") || op.equals("-")) {
			return false;
		}
		if ((op.equals("*") || op.equals("/")) && (top.equals("*") || top.equals("/"))) {
			return false;
		}
		if (op.equals("(") || op.equals(")")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Convert infix to postfix and print out the postfix.
	 */
	public void doConversion() {
		// TODO: read infix from input using readInfix(), then convert it to postfix and print it out
		String[] input = this.readInfix();
		Stack s = new Stack(input.length);
		
		for (int i = 0; i < input.length; i++) {
			
			if (input[i].equals("+") || input[i].equals("-") || input[i].equals("*") || input[i].equals("/") || input[i].equals("^") || input[i].equals("(") || input[i].equals(")")) {
				if (opLarge(s, input[i]) || input[i].equals("(")) {
					s.push(input[i]);
				}
				else {
					if (input[i].equals(")")) {
						while(s.size() != 0 && !s.top().equals("(")) {
							System.out.print(s.pop() + " ");
						}
						s.pop();
					}
					else {
						while(s.size() != 0 && !s.top().equals("(") && !opLarge(s, input[i])) {
							System.out.print(s.pop() + " ");
						}
						s.push(input[i]);
					}
				}
			}
			else {
				System.out.print(input[i] + " ");
			}
		}
		while(s.size() != 0) {
			System.out.print(s.pop() + " ");
		}
	}
	
	/**
	 * Read the input infix.
	 * @return the input infix as an array of numbers and operators  
	 */
	private String [] readInfix() {
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); 
		String inputLine;
		try {
			System.out.print("Please input infix: ");
			inputLine = input.readLine();
			return inputLine.split(" ");
		} catch (IOException e) {
			System.err.println("Input ERROR.");
		}
		
		// return empty array if error occurs
		return new String[] { };
	}

}

/**
 * The Stack class, used as stack for operators.
 * @author Chen Hung-Yu
 *
 */
class Stack {
    // TODO: implement Stack in this class
	private String[] stack;
	private int size;
	private int top_p;
	
	/**
	 * Create a Stack object.
	 * @param s the maximum size of the stack
	 */
	public Stack(int s) {
		size = s;
		stack = new String[size];
		top_p = -1;
	}
	/**
	 * Push an item to stack.
	 * @param item the operator string to be pushed onto the stack.
	 */
	public void push(String item) {
		top_p = top_p + 1;
		stack[top_p] = item;
	}
	/**
	 * Pop out the item on the top of the stack.
	 * @return the item on the top of the stack.
	 */
	public String pop() {
		top_p = top_p - 1;
		return stack[top_p + 1];
	}
	/**
	 * Access the item on the top of the stack.
	 * @return the item on the top of the stack.
	 */
	public String top() {
		return stack[top_p];
	}
	/**
	 * Get the current size of the stack.
	 * @return the current size of the stack.
	 */
	public int size() {
		return top_p + 1;
	}
}
