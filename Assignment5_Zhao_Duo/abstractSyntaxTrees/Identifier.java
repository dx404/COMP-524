package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class Identifier extends Terminal{
	
	public Identifier(String spelling){
		super(spelling);
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitIdentifier(this, o);
	}
}
