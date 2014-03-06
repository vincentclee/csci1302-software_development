import java.awt.*; //Layout manager
import java.awt.event.*; //Event listener interface
import java.io.*; //File IO
import java.util.*; //ArrayLists and Scanner
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
//Swing classes

public class Visualizer extends JFrame {
	//JFrame Setup
	private static final long serialVersionUID = 1L; //Serialize
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	private boolean DEBUG = false;
	private final int ROWS = 1000; //Number of students Dynamic..UP to that number
	
	//GUI Components
	JProgressBar progressBar;
	private JPanel mainPanel, analyzeStudentPanel, analyzeClassPanel;
	private JScrollPane scrollPane, studentScrollPane;
	private JButton updateButton, analyzeButton, closeAllTabsButton, analyzeClassButton;
	private JTable table, studentTable, classTable;
	private JTabbedPane tabbedPane;

	//Tool Bar
	private JToolBar toolBar;
	
	//Menu Bar
	private JMenuBar menuBar; //The menu bar
	private JMenu fileMenu, settingsMenu, helpMenu; //The File menu etc.
	private JMenuItem exitItem, quickTipsItem, aboutItem, openClassItem; //To exit etc.
	
	//DefaultTableModel relates to JTable
	DefaultTableModel tableModel, studentTableModel, classTableModel;
	
	private String[] options = {"Yes", "No"}; //JOPtion pane options
	private String[] columnToolTips = {"Last Name", "First Name", "User ID", "Attendance", "Total Days"};
			
	private Object[][] data = new Object[][] {}; //Main Attendance Table list
	private Object headers[] = {"Last Name", "First Name", "User ID", "Attendance", "Total Days"};
	private Object studentHeaders[] = null; //Headers for students.
	private Object classHeaders[] = null; //Headers for the whole class.
	
	private String[] firstName = new String[ROWS]; //Stores first names from the file.
	private String[] lastName = new String[ROWS]; //Stores last names from the file.
	private String[] idNumber = new String[ROWS]; //Stores id numbers from the file.
	private int[] attendance = new int[ROWS]; //Stores total attendance from file.
	private int totalStudents;
	
	private String[][] individualAttendance = new String[ROWS][ROWS]; ////Stores the actual attendence.
	private boolean[] individualAttendanceBoolean = null; //Boolean used for painting the squares red or green.
	private boolean[][] classAttendanceBoolean = null; //Boolean used for painting the squares red or green.
	private String[] individualAttendanceDate = new String[ROWS]; //Stores the dates of attendance.

	private Object[][] studentData = null; //Used for individual student report.
	private Object[][] classData = null;//Used for class report.
	
	private int days = 0; //Total days of class.
	private int number = 1; //Number appended onto the csv file.
	private String fileName = null; //Used for savedPrefs
	private String fileNameAppended = ""; //Used to open file.
	private int daysAttended; //How many days actually attended class both whole/student.
	private int selectedRow; //Allows analyze student by clicking
	private int tabsOpen = 0; //tabs open
	
	//Queue for processing attendance files.
	private Queue<String> queue = new LinkedList<String>();
	private Queue<String> queueName = new LinkedList<String>();

	/**
	 * Default Constructor used to set up GUI application Visualizer
	 */
	
	public Visualizer() {
		if (DEBUG) System.out.println("Start");

		//Set the tile bar text.
		setTitle("iTracker Beta");

		//Specify an action for the close button.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set Size
		setSize(WIDTH, HEIGHT);

		//Add a TabbedPane manager to the content pane.
		tabbedPane = new JTabbedPane();
				
		//Create the custom panels.
		menuBar();
		toolBar();
		mainPanel();
		
		//Add objects to content pane.
		add(toolBar, BorderLayout.NORTH);
		add(tabbedPane);
		tabbedPane.addTab("Attendance", mainPanel);

		//Does initial setup if saved in prefs
		lanuchControl();

		setVisible(true);
		setLocationRelativeTo(null); //Displays the window in the middle of the screen.
	}
	
