package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class RefExpr extends Expression{
	
	public Identifier id;
	
	public RefExpr(Identifier id){
		this.id = id;
	}
	
	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitRefExpr(this, o);
	}
	
}
