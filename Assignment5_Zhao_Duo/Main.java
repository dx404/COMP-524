import java.io.*;
import java.util.ArrayList;

import syntacticAnalyzer.*;
import executor.*;
import errorCenter.*;
import abstractSyntaxTrees.*;

public class Main {
	BufferedReader sourceReader;
	ArrayList<Token> scannedTokenList;
	Program parsedAST; 

	Scanner scanner;
	Parser parser;
	Exec executor;
	ASTDisplayer astDisplayer;

	boolean showingAST; //This can be set to true to visualize AST
	boolean showingTable; //Enable to show the id table

	boolean interpretProgram(
			BufferedReader sourceReader, 
			boolean showingAST, boolean showingTable) 
					throws CalculatorError{

		this.showingAST = showingAST;
		this.showingTable = showingTable;

		scanner = new Scanner(sourceReader, true);
		scannedTokenList = scanner.getTokenList();

		parser = new Parser(scannedTokenList, true);
		parsedAST = parser.getAST(); 

		if (showingAST){
			parsedAST.showAST();
		}

		executor = new Exec(parsedAST, true);

		if (showingTable){
			executor.printTable();
		}

		return true;
	}

	public static void main(String[] args){
		Main mainCalculator = new Main();
		if (args.length == 0){
			System.out.println("source file to parse must be specified: ");
			System.exit(0);
		}
		try {
			File inputFile = new File(args[0]);
			BufferedReader sourceReader = 
					new BufferedReader(new FileReader(inputFile));
			
			mainCalculator.interpretProgram(sourceReader, false, false);
			//enable them can show AST and the final Id table, respectively

			sourceReader.close();

		}catch (FileNotFoundException fe) {
			System.out.println("The file does not exist. ");
			System.exit(0);
		}catch (IOException e) {
			System.out.println("I/O Error.");
			System.exit(0);
		}catch (CalculatorError e) {
			//e.printStackTrace(); //use for debug only
			System.exit(0);
		}

	}

}