	/**
	 * The method lanuchControl opens the preferences file and checkes to see if the file referenced
	 * in the file is there. If so it does the basic opening and and displays attendence each time
	 * the program is opened.
	 */
	
	private void lanuchControl() {
		//Open file "saved.prefs"
		File file = new File("saved.prefs");
		
		//Sets the filename to null for subsequent runs.
	    fileName = null;
	    File selectedFile = null;
	    
	    //If the file exists, then perform the actions.
		if (file.exists()) {
			Scanner kb;
			
			//If the file exists which is inside the file, then perform the actions.
			try {
				kb = new Scanner(file);
				fileName = kb.nextLine();
			} catch (Exception e) {}			

			//sets the last know file to the current filename.
			fileNameAppended = fileName;
			selectedFile = new File(fileName);
			
			//If that File exists, then perform the actions. 
			if (selectedFile.exists()) {
				Scanner inputFile = null;
				try {
					inputFile = new Scanner(selectedFile);
				} catch (Exception e1) {}
				
				updateButton.setEnabled(true);
				
			    //Displays filename through title.
			    setTitle("iTracker Beta - " + selectedFile.getName());
			    
		    	//Clean data off the table.
		    	for(int i= tableModel.getRowCount(); i > 0;--i)
		    		tableModel.removeRow(i-1);   
			    
		    	//Tokenize the data from the file.
			    StringTokenizer strTok1 = new StringTokenizer(inputFile.nextLine(), ",");
			    int headerCounter = 0;
			    
			    //Sets the headers for the JTable.
			   	while (headerCounter < 3 && strTok1.hasMoreTokens()) {
			    	String token = strTok1.nextToken();
			    	headers[headerCounter] = token;
			    	columnToolTips[headerCounter] = token;
			    	headerCounter++;
			   	}
			   	try {
			   		//Sets the attendance for the particular each person.
				   	for (int i = 0; i < ROWS; i++)
				   		individualAttendanceDate[i] = strTok1.nextToken();
			   	} catch(Exception e) {}
			   	
			   	//Various counters control functions of the while loop.
			    int counter = 0;
			    int line = 0;
			    totalStudents = 0;
			    //Loop stores fileName etc, into arrays.
			    while(inputFile.hasNextLine()) {
			    	StringTokenizer strTok = new StringTokenizer(inputFile.nextLine(), ",");
				   	
			    	lastName[counter] = strTok.nextToken();
				   	firstName[counter] = strTok.nextToken();
			    	idNumber[counter] = strTok.nextToken();
	
			    	attendance[counter] = 0;
			    	
			    	int dateCounter = 0;
			    	//Actually counts attendance and loads into an array.
			    	while(strTok.hasMoreTokens()) {
			    		try {
			    			String token = strTok.nextToken();
			    			individualAttendance[line][dateCounter] = token;
			    			attendance[counter] += Integer.parseInt(token);
			    			
			    		} catch (Exception e) {}
			    		if (line == 0)
			    			days++;
			    		
			    		dateCounter++;
			    	}
			    	line++;
			    	counter++;
			    	totalStudents++;
			    }
		    	
			    //Using the addRow method available in JTable,
			    //it allows using a temporary object, and adding that temp array,
			    //into the main tableModel.
	    		for (int i = 0; i < counter; i++) {
	    			if (i == 0)
		    			tableModel.setColumnIdentifiers(headers);
	    			if (i > -1){
	    				Object[] rowData = new Object[5];
	    				rowData[0] = lastName[i];
	    				rowData[1] = firstName[i];
	    				rowData[2] = idNumber[i];
	    				if (attendance[i] < 10)
	    					rowData[3] = "0" + attendance[i];
	    				else
	    					rowData[3] = attendance[i];
	    				if (days < 10)
	    					rowData[4] = "0" + days;
	    				else 
	    					rowData[4] = days;
						tableModel.addRow(rowData);
	   				}
	   			}
			}
	    }
	}

