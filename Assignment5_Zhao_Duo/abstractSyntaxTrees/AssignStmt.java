package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class AssignStmt extends Stmt{
	public Identifier id;
	public Expression expr;

	public AssignStmt(Identifier id, Expression expr){
		this.id = id;
		this.expr = expr;
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o) 
			throws E {
		return v.visitAssignStmt(this, o);
	}

}
