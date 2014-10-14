package abstractSyntaxTrees;

import errorCenter.CalculatorError;

public class Program extends AST{
	public StmtList stmtList;
	
	public Program(StmtList stmtList){
		this.stmtList = stmtList;
	}

	@Override
	public <A, R, E extends CalculatorError> R visit(Visitor<A, R, E> v, A o)
			throws E {
		return v.visitProgram(this, o);
	}

}
