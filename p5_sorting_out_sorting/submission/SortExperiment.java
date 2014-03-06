
/**
 * SortExperiment.java contains the GUI builder for the project
 * @author Vincent Lee
 * @version 1.0
 */

import java.util.Random;
import java.text.DecimalFormat;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;

public class SortExperiment extends JFrame {
	//JFrame Setup
	private static final long serialVersionUID = 1L; //Serialize
	private static final int WIDTH = 800;
	private static final int HEIGHT = 680;
	private boolean DEBUG = false;
	
	//Menu Bar
	private JMenuBar menuBar; //The menu bar
	private JMenu fileMenu, helpMenu; //The File menu etc.
	private JMenuItem exitItem, quickTipsItem, aboutItem; //To exit etc.
	
	//GUI Components
	private JTabbedPane tabbedPane;
	private JSlider sizeSlider;
	private JPanel mainPanel, graphsPanel, westPanel, eastPanel, centerPanel, northPanel, northSubPanel, eastNorthPanel, eastSouthPanel;
	private JRadioButton bestCaseRadioButton, worstCaseRadioButton, averageCaseRadioButton;
	private ButtonGroup buttonGroup;
	private JButton benchmarkButton, updateButton;
	private JTextField sizeField;
	private JLabel tachometerLabel, collateLabel, insertionHelpLabel, bubbleHelpLabel, happyHourHelpLabel, quickHelpLabel, collateHelpLabel, averageHelpLabel, bestHelpLabel, worstHelpLabel;
	private JCheckBox insertionSortCheckBox, bubbleSortCheckBox, happyHourSortCheckBox, quickSortCheckBox, collateCheckBox;
	
	//Other Variables
	private int arrayLength = 0; //Number of objects sorted
	private int numberOfAlgorithms = 0; //How many are selected
	private long quickSortRuntime = 0;
	private long bubbleSortRuntime = 0;
	private long happyHourSortRuntime = 0;
	private long insertionSortRuntime = 0;
	private boolean insertionSort, bubbleSort, happyHourSort, quickSort, collate, bestCase, worstCase, averageCase, benchmark, verifyInsertionSort, verifyBubbleSort, verifyHappyHourSort, verifyQuickSort;
	private String[] options = {"Yes", "No"}; //JOPtion pane options
	
	//Image Icons
	private ImageIcon tac0 = new ImageIcon("tac0.png");
	private ImageIcon tac1 = new ImageIcon("tac1.png");
	private ImageIcon tac2 = new ImageIcon("tac2.png");
	private ImageIcon tac3 = new ImageIcon("tac3.png");
	private ImageIcon tac4 = new ImageIcon("tac4.png");
	private ImageIcon collate_yes = new ImageIcon("collate_yes.png");
	private ImageIcon collate_no = new ImageIcon("collate_no.png");
	private ImageIcon help = new ImageIcon("help_icon.gif");
	
	//Arrays
	private int[][] array = null;
	private int[][] arrayCopy = null;
	
	public SortExperiment() {
		if (DEBUG) System.out.println("Start");

		//Set the tile bar text.
		setTitle("SortBench Pro Classified Edition 2012 v114.2");

		//Specify an action for the close button.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set Size
		setSize(WIDTH, HEIGHT);
		
		//Frame cannot be resized.
		setResizable(false);

		//Add a TabbedPane manager to the content pane.
		tabbedPane = new JTabbedPane();
		
		//set the layout.
		setLayout(new BorderLayout());
		
		//Create the custom panels.
		menuBar();
		mainPanel();
		graphsPanel();

		//Add objects to tabbed pane.
		tabbedPane.addTab("Options", mainPanel);
		tabbedPane.addTab("Graphs/Results", graphsPanel);
		
		//Add objects to content pane.
		add(tabbedPane);
	
		setVisible(true); //Switches Pane on or off.
		setLocationRelativeTo(null); //Displays the window in the middle of the screen.
	}
	
	/**
	 * The graphsPanel method creates a JPanel which paints the bar graph.
	 */
	
	private void graphsPanel() {
		//Create a new panel.
		graphsPanel = new JPanel();
		
		//Add objects to content pane.
		graphsPanel.add(new graphComponent());
	}

	/**
	 * The graphComponent method/class draws the bar graph.
	 * @author Vincent Lee
	 */
	
	class graphComponent extends JComponent {
	    private static final long serialVersionUID = 1L; //Serialize

	    @Override
	    public Dimension getMinimumSize() {
	        return new Dimension(100, 100);
	    }

	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(800, 680);
	    }

	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.setColor(Color.BLACK);

	        //Sets the Title of the Graph
	        if (benchmark) {
		        String title = "";
		        if (averageCase) 
		        	title += "Average Case - ";
		        if (worstCase) 
		        	title += "Worse Case - ";
		        if (bestCase) 
		        	title += "Best Case - ";
		        if (collate)
		        	title += "Collated - ";
		        g.drawString(title + Integer.toString(arrayLength) + " items (x30 arrays)", 280, 20);
	        }
	        
	        //Grid Vertical Line
	        g.drawLine(80, 30, 80, 550);
	        //Grid Horizontal Line
	        g.drawLine(80, 550, 700, 550);
	        
	        //Algorithms Labels
	        if (numberOfAlgorithms == 1) {
		        if (bubbleSort) 
		        	g.drawString("Bubble Sort", 116, 570); //+16 to be in the center.
		        if (quickSort) 
		        	g.drawString("Quick Sort", 116, 570); //+21
		        if (happyHourSort) 
		        	g.drawString("Happy Hour Sort", 114, 570); //+5
		        if (insertionSort) 
		        	g.drawString("Insertion Sort", 116, 570); //+12
	        }
	        if (numberOfAlgorithms == 2) {
		        int spacing = 100;

	        	if (bubbleSort) {
	        		spacing += 16;
	        		g.drawString("Bubble Sort", spacing, 570); //+16
	        		spacing += 134;
	        	}
		        if (quickSort) {
		        	spacing += 21;
		        	g.drawString("Quick Sort", spacing, 570); //+21
		        	spacing += 129;
		        }
		        if (happyHourSort) {
		        	spacing += 5;
		        	g.drawString("Happy Hour Sort", spacing, 570); //+5
		        	spacing += 145;
		        }
		        if (insertionSort) {
		        	spacing += 12;
		        	g.drawString("Insertion Sort", spacing, 570); //+12
		        	spacing += 138;
		        }
	        }
	        if (numberOfAlgorithms == 3) {
	        	int spacing = 100;
	        	
		        if (bubbleSort) {
		        	spacing += 16;
		        	g.drawString("Bubble Sort", spacing, 570); //+16
		        	spacing += 134;
		        }
		        if (quickSort) {
		        	spacing += 21;
		        	g.drawString("Quick Sort", spacing, 570); //+21
		        	spacing += 129;
		        }
		        if (happyHourSort) {
		        	spacing += 5;
		        	g.drawString("Happy Hour Sort", spacing, 570); //+5
		        	spacing += 145;
		        }
		        if (insertionSort) {
		        	spacing += 12;
		        	g.drawString("Insertion Sort", spacing, 570); //+12
		        	spacing += 138;
		        }   	
	        }
	        if (numberOfAlgorithms == 4) {
		        if (bubbleSort) 
		        	g.drawString("Bubble Sort", 116, 570); //+16
		        if (quickSort) 
		        	g.drawString("Quick Sort", 271, 570); //+21
		        if (happyHourSort) 
		        	g.drawString("Happy Hour Sort", 405, 570); //+5
		        if (insertionSort) 
		        	g.drawString("Insertion Sort", 562, 570); //+12
	        }
	        
