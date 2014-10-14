package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class LiteralExpr extends Expression{
	public Literal literal;
	
	public LiteralExpr(Literal literal){
		this.literal = literal;
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitLiteralExpr(this, o);
	}

}
