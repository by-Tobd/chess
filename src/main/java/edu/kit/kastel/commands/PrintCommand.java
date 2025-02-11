package edu.kit.kastel.commands;

import edu.kit.kastel.game.Board;
import edu.kit.kastel.game.pieces.Piece;

import java.util.StringJoiner;
import java.util.regex.Pattern;

import static edu.kit.kastel.commands.CommandHandler.ERROR_MESSAGE_NO_ACTIVE_GAME;

public class PrintCommand extends Command {
    // Matches a syntactically correct start command
    private static final Pattern PATTERN = Pattern.compile("^print$");
    private final static String COMMAND_NAME = "print";

    private static final String ANSI_BACKGROUND_BLACK = "\u001B[48;2;204;51;0m";
    private static final String ANSI_BACKGROUND_WHITE = "\u001B[48;2;255;179;102m";
    public static final String ANSI_RESET = "\u001B[0m";

    static final char RANK_IDENTIFIER = '1';
    static final char FILE_IDENTIFIER = 'a';
    private static final String PADDING = " ";
    private static final String EMPTY_FIELD = PADDING + PADDING + PADDING;


    public PrintCommand(CommandHandler commandHandler) {
        super(commandHandler, COMMAND_NAME, PATTERN);
    }

    @Override
    public void execute(String[] args) throws InvalidCommandArgumentException {
        if (!commandHandler.getGame().isActive()) {
            throw new InvalidCommandArgumentException(ERROR_MESSAGE_NO_ACTIVE_GAME);
        }

        System.out.println(formatBoard(commandHandler.getGame().getBoard()));
    }

    private String formatBoard(Board board) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        for (int row = 0; row < Board.SIZE; row++) {
            addRank(row, joiner, board);
        }

        addLastRow(joiner);

        return joiner.toString();
    }

    private void addRank(int row, StringJoiner joiner, Board board) {
        StringBuilder builder = new StringBuilder();
        builder.append(Character.toString(RANK_IDENTIFIER + Board.SIZE - row - 1));
        builder.append(PADDING);
        for (int col = 0; col < Board.SIZE; col++) {
            boolean isWhite = (col + row) % 2 == 0;
            builder.append(isWhite ? ANSI_BACKGROUND_WHITE : ANSI_BACKGROUND_BLACK);
            Piece piece = board.getPiece(row, col);;
            if (piece != null) {
                builder.append(PADDING);
                builder.append(piece.getPlayer().getAnsiCode());
                builder.append(piece);
                builder.append(PADDING);
            } else {
                builder.append(EMPTY_FIELD);
            }
            builder.append(ANSI_RESET);
        }
        joiner.add(builder.toString());
    }



    private void addLastRow(StringJoiner joiner) {
        StringBuilder builder = new StringBuilder();
        builder.append(PADDING);
        for (int col = 0; col < Board.SIZE; col++) {
            builder.append(PADDING);
            builder.append(PADDING);
            builder.append(Character.toString(FILE_IDENTIFIER + col));
        }

        joiner.add(builder.toString());
    }
}
