import java.awt.*; //Layout manager
import java.awt.event.*; //Event listener interface
import javax.swing.*; //Swing classes
import java.util.*; //Stack

public class GameBoard extends JFrame {
	//FourInARow class object.
	FourInARow fourInARow = new FourInARow();

	//JFrame Setup
	private static final long serialVersionUID = 1L; //Serialize

	//Components
	private JPanel basePanel, scorePanel, buttonPanel;
	private JLabel[][] imageLabel = new JLabel[6][7];
	private JLabel[] topImageLabel = new JLabel[7];
	private JLabel scoreLabel, actualScoreLabel;
	private JButton resetButton, undoButton, exitButton;

	//Image Icons
	private ImageIcon redImage = new ImageIcon("red.png");
	private ImageIcon winImage = new ImageIcon("win.png");
	private ImageIcon clearImage = new ImageIcon("clear.png");
	private ImageIcon blankImage = new ImageIcon("blank.png");
	private ImageIcon yellowImage = new ImageIcon("yellow.png");
	private ImageIcon redSoloImage = new ImageIcon("redSolo.png");
	private ImageIcon yellowSoloImage = new ImageIcon("yellowSolo.png");

	//Global variables
	private int gamePiecePosition = 0;
	private final int GAME_ANIMATION_SPEED = 100; //Milliseconds
	private int[][] gameboard = new int[6][7];
	private boolean down = false;
	private boolean animation = false;
	private boolean currentPlayer = false; //true is red, false is yellow
	private boolean matchWin = false;
	private int redScore = 0;
	private int yellowScore = 0;
	private String[] options = {"Yes","No"}; //JOPtion pane options

	//Menu Bar
	private JMenuBar menuBar; //The menu bar
	private JMenu fileMenu, editMenu, helpMenu; //The File & Edit menu etc.
	private JMenuItem exitItem, undoItem, resetItem, getStartedItem, aboutItem; //To exit or & undo etc.

	//X&Y Coordinates
	private int x, y;

	//Stack for undo
	private Stack<Integer> gameboardRowStack = new Stack<Integer>();
	private Stack<Integer> gameboardColStack = new Stack<Integer>();

	/**
	 * Default Constructor
	 */
	
