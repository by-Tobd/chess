package edu.kit.kastel.commands;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * V4:
 * This will act as a base for all commands. It provides an easy way to add more commands to the program,
 * while also providing/enforcing all needed information that a command could have.
 *
 * ### DISCLAIMER: ###
 * As stated in the Main class, this is not the only way of solving this problem. There are many solutions
 * on how to provide information of commands and how to simply add more.
 * The example solutions provided for the exercises may implement such a solution.
 *
 * @author Programmieren-Team
 */
public abstract class Command {
    protected final String commandName;
    protected final CommandHandler commandHandler;
    private final Pattern pattern;

    /* This constructor has two major advantages.
     * First, passing the command handler to the command, allows the quit command to access
     * the command handler directly.
     * Second, each command can access the game without it being passed every time.
     */
    Command (CommandHandler commandHandler, String commandName, Pattern pattern) {
        this.commandHandler = Objects.requireNonNull(commandHandler);
        this.commandName = Objects.requireNonNull(commandName);
        this.pattern = Objects.requireNonNull(pattern);
    }

    public String getCommandName () {
        return commandName;
    }

    protected boolean isValid(String input) {
        return pattern.matcher(input).matches();
    }

    // This method requires passing the game. The command therefore does not need to hold the reference.
    // Though the command handler needs to hold the reference to that object.
    // executeAdvanced is typically better to use.
    public abstract void execute(String[] args) throws InvalidCommandArgumentException;
}


