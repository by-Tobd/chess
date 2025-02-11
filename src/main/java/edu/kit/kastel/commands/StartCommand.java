package edu.kit.kastel.commands;

import java.util.regex.Pattern;

public class StartCommand extends Command {
    // Matches a syntactically correct start command
    private static final Pattern PATTERN = Pattern.compile("^start$");
    private final static String COMMAND_NAME = "start";

    public StartCommand(CommandHandler commandHandler) {
        super(commandHandler, COMMAND_NAME, PATTERN);
    }

    @Override
    public void execute(String[] args) {
        commandHandler.getGame().reset();
    }
}

