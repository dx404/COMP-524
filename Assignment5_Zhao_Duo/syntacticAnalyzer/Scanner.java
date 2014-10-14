package syntacticAnalyzer;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import errorCenter.ScanError;

import syntacticAnalyzer.Token.Type;

public class Scanner {
	/* 0 for uninitialized scanner, the source is not provided
	 * 1 for successful scanned, tokenList has been obtained
	 * 100 for source provided, but has not been scanned
	 * 101 for source provided, but scan incomplete, EOF has not arrived 
	 * -1 for an error scan, invalid token has been scanned
	 * user may get the status value via getStatus();
	 */
	private int status;  

	private BufferedReader source;
	private ArrayList<Token> tokenList;

	private Token mostRecentToken;
	private int currentState; //the state of DFA machine
	private StringBuffer currentSpilling;
	private char currentChar;

	public Scanner(){
		this.source = null;
		this.tokenList = null;
		this.status = 0;
	}

	public Scanner(BufferedReader source){
		this.source = source;
		this.tokenList = null;
		this.status = 100;
	}

	public Scanner(BufferedReader source, boolean isScanEnabled) 
			throws ScanError{
		if (isScanEnabled){
			this.source = source;
			status = scanSource();
			if (status != 1){
				throw new ScanError("Scanner error: " +
						"Token list has not been ready for parsing");
			}
		}
		else{
			this.source = source;
			this.tokenList = null;
			this.status = 100;
		}
	}

	public int getStatus(){
		return status;
	}

	public ArrayList<Token> getTokenList(){
		return tokenList;
	}

	public void pushError(String msg) throws ScanError{
		System.out.println(msg);
		throw new ScanError(msg);
	}

	public int printOverallStatus(){
		if(status == -1){
			String previousValidTokenSpilling;
			if(tokenList.size() > 0){
				previousValidTokenSpilling = tokenList.get(tokenList.size()-1).toString();
			}
			else{
				previousValidTokenSpilling = null;
			}
			System.out.print("Error Status of token scanning: Invalid token\r\n	");
			System.out.println(
					"currentChar(" + currentChar + ") " + 
							" || Last_Valid_Token(" + previousValidTokenSpilling + ")" 
							+ " || Token_State: " + currentState);
		}
		else{
			System.out.println(
					"currentChar(" + currentChar + ") " + 
							" || CurrentToken(" + mostRecentToken.toString() + ")" 
							+ " || Token_State: " + currentState);

		}
		return status;
	}
	public String getOverallStatus(){
		String info = null;
		if(status == -1){
			String previousValidTokenSpilling;
			if(tokenList.size() > 0){
				previousValidTokenSpilling = tokenList.get(tokenList.size()-1).toString();
			}
			else{
				previousValidTokenSpilling = null;
			}
			info = "Error Status of token scanning: Invalid token\r\n	" 
					+ "currentChar(" + currentChar + ") \n"
					+ " || Last_Valid_Token(" + previousValidTokenSpilling + ")\n" 
					+ " || Token_State: " + currentState;
		}
		else{
			info = " currentChar(" + currentChar + ") \r\n" 
					+ " || CurrentToken(" + mostRecentToken.toString() + ")\r\n" 
					+ " || Token_State: " + currentState;

		}
		return info;
	}

	public int scanSource() throws ScanError{
		status = 101;
		tokenList = new ArrayList<Token>();

		boolean scannable = true; 
		while (scannable){
			scanSingleToken(); //suppose to return a token. 
			switch(mostRecentToken.state){
			case 1: //white space, does not add to token list.
				break;

			case -1: /* For error output*/
				//printOverallStatus();
				pushError(getOverallStatus());
				scannable = false;
				status = -1;
				break;

			case -2: //EOF
				tokenList.add(mostRecentToken);
				scannable = false;
				status = 1;
				break;

			default:
				tokenList.add(mostRecentToken);
				break;
			}
		}
		return status;
	}