	/**
	 * The mainPanel method sets up the main attendance panel with names and attendence.
	 */
	
	private void mainPanel() {
		//Create a new panel.
		mainPanel = new JPanel();
		
		//set the layout.
		mainPanel.setLayout(new BorderLayout());

		//Start a DefaultTableModel which has the addrow method.
		tableModel = new DefaultTableModel(data, headers) {
			private static final long serialVersionUID = 1L; //Serialize
			
			//Makes the whole table not editable.
			public boolean isCellEditable(int rowIndex, int mColIndex) {
		        return false;
		    }
		};

		//Creates a JTable
		table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L; //Serialize
			
			//Implement table header tool tips.
		    protected JTableHeader createDefaultTableHeader() {
		        return new JTableHeader(columnModel) {
					private static final long serialVersionUID = 1L; //Serialize
					
					public String getToolTipText(MouseEvent e) {
		                java.awt.Point p = e.getPoint();
		                int index = columnModel.getColumnIndexAtX(p.x);
		                int realIndex = columnModel.getColumn(index).getModelIndex();
		                return columnToolTips[realIndex];
		            }
		        };
		    }
		};

		//Adds a table listener, allows to know which line is selected.
		table.getSelectionModel().addListSelectionListener(new ListSelectioneListener());
		
		//Options for the table
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(false);
		
		//Add objects to Scrolling Panel.
		scrollPane = new JScrollPane(table);
		
		//Add objects to content pane.
		mainPanel.add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * The menuBar method builds the menu bar.
	 */

	private void menuBar() {
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Create the file, edit, and help menus.
		fileMenu();
		settingsMenu();
		helpMenu();

		//Add the file, edit, and help menus to the menu bar.
		menuBar.add(fileMenu);
		menuBar.add(settingsMenu);
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
	 * The settingsMenu method builds the Settings menu and returns a reference to its JMenu object.
	 */

	private void settingsMenu() {
		//Create an Open menu item.
		openClassItem = new JMenuItem("Open Class File");
		openClassItem.setMnemonic(KeyEvent.VK_O);
		openClassItem.addActionListener(new OpenListener());
		openClassItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));

		//Create a JMenu object for the Settings menu.
		settingsMenu = new JMenu("Settings");

		//Add the menu items to the Settings menu.
		settingsMenu.add(openClassItem);
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
	 * Private inner class that handles the event that is generated when the user selects Exit from the File menu.
	 */
	
	private void toolBar() {
		//Create the tool bar.
		toolBar = new JToolBar();

		//Set options
		toolBar.setRollover(true);
		toolBar.setFloatable(false);

		//Creates 4 Buttons.
		updateButton = new JButton("Update Attendance");
		updateButton.addActionListener(new updateListener());
		updateButton.setEnabled(false);
		
		analyzeButton = new JButton("Analyze Student");
		analyzeButton.addActionListener(new analyzeStudentListener());
		
		analyzeClassButton = new JButton("Analyze Class");
		analyzeClassButton.addActionListener(new analyzeClassListener());
		
		closeAllTabsButton = new JButton("Close ALL Tabs");
		closeAllTabsButton.addActionListener(new closeALLTabsListener());
		
		//Add the menu items to the toolbar.
	    toolBar.add(updateButton);
	    toolBar.addSeparator();
	    toolBar.add(analyzeButton);
	    toolBar.addSeparator();
	    toolBar.add(analyzeClassButton);
	    toolBar.addSeparator();
	    toolBar.add(closeAllTabsButton);
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects a row in the JTable.
	 */
	
	private class ListSelectioneListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
            //Sets selectedRow to the current row selected on the table.
			selectedRow = table.getSelectedRow();
            //Prints out on the screen what row is selected.
			if (selectedRow < 0) {
                if (DEBUG) System.out.println("");
                selectedRow = 0;
            } else
            	if (DEBUG) System.out.println("Selected row: " + selectedRow);
        }
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects the button "analyze class"
	 */
	
