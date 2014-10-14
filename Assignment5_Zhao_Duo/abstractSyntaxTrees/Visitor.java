package abstractSyntaxTrees;

import errorCenter.CalculatorError;

/**
 * Here is the visitor interface for the traversal of AST
 * @author duozhao
 *
 * @param <Agt> The generic type for argument
 * @param <Rt> The generic type of the resultant return type
 * @param <Excp> The generic Exception type define in errorCenter
 * a concrete class of CalculatorError must be defined to error Handling
 */
public interface Visitor <Agt, Rt, Excp extends CalculatorError>{
	
	public abstract Rt visitProgram(Program prog, Agt arg) throws Excp;
	
	//Statement:
	public abstract Rt visitAssignStmt(AssignStmt stmt, Agt arg) throws Excp;
	public abstract Rt visitReadStmt(ReadStmt stmt, Agt arg) throws Excp;
	public abstract Rt visitWriteStmt(WriteStmt stmt, Agt arg) throws Excp;
	public abstract Rt visitPostStmt(PostStmt stmt, Agt arg) throws Excp;
	
	//Expression:
	public abstract Rt visitBinExpr(BinExpr expr, Agt arg) throws Excp;
	public abstract Rt visitRefExpr(RefExpr expr, Agt arg) throws Excp;
	public abstract Rt visitLiteralExpr(LiteralExpr expr, Agt arg) throws Excp;

	//Terminal:
	public abstract Rt visitIdentifier(Identifier id, Agt arg) throws Excp;
	public abstract Rt visitLiteral(Literal ltr, Agt arg) throws Excp;
	public abstract Rt visitOperator(Operator op, Agt arg) throws Excp;
}
