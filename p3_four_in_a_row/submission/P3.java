import java.io.*; //File IO

public class P3 {
	public static void main(String[] args) throws IOException {
		//Check if game pieces are available to use.
		File redImage = new File("red.png");
		File winImage = new File("win.png");
		File clearImage = new File("clear.png");
		File blankImage = new File("blank.png");
		File yellowImage = new File("yellow.png");
		File redSoloImage = new File("redSolo.png");
		File yellowSoloImage = new File("yellowSolo.png");
		
		//If one or all do not exist, and any combination in between, the program will quit.
		if (!redImage.exists() || !winImage.exists() || !clearImage.exists() || !blankImage.exists() 
				|| !yellowImage.exists() || !redSoloImage.exists() || !yellowSoloImage.exists()) {
			System.out.println("Hi! Some of, if not all the gameboard pieces are missing.");
			System.out.println("I will exit now.");
			System.exit(0);
		}
		
		//This program does not accept command line arguments.
		//If command line arguments are given, the program will exit.
		if (args.length == 0)
			//Try and catch, to catch any and all errors.
			try {
				new GameBoard();
			} catch(Exception e) {
				System.out.println("Hi! There has been a error.");
				System.out.println("I will exit now.");
				System.exit(0);
			}
		else {
			System.out.println("Hi! This program cannot accept command line arguments.");
			System.out.println("I will exit now.");
			System.exit(0);	
		}
	}
}
