package errorCenter;

public class ParseError extends CalculatorError{

	private static final long serialVersionUID = 7443013825108232489L;

	public ParseError(String msg){
		super(msg);
	}
	public ParseError(){
		super();
	}

}
