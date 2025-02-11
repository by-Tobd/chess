package edu.kit.kastel.commands;

import java.util.regex.Pattern;

public class QuitCommand extends Command {
    // Matches a syntactically correct start command
    private static final Pattern PATTERN = Pattern.compile("^quit$");
    private final static String COMMAND_NAME = "quit";

    public QuitCommand(CommandHandler commandHandler) {
        super(commandHandler, COMMAND_NAME, PATTERN);
    }

    @Override
    public void execute(String[] args) {
        commandHandler.quit();
    }
}
