package abstractSyntaxTrees;

public abstract class Terminal extends AST{
	public String spelling;
	public Terminal(String s){
		spelling = s;
	}
}