	public int scanSingleToken() { 
		status = 101;
		String tokenName;
		Type tokenType; // for passing to the scanned token	

		mostRecentToken = null;
		currentState = 1; //the state of DFA machine
		currentSpilling = new StringBuffer();
		currentChar = '\0';

		StringBuffer spillingRecord = new StringBuffer();
		int statusRecord; 

		try{
			int currentCharValue = source.read();
			if (currentCharValue != -1)
				currentChar = (char) currentCharValue;
			else{
				//return -2; //-2 is a self-defined flag for EOF
				mostRecentToken = new Token(Token.Type.EOF, 
						currentSpilling.toString(), "eof", -2);
				return currentState;
			}

			/*
			 * Nested Cases starts here, traversal the DFA's
			 */
			switch(currentChar){
			case '(':
				currentSpilling.append(currentChar);
				currentState = 6;
				tokenName = "lparen";
				tokenType = Token.Type.LPAREN;
				//return currentState;
				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			case ')':
				currentSpilling.append(currentChar);
				currentState = 7;
				tokenName = "rparen";
				tokenType = Token.Type.RPAREN;
				mostRecentToken =  new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return status;

			case '+':
				currentSpilling.append(currentChar);
				currentState = 8;
				tokenName = "plus";
				tokenType = Token.Type.ADD_OP;

				source.mark(markSize);
				currentChar = (char) source.read();
				if (currentChar != '+'){
					source.reset();
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
				}

				currentSpilling.append(currentChar);
				currentState = 17; //17 is for state "++"
				tokenName = "increment";
				tokenType = Token.Type.POST_INCREMENT;
				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			case '-':
				currentSpilling.append(currentChar);
				currentState = 9;
				tokenName = "minus";
				tokenType = Token.Type.ADD_OP;

				source.mark(markSize);
				currentChar = (char) source.read();
				if (currentChar != '-'){
					source.reset();
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
				}

				currentSpilling.append(currentChar);
				currentState = 18; //18 is for state "--"
				tokenName = "decrement";
				tokenType = Token.Type.POST_DECREMENT;
				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			case '*':
				currentSpilling.append(currentChar);
				currentState = 10;
				tokenName = "times";
				tokenType = Token.Type.MULT_OP;
				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			case '^':
				currentSpilling.append(currentChar);
				currentState = 19;
				tokenName = "exponent";
				tokenType = Token.Type.EXPONENT;
				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			case ':':
				currentSpilling.append(currentChar);
				currentState = 11;
				tokenName = "11-in_transit";
				tokenType = Token.Type.IN_TRANSIT;

				if ((currentCharValue = source.read()) != -1)
					currentChar = (char) currentCharValue;

				if (currentChar == '='){
					currentSpilling.append(currentChar);
					currentState = 12;
					tokenName = "assign";
					tokenType = Token.Type.ASSIGN;
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
				}

				currentState = -1;
				tokenName = "Error From 11, '=' is expected after ':' ";
				tokenType = Token.Type.ERROR;
				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			case '/':
				currentSpilling.append(currentChar);
				currentState = 2;
				tokenName = "div";
				tokenType = Token.Type.MULT_OP;

				source.mark(markSize);
				currentChar = (char) source.read();

				switch(currentChar){
				case '/':
					currentState = 3;
					tokenName = "3-in_transit";
					tokenType = Token.Type.IN_TRANSIT;

					do{
						currentCharValue = source.read();
						currentChar = (char) currentCharValue;
					}
					while (currentChar !='\n' && currentCharValue != -1);

					currentState = 1;
					tokenName = "1 -Comments back";
					tokenType = Token.Type.COMMENT;
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
					//for scanned comments;  type // \newline
					//go back to initial state, 
					//successfully scanned comments

				case '*':
					currentState = 4;
					tokenName = "4-in-transit";
					tokenType = Token.Type.IN_TRANSIT;

					do{
						do{
							currentCharValue = source.read();
							if (currentCharValue == -1){
								currentState = -1;
								tokenName = "error, unexpected EOF";
								tokenType = Token.Type.ERROR;
								mostRecentToken = new Token(tokenType, 
										currentSpilling.toString(), 
										tokenName, currentState);
								return currentState;
							}
							currentChar = (char) currentCharValue;
						}
						while (currentChar != '*');

						currentState = 5;
						tokenName = "5-in-transit";
						tokenType = Token.Type.IN_TRANSIT;

						while ((currentChar = (char)source.read())=='*');
						if (currentChar == '/'){
							currentState = 1;
							tokenName = "1 -Comments back";
							tokenType = Token.Type.COMMENT;
							mostRecentToken = new Token(tokenType, 
									currentSpilling.toString(), 
									tokenName, currentState);
							return currentState;
						}
						currentState = 4;
						tokenName = "4-in-transit";
					}
					while (currentState == 4);

				default:
					source.reset();
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
					//not a comment, '/' is scanned
				}

			case '.':
				currentSpilling.append(currentChar);
				currentState = 13;
				tokenName = "13 - in-transit";
				tokenType = Token.Type.IN_TRANSIT;

				source.mark(markSize);
				currentChar = (char) source.read();
				if (!Character.isDigit(currentChar)){
					source.reset();
					currentState = -1;
					tokenName = "Error: from 13";
					tokenType = Token.Type.ERROR;
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
					//no such a status, resort to error
				}
				currentSpilling.append(currentChar);
				currentState = 15;
				tokenName = "number";
				tokenType = Token.Type.NUMBER;
				source.mark(markSize);

				while (Character.isDigit(currentChar = 
						(char) source.read())){
					currentSpilling.append(currentChar);
					source.mark(markSize);
				}
				spillingRecord = new StringBuffer(currentSpilling);

				if (currentChar != 'e' && currentChar != 'E'){
					source.reset();
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
				}

				currentSpilling.append(currentChar);
				currentState = 20;
				tokenName = "20 - in-transit";
				tokenType = Token.Type.IN_TRANSIT;

				currentChar = (char) source.read();
				if (currentChar == '+' || currentChar == '-'){
					currentSpilling.append(currentChar);
					currentState = 21;
					tokenName = "21 - in-transit";
					tokenType = Token.Type.IN_TRANSIT;
					currentChar = (char) source.read();
				}		
				if (!Character.isDigit(currentChar)){
					source.reset();
					//currentSpilling.append(currentChar);
					currentSpilling = spillingRecord; //at 15
					currentState = 15;
					tokenName = "number";
					tokenType = Token.Type.NUMBER;
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
				}

				currentSpilling.append(currentChar);
				currentState = 22;
				tokenName = "number";
				tokenType = Token.Type.NUMBER;
				source.mark(markSize);

				while (Character.isDigit(currentChar 
						= (char) source.read())){
					currentSpilling.append(currentChar);
					source.mark(markSize);
				}
				source.reset();
				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			case '0': case '1': case '2': case '3': case '4': 
			case '5': case '6': case '7': case '8': case '9':
				currentSpilling.append(currentChar);
				currentState = 14;
				tokenName = "number";
				tokenType = Token.Type.NUMBER;
				source.mark(markSize);
				currentChar = (char) source.read();

				while (Character.isDigit(currentChar)){
					currentSpilling.append(currentChar);
					source.mark(markSize);
					currentChar= (char) source.read();
				}
				if (currentChar == '.'){
					currentSpilling.append(currentChar);
					currentState = 15;
					tokenName = "number";
					tokenType = Token.Type.NUMBER;
					source.mark(markSize);
					currentChar = (char) source.read();
					while (Character.isDigit(currentChar)){
						currentSpilling.append(currentChar);
						source.mark(markSize);
						currentChar= (char) source.read();
					}
				}

				spillingRecord = new StringBuffer(currentSpilling);
				statusRecord = currentState; //record at 14 or 15

				if (currentChar != 'e' && currentChar != 'E'){
					source.reset();
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
				}

				currentSpilling.append(currentChar); // 'e' or 'E'
				currentState = 20;
				tokenName = "20 - in-transit";
				tokenType = Token.Type.IN_TRANSIT;
				currentChar = (char) source.read();

				if (currentChar == '+' || currentChar == '-'){
					currentSpilling.append(currentChar);
					currentState = 21;
					tokenName = "21 - in-transit";
					tokenType = Token.Type.IN_TRANSIT;
					currentChar = (char) source.read();
				}

				if (!Character.isDigit(currentChar)){
					source.reset();
					//currentSpilling.append(currentChar);
					currentSpilling = spillingRecord;//at 15
					currentState = statusRecord;
					tokenName = "number";
					tokenType = Token.Type.NUMBER;
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
				}

				currentSpilling.append(currentChar); //digit
				currentState = 22;
				tokenName = "number";
				tokenType = Token.Type.NUMBER;
				source.mark(markSize);
				currentChar = (char) source.read();

				while (Character.isDigit(currentChar)){
					currentSpilling.append(currentChar);
					source.mark(markSize);
					currentChar = (char) source.read();
				}
				source.reset();
				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': 
			case 'g': case 'h': case 'i': case 'j': case 'k': case 'l': 
			case 'm': case 'n': case 'o': case 'p': case 'q': case 'r': 
			case 's': case 't': case 'u': case 'v': case 'w': case 'x': 
			case 'y': case 'z': case 'A': case 'B': case 'C': case 'D': 
			case 'E': case 'F': case 'G': case 'H': case 'I': case 'J': 
			case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': 
			case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': 
			case 'W': case 'X': case 'Y': case 'Z': 
				currentSpilling.append(currentChar);
				currentState = 16;
				tokenName = "id";
				tokenType = Token.Type.ID;
				source.mark(markSize);
				currentChar = (char) source.read();

				while (Character.isLetterOrDigit(currentChar)){
					currentSpilling.append(currentChar);
					source.mark(markSize);
					currentChar = (char) source.read();
				}
				source.reset();

				if ("read".contentEquals(currentSpilling)){
					tokenName = "read";
					tokenType = Token.Type.READ;
				}
				else if ("write".contentEquals(currentSpilling)){
					tokenName = "write";
					tokenType = Token.Type.WRITE;
				}

				mostRecentToken = new Token(tokenType, 
						currentSpilling.toString(), 
						tokenName, currentState);
				return currentState;

			default:
				if (Character.isWhitespace(currentChar)){
					do{
						source.mark(markSize);
						currentChar = (char) source.read();
					}
					while (Character.isWhitespace(currentChar));
					source.reset();

					currentState = 1;
					tokenName = "1 - while space";
					tokenType = Token.Type.WHITESPACE;
					mostRecentToken = new Token(tokenType, 
							currentSpilling.toString(), 
							tokenName, currentState);
					return currentState;
				}
				break;
			}

			/* For processing the undefined branch */
			//System.out.println("======Invalid/Undefined Character, cannot create valid Token ====");
			status = -1;
			currentState = -1;
			tokenName = "Error, unknown character";
			tokenType = Token.Type.ERROR;
			mostRecentToken = new Token(tokenType, 
					currentSpilling.toString(), tokenName, currentState);
			return currentState;
		}
		catch(EOFException e){
			status = 1;
			currentState = -2;
			tokenName = "eof";
			tokenType = Token.Type.EOF;		
			mostRecentToken = new Token(tokenType, 
					currentSpilling.toString(), tokenName, currentState);
			return currentState;

		}
		catch(IOException e){
			System.out.println("Other Exception");
			status = -1;
			currentState = -1;
			tokenName = "other exception";
			tokenType = Token.Type.ERROR;
			mostRecentToken = new Token(tokenType, 
					currentSpilling.toString(), tokenName, currentState);
			return currentState;
		}
	}

	public static final int markSize = 10;

	public static final String[] keywords = 
		{"read", "write"};


}
