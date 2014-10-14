package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class PostStmt extends Stmt{
	public Identifier id;
	public Operator postOp;
	
	public PostStmt(Identifier id, Operator postOp){
		this.id = id;
		this.postOp = postOp;
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitPostStmt(this, o);
	}

}
