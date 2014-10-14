package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class ReadStmt extends Stmt{

	public Identifier id;

	public ReadStmt(Identifier id){
		this.id = id;
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitReadStmt(this, o);
	}

}
