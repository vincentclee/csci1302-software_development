
public class CutThroatComputerPlayer extends ComputerPlayer {
	/**
    Default constructor which sets the word with dashes, and guessed letters.
    @param word The word with dashes.
    @param guesses The string of already missed letters.
    */
	
	public CutThroatComputerPlayer(String word, String guesses) {
    	super(word, guesses);
    }

	/**
	The makeGuess method runs Chris Plaue's "Doc's" AI.
	@return A character the player chooses.
	*/
	
    public char makeGuess() {
    	HangmanAI hAI = new HangmanAI();
    	
    	char guess = hAI.makeGuess(word, guesses);
    	
    	System.out.print("Please enter a letter: " + guess + "\n");
    	
    	return guess;
    }
}
