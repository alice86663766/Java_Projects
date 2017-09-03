public class JavaDocExample {

	public static void main(String[] args) {
		JavaDocExample app = new JavaDocExample("Hello, World!");
		System.out.print("Hello world length = " + app.getLength());
	}
	
	public String message;
	
	public JavaDocExample(String message) {
		this.message = message;
	}

	public int getLength() {
	    return message.length();
	}

}
