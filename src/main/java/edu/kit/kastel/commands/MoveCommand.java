package edu.kit.kastel.commands;

import edu.kit.kastel.game.Board;
import edu.kit.kastel.game.InvalidMoveException;
import edu.kit.kastel.game.Move;
import edu.kit.kastel.game.Vector2D;

import java.util.regex.Pattern;

import static edu.kit.kastel.commands.CommandHandler.ERROR_MESSAGE_NO_ACTIVE_GAME;
import static edu.kit.kastel.commands.PrintCommand.FILE_IDENTIFIER;
import static edu.kit.kastel.commands.PrintCommand.RANK_IDENTIFIER;

public class MoveCommand extends Command {
    // Matches a syntactically correct start command
    private static final Pattern PATTERN = Pattern.compile("^move [a-h][1-8] [a-h][1-8]$");
    private final static String COMMAND_NAME = "move";

    public MoveCommand(CommandHandler commandHandler) {
        super(commandHandler, COMMAND_NAME, PATTERN);
    }

    private Vector2D parseIdentifier(String identifier) {
        int file = identifier.charAt(0) - FILE_IDENTIFIER;
        int rank = identifier.charAt(1) - RANK_IDENTIFIER;

        return new Vector2D(file, Board.SIZE - rank - 1);

    }

    @Override
    public void execute(String[] args) throws InvalidCommandArgumentException {
        if (!commandHandler.getGame().isActive()) {
            throw new InvalidCommandArgumentException(ERROR_MESSAGE_NO_ACTIVE_GAME);
        }

        Move move = new Move(parseIdentifier(args[1]), parseIdentifier(args[2]));
        boolean isMate = false;
        try {
            isMate = commandHandler.getGame().doMove(move);
        } catch (InvalidMoveException e) {
            throw new InvalidCommandArgumentException(e.getRawMessage());
        }

        if (isMate) {
            System.out.println(commandHandler.getGame().getWinner() + " won");
        }
    }
}
