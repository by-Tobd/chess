package edu.kit.kastel.commands;

import java.util.regex.Pattern;

import static edu.kit.kastel.commands.CommandHandler.ERROR_MESSAGE_NO_ACTIVE_GAME;

public class PrintCommand extends Command {
    // Matches a syntactically correct start command
    private static final Pattern PATTERN = Pattern.compile("^print$");
    private final static String COMMAND_NAME = "print";

    public PrintCommand(CommandHandler commandHandler) {
        super(commandHandler, COMMAND_NAME, PATTERN);
    }

    @Override
    public void execute(String[] args) throws InvalidCommandArgumentException {
        if (commandHandler.getGame() == null) {
            throw new InvalidCommandArgumentException(ERROR_MESSAGE_NO_ACTIVE_GAME);
        }
    }
}
