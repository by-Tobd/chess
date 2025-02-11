package edu.kit.kastel.game;

public class InvalidMoveException extends Exception {
    private static final String PREFIX = "Error: ";
    private final String rawMessage;


    public InvalidMoveException(String message) {
        super(PREFIX + message);
        rawMessage = message;
    }

    public String getRawMessage() {
        return rawMessage;
    }
}
