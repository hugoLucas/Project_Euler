import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * In the 20�20 grid below, four numbers along a diagonal line have been marked in red.
 * 
 * 08 02 22 97 38 15 00 40 00 75 04 05 07 78 52 12 50 77 91 08
 * 49 49 99 40 17 81 18 57 60 87 17 40 98 43 69 48 04 56 62 00
 * 81 49 31 73 55 79 14 29 93 71 40 67 53 88 30 03 49 13 36 65
 * 52 70 95 23 04 60 11 42 69 24 68 56 01 32 56 71 37 02 36 91
 * 22 31 16 71 51 67 63 89 41 92 36 54 22 40 40 28 66 33 13 80
 * 24 47 32 60 99 03 45 02 44 75 33 53 78 36 84 20 35 17 12 50
 * 32 98 81 28 64 23 67 10 26 38 40 67 59 54 70 66 18 38 64 70
 * 67 26 20 68 02 62 12 20 95 63 94 39 63 08 40 91 66 49 94 21
 * 24 55 58 05 66 73 99 26 97 17 78 78 96 83 14 88 34 89 63 72
 * 21 36 23 09 75 00 76 44 20 45 35 14 00 61 33 97 34 31 33 95
 * 78 17 53 28 22 75 31 67 15 94 03 80 04 62 16 14 09 53 56 92
 * 16 39 05 42 96 35 31 47 55 58 88 24 00 17 54 24 36 29 85 57
 * 86 56 00 48 35 71 89 07 05 44 44 37 44 60 21 58 51 54 17 58
 * 19 80 81 68 05 94 47 69 28 73 92 13 86 52 17 77 04 89 55 40
 * 04 52 08 83 97 35 99 16 07 97 57 32 16 26 26 79 33 27 98 66
 * 88 36 68 87 57 62 20 72 03 46 33 67 46 55 12 32 63 93 53 69
 * 04 42 16 73 38 25 39 11 24 94 72 18 08 46 29 32 40 62 76 36
 * 20 69 36 41 72 30 23 88 34 62 99 69 82 67 59 85 74 04 36 16
 * 20 73 35 29 78 31 90 01 74 31 49 71 48 86 81 16 23 57 05 54
 * 01 70 54 71 83 51 54 69 16 92 33 48 61 43 52 01 89 19 67 48
 * 
 * The product of these numbers is 26 � 63 � 78 � 14 = 1788696.
 * What is the greatest product of four adjacent numbers in the same direction 
 * (up, down, left, right, or diagonally) in the 20�20 grid?
 */

/*
 * SOLUTION: Taking a file containing the number above as an input, this program
 * will parse that file and input each number into a "matrix". This matrix will be
 * a two dimensional array consisting of a mother array that tracks the row number
 * and a child array for each element in that row (i.e. the second number of the 
 * third row in the list above will be the second element of the array list 
 * referenced by the mother array's third element). Next we will loop through this
 * matrix starting at the first row, and ending at the 20th, and visit each 
 * element in that row and every subsequent row. Using helper methods, we will then
 * check if there are enough numbers in every direction to calculate any product (e.g.
 * are there enough elements "above" the third number in the second row to calculate
 * the product of that the number and the three numbers on top of it). Using the 
 * results of those methods we will then compute all possible products and
 * compare them to the current max. At the end of the loop we should be left with 
 * the largest product possible. 
 */

public class Prob_11 {
		
	final static int height = 20; 
	final static int width = 20; 
	final static int productLength = 4; 
	
	static int maxProduct = 0; 
	static ArrayList<ArrayList<Integer>> inputMatrix;
	
	public static void main(String[] args) throws IOException{
		
		try{
			/*
			 * Create file object, parse it into a matrix and then look through 
			 * matrix to find the maximum 4 digit product. 
			 */
			File inputFile = new File("c:\\Users\\Hugo Lucas\\Documents\\Prob_11_Info.txt"); 
			inputMatrix = fileToMatrix(inputFile); 
			traverseMatrix(inputMatrix); 
			System.out.println(maxProduct); 
			
		}catch (IOException e){
			System.out.println("Check file path!");
			e.printStackTrace();
		}
	}
	
