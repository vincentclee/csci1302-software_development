import javax.swing.*; //Swing classes

public class FourInARow {
	private static final long serialVersionUID = 1L; //Serialize

	/**
	 * Default Constructor
	 */
	
	public FourInARow() {}

	/**
	 * The win method determines if there is a winner, and changes colors accordingly.
	 * @param gameboard the 0, 1, or 3 representation of the board.
	 * @param imageLabel the game pieces on the board.
	 * @param winImage a imagelabel icon used to indicate winner status.
	 * @param matchWin boolean to determine if there is a winner.
	 * @return if the match has been won.
	 */
	
	public boolean win(int[][] gameboard, JLabel[][] imageLabel, ImageIcon winImage, boolean matchWin) {
		//Prints out the gameboard, 3 is null, 1 is true red, 0 is false yellow
		for (int row = 0; row < gameboard.length; row++) {
			for (int col = 0; col < gameboard[0].length; col++) {
				System.out.print(gameboard[row][col] + " ");
			}
			System.out.println();
		}
		//Yellow Wins Horizontal
		//Checks across to the right for win.
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (gameboard[row][col] == 0 && !matchWin)
					if (col < 6)
						if (gameboard[row][col+1] == 0)
							if (col < 5)
								if (gameboard[row][col+2] == 0)
									if (col < 4)
										if (gameboard[row][col+3] == 0) {
											imageLabel[row][col].setIcon(winImage);
											imageLabel[row][col+1].setIcon(winImage);
											imageLabel[row][col+2].setIcon(winImage);
											imageLabel[row][col+3].setIcon(winImage);
											matchWin = true;
										}
			}
		}
		//Red Wins Horizontal
		//Checks across to the right for win.
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (gameboard[row][col] == 1 && !matchWin)
					if (col < 6)
						if (gameboard[row][col+1] == 1)
							if (col < 5)
								if (gameboard[row][col+2] == 1)
									if (col < 4)
										if (gameboard[row][col+3] == 1) {
											imageLabel[row][col].setIcon(winImage);
											imageLabel[row][col+1].setIcon(winImage);
											imageLabel[row][col+2].setIcon(winImage);
											imageLabel[row][col+3].setIcon(winImage);
											matchWin = true;
										}
			}
		}
		//Yellow Wins Vertically
		//Checks down vertically for a win.
		for (int col = 0; col < 7; col++) {
			for (int row = 0; row < 6; row++) {
				if (gameboard[row][col] == 0 && !matchWin)
					if (row < 5)
						if (gameboard[row+1][col] == 0)
							if (row < 4)
								if (gameboard[row+2][col] == 0)
									if (row < 3)
										if (gameboard[row+3][col] == 0) {
											imageLabel[row][col].setIcon(winImage);
											imageLabel[row+1][col].setIcon(winImage);
											imageLabel[row+2][col].setIcon(winImage);
											imageLabel[row+3][col].setIcon(winImage);
											matchWin = true;
										}
			}
		}
		//Red Wins Vertically
		//Checks down vertically for a win.
		for (int col = 0; col < 7; col++) {
			for (int row = 0; row < 6; row++) {
				if (gameboard[row][col] == 1 && !matchWin)
					if (row < 5)
						if (gameboard[row+1][col] == 1)
							if (row < 4)
								if (gameboard[row+2][col] == 1)
									if (row < 3)
										if (gameboard[row+3][col] == 1) {
											imageLabel[row][col].setIcon(winImage);
											imageLabel[row+1][col].setIcon(winImage);
											imageLabel[row+2][col].setIcon(winImage);
											imageLabel[row+3][col].setIcon(winImage);
											matchWin = true;
										}
			}
		}
		//Yellow Wins Diagonally Down
		//Checks for diagonal from left to right, and top to down.
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (gameboard[row][col] == 0 && !matchWin)
					if (row < 5 && col < 6)
						if (gameboard[row+1][col+1] == 0)
							if (row < 4 && col < 5)
								if (gameboard[row+2][col+2] == 0)
									if (row < 3 && col < 4)
										if (gameboard[row+3][col+3] == 0) {
											imageLabel[row][col].setIcon(winImage);
											imageLabel[row+1][col+1].setIcon(winImage);
											imageLabel[row+2][col+2].setIcon(winImage);
											imageLabel[row+3][col+3].setIcon(winImage);
											matchWin = true;
										}
			}
		}
		//Red Wins Diagonally Down
		//Checks for diagonal from left to right, and top to down.
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (gameboard[row][col] == 1 && !matchWin)
					if (row < 5 && col < 6)
						if (gameboard[row+1][col+1] == 1)
							if (row < 4 && col < 5)
								if (gameboard[row+2][col+2] == 1)
									if (row < 3 && col < 4)
										if (gameboard[row+3][col+3] == 1) {
											imageLabel[row][col].setIcon(winImage);
											imageLabel[row+1][col+1].setIcon(winImage);
											imageLabel[row+2][col+2].setIcon(winImage);
											imageLabel[row+3][col+3].setIcon(winImage);
											matchWin = true;
										}
			}
		}
		//Yellow Wins Diagonally Up
		//Checks for diagonal from left to right, and down to top.
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (gameboard[row][col] == 0 && !matchWin)
					if (row > 0 && col < 6)
						if (gameboard[row-1][col+1] == 0)
							if (row > 1 && col < 5)
								if (gameboard[row-2][col+2] == 0)
									if (row > 2 && col < 4)
										if (gameboard[row-3][col+3] == 0) {
											imageLabel[row][col].setIcon(winImage);
											imageLabel[row-1][col+1].setIcon(winImage);
											imageLabel[row-2][col+2].setIcon(winImage);
											imageLabel[row-3][col+3].setIcon(winImage);
											matchWin = true;
										}
			}
		}
		//Red Wins Diagonally Up
		//Checks for diagonal from left to right, and down to top.
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (gameboard[row][col] == 1 && !matchWin)
					if (row > 0 && col < 6)
						if (gameboard[row-1][col+1] == 1)
							if (row > 1 && col < 5)
								if (gameboard[row-2][col+2] == 1)
									if (row > 2 && col < 4)
										if (gameboard[row-3][col+3] == 1) {
											imageLabel[row][col].setIcon(winImage);
											imageLabel[row-1][col+1].setIcon(winImage);
											imageLabel[row-2][col+2].setIcon(winImage);
											imageLabel[row-3][col+3].setIcon(winImage);
											matchWin = true;
										}
			}
		}
		//Returns true or false, if there is a win.
		return matchWin;
	}
}
