package errorCenter;

public abstract class CalculatorError extends Exception{

	private static final long serialVersionUID = 2469713939091765163L;
	
	public CalculatorError(String msg){
		super(msg);
	}
	public CalculatorError(){
		super();
	}


}
