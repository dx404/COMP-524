package executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import errorCenter.ExecError;

import abstractSyntaxTrees.*;

/**
 * Here is the major part of HW5
 * which implements the Visitor interface
 * applying the tree traversal technique to calculator the result. 
 * @author duozhao
 *
 */
public class Exec implements Visitor<IdTable, Double, ExecError> {
	
	Program parsedAST;
	IdTable idTable; //for identifier lookup

	ExecError errorReporter;

	public Exec(Program ast) throws ExecError{
		parsedAST = ast;
		idTable = new IdTable();
	}
	
	public Exec(Program ast, boolean isExecEnabled) throws ExecError{
		parsedAST = ast;
		idTable = new IdTable();
		if (isExecEnabled){
			start();
		}
	}

	public int start() throws ExecError{
		if (parsedAST != null){
			parsedAST.visit(this, idTable);
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public void printTable(){
		idTable.print();
	}

	void pushError(String errMsg) throws ExecError{
		System.out.println(errMsg);
		throw new ExecError(errMsg);
	}
	

	@Override
	public Double visitProgram(Program prog, IdTable idt) 
			throws ExecError{
		for (Stmt s: prog.stmtList){
			s.visit(this, idt);
		}
		return null;
	}

	@Override
	public Double visitAssignStmt(AssignStmt stmt, IdTable idt) 
			throws ExecError{
		String spelling = stmt.id.spelling;
		Double exprResult = stmt.expr.visit(this, idt);
		idt.put(spelling, exprResult);
		return null;
	}

	@Override
	public Double visitReadStmt(ReadStmt stmt, IdTable idt) 
			throws ExecError{
		String spelling = stmt.id.spelling;
		BufferedReader in = 
				new BufferedReader(
						new InputStreamReader(System.in));
		String inStr = null;
		try {
			do{
				inStr = in.readLine();
			}
			while (inStr.length() == 0); 
		} catch (IOException e) {
			e.printStackTrace();
			pushError("IOException, StackTrace as above: ");
		}
		LiteralExpr ltrExpr = new LiteralExpr(new Literal(inStr));
		Double inValue = ltrExpr.visit(this, idt);
		idt.put(spelling, inValue);
		return null;
	}

	@Override
	public Double visitWriteStmt(WriteStmt stmt, IdTable idt) 
			throws ExecError {
		Double exprResult = stmt.expr.visit(this, idt);
		System.out.println(exprResult.toString());
		return null;
	}

	@Override
	public Double visitPostStmt(PostStmt stmt, IdTable idt) 
			throws ExecError {
		Double value = stmt.id.visit(this, idt);
		if (stmt.postOp.equals("++")){
			value++;
		}
		else if (stmt.postOp.equals("--")){
			value--;
		}
		else {
			//not possible here
			pushError("Non post operator encountered in post statement");
		}
		idt.put(stmt.id.spelling, value); //override
		return null;
	}

	@Override
	public Double visitBinExpr(BinExpr expr, IdTable idt) 
			throws ExecError {
		Double left = expr.left.visit(this, idt);
		Operator op = expr.op;
		Double right = expr.right.visit(this, idt);

		Double result = 0.0;
		if (op.equals("+")){
			result = left + right;
		} 
		else if (op.equals("-")){
			result = left - right;
		}
		else if (op.equals("*")){
			result = left * right;
		}
		else if (op.equals("/")){
			result = left / right;
		}
		else if (op.equals("^")){
			result = Math.pow(left, right);
		}
		else {
			result = null; //cannot arrive here
			pushError("Unrecognized Operator: (" + op.spelling + ")");
		}
		return result;
	}

	@Override
	public Double visitRefExpr(RefExpr expr, IdTable idt) 
			throws ExecError {
		return expr.id.visit(this, idt);
	}

	@Override
	public Double visitLiteralExpr(LiteralExpr expr, IdTable idt) 
			throws ExecError {
		return expr.literal.visit(this, idt);
	}

	@Override
	public Double visitIdentifier(Identifier id, IdTable idt) 
			throws ExecError{
		Double value = idt.get(id.spelling);
		//pushError("The id (" + id.spelling + ") cannot be resolved");
		return (value == null) ? 0.0 : value;
	}

	@Override
	public Double visitLiteral(Literal ltr, IdTable idt) 
			throws ExecError {
		Double parsedNumLiteral = null;
		try{
			parsedNumLiteral = Double.parseDouble(ltr.spelling);
		}
		catch(NumberFormatException e){
			pushError("Your input: (" + ltr.spelling + 
					") is not a valid number. \n" +
					"Please check its format.");
		}
		return parsedNumLiteral; //OK??
	}

	@Override
	public Double visitOperator(Operator op, IdTable idt) 
			throws ExecError {
		return null; //nothing to do here
	}

}
