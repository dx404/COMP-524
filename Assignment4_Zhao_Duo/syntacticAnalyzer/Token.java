package syntacticAnalyzer;

public class Token {
	public Type type;
	public String spelling; 
	public String name;
	public int state;
	
	boolean isValid;
	
	public enum Type {
		DIV,
		LPAREN,
		RPAREN,
		PLUS,
		MINUS,
		TIMES,
		ASSIGN,
		NUMBER,
		ID,
		KEYWORD,
		READ,
		WRITE,
		POST_INCREMENT,
		POST_DECREMENT,
		EXPONENT,
		ERROR,
		EOF,
		ADD_OP,
		MULT_OP,
		INITIAL,
		IN_TRANSIT,
		COMMENT,
		WHITESPACE
	}

	
	public Token(Type type, String spelling, String name, int tokenState) {
		this.type = type;
		this.spelling = spelling;
		this.name = name;
		this.state = tokenState;
	}
	
	public static final String[] keywords = 
		{"read", "write"};

	public String toString() {
		return name + '(' + spelling +')';
	}


}

