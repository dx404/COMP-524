
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import syntacticAnalyzer.Parser;
import syntacticAnalyzer.Scanner;
import syntacticAnalyzer.Token;

public class Main {

	public static void main(String[] args) {
		if(args.length == 0){
			System.out.println("source file to parse must be specified: ");
			System.exit(0);
		}


		try {
		//Doing I/O's, get started to connected to the file to be processed.
			File inputFile = new File(args[0]);
			BufferedReader sourceReader = 
					new BufferedReader(new FileReader(inputFile));
            // GRADING: Shouldn't print anything that isn't on the assignment
            // statement.  Fixing for testing.
			// System.out.println("Processing file: " 
			//		+ inputFile.getAbsolutePath());

			Scanner lexer = new Scanner(sourceReader, true);
			ArrayList<Token> scannedTokenList = lexer.getTokenList();

			if (lexer.getStatus() == 1){
				Parser parseTest = new Parser(scannedTokenList, true);
			}else{
				System.out.println("Scanner error: " +
						"Token list has not been ready for parsing");
				System.exit(4);
			}

			sourceReader.close();

		}catch (FileNotFoundException fe) {
			System.out.println("The file does not exist. ");
		}catch (IOException e) {
			System.out.println("I/O Error.");
		}

	}

}
