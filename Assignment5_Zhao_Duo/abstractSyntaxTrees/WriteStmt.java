package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class WriteStmt extends Stmt{
	public Expression expr;
	
	public WriteStmt(Expression expr){
		this.expr = expr;
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitWriteStmt(this, o);
	}

}
