package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class Operator extends Terminal{
	public Operator(String spelling){
		super(spelling);
	}

	public boolean equals(String op){
		return spelling.contentEquals(op);
	}
	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitOperator(this, o);
	}

}
