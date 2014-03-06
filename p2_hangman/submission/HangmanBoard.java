import java.io.*;


public class HangmanBoard implements Board {
	private String[] baseImage = new String[13];
	private int counter = 0;
	private String word;
	
	/**
	The default constructor sets each line of the ascii art hangaman base
	board to an array. & accepts a String of the word for display if the game is lost.
	*/
	
	public HangmanBoard(String word) {
		this.word = word; //For when the game is won, it displays the word.
		
		baseImage[0]  = ("       _______");
		baseImage[1]  = ("       | /   |");
		baseImage[2]  = ("       |/");
		baseImage[3]  = ("       |");
		baseImage[4]  = ("       |");
		baseImage[5]  = ("       |");
		baseImage[6]  = ("    ---|--------------");
		baseImage[7]  = ("   /   |            / |");
		baseImage[8]  = ("  /    |           /  /");
		baseImage[9]  = (" /                /  /");
		baseImage[10] = ("------------------  /");
		baseImage[11] = ("|                | /");
		baseImage[12] = ("------------------");
	}
	
	/**
	The addonsDraw method adds body parts to the base hangman image.
	Each time it is called a body part is drawn, and the counter is advanced one number.
	*/
	
	public void addonsDraw() {
		if (counter == 0)
			baseImage[2] = "       |/    @";
		if (counter == 1) {
			baseImage[3] = "       |     |";
			baseImage[4] = "       |     |";
		}
		if (counter == 2)
			baseImage[3] = "       |    /|";
		if (counter == 3)
			baseImage[3] = "       |    /|\\";
		if (counter == 4)
			baseImage[5] = "       |    /";
		if (counter == 5)
			baseImage[5] = "       |    / \\";
		
		counter++;
	}

	/**
	The isWin method defines winning condition.
	*/
	
	public boolean isWin() {
		if (counter < 6)
			return true;
		return false;
	}

	/**
	The isLoose method defines loosing condition.
	*/
	
	public boolean isLoose() {
		if (counter == 6)
			return true;
		return false;
	}
	
	/**
	The isGameOver method defines game ending condition.
	*/
	
	public boolean isGameOver() {
		if (isLoose()) {
			System.out.println("You lost!");
			System.out.println("The word was: " + word);
			return true;
		}
		if (isWin()) {
			System.out.println("Game Over!\nYou won the game!\nCongratulations!\n");
			return true;
		}
		else 
			return false;
	}
	
	/**
	The toString method assembles the board each time the object is called.
	*/
	
	public String toString() {
		String str = "";
		
		for (int i = 0; i < baseImage.length; i++) {
			str += baseImage[i];
			str += "\n";
		}
		
		return str;
	}
}