	public static void traverseMatrix (ArrayList<ArrayList<Integer>> matrix){
		/*
		 * Looks at every row in the matrix
		 */
		for (int y = 0; y < matrix.size(); y ++){
			/*
			 * There should be the same number of rows above a given row y, so 
			 * these booleans will be constant for any given row. 
			 */
			boolean down = down(y); 
			boolean up = up(y);
			
			/*
			 * Looks through every element in a row. 
			 */
			for (int x = 0; x < matrix.get(y).size(); x++){
				/*
				 * These booleans will not be constant so they 
				 * must be determined for every element in a row
				 */
				boolean right = right(x); 
				boolean left = left(x);
				
				/*
				 * For a given element (x,y) we check if there 
				 * are enough numbers in any given direction to 
				 * calculate a 4 digit product. If so, calculate the
				 * product and compare it to the current max. 
				 */
				if(down)
					max(computeProduct("d", x, y));
				if(up)
					max(computeProduct("u", x, y)); 
				if(left)
					max(computeProduct("l", x, y));
				if(right)
					max(computeProduct("r", x, y));
				
				if(down & right)
					max(computeProduct("dr", x, y));
				if(up & right)
					max(computeProduct("ur", x, y)); 
				if(down & left)
					max(computeProduct("dl", x, y));
				if(up & left)
					max(computeProduct("ul", x, y));
			} 
		} 
	}
	
	/*
	 * Given a file object, this method will return a "matrix" representation of the 
	 * contents. 
	 */
	public static ArrayList<ArrayList<Integer>> fileToMatrix (File input) throws IOException{
		/*
		 * Create a two dimensional array list. Loop is to add the correct number 
		 * of "child" lists to the master "mother" list. 
		 */
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>> (width);
		for(int i = 0; i < width; i++)
			matrix.add(new ArrayList<Integer>(height)); 
		
		BufferedReader fileReader = new BufferedReader(new FileReader (input));
		
		/*
		 * Read a complete line of the file and input every number into the 
		 * corresponding child list. Because the numbers are always two digits, 
		 * this will read two characters at a time. The first digit must be 
		 * multiplied by 10 in order to make sure the correct number is input
		 * into the list (e.g. 17 will be read as "1" and "7" so to input 17 
		 * we do (1x10) + 7 = 17).
		 */
		int row = 0; 
		while(fileReader.ready()){
			char buf[] = fileReader.readLine().toCharArray();
			for(int i = 0; i < buf.length; i = i + 3)
				matrix.get(row).add((Character.getNumericValue(buf[i])*10) 
						+ (Character.getNumericValue(buf[i+1]))); 
			
			row ++; 
		}
		
		fileReader.close();
		return matrix;
	}
	
	/*
	 * The following four methods just check to see if there 
	 * are 3 additional numbers in each direction. 
	 */
	public static boolean up (int y){
		if ( y <= 2)
			return false; 
		return true; 
	}
	
	public static boolean down (int y){
		if ( y >= 17)
			return false; 
		return true; 
	}
	
	public static boolean left (int x){
		if ( x <= 2)
			return false; 
		return true; 
	}
	
	public static boolean right (int x){
		if ( x >= 17)
			return false; 
		return true; 
	}
	
	/*
	 * Given a string code, this method calculates the four number
	 * product using a four loop. 
	 */
	public static int computeProduct(String direction, int x, int y){
		int product = 1; 
		
		if(direction.equals("u"))
			for(int yCord = 0; yCord < productLength; yCord++)
				product = product * inputMatrix.get(y - yCord).get(x); 

		else if (direction.equals("d"))
			for(int yCord = 0; yCord < productLength; yCord++)
				product = product * inputMatrix.get(y + yCord).get(x); 
		
		else if (direction.equals("r"))
			for(int xCord = 0; xCord < productLength; xCord++)
				product = product * inputMatrix.get(y).get(x + xCord); 
		
		else if (direction.equals("l"))
			for(int xCord = 0; xCord < productLength; xCord++)
				product = product * inputMatrix.get(y).get(x - xCord); 
		
		else if (direction.equals("ur"))
			for(int xyCord = 0; xyCord < productLength; xyCord++)
				product = product * inputMatrix.get(y - xyCord).get(x + xyCord); 
		
		else if (direction.equals("ul"))
			for(int xyCord = 0; xyCord < productLength; xyCord++)
				product = product * inputMatrix.get(y - xyCord).get(x - xyCord); 
		
		else if (direction.equals("dr"))
			for(int xyCord = 0; xyCord < productLength; xyCord++)
				product = product * inputMatrix.get(y + xyCord).get(x + xyCord); 
		
		else if (direction.equals("dl"))
			for(int xyCord = 0; xyCord < productLength; xyCord++)
				product = product * inputMatrix.get(y + xyCord).get(x - xyCord); 
		
		return product; 
	}
	
	/*
	 * Tracks the current maximum product. Mainly created to save space. 
	 */
	public static void max (int product){
		if(product > maxProduct)
			maxProduct = product; 
	}
}
