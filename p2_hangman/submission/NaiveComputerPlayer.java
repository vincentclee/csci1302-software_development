
public class NaiveComputerPlayer extends ComputerPlayer {
    
    /**
    Default constructor which sets the word with dashes, and guessed letters.
    @param word The word with dashes.
    @param guesses The string of already missed letters.
    */
	
	public NaiveComputerPlayer(String word, String guesses) {
    	super(word, guesses);
    }
	
	/**
	The makeGuess method makes a guess alphabetically starting with a.
	@return A character the player chooses.
	*/
	
    public char makeGuess() {
    	char guess = ' ';
    	
    	//Iterates over the length of the 26 character alphabet.
    	//If the word & misses does not contain character at i, guess is set to that letter.
    	for (int i = 0; i < alphabet.length(); i++) {
    		if (!word.contains(Character.toString(alphabet.charAt(i))) && !guesses.contains(Character.toString(alphabet.charAt(i)))) {
    			guess = alphabet.charAt(i);
    			break;
    		}
    	}
    	
    	System.out.print("Please enter a letter: " + guess + "\n");
    	
    	return guess;
    }
}
