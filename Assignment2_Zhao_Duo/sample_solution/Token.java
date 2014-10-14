// Assignment 2 Solution
// Jeremy Erickson
// February 9, 2012

public class Token {
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
        READ_KEYWORD,
        WRITE_KEYWORD,
        INCREMENT,
        DECREMENT,
        EXPONENT,
        EOF
    }

    public Token(Type newType, String newContents) {
        type = newType;
        contents = newContents;
    }

    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        switch (type) {
        case DIV:
            toReturn.append("div");
            break;
        case LPAREN:
            toReturn.append("lparen");
            break;
        case RPAREN:
            toReturn.append("rparen");
            break;
        case PLUS:
            toReturn.append("plus");
            break;
        case MINUS:
            toReturn.append("minus");
            break;
        case TIMES:
            toReturn.append("times");
            break;
        case ASSIGN:
            toReturn.append("assign");
            break;
        case NUMBER:
            toReturn.append("number");
            break;
        case ID:
            toReturn.append("id");
            break;
        case READ_KEYWORD:
        case WRITE_KEYWORD:
            toReturn.append("keyword");
            break;
        case INCREMENT:
            toReturn.append("increment");
            break;
        case DECREMENT:
            toReturn.append("decrement");
            break;
        case EXPONENT:
            toReturn.append("exponent");
            break;
        case EOF:
            toReturn.append("eof");
            break;
        }
        toReturn.append("(");
        toReturn.append(contents);
        toReturn.append(")");
        return toReturn.toString();
    }

    private Type type;
    private String contents;
}
