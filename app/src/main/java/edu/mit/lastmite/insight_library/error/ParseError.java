package edu.mit.lastmite.insight_library.error;

public class ParseError extends RuntimeException {
    public ParseError(String message) {
        super(message);
    }
}