	private class analyzeClassListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (DEBUG) System.out.println("Analyze Whole Class");
			//If the view is on the first tab, will it create a new tab.
			if (tabbedPane.getSelectedIndex() == 0) {
				tabsOpen++;
				
				//Creates a new JPanel.
				analyzeClassPanel = new JPanel();
				
				//Set a layout.
				analyzeClassPanel.setLayout(new BorderLayout());
				
				//Sets the variables to their default state 0.
				int totalClassAttendance = 0;
				for (int i = 0; i < attendance.length; i++)
					totalClassAttendance += attendance[i];
				
				if (DEBUG) System.out.println(totalClassAttendance);
				if (DEBUG) System.out.println(totalStudents);

				//How many days attendance was taken.
				int datesLoged = 0;
				for (int i = 0; i < individualAttendanceDate.length; i++) {
					//Array are fixed, but other numbers will be null. 
					if (individualAttendanceDate[i] != null)
						datesLoged++;
				}
				
				//Sets a classHeaders object with the default name, id number information.
				classHeaders = new Object[(datesLoged+3)];
				classHeaders[0] = headers[0];
				classHeaders[1] = headers[1];
				classHeaders[2] = headers[2];
								
				//Sets the rest of the headers with dates of attendance gathered.
				for (int i = 0; i < individualAttendanceDate.length; i++) {
					if (individualAttendanceDate[i] != null)
						classHeaders[i+3] = individualAttendanceDate[i];
				}
				//Creates a classData Object for the JTable
				classData = new Object[totalStudents][(datesLoged+3)];

				//Offset datesLoged, by 3 becuase of name, last name, and idNumber headers
				int daysLogedPlusOrigHeaders = datesLoged + 3;
				
				//Sets the first three colums of the JTable with information.
				for (int i = 0; i < totalStudents; i++) {
					classData[i][0] = lastName[i];
					classData[i][1] = firstName[i];
					classData[i][2] = idNumber[i];
				}
				
				//Creates a boolean array which will be used to display green or red.
				classAttendanceBoolean = new boolean[totalStudents][datesLoged+3];
				
				//If the day was 1 which means that the student was present, 
				//mark that spot as true, which will be green, else it will be false and red
				for (int j = 0; j < totalStudents; j++) {
					for (int i = 3; i < daysLogedPlusOrigHeaders; i++) {
						if (Integer.parseInt(individualAttendance[j][i-3]) == 1)
							classAttendanceBoolean[j][i] = true;
						else
							classAttendanceBoolean[j][i] = false;
					}
				}

				//Create a DefaultTable model.
				classTableModel = new DefaultTableModel(classData, classHeaders) {
					private static final long serialVersionUID = 1L; //Serialize
					
					//Makes all the cells non editable.
					public boolean isCellEditable(int rowIndex, int mColIndex) {
				        return false;
				    }
				};
				
				//Creates a JTable from the DefaultTableModel.
				classTable = new JTable(classTableModel) {
					private static final long serialVersionUID = 1L; //Serialize
					
					//Main method used to mark the cell either green or red.
					//The first three columns are marked white. They include name, last name, and idNumber
					@Override
					public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
						JLabel label = (JLabel) super.prepareRenderer(renderer, row, column);
					   
						if (column < 3) {
							label.setBackground(Color.WHITE);
							return label;
						}
						else {
							if (classAttendanceBoolean[row][column])
						    	label.setBackground(Color.GREEN);
						    else
						    	label.setBackground(Color.RED);
						    return label;
					    }
					}
				};
				
				//Sets options for the table.
				classTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				classTable.setAutoCreateRowSorter(false);
				classTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				
				//Creates a scrolling pane. 
				studentScrollPane = new JScrollPane(classTable);
				
				//Progress Bar
				progressBar = new JProgressBar(0, (days * totalStudents));
				progressBar.setValue(totalClassAttendance);
				progressBar.setStringPainted(true);
				
