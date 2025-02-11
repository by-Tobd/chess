package edu.kit.kastel.commands;

/**
 * V5:
 * This exception is used to symbolize a syntax error within the commands' execution.
 *
 * @author Programmieren-Team
 */
public class InvalidCommandArgumentException extends Exception {

    private static final String MESSAGE = "Error: ";

    public InvalidCommandArgumentException(String cause) {
        super(MESSAGE + cause);
    }
}

