package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class Literal extends Terminal{

	public Literal(String ls){
		super(ls);
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitLiteral(this, o);
	}

}
