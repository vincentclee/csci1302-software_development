import java.io.*; //File Import
import java.util.*; //Random Generator & ArrayList & Scanner

public class Hangman {
	public static void main(String[] args) throws IOException {
		String word = wordPicker(); //Word randomly selected.
		HangmanBoard gallows = new HangmanBoard(word);
		String  blankWord = "";
		String guesses = "";
		Player player = null;
		Boolean test = false; //Testing purposes ONLY
		String fileName = "gameplay.output.txt";
		File file = null;
		PrintWriter outputFile = null;
		Boolean gameplayOutput = false; //For gameplay output logging.
		
		//For gameplay output logging.
		if (args.length == 3) {
			gameplayOutput = true;
		}
		
		//Generates the correct number of blanks representing the word.
		for (int i = 0; i < word.length(); i++) {blankWord += "-";}
		
		//Try and catch statements for end of the world errors.
		try {
			//Method contains ALL exception handling for command line arguments.
			//Also sets player.
			player = exceptions(player, blankWord, guesses, args);
		} catch (Exception e) {
			System.out.println("Hi! I have detected an error.");
			System.out.println("I will exit now.");
			System.exit(0);
		}
		
		//gameplay Output loging to txt file.
		if (gameplayOutput) {
			if (args[2].equalsIgnoreCase("true")) {
				file = new File(fileName);
				outputFile = new PrintWriter(file);
			}
		}

		//Hangman game splash screen
		System.out.println(instructions());
		if (gameplayOutput) outputFile.println(instructions());

		//Shows who is playing the game with the computer
		definePlayer2(args);
		
		System.out.println(gallows); //Displays the gallows using toString()
		if (gameplayOutput) outputFile.println(gallows);

		if (test) System.out.println(word); //Testing purposes ONLY
		System.out.println("Word: " + blankWord);
		if (gameplayOutput) outputFile.println("Word: " + blankWord);

		System.out.println("Misses: " + guesses + "\n");
		if (gameplayOutput) outputFile.println("Misses: " + guesses + "\n");
		
		//The program will continue until misses equals 6, or
		//the word with dashes becomes the same as the randomly picked word.
		//...the player wins the game. 
		while (guesses.length() < 6 && !blankWord.contains(word)) {
			char guess = player.makeGuess();
			blankWord = wordUpdater(guess, blankWord, word);
			guesses = guessesUpdater(guess, word, guesses, gallows);
			player.setInput(blankWord, guesses);
			System.out.println(gallows);
			if (gameplayOutput) outputFile.println(gallows);
			if (test) System.out.println(word); //Testing purposes ONLY
			System.out.println("Word: " + blankWord);
			if (gameplayOutput) outputFile.println("Word: " + blankWord);
			System.out.println("Misses: " + guesses + "\n");
			if (gameplayOutput) outputFile.println("Misses: " + guesses + "\n");
		}
		
		//If game is over, it determines win or loose, and prints accordingly.
		gallows.isGameOver();
		if (gameplayOutput) {
			if (gallows.isLoose()) outputFile.println("You lost!\nThe word was: " + word);
			if (gallows.isWin()) outputFile.println("Game Over!\nYou won the game!\nCongratulations!\n");
		}
		if (gameplayOutput) outputFile.close();
	}
	
	/**
	This instructions method prints out a ascii art Hangman logo.
	@return the ascii art.
	*/
	
	public static String instructions() {
		 return "{}    {}    {}{}    {}    {}   {}}}}}   {}      {}    {}{}    {}    {}\n" +
		 		"{}    {}   {}  {}   {}}}  {}  {}    {}  {}}}  {{{}   {}  {}   {}}}  {}\n" +
		 		"{}{{}}{}  {}{{}}{}  {} {} {}  {}        {} {{}} {}  {}{{}}{}  {} {} {}\n" +
		 		"{}    {}  {}    {}  {}  {{{}  {}  {{{{  {}  {}  {}  {}    {}  {}  {{{}\n" +
		 		"{}    {}  {}    {}  {}    {}   {}}}}}   {}      {}  {}    {}  {}    {}\n";
	}
	
	/**
	The exceptions method accepts a command line argument, and checks for
	...improper argument length, and makes sure first argument is "computer"
	@param player A Player object, which sets which player: human, naive, random, cutthroat, etc.
	@param blankWord The word with dashes.
	@param guesses The string of already missed letters.
	@param args Contains parameters for program execution.
	@return player object.
	*/
	
