import java.io.*; //File IO
import javax.imageio.*; //BufferedImage
import java.awt.image.*; //getRGB & setRGB

public class GreenScreen {
	public static void main(String[] args) throws IOException {
		//Runs exception class which if all are true, will execute the class color & writer.
		exceptions(args);
	}
	
	/**
	The exceptions method accepts a String array argument.
	This method handles exceptions that might bring up.
	@param args Contains parameters for program execution.
	@throws IOException For catching file problems. 
	*/
	
	public static void exceptions(String args[]) throws IOException {
		//Creates 7 boolean variables, all of which have to be true for the colorChanger & colorWriter to execute.
		boolean[] bool = new boolean[6];
		
		//args[0] try & catch statements
		try {
			new FileReader(args[0]);
			bool[0] = true;
		}
		catch (FileNotFoundException e) {
			System.out.println("Input file for color swap not found.");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("No input file specified.");
		}
		catch (Exception e) {
			System.out.println("There is a problem with the inputfile.");
		}
		
		//args[1] try & catch statements
		try {
			new FileReader(args[1]);
			bool[1] = true;
		}
		catch (FileNotFoundException e) {
			System.out.println("Input File not Found");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("No input file specified.");
		}
		catch (Exception e) {
			System.out.println("There is a problem with the inputfile.");
		}
		
		//args[2] try & catch statements
		try {
			if (!args[2].contains("."))
				bool[2] = true;
			else
				System.out.println("Outfile name invalid.");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Output file not specified.");
		}
		catch (Exception e) {
			System.out.println("There is a problem with the outputfile.");
		}
		
		//args[3] try & catch statements
		try {
			if (args[0].endsWith(".png") && args[1].endsWith(".png") && args[3].equalsIgnoreCase("png")){
				bool[3] = true;
			}
			else if (args[0].endsWith(".jpg") && args[1].endsWith(".jpg") && args[3].equalsIgnoreCase("jpg")) {
				bool[3] = true;
			}
			else 
				System.out.println("The extension of all input files do not match!");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Extension not specified.");
		}
		catch (Exception e) {
			System.out.println("There is a problem with the extension.");
		}
		
		//args[4] try & catch statements
		try {
			if (args[4].equalsIgnoreCase("green") || args[4].equalsIgnoreCase("white") || args[4].equalsIgnoreCase("auto"))
				bool[4] = true;
			else 
				System.out.println("The spedified color is not valid.");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Color of replacement not specified.");
		}
		catch (Exception e) {
			System.out.println("There is a problem with the color of replacement.");
		}
		
		//Checks the dimensions of both the input and output image for same dimensions.
		if (bool[0] && bool[1] && bool[2] && bool[3] && bool[4]) {
			BufferedImage imageIn = ImageIO.read(new File(args[0]));

			BufferedImage imageOut = ImageIO.read(new File(args[1]));
			
			if (imageIn.getWidth() == imageOut.getWidth() && imageIn.getHeight() == imageOut.getHeight())
				bool[5] = true;
			else 
				System.out.println("The imgaes are not the same dimensions.");
		}
		
		//All arguments and dimensions have to be true for this to execute.
		//Just to be safe, this has a general exception built in. 
		try {
			if (bool[0] && bool[1] && bool[2] && bool[3] && bool[4] && bool[5])
				colorWriter(colorChanger(args), args);
			else 
				System.out.println("Program Terminated.");
		}
		catch (Exception e) {
			System.out.println("General Program Failure");
		}
	}
	
	/**
	The color Changer method accepts a String array argument.
	This method changes the colors in the picture. 
	@param args Contains parameters for program execution.
	@return imageIn BufferedImage stream for output
	@throws IOException For catching file problems. 
	*/
	
	public static BufferedImage colorChanger(String[] args) throws IOException {
		//Open file for replacement through Buffered Image stream.
		BufferedImage imageIn = ImageIO.read(new File(args[0]));
		
		//Open file background through Buffered Image stream.
		BufferedImage imageOut = ImageIO.read(new File(args[1]));

		//Array to store pixel information for calculation on extra credit
		int[][] pixels = new int[imageIn.getHeight()][imageIn.getWidth()];

		//Determines each pixel color and stores it into a 2D array to a corresponding location. 
		for (int col = 0; col < imageIn.getWidth(); col++) {
			for (int row = 0; row < imageIn.getHeight(); row++) {
				pixels[row][col] = imageIn.getRGB(col, row);
			}
		}
		
		//Array to store different colors and their pixel counts.
		int[][] colors = new int[10000][2];
		colors[0][0] = pixels[0][0]; //Sets color value at position (0,0) to first array.

		//Gets color information and stores it in a array, then it checks the array for color, and adds count.
		//If the color does not exists, it goes down to a empty space, and creates a new entry, and sets one for count.
		for (int col = 0; col < imageIn.getWidth(); col++) {
			for (int row = 0; row < imageIn.getHeight(); row++) {
				boolean bool = true;
				int counter = 0;
				for (int i = 0; i < colors.length; i++) {
					if (pixels[row][col] == colors[i][0]) {
						colors[i][1]++;
						bool = false;
					}
					if (colors[i][0] == 0) {
						counter = i;
						break;
					}
				}
				if (bool) {
					colors[counter][0] = pixels[row][col];
					colors[counter][1]++;
				}
			}
		}

		//Prints out array of color, and number of hits greater than 10.
		System.out.println("Top Colors:");
		for (int row = 0; row < colors.length; row++) {
			if (colors[row][0] != 0 && colors[row][1] > 10)
				System.out.println(colors[row][0] + " " + colors[row][1]);
		}

		//Determine's the color with the highest pixel count.
		int high = colors[0][1];
		int backgroundColor = colors[0][0];
		for (int row = 0; row < colors.length; row++) {
			if (colors[row][1] > high) {
				backgroundColor = colors[row][0];
				high = colors[row][1];
			}
		}
		System.out.println("Color: "+ backgroundColor + " Count: " + high);
		
		//Override for args[4] color selector
		if (args[4].equalsIgnoreCase("green")) {
			backgroundColor = -16711935;
		}

		if (args[4].equalsIgnoreCase("white")) {
			backgroundColor = -1;
		}

		
		//Color Changer
		//If the pixel on the image to be changed is the same as the color to be changed, it changes the color.
		//There is also a 50 point tolerance. 
		for (int col = 0; col < imageIn.getWidth(); col++) {
			for (int row = 0; row < imageIn.getHeight(); row++) {
				if (imageIn.getRGB(col, row) > (backgroundColor - 50) && imageIn.getRGB(col, row) < (backgroundColor + 50))
					imageIn.setRGB(col, row, imageOut.getRGB(col, row));
			}
		}

		return imageIn;
	}
	
	/**
	The colorWriter method accepts a BufferedImage stream, and a String array argument.
	@param imageIn A BufferedImage stream for inputImage.
	@param args Contains parameters for program execution.
	@throws IOException For catching file problems. 
	*/
	
	public static void colorWriter(BufferedImage imageIn, String[] args) throws IOException {
		//Generates a *.extension String.
		String outputFile = args[2]+ "." + args[3];
		
		//Writes output File
		ImageIO.write(imageIn, args[3], new File (outputFile));
	}
}
