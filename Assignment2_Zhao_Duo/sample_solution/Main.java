// Assignment 2 Solution
// Jeremy Erickson
// February 9, 2012

import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.io.IOException;

class Main {
    
public static void main(String[] args) {
    if (args.length < 1) {
        System.out.println("Must specify filename.");
        System.exit(0);
    }
    try {
        Scanner scanner = new Scanner(new FileReader(args[0]));
        ArrayList<Token> tokens = scanner.getTokens();
        Iterator it = tokens.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }
    catch (IOException ex) {
        System.out.println("I/O Error.");
    }
    catch (ScannerException ex) {
        System.out.println("Invalid token starting with " + ex.getToken());
    }
}

}

