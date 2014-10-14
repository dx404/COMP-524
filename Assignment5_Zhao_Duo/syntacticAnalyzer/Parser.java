package syntacticAnalyzer;

import java.util.ArrayList;
import java.util.Iterator;

import errorCenter.ParseError;

import abstractSyntaxTrees.*;

import syntacticAnalyzer.Token;
import syntacticAnalyzer.Token.Type;

/**
 * The parser generates an AST during parsing. 
 * Compared with HW4, the return type is an AST object
 * @author duozhao
 *
 */
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
	
	private Program programAST;
	//private, only created from tokenList

	Token currentToken;

	boolean isValid; 

	/**
	 * The parser constructor, taking an array of valid tokens. 
	 * @param tokenList, an ArrayList of tokens
	 */
	public Parser(ArrayList<Token> tokenList) throws ParseError{
		if(tokenList.size() <= 1){ //include EOF
			tokenList = null;
			tokenIterator = null;
			currentToken = null;
			isValid = false;
			//pushError("Empty token list"); OK for empty
		}
		else{
			this.tokenList = tokenList;
			tokenIterator = tokenList.iterator();
			currentToken = tokenIterator.next();
		}
	}

	public Parser(ArrayList<Token> tokenList, boolean isParseEnabled)
			 throws ParseError{
		if(tokenList.size() <= 1){ //given EOF
			tokenList = null;
			tokenIterator = null;
			currentToken = null;
			isValid = false;
			//pushError("Empty token list"); OK for empty
		}
		else{
			this.tokenList = tokenList;
			tokenIterator = tokenList.iterator();
			currentToken = tokenIterator.next();
			if (isParseEnabled){
				parseProgram();
			}
		}
	}
	
	public int getStatus(){
		return status;
	}
	
	public Program getAST(){
		return programAST;
	}
	public ArrayList<Token> getTokenList(){
		return tokenList;
	}

	void acceptIt() throws ParseError{ //unconditionally accept
		if (tokenIterator.hasNext()){
			currentToken = tokenIterator.next();
		}
		else{
			//System.out.println("EOF: End of the token iterator");
		}
	}
	
	void pushError(String errMsg) throws ParseError{
		System.out.println(errMsg);
		throw(new ParseError(errMsg));
	}
	

	void accept(Type tokenExpected) throws ParseError{ 
		// conditionally accept
		if(currentToken.type == tokenExpected){
			acceptIt();
		}
		else{		
			pushError(
					"Token Type " + tokenExpected 
					+ " expected, but " 
					+ currentToken.type 
					+ '(' + currentToken.spelling + ')' 
					+ " encountered");
		}
	}

	public Program parseProgram() throws ParseError{// get root node
		StmtList stmtList = new StmtList();

		stmtList.add(parseStmt());
		while(currentToken.type != Token.Type.EOF){
			stmtList.add(parseStmt());
		}
		accept(Token.Type.EOF); //must terminated by an EOF token

//		System.out.println("Valid calculator source file");

		programAST = new Program(stmtList);
		return programAST;
	}

	public Stmt parseStmt() throws ParseError{
		Stmt stmt = null;
		if (currentToken.type == Token.Type.READ){// stmt --> read id
			acceptIt();
			if(currentToken.type != Token.Type.ID){
				pushError("Error: Id is expected " 
						+ "after \"read\"");
			}
			stmt = new ReadStmt(parseId());
		}
		else if (currentToken.type == Token.Type.WRITE){
			//stmt -> write expr
			acceptIt();
			stmt = new WriteStmt(parseExpr());
		}
		else if (currentToken.type == Token.Type.ID){
			Identifier idAsFirstStmt = parseId();
			if(currentToken.type == Token.Type.ASSIGN ){
				//stmt -> id := expr
				acceptIt();
				Expression exprInAssign = parseExpr();
				stmt = new AssignStmt(idAsFirstStmt, exprInAssign);
			}
			else if (currentToken.type == Token.Type.POST_INCREMENT
					|| currentToken.type == Token.Type.POST_DECREMENT){
				//stmt -> id ++ || id --
				Operator postOp = parseOp();
				stmt = new PostStmt(idAsFirstStmt, postOp);
			}
			else{
				// not defined, error
				pushError("Error from parsing Statement: \n" +
						" Token :=, ++ or -- expected after id, but " + 
						currentToken.type +'('+ currentToken.spelling+')'
						+ " encountered");
			}			
		}
		else{
			// not defined, error
			pushError("Error from parsing Statement: \n" +
					" Token id, read or write expected as first token of" +
					" Stmt, but " + currentToken.type 
					+'('+ currentToken.spelling+')'
					+ " encountered");
		}
		return stmt;

	}

	public Expression parseExpr() throws ParseError{
		Expression expr = null;

		expr = parseTerm(); //term
		while(currentToken.type == Token.Type.ADD_OP){ //term_tail
			Operator op =  parseOp();
			Expression exprNext = parseTerm();
			expr = new BinExpr(expr, op, exprNext); //left-associative
		}

		return expr;
	}

	public Expression parseTerm() throws ParseError{
		Expression expr = null;

		expr = parseFactor(); //factor
		while (currentToken.type == Token.Type.MULT_OP){ //factor_tail
			Operator op = parseOp();
			Expression exprNext = parseFactor();
			expr = new BinExpr(expr, op, exprNext);
		}
		return expr;
	}

	public Expression parseFactor() throws ParseError{
		Expression expr = null;

		expr = parsePower(); //power
		if (currentToken.type == Token.Type.EXPONENT){//power_tail
			Operator op = parseOp();
			Expression exprTail = parseFactor(); //done by recursion
			expr = new BinExpr(expr, op, exprTail);
		}

		return expr;
	}

	public Expression parsePower() throws ParseError{
		Expression expr = null;

		if(currentToken.type == Token.Type.ID){ //power -->id
			expr = new RefExpr(parseId());
		}
		else if(currentToken.type == Token.Type.NUMBER){//power -->num
			expr = new LiteralExpr(parseLiteral());
		}
		else if(currentToken.type == Token.Type.LPAREN){//power-->(expr)
			acceptIt();
			expr = parseExpr();
			accept(Token.Type.RPAREN);
		}
		else{
			pushError("Error: parsing from power level: \n" +
					" Token id, num or '(',  but " + 
					currentToken.type +'('+ currentToken.spelling+')'
					+ " encountered");
		}

		return expr;
	}

	public Identifier parseId() throws ParseError{
		Identifier id = null;
		if (currentToken.type == Token.Type.ID){
			id = new Identifier(currentToken.spelling);
			acceptIt();
		}
		return id;
	}

	public Literal parseLiteral() throws ParseError{
		Literal literal = null;
		if (currentToken.type == Token.Type.NUMBER){
			literal = new Literal(currentToken.spelling);
			acceptIt();
		}
		return literal;
	}

	public Operator parseOp() throws ParseError{
		Operator op = null;
		if(currentToken.type == Token.Type.ADD_OP ||
				currentToken.type == Token.Type.MULT_OP ||
				currentToken.type == Token.Type.EXPONENT ||
				currentToken.type == Token.Type.POST_INCREMENT ||
				currentToken.type == Token.Type.POST_DECREMENT){
			op = new Operator(currentToken.spelling);
			acceptIt();
		}
		else{
			pushError("Error: Operator expected: ");
		}
		return op;
	}

}
