package abstractSyntaxTrees;

import errorCenter.DisplayError;

/**
 * The AST display class to show the tree of AST
 * It can be invoked by de-comment 
 * @author duozhao
 *
 */
public class ASTDisplayer implements Visitor<String, Object, DisplayError>{
	private AST ast;
	
	public ASTDisplayer(AST ast){
		this.ast = ast;
	}
	public ASTDisplayer(){
		this.ast = null;
	}
	
	public void showTree() throws DisplayError{
		System.out.println("=========== Caculator AST Display ===========");
		ast.visit(this, "");
		System.out.println("=============================================");
	}
	
	public void showTree(Program ast) throws DisplayError{
		this.ast = ast;
		showTree();
	}

	// procedures to format output
	private void show(String arg, String text) throws DisplayError{
		System.out.println(arg + text);
	}

	private void show(String arg, AST node) throws DisplayError{
		System.out.println(arg + node.toString());
	}

	private String indent(String arg)  throws DisplayError{
		return arg + "  ";
	}

	@Override
	public Object visitProgram(Program prog, String arg) 
			throws DisplayError {
		show(arg, prog);
		StmtList sl = prog.stmtList;
		show(arg,"  StmtlList [" + sl.size() + "]");
		for (Stmt s: prog.stmtList){
			s.visit(this, arg + "  . ");
		}
		return null;
	}

	@Override
	public Object visitAssignStmt(AssignStmt stmt, String arg) 
			throws DisplayError {
		show(arg, stmt);
		stmt.id.visit(this, indent(arg));
		stmt.expr.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitReadStmt(ReadStmt stmt, String arg) 
			throws DisplayError {
		show(arg, stmt);
		stmt.id.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitWriteStmt(WriteStmt stmt, String arg) 
			throws DisplayError {
		show(arg, stmt);
		stmt.expr.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitPostStmt(PostStmt stmt, String arg) 
			throws DisplayError {
		show(arg, stmt);
		stmt.id.visit(this, indent(arg));
		stmt.postOp.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitBinExpr(BinExpr expr, String arg) 
			throws DisplayError {
		show(arg, expr);
		expr.op.visit(this, indent(arg));
		expr.left.visit(this, indent(indent(arg)));
		expr.right.visit(this, indent(indent(arg)));
		return null;
	}

	@Override
	public Object visitRefExpr(RefExpr expr, String arg) 
			throws DisplayError {
		show(arg, expr);
		expr.id.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitLiteralExpr(LiteralExpr expr, String arg) 
			throws DisplayError {
		show(arg, expr);
		expr.literal.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitIdentifier(Identifier id, String arg) 
			throws DisplayError {
		show(arg, "\"" + id.spelling + "\" " + id.toString());
		return null;
	}

	@Override
	public Object visitLiteral(Literal ltr, String arg) 
			throws DisplayError {
		show(arg, "\"" + ltr.spelling + "\" " + ltr.toString());
		return null;
	}

	@Override
	public Object visitOperator(Operator op, String arg) 
			throws DisplayError {
		show(arg, "\"" + op.spelling + "\" " + op.toString());
		return null;
	}
	
}
