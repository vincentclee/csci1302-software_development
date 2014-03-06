import java.util.*; //Scanner for input

public class HumanPlayer extends Player {
    
	/**
    Default constructor which sets the word with dashes, and guessed letters.
    @param word The word with dashes.
    @param guesses The string of already missed letters.
    */
	
	public HumanPlayer(String word, String guesses) {
    	super(word, guesses);
    }

	/**
	The makeGuess method allows keyboard input for the human player.
	@return A character the player chooses.
	*/
	
    public char makeGuess() {
    	String input = "";
    	
    	Scanner kb = new Scanner(System.in);
    	System.out.print("Please enter a letter: ");
    	input = kb.nextLine();
    	input.toLowerCase();
    	
    	//If the letter guessed is not in the alphabet or the user enters less than or more than one letter.
    	//This will loop until correct input is detected.
    	while (!alphabet.contains(input.toLowerCase()) || input.length() != 1) {
    		System.out.println("\nYou have entered invalid input.");
    		System.out.print("Please enter a letter: ");
        	input = kb.nextLine();
        	input.toLowerCase();
    	}
    	
    	//If the letter guessed is already in the missed String, or is already guessed in the word.
    	//The loop will continue until proper input detected.
    	while (guesses.contains(input.toLowerCase()) || word.contains(input.toLowerCase())) {
    		System.out.println("\nYou have already used this letter.");
    		System.out.print("Please enter a letter: ");
        	input = kb.nextLine();
        	input.toLowerCase();
        	
        	//If the user enters e but the word was "e---w-e", "You have already used this letter"
        	//will display, this loop catches non alphabet characters and more than or less than one letter.
        	while (!alphabet.contains(input.toLowerCase()) || input.length() != 1) {
        		System.out.println("\nYou have entered invalid input.");
        		System.out.print("Please enter a letter: ");
            	input = kb.nextLine();
            	input.toLowerCase();
        	}
    	}

    	return input.charAt(0);
    }
}
