// Assignment 2 Solution
// Jeremy Erickson
// February 9, 2012

class ScannerException extends Exception {
    ScannerException(String badToken) {
        token = badToken;
    }

    public String getToken() {
        return token;
    }

    private String token;
}
