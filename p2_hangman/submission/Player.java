
public class Player {
	//Protected allows all child classes to have direct access to the String objects.
	protected String word = "";
    protected String guesses = "";
    protected String alphabet = "abcdefghijklmnopqrstuvwxyz";
    
    /**
    Default constructor which sets the word with dashes, and guessed letters.
    @param word The word with dashes.
    @param guesses The string of already missed letters.
    */
    
    public Player(String word, String guesses) {
    	this.word = word;
    	this.guesses = guesses;
    }

    /**
    The setInput method allows the main method to update the word with dashes
    ...and the String of guessed letters.
    @param word The word with dashes.
    @param guesses The string of already missed letters.
    */
    
    public void setInput(String word, String guesses) {
    	this.word = word;
    	this.guesses = guesses;
    }
    
    /**
    The makeGuess method is a place holder for child classes.
    @return Blank Character.
    */
    
    public char makeGuess() {
    	return ' ';
    }
}
