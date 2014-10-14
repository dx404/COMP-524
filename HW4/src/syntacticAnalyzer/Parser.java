package syntacticAnalyzer;

import java.util.ArrayList;
import java.util.Iterator;

import syntacticAnalyzer.Token;
import syntacticAnalyzer.Token.Type;

public class Parser {
	/* 0 for uninitialized parser, the tokenList is not provided
	 * 1 for successful parsed, AST has been created
	 *   for current homework, and isValid bit is obtained
	 * 100 for tokenList provided, but parsing does not start
	 * 101 for tokenList provided, but parsing incomplete, wait for EOF 
	 * -1 for an parsing error, invalid token has been scanned
	 * user may get the status value via getStatus();
	 */
	private int status; 

	private ArrayList<Token> tokenList;
	private Iterator<Token> tokenIterator; 
	//private, only created from tokenList

	Token currentToken;

	boolean isValid; 

	/**
	 * The parser constructor, taking an array of valid tokens. 
	 * @param tokenList, an ArrayList of tokens
	 */
	public Parser(ArrayList<Token> tokenList){
		if(tokenList.isEmpty()){
			System.out.println("Empty token list");
			tokenList = null;
			tokenIterator = null;
			currentToken = null;
			isValid = false;
			System.exit(4);
		}
		else{
			this.tokenList = tokenList;
			tokenIterator = tokenList.iterator();
			currentToken = tokenIterator.next();
		}
	}

	public Parser(ArrayList<Token> tokenList, boolean isParseEnabled){
		if(tokenList.isEmpty()){
			System.out.println("Empty token list");
			tokenList = null;
			tokenIterator = null;
			currentToken = null;
			isValid = false;
			System.exit(4);
		}
		else{
			this.tokenList = tokenList;
			tokenIterator = tokenList.iterator();
			currentToken = tokenIterator.next();
			parseProgram();
		}
	}

	void acceptIt(){ //unconditionally accept
		if (tokenIterator.hasNext()){
			currentToken = tokenIterator.next();
		}
		else{
			//System.out.println("EOF: End of the token iterator");
		}
	}

	void accept(Type tokenExpected){ // conditionally accept
		if(currentToken.type == tokenExpected){
			acceptIt();
		}
		else{		
			System.out.println(
					"Token Type " + tokenExpected 
					+ " expected, but " 
					+ currentToken.type 
					+ '(' + currentToken.spelling + ')' 
					+ " encountered");
			System.exit(4);
		}
	}

	public boolean parseProgram(){// get root node
		isValid = false;
		parseStmt();
		while(currentToken.type != Token.Type.EOF){
			parseStmt();
		}
		accept(Token.Type.EOF);

		System.out.println("Valid calculator source file");

		isValid = true;
		return isValid;
	}

	public void parseStmt(){
		if (currentToken.type == Token.Type.READ){// stmt --> read id
			acceptIt();
			if(currentToken.type != Token.Type.ID){
				System.out.println("Error: Id is expected " 
						+ "after \"read\"");
			}
			parseId();
		}
		else if (currentToken.type == Token.Type.WRITE){
			//stmt -> write expr
			acceptIt();
			parseExpr();
		}
		else if (currentToken.type == Token.Type.ID){
			parseId();
			if(currentToken.type == Token.Type.ASSIGN ){
				//stmt -> id := expr
				acceptIt();
				parseExpr();
			}
			else if(currentToken.type == Token.Type.POST_INCREMENT ||
					currentToken.type == Token.Type.POST_DECREMENT){
				//stmt -> id ++ | id --
				acceptIt();
			}
			else{
				// not defined, error
				System.out.println("Error from parsing Statement: \n" +
						" Token :=, ++ or -- expected after id, but " + 
						currentToken.type +'('+ currentToken.spelling+')'
						+ " encountered");
				System.exit(4);
			}			
		}
		else{
			// not defined, error
			System.out.println("Error from parsing Statement: \n" +
					" Token id, read or write expected as first token of" +
					" Stmt, but " + currentToken.type 
					+'('+ currentToken.spelling+')'
					+ " encountered");
			System.exit(4);		
		}

	}

	public void parseExpr(){
		parseTerm(); //term
		while(currentToken.type == Token.Type.ADD_OP){ //term_tail
			parseOp();
			parseTerm();		
		}
	}

	public void parseTerm(){
		parseFactor(); //factor
		while(currentToken.type == Token.Type.MULT_OP){ //factor_tail
			parseOp();
			parseFactor();
		}
	}

	public void parseFactor(){
		parsePower(); //power
		while(currentToken.type == Token.Type.EXPONENT){//power_tail
			parseOp();
			parsePower();
		}
	}

	public void parsePower(){
		if(currentToken.type == Token.Type.ID){ //power -->id
			parseId();
		}
		else if(currentToken.type == Token.Type.NUMBER){//power -->num
			parseNum();
		}
		else if(currentToken.type == Token.Type.LPAREN){//power-->(expr)
			acceptIt();
			parseExpr();
			accept(Token.Type.RPAREN);
		}
		else{
			System.out.println("Error: parsing from power level: \n" +
					" Token id, num or '(',  but " + 
					currentToken.type +'('+ currentToken.spelling+')'
					+ " encountered");
			System.exit(4);
		}

	}

	public void parseId(){
		accept(Token.Type.ID);
		//for later constructing idAST
	}

	public void parseNum(){
		accept(Token.Type.NUMBER);
		//for later constructing NumAST
	}

	public void parseOp(){
		if(currentToken.type == Token.Type.ADD_OP ||
				currentToken.type == Token.Type.MULT_OP ||
				currentToken.type == Token.Type.EXPONENT){
			acceptIt();
		}
		else{
			System.out.println("Error: Operator expected: ");
		}
	}

}