	public static Player exceptions(Player player, String blankWord, String guesses, String[] args) {
		//Will only allow programs to run with two arguments. 
		//No arguments will make the program exit.
		if (args.length == 0) {
			System.out.println("Hi! You did not give me any arguments. I only accept up to three parameters seperated by a space.");
			System.out.println("I will exit now.");
			System.exit(0);
		}
		
		//Fewer than 2 will cause the program to exit.
		if (args.length < 2) {
			System.out.println("Hi! You did not give me enough arguments. I only accept up to three parameters seperated by a space.");
			System.out.println("I will exit now.");
			System.exit(0);
		}
		
		//Higher than 2 will cause the program to exit.
		if (args.length > 3) {
			System.out.println("Hi! You gave me too many arguments. I only accept up to three parameters seperated by a space.");
			System.out.println("I will exit now.");
			System.exit(0);
		}
		
		//args[0]
		//The first argument has to be computer, if not the program will exit.
		if (!args[0].equalsIgnoreCase("computer")) {
			System.out.println("Hi! I am currently a single player game, please re-run me with the first argument as computer.");
			System.out.println("I will exit now.");
			System.exit(0);
		}
		
		//args[1] or Player 2
		//If the players is not defined, the program will terminate.
		if(args[1].equalsIgnoreCase("human"))
			player = new HumanPlayer(blankWord, guesses);
		else if(args[1].equalsIgnoreCase("naive"))
			player = new NaiveComputerPlayer(blankWord, guesses);
		else if(args[1].equalsIgnoreCase("random"))
			player = new RandomComputerPlayer(blankWord, guesses);
		else if(args[1].equalsIgnoreCase("cutthroat"))
			player = new CutThroatComputerPlayer(blankWord, guesses);
		else {
			System.out.println("Hi! You have provided me with an invalid player input.");
			System.out.println("I will exit now.");
			System.exit(0);
		}
		
		//args[2] or gameplay.output
		if (args.length == 3) {
			if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
				System.out.println("Hi! I can only turn log on or off using true or false.");
				System.out.println("I will exit now.");
				System.exit(0);
			}
		}
		
		return player;
	}
	
	/**
	The definePlayer2 method prints out the person playing the game.
	@param args Contains parameters for program execution.
	*/
	
	public static void definePlayer2(String[] args) {
		if(args[1].equalsIgnoreCase("human"))
		    System.out.println("Human Player");
		if(args[1].equalsIgnoreCase("naive"))
		    System.out.println("Naive Computer Player");
        if(args[1].equalsIgnoreCase("random"))
		    System.out.println("Random Computer Player");
        if(args[1].equalsIgnoreCase("cutthroat"))
		    System.out.println("Cut Throat Computer Player");
        
        //Adds a blank line.
        System.out.println();
	}
	
	/**
	The wordPicker method generates a random word from the dictionary.data file
	@return A word picked at random.
	*/
	
	public static String wordPicker() {
		ArrayList<String> dictionary = new ArrayList<String>();
		String filename = "dictionary.data";

		File file = null;
		Scanner kb = null;
		try {
			file = new File(filename);
		    kb = new Scanner(file);
		} catch (FileNotFoundException e) {
		    System.err.println("Hi! I could not find the file: " + filename);
		    System.out.println("I will exit now.");
		    System.exit(0);
		}
		
		//Puts all of the words in ArrayList dictionary.
		while (kb.hasNext()) {
			dictionary.add(kb.next());
		}
		
		//Generates random number from which a word is picked.
		int line = 0;
		Random randomGenerator = new Random();
		line = randomGenerator.nextInt(dictionary.size());
		
		return dictionary.get(line);
	}
	
	/**
	The wordUpdater method updates the word with dashes, if the character
	...being guessed is in the string word.
	@param guess The character which is being guessed.
	@param blankWord The word with dashes.
	@param word The word being solved for.
	@return The word with blanks appended. 
	*/
	
	public static String wordUpdater(char guess, String blankWord, String word) {
		String blankWordAppend = "";
		guess = Character.toLowerCase(guess);

		//Iterates the word length
		//If the guessed character is in the word, it appends the dashes.
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess)
				blankWordAppend += guess;
			else
				blankWordAppend += blankWord.charAt(i);
		}
		
		return blankWordAppend;
	}
	
	/**
	The guessesUpdater method updates the guesses string, if the character
	...being guessed is not in the word.
	@param guess The character which is being guessed.
	@param word The word being solved for.
	@param guesses The string of already missed letters.
	@param gallows A HangmanBoard object.
	@return A String of missed letters.
	*/
	
	public static String guessesUpdater(char guess, String word, String guesses, HangmanBoard gallows) {
		guess = Character.toLowerCase(guess);
		
		//If the character being guessed is not in the word, it adds it to misses.
		if (!word.contains(Character.toString(guess))) {
			guesses += guess;
			gallows.addonsDraw(); //Draws a additional body part on the gallows.
		}
		
		return guesses;
	}
}
