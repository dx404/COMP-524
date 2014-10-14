import java.io.*;

/**
 * Calculator Program
 * --Here is the scanner part
 * File Name: Main.java
 * Class Name: Main
 * @author Duo Zhao
 * @version 1.0 02/09/2012
 */
public class Main {
	/** For the storage of processed string of characters */
	public static StringBuffer currentSpilling;
	/** For the record tracking of current status */
	public static int currentStatus;
	/** The processed result of scanner, a token Name is obtained if successful */
	public static String tokenName;
	/** A buffered character read from a text file */
	public static char currentChar;  // = '\0';
	
	public static final String[] categoricalTokenNames = 
		{"id", "keyword", "number", "operator"};

	public static final String[] keywords = 
		{"read", "write"};

	public static final String[] operatorTokenNames = 
		{"assign", "lparen", "rparen", "plus", "increment", "decrement", 
		"times", "div", "exponent"};

	public static final String[] operatorTokens = 
		{":=", "(", ")", "+", "++", "-", "--", "*", "/", "^"};
	
	public static final int markSize = 10;

	/**
	 * The main method starts here
	 * @param args is at least of length 1. args[0] indicates the file to be scanned.  
	 */
	public static void main(String[] args) {
		try {
			//Doing I/O's, get started to connected to the file to be processed.
			File inputFile = new File(args[0]);
			BufferedReader sourceReader = new BufferedReader(new FileReader(inputFile));
			currentSpilling = new StringBuffer();

			/*scannable is the flag for tracking if further scanning is possible.
			* When case -1: Error encountered or case -2: EOF encountered, Scanning is terminated
			* case 1 corresponds to state 1, which indicating while space or comments are scanned.
			*/
			boolean scannable = true; 
			while(scannable){
				int postScanStatus = scanToken(sourceReader);
				switch(postScanStatus){
				case 1:
					/* This sentence is for debugging only, indicating proper comments processing */
					//System.out.println("/********Comments or WhileSpace:" + "(" + currentSpilling + ")");
					break;
				case -1:
					/* For error output*/
					System.out.println("===Error:===" + "Spilling:" + "(" + currentSpilling + ")" +" State: " + currentStatus);
					scannable = false;
					break;
				case -2:
					/*For debugging only, indicating EOF is encountered, text file sucessfully processed */  
					System.out.println("eof($$)");
					scannable = false;
					break;
				default:
					/*For the regular state other than the start state 1, output is printed out */
					System.out.println(tokenName + "(" + currentSpilling + ")");
					break;
				}
			}
			sourceReader.close();

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	/**
	 * Token scanning method. Moving forward one token in text file
	 * @param source, a BufferedReader linked to the text file
	 * @return return the state after processing one token
	 */
	public static int scanToken(BufferedReader source) {
		try{
			/* Initializing variables*/
			currentSpilling = new StringBuffer();
			StringBuffer spillingRecord = new StringBuffer();
			int statusRecord; 

			int currentCharValue = source.read();
			if(currentCharValue != -1)
				currentChar = (char) currentCharValue;
			else{
				return -2; //-2 is a self-defined flag for EOF
			}
			
			/*
			 * Nested Cases starts here, traversal the DFA's
			 */
			switch(currentChar){
			case '(':
				currentSpilling.append(currentChar);
				currentStatus = 6;
				tokenName = "lparen";
				return currentStatus;
				
			case ')':
				currentSpilling.append(currentChar);
				currentStatus = 7;
				tokenName = "rparen";
				return currentStatus;

			case '+':
				currentSpilling.append(currentChar);
				currentStatus = 8;
				tokenName = "plus";

				source.mark(markSize);
				currentChar = (char) source.read();
				if(currentChar != '+'){
					source.reset();
					return currentStatus;
				}

				currentSpilling.append(currentChar);
				currentStatus = 17; //17 is for state "++"
				tokenName = "increment";
				return currentStatus;

			case '-':
				currentSpilling.append(currentChar);
				currentStatus = 9;
				tokenName = "minus";

				source.mark(markSize);
				currentChar = (char) source.read();
				if(currentChar != '-'){
					source.reset();
					return 9;
				}

				currentSpilling.append(currentChar);
				currentStatus = 18; //18 is for state "--"
				tokenName = "decrement";
				return currentStatus;

			case '*':
				currentSpilling.append(currentChar);
				currentStatus = 10;
				tokenName = "times";
				return currentStatus;

			case '^':
				currentSpilling.append(currentChar);
				currentStatus = 19;
				tokenName = "exponent";
				return currentStatus;

			case ':':
				currentSpilling.append(currentChar);
				currentStatus = 11;
				tokenName = "11-Intermediate";

				if((currentCharValue = source.read()) != -1)
					currentChar = (char) currentCharValue;

				if(currentChar == '='){
					currentSpilling.append(currentChar);
					currentStatus = 12;
					tokenName = "assign";
					return currentStatus;
				}
				tokenName = "Error From 11";
				return -1;

			case '/':
				currentSpilling.append(currentChar);
				currentStatus = 2;
				tokenName = "div";

				source.mark(markSize);
				currentChar = (char) source.read();

				switch(currentChar){
				case '/':
					currentStatus = 3;
					tokenName = "3-Intermediate";

					do{
						currentCharValue = source.read();
						currentChar = (char) currentCharValue;
					}
					while(currentChar !='\n' && currentCharValue != -1);

					currentStatus = 1;
					tokenName = "1 -Comments back";
					return currentStatus; //for scanned comments;  type // \newline
					//go back to initial state, successfully scanned comments

				case '*':
					currentStatus = 4;
					tokenName = "4-Intermediate";
					do{
						do{
							currentCharValue = source.read();
							if(currentCharValue == -1)
								return -1;
							currentChar = (char) currentCharValue;
						}
						while(currentChar != '*');
						
						currentStatus = 5;
						tokenName = "5-Intermediate";

						while((currentChar = (char) source.read()) == '*');
						if(currentChar == '/'){
							currentStatus = 1;
							tokenName = "1 -Comments back";
							return currentStatus;
						}
						currentStatus = 4;
						tokenName = "4-Intermediate";
					}
					while(currentStatus == 4);

				default:
					source.reset();
					return currentStatus; //not a comment, '/' is scanned
				}

			case '.':
				currentSpilling.append(currentChar);
				currentStatus = 13;
				tokenName = "13 - Intermediate";

				source.mark(markSize);
				currentChar = (char) source.read();
				if(!Character.isDigit(currentChar)){
					source.reset();
					return -1; //no such a status, resort to error
				}
				currentSpilling.append(currentChar);
				currentStatus = 15;
				tokenName = "number";
				source.mark(markSize);

				while(Character.isDigit(currentChar = (char) source.read())){
					currentSpilling.append(currentChar);
					source.mark(markSize);
				}
				spillingRecord = new StringBuffer(currentSpilling);//record String

				if(currentChar != 'e' && currentChar != 'E'){
					source.reset();
					return currentStatus;
				}

				currentSpilling.append(currentChar);
				currentStatus = 20;
				tokenName = "20 - Intermediate";

				currentChar = (char) source.read();
				if(currentChar == '+' || currentChar == '-'){
					currentSpilling.append(currentChar);
					currentStatus = 21;
					tokenName = "21 - Intermediate";
					currentChar = (char) source.read();
				}		
				if(!Character.isDigit(currentChar)){
					source.reset();
					//currentSpilling.append(currentChar);
					currentSpilling = spillingRecord; //at 15
					currentStatus = 15;
					tokenName = "number";
					return currentStatus;
				}

				currentSpilling.append(currentChar);
				currentStatus = 22;
				tokenName = "number";
				source.mark(markSize);

				while(Character.isDigit(currentChar = (char) source.read())){
					currentSpilling.append(currentChar);
					source.mark(markSize);
				}
				source.reset();
				return currentStatus;

			case '0': case '1': case '2': case '3': case '4': 
			case '5': case '6': case '7': case '8': case '9':
				currentSpilling.append(currentChar);
				currentStatus = 14;
				tokenName = "number";
				source.mark(markSize);
				currentChar = (char) source.read();

				while(Character.isDigit(currentChar)){
					currentSpilling.append(currentChar);
					source.mark(markSize);
					currentChar= (char) source.read();
				}
				if(currentChar == '.'){
					currentSpilling.append(currentChar);
					currentStatus = 15;
					tokenName = "number";
					source.mark(markSize);
					currentChar = (char) source.read();
					while(Character.isDigit(currentChar)){
						currentSpilling.append(currentChar);
						source.mark(markSize);
						currentChar= (char) source.read();
					}
				}

				spillingRecord = new StringBuffer(currentSpilling);
				statusRecord = currentStatus; //record at 14 or 15

				if(currentChar != 'e' && currentChar != 'E'){
					source.reset();
					return currentStatus;
				}

				currentSpilling.append(currentChar); // 'e' or 'E'
				currentStatus = 20;
				tokenName = "20 - Intermediate";			
				currentChar = (char) source.read();

				if(currentChar == '+' || currentChar == '-'){
					currentSpilling.append(currentChar);
					currentStatus = 21;
					tokenName = "21 - Intermediate";
					currentChar = (char) source.read();
				}

				if(!Character.isDigit(currentChar)){
					source.reset();
					//currentSpilling.append(currentChar);
					currentSpilling = spillingRecord;//at 15
					currentStatus = statusRecord;
					tokenName = "number";
					return currentStatus;
				}

				currentSpilling.append(currentChar); //digit
				currentStatus = 22;
				tokenName = "number";
				source.mark(markSize);
				currentChar = (char) source.read();

				while(Character.isDigit(currentChar)){
					currentSpilling.append(currentChar);
					source.mark(markSize);
					currentChar = (char) source.read();
				}
				source.reset();
				return currentStatus;


			case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': 
			case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': 
			case 'o': case 'p': case 'q': case 'r': case 's': case 't': 
			case 'u': case 'v': case 'w': case 'x': case 'y': case 'z': 
			case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G':
			case 'H': case 'I': case 'J': case 'K': case 'L': case 'M': case 'N':
			case 'O': case 'P': case 'Q': case 'R': case 'S': case 'T': 
			case 'U': case 'V': case 'W': case 'X': case 'Y': case 'Z': 
				currentSpilling.append(currentChar);
				currentStatus = 16;
				tokenName = "id";
				source.mark(markSize);
				currentChar = (char) source.read();

				while(Character.isLetterOrDigit(currentChar)){
					currentSpilling.append(currentChar);
					source.mark(markSize);
					currentChar = (char) source.read();
				}
				source.reset();

				for(int i = 0; i < keywords.length; i++){ //keyword checking
					if(keywords[i].contentEquals(currentSpilling)){
						tokenName = "keyword";
						return currentStatus;
					}
				}
				return currentStatus;

			default:
				if(Character.isWhitespace(currentChar)){
					do{
						source.mark(markSize);
						currentChar = (char) source.read();
					}
					while(Character.isWhitespace(currentChar));
					source.reset();
					return 1;
				}
				break;
			}
			
			/* For processing the undefined branch */
			System.out.println("========Invalid/Undefined Character======");
			return -1;
		}
		catch(EOFException e){
			System.out.println("========EOF========");
			return -2;
		}
		catch(IOException e){
			System.out.println("Other Exception");
			return -1;
		}
	}

}