				//Adds components to the analyzeClassPanel
				analyzeClassPanel.add(studentScrollPane, BorderLayout.CENTER);
				analyzeClassPanel.add(progressBar, BorderLayout.SOUTH);
				
				//Adds the analyzeClassPanel to a tab group.
				tabbedPane.addTab("Whole Class", analyzeClassPanel);
				tabbedPane.setSelectedIndex(tabsOpen); 
			}
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects the button "close all tabs"
	 */
	
	private class closeALLTabsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Removes all open tabs except for the first one.
			for (int i = 1; i <= tabsOpen; i++)
				tabbedPane.remove(1);
			
			//Sets the tab count to 0
			tabsOpen = 0;
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects the button "analyze student"
	 */
	
	private class analyzeStudentListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (DEBUG) System.out.println("Analyze Row: " + selectedRow);

			//It will only open a new tab is the first tab is in view.
			if (tabbedPane.getSelectedIndex() == 0) {
				//Counts on the tabs open
				tabsOpen++;
			
				//Creates a new JPanel.
				analyzeStudentPanel = new JPanel();
				
				//Sets a layout.
				analyzeStudentPanel.setLayout(new BorderLayout());

				//Keeps track of how many days of attendance are actually red.
				int datesLoged = 0;
				//If not null, it is a day of attendance. 
				for (int i = 0; i < individualAttendanceDate.length; i++) {
					if (individualAttendanceDate[i] != null)
						datesLoged++;
				}
				
				//Creates a new headers Object
				studentHeaders = new Object[datesLoged];
				
				//Sets the headers for the dates of attendance.
				for (int i = 0; i < individualAttendanceDate.length; i++) {
					if (individualAttendanceDate[i] != null)
						studentHeaders[i] = individualAttendanceDate[i];
				}
				
				//Creates a new array used for the JTable.
				studentData = new Object[1][datesLoged];
				
				//Creates a boolean array for use in red of green squares.
				individualAttendanceBoolean = new boolean[datesLoged];
				
				//Actual days attended to zero.
				daysAttended = 0;
				
				//If the student attended class it is logged as a one, if it is a one, it is true
				//if it is 0, the student was not there, and marked with a false.
				for (int i = 0; i < datesLoged; i++) {
					if (Integer.parseInt(individualAttendance[selectedRow][i]) == 1) {
						individualAttendanceBoolean[i] = true;
						daysAttended++;
					}
					else
						individualAttendanceBoolean[i] = false;
				}
				
				//Creates a new DefaultTableModel
				studentTableModel = new DefaultTableModel(studentData, studentHeaders) {
					private static final long serialVersionUID = 1L; //Serialize
					
					//Set all the cells to be non editable.
					public boolean isCellEditable(int rowIndex, int mColIndex) {
				        return false;
				    }
				};

				//Creates a new JTable.
				studentTable = new JTable(studentTableModel) {
					private static final long serialVersionUID = 1L; //Serialize
					
					//Sets the grid blocks to either be green or red based on if the student showed up at all.
					@Override
					public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
						JLabel label = (JLabel) super.prepareRenderer(renderer, row, column);
					    if (individualAttendanceBoolean[column])
					    	label.setBackground(Color.GREEN);
					    else
					    	label.setBackground(Color.RED);
					    return label;
					}
				};
			
				//Table options below.
				studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				studentTable.setAutoCreateRowSorter(true);
				studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				
				//Create a new scroll pane, and select is source as the table.
				studentScrollPane = new JScrollPane(studentTable);
				
				//Progress Bar
				progressBar = new JProgressBar(0, days);
				progressBar.setValue(daysAttended);
				progressBar.setStringPainted(true);
				
				//Adds components onto the analyzeStudentPanel
				analyzeStudentPanel.add(new JLabel("<html>" + firstName[selectedRow] + " " + lastName[selectedRow] + "<br/>" + idNumber[selectedRow] + "<br/><br/>Attended: " + daysAttended + " of " + days + "<html/>"), BorderLayout.NORTH);
				analyzeStudentPanel.add(studentScrollPane, BorderLayout.CENTER);
				analyzeStudentPanel.add(progressBar, BorderLayout.SOUTH);
			
				//Adds analyzeStudentPanel onto a new tab group
				tabbedPane.addTab(lastName[selectedRow] + ", " + firstName[selectedRow], analyzeStudentPanel);
				tabbedPane.setSelectedIndex(tabsOpen); 
			}
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects the button "update attendence"
	 */
	
	private class updateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (DEBUG) System.out.println(fileName);
						
			//File Chooser
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			//Open the file choose dialog.
			int status = fileChooser.showOpenDialog(null);
			
			//If the use clicks open will this execute.
			if (status == JFileChooser.APPROVE_OPTION) {
				//Default file array used to store selected file names.
				File[] selectedFile = fileChooser.getSelectedFiles();
				
				//Adds the filenames into a queue.
				for (int i = 0; i < selectedFile.length; i++)
					queue.add(selectedFile[i].getAbsolutePath());
				
				//File Name Only
				for (int i = 0; i < selectedFile.length; i++)
					queueName.add(selectedFile[i].getName());
				
				//Shows user that the files are being processed. 
				JOptionPane.showMessageDialog(null, "The attendance files: " + queueName.toString() + " are being processed.");
				
				//Uses the Helper class to update the attendance, and uses the poll feature in queues to
				//remove each one until there is no more.
				for (int i = 0; i < selectedFile.length; i++) {
					Helper helper = new Helper();
					helper.parseFile(fileNameAppended, queue.poll(), number);
					fileNameAppended = fileNameAppended + "_" + number;
					number++;
				}

				//Saved.prefs START
				FileWriter file;
				try {
					file = new FileWriter("saved.prefs");
					PrintWriter outputFile = new PrintWriter(file);
					
					outputFile.println(fileNameAppended);
					
					//Has to close file to to save data.
					outputFile.close();
					if (DEBUG) System.out.println("Data written to the saved.prefs file.");
				} catch (Exception e2) {}
				//Saved.prefs END
			}
			//Days is set to 0, and launControll starts setup process to refresh the grid.
			days = 0;
			lanuchControl();
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
	 * Private inner class that handles the event that is generated when the user selects Open from the File menu.
	 */
	
	private class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//File Chooser
			JFileChooser fileChooser = new JFileChooser();
			//Shows the file chooser dialog.
			int status = fileChooser.showOpenDialog(null);
			//It will only process a file if user clicks yes.
			if (status == JFileChooser.APPROVE_OPTION) {
				//Opens file.
				File selectedFile = fileChooser.getSelectedFile();
			    fileName = selectedFile.getAbsolutePath();
			    fileNameAppended = fileName;
			    
			    //Sets the variables back to zero or null.
			    daysAttended = 0;
			    days = 0;
			    studentHeaders = null;
				for (int i = 0; i < individualAttendanceDate.length; i++) {
					individualAttendanceDate[i] = null;
				}
			    
				//Saved.prefs START
				FileWriter file;
				try {
					file = new FileWriter("saved.prefs");
					PrintWriter outputFile = new PrintWriter(file);
					
					outputFile.println(fileName);
					
					//Has to close file to to save data.
					outputFile.close();
					if (DEBUG) System.out.println("Data written to the saved.prefs file.");
				} catch (Exception e2) {}
				//Saved.prefs END
							    
				Scanner inputFile = null;
				try {
					inputFile = new Scanner(selectedFile);
				} catch (Exception e1) {}
			    
			    //Displays filename through title.
			    setTitle("iTracker Beta - " + selectedFile.getName());
			    
			    // Read the image file.
			    if (fileName.toLowerCase().endsWith(".csv") || fileName.toLowerCase().endsWith(".CSV")) {
			    	//Clean data off the table.
			    	 for(int i= tableModel.getRowCount(); i > 0;--i)
			    		 tableModel.removeRow(i-1);   

			    	//Tokenizes the data.
				    StringTokenizer strTok1 = new StringTokenizer(inputFile.nextLine(), ",");
				    
				    
				    int headerCounter = 0;
			    	//Sets the headers and column tool tips bases on the first line of tokens. 
				    while (strTok1.hasMoreTokens() && headerCounter < 3) {
			    		String token = strTok1.nextToken();
			    		headers[headerCounter] = token;
			    		columnToolTips[headerCounter] = token;
			    		headerCounter++;
			    	}
			    	//Is used for each file appended number.
			    	int counter = 0;
			    	
			    	//Sets the names, last name, and id number+ attendance to individual arrays.
				    while(inputFile.hasNextLine()) {
				    	StringTokenizer strTok = new StringTokenizer(inputFile.nextLine(), ",");
					   	
				    	lastName[counter] = strTok.nextToken();
					   	firstName[counter] = strTok.nextToken();
				    	idNumber[counter] = strTok.nextToken();
				    	
				    	attendance[counter] = 0;
				    	//Adds up the attendance up per line.
				    	while(strTok.hasMoreTokens()) {
				    		attendance[counter] += Integer.parseInt(strTok.nextToken());
				    	}
				    	counter++;
				    }
			    	
				    //Sets the headers for the table model.
	    			for (int i = 0; i < counter; i++) {
	    				if (i == 0) {
		    				tableModel.setColumnIdentifiers(headers);
	    				}
	    				
	    				//Adds data to a temporary object, and then uses the addRow method
	    				//to create the JTable row by row.
	    				if (i > -1) {
		    				Object[] rowData = new Object[4];
		    				rowData[0] = lastName[i];
		    				rowData[1] = firstName[i];
		    				rowData[2] = idNumber[i];
							tableModel.addRow(rowData);
	    				}
	    			}
	    			updateButton.setEnabled(true);
			    }
			    //Original file must be a csv. if not this displays.
			    else
			    	JOptionPane.showMessageDialog(null, "Invalid File Spedified. File must end with .csv", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	/**
	 * Private inner class that handles the event that is generated when the user selects Get Started from the Help menu.
	 */

	private class GetStartedListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Getting Started message.
			JOptionPane.showMessageDialog(null, "<html>Congratulations on your purchase of iTracker Beta!<br/>" +
					"This application is designed to track attendance.<br/><br/>" + 
					"Green is for attended class.<br/>" + 
					"Red is for a no show.<br/>" + 
					"Each panel will have a  progress bar with the percentage of current attendance.<br/>" + 
					"Open the class roster through the settings menu.<br/>" +
					"Once the data is loaded into the main list, click update attendance and select one or more files.<br/>" +
					"Now each person will have attendance. Select a row, and select analyze student.<br/>" +
					"This will bring up a new tab, and display graphical data for that individual.<br/>" +
					"Going back to the Attendance tab, we can analyze another student.<br/>" +
					"Again on the Attendance tab, we can click analyze class.<br/>" +
					"This will bring up a whole class summary also with a progress bar showing total attendance for the whole class.<br/>" +
					"Clicking the close all tabs button will close all tabs except the first Attendance tab.<br/><br/>"+
					"SHORTCUTS<br/>" +
					"Ctrl-W Exit the program<br/>" +
					"Ctrl-O Open new Class Roster<html/>", "Getting Started", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Private inner class that handles the event that is generated when the user selects About from the Help menu.
	 */

	private class AboutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (DEBUG) System.out.println(":About");
			
			//About message.
			JOptionPane.showMessageDialog(null, new JLabel("<html><center>iTracker Beta</center><br/>" +
					"This program was submitted as a student programming assignment for CSCI 1302, <br/>" +
					"Spring 2012, and was written by Vincent Lee and Glenn Maust.  This program is <br/>" +
					"currently in beta testing.  No expressed or implied warranty." +
					"</html>"), "About", JOptionPane.PLAIN_MESSAGE);
		}
	}
}
