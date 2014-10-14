package abstractSyntaxTrees;

import errorCenter.CalculatorError;
import errorCenter.DisplayError;

public abstract class AST {

	public String toString() {
		String fullClassName = this.getClass().getName();
		String cn = fullClassName.substring(1 
				+ fullClassName.lastIndexOf('.'));
		return cn;
	}

	public abstract <A,R,E extends CalculatorError> 
		R visit(Visitor<A,R,E> v, A o) throws E;
	
	public void showAST() throws DisplayError{
		new ASTDisplayer(this).showTree();
	}

}