	        //Determines which took the longest and sets that one.
	        boolean bubbleSortLongest = false;
	        boolean quickSortLongest = false;
	        boolean happyHourSortLongest = false;
	        boolean insertionSortLongest = false;
	        long longestTime = 0;
	        if (insertionSortRuntime > longestTime) {
	        	insertionSortLongest = true;
	        	longestTime = insertionSortRuntime;
	        	bubbleSortLongest = false;
	        	happyHourSortLongest = false;
	        	quickSortLongest = false;
	        }
	        if (bubbleSortRuntime > longestTime) {
	        	bubbleSortLongest = true;
	        	longestTime = bubbleSortRuntime;
	        	insertionSortLongest = false;
	        	happyHourSortLongest = false;
	        	quickSortLongest = false;
	        }
	        if (happyHourSortRuntime > longestTime) {
	        	happyHourSortLongest = true;
	        	longestTime = happyHourSortRuntime;
	        	insertionSortLongest = false;
	        	bubbleSortLongest = false;
	        	quickSortLongest = false;
	        }
	        if (quickSortRuntime > longestTime) {
	        	quickSortLongest = true;
	        	longestTime = quickSortRuntime;
	        	insertionSortLongest = false;
	        	happyHourSortLongest = false;
	        	bubbleSortLongest = false;
	        }
	        
	        if (bubbleSortLongest && DEBUG)
	        	System.out.println("Longest: Bubble Sort");
	        if (quickSortLongest && DEBUG)
	        	System.out.println("Longest: Quick Sort");
	        if (happyHourSortLongest && DEBUG)
	        	System.out.println("Longest: Happy Hour Sort");
	        if (insertionSortLongest && DEBUG)
	        	System.out.println("Longest: Insertion Sort");
	        
	        //Determines the size of the bar on the graph.
	        double maxLength = 0;
	        
	        //Sometimes a array can take 0 seconds to complete, this is not possible
	        //...Therefore we give it a value of 1 milliseconds.
			if (longestTime == 0) {
				longestTime = 1;
			}
	        
	        //Determines Bar size based on pixels
	        if (longestTime > 500)
	        	maxLength = (double) longestTime / 500;
	        else
	        	maxLength = (double) 500 / longestTime;
	        
	        if (DEBUG) System.out.println("Bar %: " + maxLength);
	        
	        
	        //Increment Lines
	        int amount = 25;
	        int incriment = 550 - amount;
	        DecimalFormat decimal = new DecimalFormat("0.00");
	        double longestTimeLabelIncriment = (double) longestTime / 20;
	        double longestTimeLabel = 0;
	        int paddingTicks = 0;
	        int barPadding = 0;
	        
	        //Compensates for number and attempts to set value to center of bar
	        if (longestTime > 1 && longestTime < 10) {
	        	paddingTicks = 0;
	        	barPadding = 3;
	        }
	        if (longestTime > 10 && longestTime < 100) {
	        	paddingTicks = 5;
	        	barPadding = 7;
	        }
	        if (longestTime > 100 && longestTime < 1000) {
	        	paddingTicks = 15;
	        	barPadding = 10;
	        }
	        if (longestTime > 1000 && longestTime < 10000) {
	        	paddingTicks = 20;
	        	barPadding = 14;
	        }
	        if (longestTime > 10000 && longestTime < 100000) {
	        	paddingTicks = 30;
	        	barPadding = 17;
	        }
	        if (longestTime > 100000 && longestTime < 1000000) {
	        	paddingTicks = 40;
	        	barPadding = 21;
	        }
	        
	        //Draws the labels on the right side.
	        g.drawString("0.00", 45-paddingTicks, 555);
	        longestTimeLabel += longestTimeLabelIncriment;
	        for (int i = 0; i < 20; i++) {
	        	g.drawString(decimal.format(longestTimeLabel), 45-paddingTicks, incriment+5);
		        g.drawLine(75, incriment, 700, incriment);
		        incriment-=amount;
		        longestTimeLabel += longestTimeLabelIncriment;
	        }
	        
	        //Draws "milliseconds" on the left side.
	        g.drawString("milliseconds", 10, 40);
	        
	        //Draws algorithms on the bottom
	        g.drawString("Algorithms", 345, 585);
	        
