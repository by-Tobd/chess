package edu.kit.kastel.game;

import edu.kit.kastel.game.pieces.Piece;
import edu.kit.kastel.game.pieces.PieceType;

import java.util.BitSet;
import java.util.List;

public class Game {
    private static final String ERROR_MESSAGE_INVALID_PIECE = "Invalid piece.";
    private static final String ERROR_MESSAGE_INVALID_FIELD = "Invalid field.";
    private static final String ERROR_MESSAGE_INVALID_MOVE = "Invalid move";
    private static final String ERROR_MESSAGE_PIECE_IS_PINNED = "Pinned piece.";
    private static final String ERROR_MESSAGE_CHECK = "You are in check.";

    private boolean isActive = false;
    private Player winner;

    private Player currentPlayer;
    private Board board;


    public void reset() {
        board = new Board(StartConfiguration.CLASSIC);
        isActive = true;
        currentPlayer = Player.WHITE;
    }

    public boolean doMove(Move move) throws InvalidMoveException {
        if (!board.moveInBounds(move)) {
            throw new InvalidMoveException(ERROR_MESSAGE_INVALID_FIELD);
        }

        Piece piece = board.getPiece(move.getStart());
        if (piece == null || piece.getPlayer() != currentPlayer) {
            throw new InvalidMoveException(ERROR_MESSAGE_INVALID_PIECE);
        }
        BitSet accessibleFields = piece.getAccessibleFields(board);

        if (!Board.isPositionSet(accessibleFields, move.getTarget())) {
            throw new InvalidMoveException(ERROR_MESSAGE_INVALID_MOVE);
        }

        if (board.isPiecePinned(piece)) {
            throw new InvalidMoveException(ERROR_MESSAGE_PIECE_IS_PINNED);
        }

        if (piece.getPieceType() != PieceType.KING && board.isInCheck(currentPlayer, move)) {
            throw new InvalidMoveException(ERROR_MESSAGE_CHECK);
        }

        board.movePiece(move);
        nextPlayer();

        if (board.isMate()) {
            winner = piece.getPlayer();
            isActive = false;
        }

        return board.isMate();
    }

    public Player getWinner() {
        return winner;
    }

    private void nextPlayer() {
        currentPlayer = currentPlayer.next();
    }

    public Board getBoard() {
        return board;
    }

    public boolean isActive() {
        return isActive;
    }

}
