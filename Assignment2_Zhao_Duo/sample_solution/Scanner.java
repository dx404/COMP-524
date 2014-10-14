// Assignment 2 Solution
// Jeremy Erickson
// February 9, 2012

import java.util.ArrayList;
import java.io.Reader;
import java.io.IOException;

class Scanner {

// Two public functions: constructor (actually does scanning), and list of
// tokens.
public Scanner(Reader inputStream) throws ScannerException, IOException {
    int nextCharacter = inputStream.read();
    while (nextCharacter != -1) {
        char nextChar = (char)nextCharacter;
        // Add non-whitespace characters to current token.  Automatically reset
        // by reportToken and reportComment methods.
        if (!Character.isWhitespace(nextChar)) {
            currentToken.append(nextChar);
        }
        dfaTransition(nextChar);
        nextCharacter = inputStream.read();
    }
    dfaTransition(' ');
    if (state != START_STATE) {
        errorOut();
    }
}

public ArrayList<Token> getTokens() {
    return tokens;
}

// DFA Implementation Code

private final static int START_STATE = 1;

private int state = START_STATE;

private ArrayList<Token> tokens = new ArrayList<Token>();

StringBuilder currentToken = new StringBuilder();

private void reportToken(Token.Type type) {
    tokens.add(new Token(type, currentToken.toString()));
    // Clear currentToken
    currentToken = new StringBuilder();
}

private void reportComment() {
    // Just clear currentToken
    currentToken = new StringBuilder();
}

private void errorOut() throws ScannerException {
    throw new ScannerException(currentToken.toString());
}

// If given white space, will report a token of the given type and return true.
// Otherwise, returns false.
private boolean handleWhitespaceInFinal(char input, Token.Type type) {
    if (Character.isWhitespace(input)) {
        reportToken(type);
        state = START_STATE;
        return true;
    }
    else {
        return false;
    }
}

// Transitions state of DFA.  Does not maintain token text, but does report
// tokens when they are found.
private void dfaTransition(char input) throws ScannerException {
    switch (state) {
    case 1:
        if (Character.isWhitespace(input)) {
            // Self transition; do nothing.
        }
        else if (input == '/') {
            state = 2;
        }
        else if (input == '(') {
            state = 6;
        }
        else if (input == ')') {
            state = 7;
        }
        else if (input == '+') {
            state = 8;
        }
        else if (input == '-') {
            state = 9;
        }
        else if (input == '*') {
            state = 10;
        }
        else if (input == ':') {
            state = 11;
        }
        else if (input == '.') {
            state = 13;
        }
        else if (Character.isDigit(input)) {
            state = 14;
        }
        else if (Character.isLetter(input)) {
            state = 16;
        }
        else if (input == '^') {
            state = 22;
        }
        else {
            errorOut();
        }
        break;
    case 2:
        if (handleWhitespaceInFinal(input, Token.Type.DIV)) {
            // Handled in function, doing nothing here.
        }
        else if (input == '/') {
            state = 3;
        }
        else if (input == '*') {
            state = 4;
        }
        else {
            errorOut();
        }
        break;
    case 3:
        if (input == '\n') {
            // reportComment is necessary to clear the token string
            reportComment();
            state = START_STATE;
        }
        // No 'else' clause necessary - all non-newline characters legal in
        // comments of this style
        break;
    case 4:
        if (input == '*') {
            state = 5;
        }
        // No 'else' clause necessary - all non-asterisk characters legal in
        // comments of this style
        break;
    case 5:
        if (input == '*') {
            // Do nothing - stay in state 5.
        }
        else if (input == '/') {
            // reportComment is necessary to clear the token string
            reportComment();
            state = START_STATE;
        }
        else {
            state = 4;
        }
        break;
    case 6:
        if (handleWhitespaceInFinal(input, Token.Type.LPAREN)) {
            // Handled in function; do nothing.
        }
        else {
            errorOut();
        }
        break;
    case 7:
        if (handleWhitespaceInFinal(input, Token.Type.RPAREN)) {
            // Handled in function; do nothing.
        }
        else {
            errorOut();
        }
        break;
    case 8:
        if (handleWhitespaceInFinal(input, Token.Type.PLUS)) {
            // Handled in function, do nothing
        }
        else if (input == '+') {
            state = 20;
        }
        else {
            errorOut();
        }
        break;
    case 9:
        if (handleWhitespaceInFinal(input, Token.Type.MINUS)) {
            // Handled in function, do nothing.
        }
        else if (input == '-') {
            state = 21;
        }
        else {
            errorOut();
        }
        break;
    case 10:
        if (handleWhitespaceInFinal(input, Token.Type.TIMES)) {
            // Handled in function, do nothing.
        }
        else {
            errorOut();
        }
        break;
    case 11:
        if (input == '=') {
            state = 12;
        }
        else {
            errorOut();
        }
        break;
    case 12:
        if (handleWhitespaceInFinal(input, Token.Type.ASSIGN)) {
            // Handled in function, do nothing.
        }
        else {
            errorOut();
        }
        break;
    case 13:
        if (Character.isDigit(input)) {
            state = 15;
        }
        else {
            errorOut();
        }
        break;
    case 14:
        if (handleWhitespaceInFinal(input, Token.Type.NUMBER)) {
            // Handled in function, do nothing.
        }
        else if (Character.isDigit(input)) {
            // Self-loop; do nothing.
        }
        else if (input == '.') {
            state = 15;
        }
        else if (input == 'e' || input == 'E') {
            state = 17;
        }
        else {
            errorOut();
        }
        break;
    case 15:
        if (handleWhitespaceInFinal(input, Token.Type.NUMBER)) {
            // Handled in function, do nothing.
        }
        else if (Character.isDigit(input)) {
            // Self-loop; do nothing.
        }
        else if (input == 'e' || input == 'E') {
            state = 17;
        }
        else {
            errorOut();
        }
        break;
    case 16:
        // Must handle whitespace case manually, since we must distinguish
        // between id and keyword.
        if (Character.isWhitespace(input)) {
            if (currentToken.toString().equals("read")) {
                reportToken(Token.Type.READ_KEYWORD);
            }
            else if (currentToken.toString().equals("write")) {
                reportToken(Token.Type.WRITE_KEYWORD);
            }
            else {
                reportToken(Token.Type.ID);
            }
            state = START_STATE;
        }
        else if (Character.isLetterOrDigit(input)) {
            // Self-loop; do nothing.
        }
        else {
            errorOut();
        }
        break;
    case 17:
        if (input == '+' || input == '-') {
            state = 18;
        }
        else if (Character.isDigit(input)) {
            state = 19;
        }
        else {
            errorOut();
        }
        break;
    case 18:
        if (Character.isDigit(input)) {
            state = 19;
        }
        else {
            errorOut();
        }
        break;
    case 19:
        if (handleWhitespaceInFinal(input, Token.Type.NUMBER)) {
            // Handled in function, do nothing.
        }
        else if (Character.isDigit(input)) {
            // Self-loop; do nothing.
        }
        else {
            errorOut();
        }
        break;
    case 20:
        if (handleWhitespaceInFinal(input, Token.Type.INCREMENT)) {
            // Handled in function, do nothing.
        }
        else {
            errorOut();
        }
        break;
    case 21:
        if (handleWhitespaceInFinal(input, Token.Type.DECREMENT)) {
            // Handled in function, do nothing.
        }
        else {
            errorOut();
        }
        break;
    case 22:
        if (handleWhitespaceInFinal(input, Token.Type.EXPONENT)) {
            // Handled in function, do nothing.
        }
        else {
            errorOut();
        }
        break;
    }
}

}
