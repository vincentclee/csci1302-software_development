
public class Attendance {
	public static void main(String[] args) {
		//This program will not accept any command line arguments.
		//If the file Visualizer is not there, it will generate a error message.
		try {
			if (args.length == 0)
				new Visualizer();
			else {
				System.out.println("Hi. I cannot accept any command line arguments.");
				System.out.println("I will exit now.");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("General program failure.");
			System.exit(0);
		}
	}
}
