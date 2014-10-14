package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class BinExpr extends Expression{
	public Expression left;
	public Operator op;
	public Expression right;
	
	public BinExpr(Expression left, Operator op, Expression right){
		this.left = left;
		this.op = op;
		this.right = right;
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitBinExpr(this, o);
	}

}