	        //Draws the Bars
	        int spacingBars = 100;
	        //If bubble sort is selected draw the bar for it.
	        if (bubbleSort) {
		        //Sometimes a array can take 0 seconds to complete, this is not possible
		        //...Therefore we give it a value of 1 milliseconds.
    			if (bubbleSortRuntime == 0)
    				bubbleSortRuntime = 1;
    			//If this method is the longest, automatically set it to max length.
	        	if (bubbleSortLongest) {
	        		g.fillRect(spacingBars, 50, 100, 500);
	        		g.setColor(Color.RED);
	        		g.drawString(Long.toString(bubbleSortRuntime), spacingBars+45-barPadding, 45);
	        		g.setColor(Color.BLACK);
	        	}
	        	else {
	        		//Draw the bar if not the longest and longestTime is greater than the length of the bar.
	        		if (longestTime > 500) {
		        		g.fillRect(spacingBars, 550 - (int)(bubbleSortRuntime/maxLength), 100, (int)(bubbleSortRuntime/maxLength));
		        		g.setColor(Color.RED);
		        		g.drawString(Long.toString(bubbleSortRuntime), spacingBars+45-barPadding, 545 - (int)(bubbleSortRuntime/maxLength));
		        		g.setColor(Color.BLACK);
	        		}
	        		//Draw the bar if not the longest.
	        		else {
	        			g.fillRect(spacingBars, 550 - (int)(bubbleSortRuntime*maxLength), 100, (int)(bubbleSortRuntime*maxLength));
	        			g.setColor(Color.RED);
		        		g.drawString(Long.toString(bubbleSortRuntime), spacingBars+45-barPadding, 545 - (int)(bubbleSortRuntime*maxLength));
		        		g.setColor(Color.BLACK);
	        		}
	        	}
	        	
	        	spacingBars += 150;
	        }
	        //If quick sort is selected draw the bar for it.
	        if (quickSort) {
		        //Sometimes a array can take 0 seconds to complete, this is not possible
		        //...Therefore we give it a value of 1 milliseconds.
    			if (quickSortRuntime == 0)
    				quickSortRuntime = 1;
    			//If this method is the longest, automatically set it to max length.
	        	if (quickSortLongest) {
	        		g.fillRect(spacingBars, 50, 100, 500);
	        		g.setColor(Color.RED);
	        		g.drawString(Long.toString(quickSortRuntime), spacingBars+45-barPadding, 45);
	        		g.setColor(Color.BLACK);
	        	}
	        	else {
	        		//Draw the bar if not the longest and longestTime is greater than the length of the bar.
	        		if (longestTime > 500) {
		        		g.fillRect(spacingBars, 550 - (int)(quickSortRuntime/maxLength), 100, (int)(quickSortRuntime/maxLength));
		        		g.setColor(Color.RED);
		        		g.drawString(Long.toString(quickSortRuntime), spacingBars+45-barPadding, 545 - (int)(quickSortRuntime/maxLength));
		        		g.setColor(Color.BLACK);
	        		}
	        		//Draw the bar if not the longest.
	        		else {
		        		g.fillRect(spacingBars, 550 - (int)(quickSortRuntime*maxLength), 100, (int)(quickSortRuntime*maxLength));
		        		g.setColor(Color.RED);
		        		g.drawString(Long.toString(quickSortRuntime), spacingBars+45-barPadding, 545 - (int)(quickSortRuntime*maxLength));
		        		g.setColor(Color.BLACK);
	        		}
	        	}
	        	
	        	spacingBars += 150;
	        }
	        //If happy Hour sort is selected draw the bar for it.
	        if (happyHourSort) {
		        //Sometimes a array can take 0 seconds to complete, this is not possible
		        //...Therefore we give it a value of 1 milliseconds.
    			if (happyHourSortRuntime == 0)
    				happyHourSortRuntime = 1;
    			//If this method is the longest, automatically set it to max length.
	        	if (happyHourSortLongest) {
	        		g.fillRect(spacingBars, 50, 100, 500);
	        		g.setColor(Color.RED);
	        		g.drawString(Long.toString(happyHourSortRuntime), spacingBars+45-barPadding, 45);
	        		g.setColor(Color.BLACK);
	        	}
	        	else {
	        		//Draw the bar if not the longest and longestTime is greater than the length of the bar.
	        		if (longestTime > 500) {
		        		g.fillRect(spacingBars, 550 - (int)(happyHourSortRuntime/maxLength), 100, (int)(happyHourSortRuntime/maxLength));
		        		g.setColor(Color.RED);
		        		g.drawString(Long.toString(happyHourSortRuntime), spacingBars+45-barPadding, 545 - (int)(happyHourSortRuntime/maxLength));
		        		g.setColor(Color.BLACK);
	        		}
	        		//Draw the bar if not the longest.
	        		else {
		        		g.fillRect(spacingBars, 550 - (int)(happyHourSortRuntime*maxLength), 100, (int)(happyHourSortRuntime*maxLength));
		        		g.setColor(Color.RED);
		        		g.drawString(Long.toString(happyHourSortRuntime), spacingBars+45-barPadding, 545 - (int)(happyHourSortRuntime*maxLength));
		        		g.setColor(Color.BLACK);
	        		}
	        	}
	        	
	        	spacingBars += 150;
	        }
	        //If insertion Sort is selected draw the bar for it.
	        if (insertionSort) {
		        //Sometimes a array can take 0 seconds to complete, this is not possible
		        //...Therefore we give it a value of 1 milliseconds.
    			if (insertionSortRuntime == 0)
    				insertionSortRuntime++;
    			//If this method is the longest, automatically set it to max length.
	        	if (insertionSortLongest) {
	        		g.fillRect(spacingBars, 50, 100, 500);
	        		g.setColor(Color.RED);
	        		g.drawString(Long.toString(insertionSortRuntime), spacingBars+45-barPadding, 45);
	        		g.setColor(Color.BLACK);
	        	}
	        	else {
	        		//Draw the bar if not the longest and longestTime is greater than the length of the bar.
	        		if (longestTime > 500) {
		        		g.fillRect(spacingBars, 550 - (int)(insertionSortRuntime/maxLength), 100, (int)(insertionSortRuntime/maxLength));
		        		g.setColor(Color.RED);
		        		g.drawString(Long.toString(insertionSortRuntime), spacingBars+45-barPadding, 545 - (int)(insertionSortRuntime/maxLength));
		        		g.setColor(Color.BLACK);
	        		}
	        		//Draw the bar if not the longest.
	        		else {
		        		g.fillRect(spacingBars, 550 - (int)(insertionSortRuntime*maxLength), 100, (int)(insertionSortRuntime*maxLength));
		        		g.setColor(Color.RED);
		        		g.drawString(Long.toString(insertionSortRuntime), spacingBars+45-barPadding, 545 - (int)(insertionSortRuntime*maxLength));
		        		g.setColor(Color.BLACK);
	        		}
	        	}
	        	
	        	spacingBars += 150;
	        }
	    }
	}
	
	/**
	 * The mainPanel method creates a panel which all the options for the program reside.
	 */
	
	private void mainPanel() {
		//Create a new panel.
		mainPanel = new JPanel();
		
		//Set the layout.
		mainPanel.setLayout(new BorderLayout());

		//Create a new JButton "Begin Benchmark Run"
		benchmarkButton = new JButton("<html><big>Begin Benchmark Run<html/>");
		benchmarkButton.addActionListener(new BenchmarkListener());
		
		//Create the custom panels.
		westPanel();
		centerPanel();
		eastPanel();
		northPanel();
		
		//Add objects to content pane.
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(westPanel, BorderLayout.WEST);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(eastPanel, BorderLayout.EAST);
		mainPanel.add(benchmarkButton, BorderLayout.SOUTH);
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects the button "run benchmark"
	 */
	
	private class BenchmarkListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (DEBUG) System.out.println(":Run Benchmark:");
			benchmark = true;
			//Sets Back to default state
			numberOfAlgorithms = 0;
			insertionSortRuntime = 0;
			bubbleSortRuntime = 0;
			happyHourSortRuntime = 0;
			quickSortRuntime = 0;
			verifyInsertionSort = false;
			verifyBubbleSort = false;
			verifyHappyHourSort = false;
			verifyQuickSort = false;
			collate = false;
			
			//Get Slider Position
			arrayLength = sizeSlider.getValue();

			//Create 30 Blank Arrays
			arrayCreator();
			
			//Create the type of array ie. worst, best, average
			arrayGenerator();
			
			//Check whether one or more algorithms were selected, and range was greater than 10.
			if (conditionChecker()) {
				if (DEBUG) System.out.println("Slider: " + sizeSlider.getValue());
				
				//If user wants collate, 1-2-3-4, 1-2-3-4, etc.
				if (collateCheckBox.isSelected()) {
					collate = true;
					//Run the collate sort.
					collateSort();
					//Make the necessary GUI detection, to see which ones are selected.
					//Algorithm Check Boxes
					if (insertionSortCheckBox.isSelected()) {
						insertionSort = true;
						numberOfAlgorithms++;
						//Verify the sort.
						verifyInsertionSort = verifySort();
						if (DEBUG) System.out.println(insertionSortRuntime);
					}
					if (bubbleSortCheckBox.isSelected()) {
						bubbleSort = true;
						numberOfAlgorithms++;
						//Verify the sort.
						verifyBubbleSort = verifySort();
						if (DEBUG) System.out.println(bubbleSortRuntime);
					}
					if (happyHourSortCheckBox.isSelected()) {
						happyHourSort = true;
						numberOfAlgorithms++;
						//Verify the sort.
						verifyHappyHourSort = verifySort();
						if (DEBUG) System.out.println(happyHourSortRuntime);
					}
					if (quickSortCheckBox.isSelected()) {
						quickSort = true;
						numberOfAlgorithms++;
						//Verify the sort.
						verifyQuickSort = verifySort();
						if (DEBUG) System.out.println(quickSortRuntime);
					}
				}
				//If user does not want collate, 1-1-1-1, 2-2-2-2, 3-3-3-3, 4-4-4-4
				else {
					//Algorithms Check Boxes
					if (insertionSortCheckBox.isSelected()) {
						if (DEBUG) System.out.println("Insertion Sort");
						//Makes the copy of the array the array.
						copyArray(arrayCopy, array, 0);
						insertionSort = true;
						numberOfAlgorithms++;
						//Run the algorithm and log the time.
						long startTime = System.currentTimeMillis();
						insertionSort();
						insertionSortRuntime = System.currentTimeMillis() - startTime;
						//Verify the sort.
						verifyInsertionSort = verifySort();
						if (DEBUG) System.out.println(insertionSortRuntime);
					}
					if (bubbleSortCheckBox.isSelected()) {
						if (DEBUG) System.out.println("Bubble Sort");
						//Makes the copy of the array the array.
						copyArray(arrayCopy, array, 0);
						bubbleSort = true;
						numberOfAlgorithms++;
						//Run the algorithm and log the time.
						long startTime = System.currentTimeMillis();
						bubbleSort();
						bubbleSortRuntime = System.currentTimeMillis() - startTime;
						//Verify the sort.
						verifyBubbleSort = verifySort();
						if (DEBUG) System.out.println(bubbleSortRuntime);
					}
					if (happyHourSortCheckBox.isSelected()) {
						if (DEBUG) System.out.println("Happy Hour Sort");
						//Makes the copy of the array the array.
						copyArray(arrayCopy, array, 0);
						happyHourSort = true;
						numberOfAlgorithms++;
						//Run the algorithm and log the time.
						long startTime = System.currentTimeMillis();
						happyHourSort();
						happyHourSortRuntime = System.currentTimeMillis() - startTime;
						//Verify the sort.
						verifyHappyHourSort = verifySort();
						if (DEBUG) System.out.println(happyHourSortRuntime);
					}
					if (quickSortCheckBox.isSelected()) {
						if (DEBUG) System.out.println("Quick Sort");
						//Makes the copy of the array the array.
						copyArray(arrayCopy, array, 0);
						quickSort = true;
						numberOfAlgorithms++;
						//Run the algorithm and log the time.
						long startTime = System.currentTimeMillis();
						quickSort();
						quickSortRuntime = System.currentTimeMillis() - startTime;
						//Verify the sort.
						verifyQuickSort = verifySort();
						if (DEBUG) System.out.println(quickSortRuntime);
					}
				}
				
				//Check if all the arrays are sorted properly. If not erro message dispalyed.
				if (!verifyInsertionSort && !verifyBubbleSort && !verifyHappyHourSort && !verifyQuickSort)
					JOptionPane.showMessageDialog(null, "All Arrays Sorted");
				else
					JOptionPane.showMessageDialog(null, "Arrays not sorted.", "Error", JOptionPane.WARNING_MESSAGE);
				
				//Switch to the Graphs Page
				tabbedPane.setSelectedIndex(1);
				
				//Back to Defaults after benchmarking
				insertionSortCheckBox.setSelected(false);
				bubbleSortCheckBox.setSelected(false);
				happyHourSortCheckBox.setSelected(false);
				quickSortCheckBox.setSelected(false);
				tachometerLabel.setIcon(tac0);
				sizeSlider.setValue(11);
				averageCaseRadioButton.setSelected(true);
				collateCheckBox.setSelected(false);
				collateLabel.setIcon(collate_no);
			}
		}
	}
	
	/**
	 * The method collateSort will process one array through one algorithm
	 * and move to the second algorithm, to process the first array.
	 * In essence we are doing 1-2-3-4 instead of 1-1-1-1 2-2-2-2 3-3-3-3 4-4-4-4
	 */
	
	private void collateSort() {
		//Sorts through all 30 arrays, one at a time.
		for (int i = 0; i < 30; i++) {
			//Insertion Sort
			if (insertionSortCheckBox.isSelected()) {
				//Copies the preserved copy back to the original.
				//Uses the for loop's counted to only set data above that one.
				//This is done to comply with verifySort.
				copyArray(arrayCopy, array, i);
				
				long startTime = System.currentTimeMillis();
				Sorting.insertionSort(array[i]);
				insertionSortRuntime += System.currentTimeMillis() - startTime;
			}
			//Bubble Sort
			if (bubbleSortCheckBox.isSelected()) {
				//Copies the preserved copy back to the original.
				//Uses the for loop's counted to only set data above that one.
				//This is done to comply with verifySort.
				copyArray(arrayCopy, array, i);
				
				long startTime = System.currentTimeMillis();
				Sorting.bubbleSort(array[i]);
				bubbleSortRuntime += System.currentTimeMillis() - startTime;
			}
			//Happy Hour Sort
			if (happyHourSortCheckBox.isSelected()) {
				//Copies the preserved copy back to the original.
				//Uses the for loop's counted to only set data above that one.
				//This is done to comply with verifySort.
				copyArray(arrayCopy, array, i);

				long startTime = System.currentTimeMillis();
				Sorting.happyHourSort(array[i], array[i].length);
				happyHourSortRuntime += System.currentTimeMillis() - startTime;
			}
			//Quick Sort
			if (quickSortCheckBox.isSelected()) {
				//Copies the preserved copy back to the original.
				//Uses the for loop's counted to only set data above that one.
				//This is done to comply with verifySort.
				copyArray(arrayCopy, array, i);

				long startTime = System.currentTimeMillis();
				Sorting.quickSort(array[i], 0, array[i].length-1);
				quickSortRuntime += System.currentTimeMillis() - startTime;
			}
		}
	}
	
	/**
	 * The arrayCreator method initializes the array and copy of the array.
	 */
	
	private void arrayCreator() {
		//Main Array
		array = new int[30][arrayLength];
		//Copy of the Array used to perserve data.
		arrayCopy = new int[30][arrayLength];
	}
	
	/**
	 * The arrayGenerator method determines which types of array to generate whether they be
	 * in the best, worst, or average case scenario.
	 */
	
	private void arrayGenerator() {
		//Cases Radio Button
		if (bestCaseRadioButton.isSelected()) {
			bestCaseArrayCreator();
			bestCase = true;					
		}
		if (worstCaseRadioButton.isSelected()) {
			worstCaseArrayCreator();
			worstCase = true;
		}
		if (averageCaseRadioButton.isSelected()) {
			averageCaseArrayCreator();
			averageCase = true;
		}
	}
	
	/**
	 * The method insertionSort runs the static method in the Sorting class for all 30 arrays.
	 */
	
	private void insertionSort() {
		//Runs through the 30 arrays.
		for (int i = 0; i < 30; i++)
			Sorting.insertionSort(array[i]);
	}
	
	/**
	 * The method bubbleSort runs the static method in the Sorting class for all 30 arrays.
	 */
	
	private void bubbleSort() {
		//Runs through the 30 arrays.
		for (int i = 0; i < 30; i++)
			Sorting.bubbleSort(array[i]);
	}
	
	/**
	 * The method happyHourSort runs the static method in the Sorting class for all 30 arrays.
	 */
	
	private void happyHourSort() {
		//Runs through the 30 arrays.
		for (int i = 0; i < 30; i++)
			Sorting.happyHourSort(array[i], array[i].length);
	}
	
	/**
	 * The method quickSort runs the static method in the Sorting class for all 30 arrays.
	 */
	
	private void quickSort() {
		//Runs through the 30 arrays.
		for (int i = 0; i < 30; i++)
			Sorting.quickSort(array[i], 0, array[i].length-1);
	}
	
	/**
	 * The method verifySort will verify all 30 arrays to make sure they were sorted properly.
	 * @return False if there was no errors, true if there was a mistake in sorting.
	 */
	
	private boolean verifySort() {
		boolean bool = false;
		//The loop will run through the 30 arrays.
		//The inner for loop will check to see if there are any numbers
		//That are larger than the previous one. 
		//If found that is a error and the bool will change to true, and be returned.
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < arrayLength-1; j++) {
				if (array[i][j] > array[i][j+1]) {
					bool = true;
					break;
				}
			}
		}
		return bool;
	}
	
	/**
	 * The bestCaseArrayCreator method generates 30 arrays based on the best case scenario
	 * This would be 0, 1, 2, 3, 4...
	 */
	
	private void bestCaseArrayCreator() {
		//Creates 30 arrays starting from 0, and ending at the length of the array.
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < arrayLength; j++) {
				array[i][j] = j;
			}
		}
		//Creates a copy of the original array stored in arrayCopy.
		copyArray(array, arrayCopy, 0);
	}
	
	/**
	 * The worstCaseArrayCreator method generates 30 arrays based on the worst case scenario
	 * We take the length and say 10, 9, 8, 7...
	 */
	
	private void worstCaseArrayCreator() {
		//Creates 30 arrays starting from the arrayLength and incrementing by one backwards.
		for (int i = 0; i < 30; i++) {
			int number = arrayLength - 1;
			//Sets each array to the appropriate reverse ordered number
			for (int j = 0; j < arrayLength; j++) {
				array[i][j] = number;
				
				number--;
			}
		}
		//Creates a copy of the original array stored in arrayCopy.
		copyArray(array, arrayCopy, 0);
	}
	
	/**
	 * The averageCaseArrayCreator method uses a random generator to create 30 arrays of random numbers
	 * We utilize the maximum value of int 2147483647
	 */
	
	private void averageCaseArrayCreator() {
		//Generates random ints.
		Random randomGenerator = new Random();
		
		//Creates 30 arrays with randomly generated numbers.
		for (int i = 0; i < 30; i++) {
			//Uses the nextInt feature to request a new random integer up to 2147483647
			for (int j = 0; j < arrayLength; j++) {
				array[i][j] = randomGenerator.nextInt(2147483647);
			}
		}
		//Creates a copy of the original array stored in arrayCopy.
		copyArray(array, arrayCopy, 0);
	}
	
	/**
	 * The copyArray method will make a backup copy of the initial array, to guarantee each algorithm 
	 * sorts through exactly the same data.
	 * @param original The array that wants to be copied.
	 * @param copy The array that data will be copied to.
	 * @param i Which array to start replacing from (used in collate)
	 */
	
	private void copyArray(int[][] original, int[][] copy, int i) {
		//Processes through the 30 arrays starting from i array.
		while (i < 30) {
			//Does a arrayLength copy 
			//Copies each array's data one by one.
			//Safest way to copy data.
			//Clone method was just not doing it.
			for (int j = 0; j < arrayLength; j++)
				copy[i][j] = original[i][j];
			i++;
		}
	}
	
	/**
	 * The conditionChecker will make sure the array has more than 10 items
	 * and at least one of the sorting algorithms are selected.
	 * @return True if all is a go, False if the criteria is not met.
	 */
	
	private boolean conditionChecker() {
		//Two conditions.
		boolean algorithms = true;
		boolean clear = true;
		
		//Algorithms check boxes.
		if (insertionSortCheckBox.isSelected()) {
			algorithms = false;
		}
		if (bubbleSortCheckBox.isSelected()) {
			algorithms = false;
		}
		if (happyHourSortCheckBox.isSelected()) {
			algorithms = false;
		}
		if (quickSortCheckBox.isSelected()) {
			algorithms = false;
		}
					
		//If no algorithms are selected, and slider is set to a value between 10 and Y
		if (algorithms & sizeSlider.getValue() < 11) {
			JOptionPane.showMessageDialog(null, "<html>-Please select at least one (1) algorithm.<br/>" +
					"-The range must be greater than 10.<html/>", "2 Errors", JOptionPane.WARNING_MESSAGE);
			
			algorithms = false;
			clear = false;
		}
		//If the slider is less than 11.
		else if (sizeSlider.getValue() < 11) {
			JOptionPane.showMessageDialog(null, "The range must be greater than 10.", "Error", JOptionPane.WARNING_MESSAGE);
			
			algorithms = false;
			clear = false;
		}
		//If no algorithms are selected. A reminder is issued.
		else if (algorithms) {
			JOptionPane.showMessageDialog(null, "Please select at least one (1) algorithm.", "Error", JOptionPane.WARNING_MESSAGE);
			
			clear = false;
		}
		
		//Radio Button selection.
		if (bestCaseRadioButton.isSelected()) {
			if (DEBUG) System.out.println("Best Case");				
		}
		if (worstCaseRadioButton.isSelected()) {
			if (DEBUG) System.out.println("Worst Case");
		}
		if (averageCaseRadioButton.isSelected()) {
			if (DEBUG) System.out.println("Average Case");
		}
		
		//Clear is returned true or false.
		return clear;
	}
	
	/**
	 * The westPanel method builds the West Panel.
	 */
	
	private void westPanel() {
		//Create a new panel.
		westPanel = new JPanel();
		JPanel westPanel1 = new JPanel();
		JPanel westPanel2 = new JPanel();
		JPanel westPanel3 = new JPanel();
		JPanel westPanel4 = new JPanel();
		
		//Create a border with a title
		westPanel.setBorder(BorderFactory.createTitledBorder("Algorithms"));
		
		//Set the layout.
		westPanel.setLayout(new GridLayout(4, 1));
		westPanel1.setLayout(new BorderLayout());
		westPanel2.setLayout(new BorderLayout());
		westPanel3.setLayout(new BorderLayout());
		westPanel4.setLayout(new BorderLayout());
		
		//Create Check Boxes
		insertionSortCheckBox = new JCheckBox("Insertion Sort");
		insertionSortCheckBox.addActionListener(new tacBoostListener());
		bubbleSortCheckBox = new JCheckBox("Bubble Sort");
		bubbleSortCheckBox.addActionListener(new tacBoostListener());
		happyHourSortCheckBox = new JCheckBox("Happy Hour Sort");
		happyHourSortCheckBox.addActionListener(new tacBoostListener());
		quickSortCheckBox = new JCheckBox("Quick Sort");
		quickSortCheckBox.addActionListener(new tacBoostListener());
		
		//Create help icons place holders
		insertionHelpLabel = new JLabel();
		bubbleHelpLabel = new JLabel();
		happyHourHelpLabel = new JLabel();
		quickHelpLabel = new JLabel();
		
		//Display help icons
		insertionHelpLabel.setIcon(help);
		bubbleHelpLabel.setIcon(help);
		happyHourHelpLabel.setIcon(help);
		quickHelpLabel.setIcon(help);
		
		//Help Items
		insertionHelpLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseReleased(MouseEvent e) {  
			     JOptionPane.showMessageDialog(null, "<html>Insertion sort is a simple sorting algorithm that is relatively <br/>" +
			     		"efficient for small lists and mostly sorted lists, <br/>" +
			     		"and often is used as part of more sophisticated algorithms. <br/><br/>" +
			     		"It works by taking elements from the list one by one and inserting<br/> " +
			     		"them in their correct position into a new sorted list. In arrays, <br/>" +
			     		"the new list and the remaining elements can share the array's space, <br/>" +
			     		"but insertion is expensive, requiring shifting all following elements <br/>" +
			     		"over by one.<html/>", "Insertion Sort", JOptionPane.PLAIN_MESSAGE);
		    }  
		});
		bubbleHelpLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseReleased(MouseEvent e) {  
			     JOptionPane.showMessageDialog(null, "<html>The algorithm starts at the beginning of the data set. It compares the <br/>" +
			     		"first two elements, and if the first is greater than the second, <br/>" +
			     		"it swaps them. It continues doing this for each pair of adjacent <br/>" +
			     		"elements to the end of the data set. It then starts again with the <br/>" +
			     		"first two elements, repeating until no swaps have occurred on the <br/>" +
			     		"last pass.<html/>", "Bubble Sort", JOptionPane.PLAIN_MESSAGE);
		    }  
		});
		happyHourHelpLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseReleased(MouseEvent e) {  
			     JOptionPane.showMessageDialog(null, "<html>Each iteration of the algorithm is broken up into two stages.<br/>" +
			     		"The first stage loops through the data set from bottom to top, just <br/>" +
			     		"like the Bubble Sort. During the loop, adjacent items are compared. <br/>" +
			     		"If at any point the value on the left is greater than the value on<br/>" +
			     		"the right, the items are swapped. At the end of the first iteration, <br/>" +
			     		"the largest number will reside at the end of the set.<br/><br/>" +
			     		"The second stage loops through the data set in the opposite <br/>" +
			     		"direction - starting from the item just before the most recently <br/>" +
			     		"sorted item, and moving back towards the start of the list. Again, <br/>" +
			     		"adjacent items are swapped if required.<html/>", "Happy Hour Sort", JOptionPane.PLAIN_MESSAGE);
		    }  
		});
		quickHelpLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseReleased(MouseEvent e) {  
			     JOptionPane.showMessageDialog(null, "<html>Quicksort is a divide and conquer algorithm which relies on a partition operation: <br/>" +
			     		"to partition an array an element called a pivot is selected. All elements <br/>" +
			     		"smaller than the pivot are moved before it and all greater elements are <br/>" +
			     		"moved after it.<html/>", "Quick Sort", JOptionPane.PLAIN_MESSAGE);
		    }  
		});
		
		//Add objects to sub-content pane.
		westPanel1.add(insertionSortCheckBox, BorderLayout.WEST);
		westPanel1.add(insertionHelpLabel, BorderLayout.CENTER);
		westPanel2.add(bubbleSortCheckBox, BorderLayout.WEST);
		westPanel2.add(bubbleHelpLabel, BorderLayout.CENTER);
		westPanel3.add(happyHourSortCheckBox, BorderLayout.WEST);
		westPanel3.add(happyHourHelpLabel, BorderLayout.CENTER);
		westPanel4.add(quickSortCheckBox, BorderLayout.WEST);
		westPanel4.add(quickHelpLabel, BorderLayout.CENTER);
		
		//Add objects to content pane.
		westPanel.add(westPanel1);
		westPanel.add(westPanel2);
		westPanel.add(westPanel3);
		westPanel.add(westPanel4);
	}
	
	/**
	 * The centerPanel method builds the Center Panel.
	 */
	
	private void centerPanel() {
		//Create a new panel.
		centerPanel = new JPanel();
		
		//Set the layout.
		centerPanel.setLayout(new BorderLayout());
		
		//Create a border with a title
		centerPanel.setBorder(BorderFactory.createTitledBorder(null, "Tachometer", TitledBorder.CENTER, TitledBorder.CENTER));
		//Create a JLabel with image icon idle.
		tachometerLabel = new JLabel(tac0);
		
		//Add objects to content pane.
		centerPanel.add(tachometerLabel, BorderLayout.CENTER);
	}
	
	/**
	 * The eastPanel method builds the East Panel.
	 */
	
	private void eastPanel() {
		//Create a new panel.
		eastPanel = new JPanel();
		
		//Set the layout.
		eastPanel.setLayout(new GridLayout(2, 1));
		
		//Create the custom panels.
		eastNorthPanel();
		eastSouthPanel();
		
		//Add objects to content pane.
		eastPanel.add(eastNorthPanel);
		eastPanel.add(eastSouthPanel);
	}
	
	/**
	 * The eastNorthPanel method builds the North East Panel.
	 */
	
	private void eastNorthPanel() {
		//Create a new panel.
		eastNorthPanel = new JPanel();
		JPanel eastNorthPanel1 = new JPanel();
		JPanel eastNorthPanel2 = new JPanel();
		JPanel eastNorthPanel3 = new JPanel();
		
		//Create a border with a title
		eastNorthPanel.setBorder(BorderFactory.createTitledBorder("Cases"));
		
		//Set the layout.
		eastNorthPanel.setLayout(new GridLayout(3, 1));
		eastNorthPanel1.setLayout(new BorderLayout());
		eastNorthPanel2.setLayout(new BorderLayout());
		eastNorthPanel3.setLayout(new BorderLayout());
		
		//Create Mutually Exclusive Radio Buttons
		bestCaseRadioButton = new JRadioButton("Best Case");
		bestCaseRadioButton.addActionListener(new tacBoostListener());
		worstCaseRadioButton = new JRadioButton("Worst Case");
		worstCaseRadioButton.addActionListener(new tacBoostListener());
		averageCaseRadioButton  = new JRadioButton("Average Case", true);
		averageCaseRadioButton.addActionListener(new tacBoostListener());
		
		//Create a button group and add items.
		buttonGroup = new ButtonGroup();
		buttonGroup.add(averageCaseRadioButton);
		buttonGroup.add(bestCaseRadioButton);
		buttonGroup.add(worstCaseRadioButton);
		
		//Create help place holders
		averageHelpLabel = new JLabel();
		bestHelpLabel = new JLabel();
		worstHelpLabel = new JLabel();
		
		//Display help icons
		averageHelpLabel.setIcon(help);
		bestHelpLabel.setIcon(help);
		worstHelpLabel.setIcon(help);
		
		//Help Items
		averageHelpLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseReleased(MouseEvent e) {  
			     JOptionPane.showMessageDialog(null, "Randomly generated numbers", "Help", JOptionPane.INFORMATION_MESSAGE);
		    }  
		});
		bestHelpLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseReleased(MouseEvent e) {  
			     JOptionPane.showMessageDialog(null, "Pre Sorted numbers", "Help", JOptionPane.INFORMATION_MESSAGE);
		    }  
		});
		worstHelpLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseReleased(MouseEvent e) {  
			     JOptionPane.showMessageDialog(null, "Reverse sorted numbers", "Help", JOptionPane.INFORMATION_MESSAGE);
		    }  
		});

		//Add objects to sub-content pane.
		eastNorthPanel1.add(bestCaseRadioButton, BorderLayout.WEST);
		eastNorthPanel1.add(bestHelpLabel, BorderLayout.CENTER);
		eastNorthPanel2.add(worstCaseRadioButton, BorderLayout.WEST);
		eastNorthPanel2.add(worstHelpLabel, BorderLayout.CENTER);
		eastNorthPanel3.add(averageCaseRadioButton, BorderLayout.WEST);
		eastNorthPanel3.add(averageHelpLabel, BorderLayout.CENTER);
		
		//Add objects to content pane.
		eastNorthPanel.add(eastNorthPanel1);
		eastNorthPanel.add(eastNorthPanel2);
		eastNorthPanel.add(eastNorthPanel3);
	}

	/**
	 * The eastSouthPanel method builds the South East Panel.
	 */
	
	private void eastSouthPanel() {
		//Create a new panel.
		eastSouthPanel = new JPanel();
		
		//Create a border with a title
		eastSouthPanel.setBorder(BorderFactory.createTitledBorder("Collate"));
		
		//Set the layout.
		eastSouthPanel.setLayout(new BorderLayout());
		
		//Create a new collate check box
		collateCheckBox = new JCheckBox("Collate");
		collateCheckBox.addActionListener((new collateListener()));
		
		//Create icons place holders
		collateHelpLabel = new JLabel();
		collateLabel = new JLabel();

		//Display icons
		collateLabel.setIcon(collate_no);
		collateHelpLabel.setIcon(help);

		//Help Items
		collateHelpLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseReleased(MouseEvent e) {  
			     JOptionPane.showMessageDialog(null, "Collate: To arrange in ordered sets.", "Help", JOptionPane.INFORMATION_MESSAGE);
		    }  
		}); 

		//Add objects to content pane.
		eastSouthPanel.add(collateCheckBox, BorderLayout.WEST);
		eastSouthPanel.add(collateHelpLabel, BorderLayout.CENTER);
		eastSouthPanel.add(collateLabel, BorderLayout.SOUTH);
	}
	
	/**
	 * The northPanel method builds the North Panel.
	 */
	
	private void northPanel() {
		//Create a new panel.
		northPanel = new JPanel();
		
		//Create a border with a title
		northPanel.setBorder(BorderFactory.createTitledBorder("Range"));
		
		//Set the layout.
		northPanel.setLayout(new BorderLayout());
		
		//Turn on labels at major tick marks.
		sizeSlider = new JSlider(0, 10000, 11);
		sizeSlider.setMajorTickSpacing(1000);
		sizeSlider.setMinorTickSpacing(100);
		sizeSlider.setPaintTicks(true);
		sizeSlider.setPaintLabels(true);
		sizeSlider.addChangeListener(new SliderListener());
		
		//Create the custom panels.
		northSubPanel();
		
		//Add objects to content pane.
		northPanel.add(sizeSlider, BorderLayout.NORTH);
		northPanel.add(northSubPanel, BorderLayout.EAST);
	}
	
	/**
	 * The northSubPanel method builds the North Sub Panel.
	 */
	
	private void northSubPanel() {
		//Create a new panel.
		northSubPanel = new JPanel();
		
		//Set the layout.
		northSubPanel.setLayout(new BorderLayout());
		
		//Create a JField
		sizeField = new JTextField(4);
		sizeField.setText(Integer.toString(sizeSlider.getValue()));
		sizeField.addActionListener(new sizeFieldListener());
		
		//Create a JButton "Update"
		updateButton = new JButton("Update");
		updateButton.addActionListener(new sizeFieldListener());
		
		//Add objects to content pane.
		northSubPanel.add(sizeField, BorderLayout.CENTER);
		northSubPanel.add(updateButton, BorderLayout.EAST);
	}
	
	/**
	 * The menuBar method builds the menu bar.
	 */
	
	private void menuBar() {
		//Create the menu bar.
		menuBar = new JMenuBar();
	
		//Create the file and help menus.
		fileMenu();
		helpMenu();
	
		//Add the file and help menus to the menu bar.
		menuBar.add(fileMenu);
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
	
		//Add the menu items to the File menu.
		fileMenu.add(exitItem);
	}

	/**
	 * The helpMenu method builds the Help menu and returns a reference to its JMenu object.
	 */
	
	private void helpMenu() {
		//Create an Quick Tips menu item.
		quickTipsItem = new JMenuItem("Quick Tips");
		quickTipsItem.setMnemonic(KeyEvent.VK_Q);
		quickTipsItem.addActionListener(new GetStartedListener());
	
		//Create an About menu item.
		aboutItem = new JMenuItem("About");
		aboutItem.setMnemonic(KeyEvent.VK_A);
		aboutItem.addActionListener(new AboutListener());
	
		//Create a JMenu object for the Help menu.
		helpMenu = new JMenu("Help");
	
		//Add the menu items to the Help menu.
		helpMenu.add(quickTipsItem);
		helpMenu.add(aboutItem);
	}

	/**
	 * Private inner class that handles the event that is generated when the user selects the collate check box.
	 */
	
	private class collateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//If the check box is selected, switch the image accordingly.
			if (collateCheckBox.isSelected()) {
				if (DEBUG) System.out.println("Collate");
				collateLabel.setIcon(collate_yes);
			}
			//If not switch the image back to default.
			else {
				if (DEBUG) System.out.println("Collate OFF");
				collateLabel.setIcon(collate_no);
			}
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user changes the slider.
	 */
	
	private class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			if (DEBUG) System.out.println("Slider : " + sizeSlider.getValue());
			//Sets the sizeTextFied to the text of the slider.
			sizeField.setText(Integer.toString(sizeSlider.getValue()));

			//Set the tile bar text.
			setTitle("SortBench Pro Classified Edition");
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user updates the slider through the text box.
	 */
	
	private class sizeFieldListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Sets the slider to the value of the sizeTextField
			sizeSlider.setValue(Integer.parseInt(sizeField.getText()));
			
			//Set the tile bar text.
			setTitle("SortBench Pro Classified Edition");
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user changes any of the radio buttons or check boxes.
	 */
	
	private class tacBoostListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Set the tile bar text.
			setTitle("SortBench Pro Classified Edition");
			
			insertionSort = false;
			bubbleSort = false;
			happyHourSort = false;
			quickSort = false;
			
			bestCase = false;
			worstCase = false;
			averageCase = false;
			
			int counter = 0;
			
			//Cases Radio Button
			if (bestCaseRadioButton.isSelected()) {
				bestCase = true;
			}
			if (worstCaseRadioButton.isSelected()) {
				worstCase = true;
			}
			if (averageCaseRadioButton.isSelected()) {
				averageCase = true;
			}
			
			//Algorithms Check Box
			if (insertionSortCheckBox.isSelected()) {
				counter++;
				insertionSort = true;
			}
			if (bubbleSortCheckBox.isSelected()) {
				counter++;
				bubbleSort = true;
			}
			if (happyHourSortCheckBox.isSelected()) {
				counter++;
				happyHourSort = true;
			}
			if (quickSortCheckBox.isSelected()) {
				counter++;
				quickSort = true;
			}
			
			//Displays appropriate reading based on options selected.
			if (counter == 0)
				tachometerLabel.setIcon(tac0);
			if (counter == 1)
				tachometerLabel.setIcon(tac1);
			if (counter == 2)
				tachometerLabel.setIcon(tac2);
			if (counter == 3)
				tachometerLabel.setIcon(tac3);
			if (counter == 4)
				tachometerLabel.setIcon(tac4);
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects Exit from the File menu.
	 */
	
	private class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Show option either yes or no, sets focus on no for accidents.
			if (JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]) == 0) {
				if (DEBUG) System.out.println(":Exit");
				System.exit(0);
			}
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects Get Started from the Help menu.
	 */

	private class GetStartedListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Getting Started message.
			JOptionPane.showMessageDialog(null, "<html>Congratulations on your purchase of SortBench Pro!<br/>" +
					"This application is designed to show differences in algorithms through sorting.<br/><br/>" + 
					"Getting Started<br/>" +
					"Select a range.<br/>" +
					"Select one or many algorithms.<br/>" +
					"Select a scenario.<br/>" +
					"And if desired select collate.<br/><br/>" +
					"Help Icons are interactive. Click on one to learn more!<br/><br/>" +
					"SHORTCUTS<br/>" +
					"Ctrl-W Exit the program<html/>", "Getting Started", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Private inner class that handles the event that is generated when the user selects About from the Help menu.
	 */

	private class AboutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (DEBUG) System.out.println(":About");
			//About message.
			JOptionPane.showMessageDialog(null, new JLabel("<html><center>SortBench Pro 2012 v114.2<br/>" +
					"Vincent Lee sponsored by Chris Plaue<br/>" +
					"Spring 2012</center></html>"), "About", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	
//	public static void main(String[] args) {
//		new SortExperiment();
//	}
	
	/**
	 * Main Method to run SortExperiment
	 * @param args Command Line Arguments.
	 */

	public static void main(String[] args) {
		//This program will not accept any command line arguments.
		try {
			if (args.length == 0)
				new SortExperiment();
			else {
				System.out.println("Hi. I cannot accept any command line arguments.");
				System.out.println("I will exit now.");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("General program failure.");
			System.exit(0);
		}
	}
}