	public GameBoard() {
		System.out.println("Start");

		//Set the tile bar text.
		setTitle("Milton Bradley Connect Four v1.0.1");

		//Specify an action for the close button.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Frame cannot be resized.
		setResizable(false);

		//Add a BorderLayout manager to the content pane.
		setLayout(new BorderLayout());
				
		//Create the custom panels.
		menuBar();
		basePanel();
		basePanel.addMouseListener(new ListenerMouse()); //Mouse
		basePanel.addKeyListener(new ListenerKeyboard()); //Keyboard
		basePanel.setFocusable(true); //allows keyboard to work.
		scorePanel();
		buttonPanel();

		//Sets starting player.
		if (currentPlayer)
			topImageLabel[0].setIcon(redSoloImage);
		else
			topImageLabel[0].setIcon(yellowSoloImage);

		//Add objects to content pane.
		add(basePanel, BorderLayout.NORTH);
		add(scorePanel, BorderLayout.EAST);
		add(buttonPanel, BorderLayout.WEST);

		//Creates a grid out of 0, 1, and 3 is null.
		for (int col = 0; col < gameboard[0].length; col++) {
			for (int row = 0; row < gameboard.length; row++) {
				gameboard[row][col] = 3;
			}
		}


		pack();
		setVisible(true);
		setLocationRelativeTo(null); //Displays the window in the middle of the screen.

		//Game gets stuck in this loop and continues until exit.
		while (0 < 1) {
			//If the drop key is pressed, it triggers this if loop.
			if (down) {
				//Key presses are not allowed during the animation.
				//animation boolean disables and enables this functionality.
				animation = true;
				animation();
				down = false;
				animation = false;
				
				//currentPlayer true is red, else it is yellow's logic.
				if (currentPlayer) {
					for (int i = 0; i < 6; i++) {
						
						//If the column is already full, it will show a error message. 
						if (gameboard[0][gamePiecePosition] == 0 || gameboard[0][gamePiecePosition] == 1) {
							JOptionPane.showMessageDialog(null, "Invalid Move. Try Again.", "Error", JOptionPane.WARNING_MESSAGE);
							currentPlayer = !currentPlayer;
							break;
						}
						
						//If the gameboard at that position is full, it subtracts one and stores a value.
						if (gameboard[i][gamePiecePosition] == 0 || gameboard[i][gamePiecePosition] == 1)
							if (i > 0) {
								gameboard[i - 1][gamePiecePosition] = 1;
								gameboardColStack.push(gamePiecePosition);
								gameboardRowStack.push(i-1);
								break;
							}
					}
					
					//First move of each game.
					if (gameboard[5][gamePiecePosition] == 3) {
						gameboard[5][gamePiecePosition] = 1;
						gameboardColStack.push(gamePiecePosition);
						gameboardRowStack.push(5);
					}
				} else {
					for (int i = 0; i < 6; i++) {	
						//If the column is already full, it will show a error message. 
						if (gameboard[0][gamePiecePosition] == 0 || gameboard[0][gamePiecePosition] == 1) {
							JOptionPane.showMessageDialog(null, "Invalid Move. Try Again.", "Error", JOptionPane.WARNING_MESSAGE);
							currentPlayer = !currentPlayer;
							break;
						}
						
						//If the gameboard at that position is full, it subtracts one and stores a value.
						if (gameboard[i][gamePiecePosition] == 0 || gameboard[i][gamePiecePosition] == 1)
							if (i > 0) {
								gameboard[i - 1][gamePiecePosition] = 0;
								gameboardColStack.push(gamePiecePosition);
								gameboardRowStack.push(i-1);
								break;
							}
					}
					
					//First move of each game.
					if (gameboard[5][gamePiecePosition] == 3) {
						gameboard[5][gamePiecePosition] = 0;
						gameboardColStack.push(gamePiecePosition);
						gameboardRowStack.push(5);
					}
				}

				//Changes the player. 
				currentPlayer = !currentPlayer;
				
				//Sets the color of the drop icon according to who's turn it is.
				if (currentPlayer)
					topImageLabel[gamePiecePosition].setIcon(redSoloImage);
				else
					topImageLabel[gamePiecePosition].setIcon(yellowSoloImage);

				//checks for a winner in a match, returns either true or false.
				matchWin = fourInARow.win(gameboard, imageLabel, winImage, matchWin);

				//If the match is won, it indicates who won, and updates the score, and reset's the board.
				if (matchWin) {
					if (!currentPlayer) {
						JOptionPane.showMessageDialog(null, "Red Won!");
						redScore++;
						actualScoreLabel.setText(new String("Red: " + redScore + " Yellow: " + yellowScore));
						reset();
					} else {
						JOptionPane.showMessageDialog(null, "Yellow Won!");
						yellowScore++;
						actualScoreLabel.setText(new String("Red: " + redScore + " Yellow: " + yellowScore));
						resetOne();
					}
					
					//Set match win to false, for next loop.
					matchWin = false;
				}

				//Worst case, all spots are filled with no winner.
				int total = 0;
				for (int row = 0; row < gameboard.length; row++) {
					for (int col = 0; col < gameboard[0].length; col++) {
						if (gameboard[row][col] != 3)
							total++;
					}
				}
				if (total == 42) {
					JOptionPane.showMessageDialog(null, "No more possible moves. Resetting...", "Error", JOptionPane.WARNING_MESSAGE);
					reset();
				}

				System.out.println("Score: Red: " + redScore + " Yellow: " + yellowScore);
			}
			
			//If there are no possible undo's, the undo feature will not click.
			if (gameboardColStack.size() == 0) {
				undoItem.setEnabled(false);
				undoButton.setEnabled(false);
			}
			else {
				undoButton.setEnabled(true);
				undoItem.setEnabled(true);
			}
				
			//Pauses the while loop for 1 millisecond.
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The menuBar method builds the menu bar.
	 */

	private void menuBar() {
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Create the file, edit, and help menus.
		fileMenu();
		editMenu();
		helpMenu();

		//Add the file, edit, and help menus to the menu bar.
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);

		//Set the window's menu bar.
		setJMenuBar(menuBar);
	}

	/**
	 * The fileMenu method builds the File menu and returns a reference to its JMenu object.
	 */

	private void fileMenu() {
		//Create an Exit menu item.
		exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ExitListener());
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));

		//Create a JMenu object for the File menu.
		fileMenu = new JMenu("File");

		//Add the Exit menu item to the File menu.
		fileMenu.add(exitItem);
	}

	/**
	 * The editMenu method builds the Edit menu and returns a reference to its JMenu object.
	 */

	private void editMenu() {
		//Create an Undo menu item.
		undoItem = new JMenuItem("Undo");
		undoItem.setMnemonic(KeyEvent.VK_U);
		undoItem.addActionListener(new UndoListener());
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));

		//Create an Reset menu item.
		resetItem = new JMenuItem("Reset");
		resetItem.setMnemonic(KeyEvent.VK_R);
		resetItem.addActionListener(new ResetListener());
		resetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));

		//Create a JMenu object for the Edit menu.
		editMenu = new JMenu("Edit");

		//Add the menu items to the Edit menu.
		editMenu.add(undoItem);
		editMenu.add(resetItem);
	}

	/**
	 * The helpMenu method builds the Help menu and returns a reference to its JMenu object.
	 */

	private void helpMenu() {
		//Create an About menu item.
		getStartedItem = new JMenuItem("Get Started");
		getStartedItem.setMnemonic(KeyEvent.VK_G);
		getStartedItem.addActionListener(new GetStartedListener());

		//Create an About menu item.
		aboutItem = new JMenuItem("About");
		aboutItem.setMnemonic(KeyEvent.VK_A);
		aboutItem.addActionListener(new AboutListener());

		//Create a JMenu object for the Edit menu.
		helpMenu = new JMenu("Help");

		//Add the menu items to the Help menu.
		helpMenu.add(getStartedItem);
		helpMenu.add(aboutItem);

	}

	/**
	 * The basePanel method sets up the main game panel with blank slots 6 rows x 7 columns.
	 */

	private void basePanel() {
		//Create a sub-panel
		basePanel = new JPanel();

		//Set a sub-panel layout independent of global layout.
		basePanel.setLayout(new GridLayout(7, 7));

		//Initiates JLabel objects
		//Sets a blank image to each label.
		//Adds imageLabel to the panel.
		for (int i = 0; i < 7; i++) {
			topImageLabel[i] = new JLabel();
			topImageLabel[i].setIcon(clearImage);
			basePanel.add(topImageLabel[i]);
		}

		//Main Game Board
		for (int col = 0; col < imageLabel.length; col++) {
			for (int row = 0; row < imageLabel[0].length; row++) {
				imageLabel[col][row] = new JLabel();
				imageLabel[col][row].setIcon(blankImage);
				basePanel.add(imageLabel[col][row]);
			}
		}
	}

	/**
	 * The scorePanel method builds the panel and displays the score.
	 */
	
	private void scorePanel() {
		//Create a sub-panel
		scorePanel = new JPanel();

		scorePanel.setLayout(new BorderLayout());
		//Initiates JLabel objects
		scoreLabel = new JLabel("SCORE ");
		actualScoreLabel = new JLabel("Red: " + redScore + " Yellow: " + yellowScore + "");

		//Adds JLabel objects to the panel.
		scorePanel.add(scoreLabel, BorderLayout.EAST);
		scorePanel.add(actualScoreLabel, BorderLayout.SOUTH);
	}

	/**
	 * The buttonPanel method builds the panel and displays the buttons.
	 */
	
	private void buttonPanel() {
		//Create a sub-panel
		buttonPanel = new JPanel();
		
		//Initiates JLabel objects
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ExitListener());
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ResetListener());
		undoButton = new JButton("Undo");
		undoButton.addActionListener(new UndoListener());
		
		//Adds JLabel objects to the panel.
		buttonPanel.add(exitButton);
		buttonPanel.add(resetButton);
		buttonPanel.add(undoButton);
	}
	
	/**
	 * The animation method controls and display animation for the piece falling down.
	 */

	private void animation() {
		try {
			//If the top row of the column is empty, it will start the animation.
			if (gameboard[0][gamePiecePosition] == 3) {
				Thread.sleep(GAME_ANIMATION_SPEED);
				topImageLabel[gamePiecePosition].setIcon(clearImage);
				if (currentPlayer)
					imageLabel[0][gamePiecePosition].setIcon(redImage);
				else
					imageLabel[0][gamePiecePosition].setIcon(yellowImage);
			}

			for (int i = 1; i < 6; i++) {
				//If the column at that particular row, it will animate. 
				if (gameboard[i][gamePiecePosition] == 3) {
					Thread.sleep(GAME_ANIMATION_SPEED);
					if (currentPlayer)
						imageLabel[i][gamePiecePosition].setIcon(redImage);
					else
						imageLabel[i][gamePiecePosition].setIcon(yellowImage);
					
					imageLabel[i - 1][gamePiecePosition].setIcon(blankImage);
				}
			}
		} catch (InterruptedException ee) {
			ee.printStackTrace();
		}
	}

	/**
	 * The method reset, resets the whole board.
	 */

	private void reset() {
		System.out.println(":Reset");
		
		//Sets the entire board to blank images.
		for (int col = 0; col < imageLabel.length; col++) {
			for (int row = 0; row < imageLabel[0].length; row++)
				imageLabel[col][row].setIcon(blankImage);
		}
		
		//Sets the starting puck position to 0, or left.
		gamePiecePosition = 0;
		
		//Sets top part of the images to clear.
		for (int i = 0; i < 7; i++)
			topImageLabel[i].setIcon(clearImage);
		
		//Changes player.
		currentPlayer = false;
		
		//Sets yellow or 0 or false as current player.
		topImageLabel[0].setIcon(yellowSoloImage);

		//Sets the numerical gameboard to all 3 or null.
		for (int col = 0; col < gameboard[0].length; col++) {
			for (int row = 0; row < gameboard.length; row++) {
				gameboard[row][col] = 3;
			}
		}
		
		//Clears the stacks of all the undo elements. 
		gameboardColStack.removeAllElements();
		gameboardRowStack.removeAllElements();
	}

	/**
	 * The method reset, resets the whole board. Also lets loosing player go first.
	 */

	private void resetOne() {
		System.out.println(":Reset1");
		
		//Sets the entire board to blank images.
		for (int col = 0; col < imageLabel.length; col++) {
			for (int row = 0; row < imageLabel[0].length; row++)
				imageLabel[col][row].setIcon(blankImage);
		}
		
		//Sets the starting puck position to 0, or left.
		gamePiecePosition = 0;
		
		//Sets top part of the images to clear.
		for (int i = 0; i < 7; i++)
			topImageLabel[i].setIcon(clearImage);
		
		//Changes player.
		currentPlayer = true;
		
		//Sets yellow or 1 or true as current player.
		topImageLabel[0].setIcon(redSoloImage);

		//Sets the numerical gameboard to all 3 or null.
		for (int col = 0; col < gameboard[0].length; col++) {
			for (int row = 0; row < gameboard.length; row++) {
				gameboard[row][col] = 3;
			}
		}
		
		//Clears the stacks of all the undo elements. 
		gameboardColStack.removeAllElements();
		gameboardRowStack.removeAllElements();
	}

	/**
	 * The undo method performs the undo. 
	 */
	
	private void undo() {
		//If the stack size, there is no undo's, error message is generated.
		if (gameboardColStack.size() == 0)
			JOptionPane.showMessageDialog(null, "Can't Undo.", "Error", JOptionPane.WARNING_MESSAGE);
		else {
			
			//col and row int's are populated with last undo. 
			//pop reads and removes top item.
			int col = gameboardColStack.pop();
			int row = gameboardRowStack.pop();
			
			//That past position, is set to blank.
			imageLabel[row][col].setIcon(blankImage);
			
			//The numeric gameboard is also set to null at that position.
			gameboard[row][col] = 3;
			
			//Player change
			currentPlayer = !currentPlayer;

			//Sets the player's turn accordingly.
			if (currentPlayer)
				topImageLabel[gamePiecePosition].setIcon(redSoloImage);
			else
				topImageLabel[gamePiecePosition].setIcon(yellowSoloImage);
		}
	}

	/**
	 * Private inner class that handles the event that is generated when the user selects Exit from the File menu.
	 */

	private class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Show option either yes or no, sets focus on no for accidents.
			if (JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]) == 0) {
				System.out.println(":Exit");
				System.exit(0);
			}
			
			//Turns off button's focus, and returns main focus to board for key events.
			exitButton.setFocusable(false);
		}
	}

	/**
	 * Private inner class that handles the event that is generated when the user selects Undo from the Edit menu.
	 */

	private class UndoListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(":Undo");
			undo();
			
			//Turns off button's focus, and returns main focus to board for key events.
			undoButton.setFocusable(false);
		}
	}

	/**
	 * Private inner class that handles the event that is generated when the user selects Reset from the Edit menu.
	 */

	private class ResetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Show option either yes or no, sets focus on no for accidents.
			//If option is 0 or yes, resets board, score, and sets score on the board.
			if (JOptionPane.showOptionDialog(null, "Are you sure you want to reset?", "Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]) == 0) {
				reset();
				redScore = 0;
				yellowScore = 0;
				actualScoreLabel.setText(new String("Red: " + redScore + " Yellow: " + yellowScore));
			}
			
			//Turns off button's focus, and returns main focus to board for key events.
			resetButton.setFocusable(false);
		}
	}

	/**
	 * Private inner class that handles the event that is generated when the user selects Get Started from the Help menu.
	 */

	private class GetStartedListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Getting Started message.
			JOptionPane.showMessageDialog(null, "<html>Congratulations on your purchase of Connect 4!<br/>" +
					"There are two different kinds of input this game allows: Keyboard and Mouse<br/><br/>" +
					"MOUSE<br/>" +
					"Click the clear space above which column you would like to place your piece.<br/>" +
					"To set piece in the grid, click anywhere in the grid, and the piece will drop into place.<br/>" +
					"To undo, click the undo button at the bottom, or under the edit menu select undo.<br/>" +
					"To reset, click the reset button at the bottom, or under the edit menu select reset.<br/>" +
					"To exit the game, click the exit button at the bottom, or file > exit.<br/><br/>" +
					"KEYBOARD<br/>" +
					"Use the left and right arrow keys to change column, and use the down arrow to set the piece in the board.<br/>" +
					"To undo, use the up arrow key.<br/><br/>" +
					"SHORTCUTS<br/>" +
					"Ctrl-W Exit the program<br/>" +
					"Ctrl-Z Undo last move<br/>" +
					"Ctrl-R Reset the board & running score<br/><html/>", "Getting Started", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Private inner class that handles the event that is generated when the user selects About from the Help menu.
	 */

	private class AboutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(":About");
			
			//About message.
			JOptionPane.showMessageDialog(null, new JLabel("<html><center>Milton Bradley Connect Four v1.0.1<br/>" +
					"Vincent Lee<br/>" +
					"Spring 2012</center></html>"), "About", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Mouse Motion Listener Class
	 */

	private class ListenerMouse implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			//Sets x and y position through get.
			x = e.getX();
			y = e.getY();
			System.out.println("X: " + x + " Y: " + y);

			//Only if animation is false, will position be set. 
			//This is done because once animating, key events can disrupt stream.
			if (!animation) {
				//If the mouse is in the range of the top clear area, sets position of the marker.
				if (y <= 60) {
					topImageLabel[gamePiecePosition].setIcon(clearImage);
					gamePiecePosition = x / 60;
					if (currentPlayer)
						topImageLabel[gamePiecePosition].setIcon(redSoloImage);
					else
						topImageLabel[gamePiecePosition].setIcon(yellowSoloImage);
				}
				//If the mouse is in the board, the marker drows down through animation.
				if (y > 60) {
					down = true;
				}
			}
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}

	/**
	 * Keyboard Motion Listener Class
	 */

	private class ListenerKeyboard implements KeyListener {
		public void keyPressed(KeyEvent e) {
			//Only if animation is false, will position be set.
			if (!animation) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					System.out.println(":Up");
					undo();
					break;
				case KeyEvent.VK_DOWN:
					System.out.println(":Down");
					down = true;
					break;
				case KeyEvent.VK_LEFT:
					//Does not allow gamepiece to go out of bounds.
					if (gamePiecePosition == 0)
						gamePiecePosition = 0;
					else
						gamePiecePosition--;

					//Sets where the icon is based on left arrow.
					if (currentPlayer) {
						topImageLabel[gamePiecePosition].setIcon(redSoloImage);
						topImageLabel[gamePiecePosition + 1].setIcon(clearImage);
					} else {
						topImageLabel[gamePiecePosition].setIcon(yellowSoloImage);
						topImageLabel[gamePiecePosition + 1].setIcon(clearImage);
					}
					
					System.out.println("Left");
					System.out.println("PiecePosition: " + gamePiecePosition);
					break;
				case KeyEvent.VK_RIGHT:
					//Does not allow gamepiece to go out of bounds.
					if (gamePiecePosition == 6)
						gamePiecePosition = 6;
					else
						gamePiecePosition++;
					
					//Sets where the icon is based on left arrow.
					if (currentPlayer) {
						topImageLabel[gamePiecePosition].setIcon(redSoloImage);
						topImageLabel[gamePiecePosition - 1].setIcon(clearImage);
					} else {
						topImageLabel[gamePiecePosition].setIcon(yellowSoloImage);
						topImageLabel[gamePiecePosition - 1].setIcon(clearImage);
					}
					
					System.out.println("Right");
					System.out.println("PiecePosition: " + gamePiecePosition);
					break;
				}
			}
		}

		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
}
