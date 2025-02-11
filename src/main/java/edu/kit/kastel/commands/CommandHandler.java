package edu.kit.kastel.commands;

import edu.kit.kastel.game.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * V5:
 * We have now copied the loop of V4 in the V4Instance class into it's own class to further separate between
 * the entry point of the program, the model and the commands. This class will do just the same.
 *
 * @author Programmieren-Team
 */
public class CommandHandler {
    private static final String ARGUMENT_SEPARATOR = " ";

    static final String ERROR_MESSAGE_NO_ACTIVE_GAME = "There is no active game.";

    private final Game game;
    private final Map<String, Command> commands = new HashMap<>();
    private boolean quitting;

    public CommandHandler(Game game) {
        this.game = Objects.requireNonNull(game);
        initCommands();
        quitting = false;
    }

    private void initCommands() {
        addCommand(new StartCommand(this));
        addCommand(new PrintCommand(this));
        addCommand(new QuitCommand(this));
        addCommand(new MoveCommand(this));

    }

    private void addCommand(Command command) {
        this.commands.put(command.getCommandName(), command);
    }

    // Due to the handler being its own class, we can now remove the quitting variable in the Main class.
    public void quit() {
        this.quitting = true;
    }

    public Game getGame() {
        return game;
    }

    public void handleInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (!quitting && scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String[] args = input.split(ARGUMENT_SEPARATOR);

                if (!commands.containsKey(args[0])) {
                    System.out.println("Unknown command: " + args[0]);
                    continue;
                }
                Command command = commands.get(args[0]);
                if (!command.isValid(input)) {
                    System.err.println("Invalid arguments for " + command.getCommandName());
                    continue;
                }

                try {
                    command.execute(args);
                    System.out.println("Ok");
                } catch (InvalidCommandArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
