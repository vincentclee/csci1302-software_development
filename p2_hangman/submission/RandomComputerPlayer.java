import java.util.*; //Random Generator

public class RandomComputerPlayer extends ComputerPlayer {
    
    /**
    Default constructor which sets the word with dashes, and guessed letters.
    @param word The word with dashes.
    @param guesses The string of already missed letters.
    */
	
	public RandomComputerPlayer(String word, String guesses) {
    	super(word, guesses);
    }
    
	/**
	The makeGuess method makes a guess based on a random number generated.
	@return A character the player chooses.
	*/
	
    public char makeGuess() {
		int letterIndex = 0;
    	String guess = "";
		
    	//Generates a random number based on the alphabet length
    	Random randomGenerator = new Random();
    	letterIndex = randomGenerator.nextInt(alphabet.length());

    	//Guess is set to the character at the random number.
    	guess = Character.toString(alphabet.charAt(letterIndex));
    	
    	//If the guessed character is already in the word with dashes, or missed characters.
    	//...a new number is generated, and checked until both conditions are false.
    	while (word.contains(guess) || guesses.contains(guess)) {
        	letterIndex = randomGenerator.nextInt(alphabet.length());
        	guess = Character.toString(alphabet.charAt(letterIndex));
    	}

    	System.out.print("Please enter a letter: " + guess + "\n");
    	
    	return guess.charAt(0);
    }
}
