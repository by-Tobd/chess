package edu.kit.kastel.game;

import edu.kit.kastel.game.pieces.Piece;
import edu.kit.kastel.game.pieces.PieceType;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Board {
    public static final int SIZE = 8;

    private final Piece[][] pieces;
    private Vector2D enPassantSquare;

    private final Map<Player, BitSet> attackedFields;

    public Board(StartConfiguration startConfiguration) {
        pieces = new Piece[SIZE][SIZE];
        attackedFields = new HashMap<>();
        for (Player player : Player.values()) {
            attackedFields.put(player, new BitSet(SIZE * SIZE));
        }

        startConfiguration.setupPieces(this);
    }

    private void addIteratively(BitSet set, Vector2D position, Player player, Vector2D step) {
        Vector2D current = position.add(step);
        while (!positionOutOfBounds(current.x(), current.y())) {
            if (getPiece(current) == null || !getPiece(current).getPlayer().equals(player)) {
                setPosition(set, current);
            }

            if (getPiece(current) != null) {
                break;
            }
            current = current.add(step);
        }
    }

    public BitSet getDiagonalAccessibleFields(Vector2D position, Player player) {
        BitSet set = new BitSet(SIZE * SIZE);

        for (Vector2D direction : Direction.getDiagonalVectors()) {
            addIteratively(set, position, player, direction);
        }

        return set;
    }

    public static void setPosition(BitSet set, Vector2D position) {
        set.set(position.x() + position.y() * SIZE);
    }

    public static boolean isPositionSet(BitSet set, Vector2D position) {
        return set.get(position.x() + position.y() * SIZE);
    }

    public BitSet getStraightAccessibleFields(Vector2D position, Player player) {
        BitSet set = new BitSet();

        for (Direction direction : Direction.values()) {
            addIteratively(set, position, player, direction.getVector());
        }
        return set;
    }

    public boolean isFieldAccessible(Vector2D position, Player player) {
        return !positionOutOfBounds(position.x(), position.y()) && (getPiece(position) == null || !getPiece(position).getPlayer().equals(player));
    }

    public boolean isEnPassantSquare(Vector2D position) {
        return position.equals(enPassantSquare);
    }

    public void movePiece(Move move) {
        if (!moveInBounds(move)) {
            return;
        }
        Piece piece = getPiece(move.getStart());

        pieces[move.getStart().y()][move.getStart().x()] = null;
        pieces[move.getTarget().y()][move.getTarget().x()] = piece;

        if (move.getTarget().equals(enPassantSquare)) {
            pieces[move.getTarget().y() - piece.getPlayer().getPawnDirection().getVector().y()][move.getTarget().x()] = null;
        }

        if (piece.getPieceType() == PieceType.PAWN && Math.abs(move.getStart().y() - move.getTarget().y()) == 2) {
            enPassantSquare = move.getStart().add(piece.getPlayer().getPawnDirection().getVector());
        } else {
            enPassantSquare = null;
        }
    }

    public boolean positionOutOfBounds(int x, int y) {
        return x < 0 || x >= SIZE
                || y < 0 || y >= SIZE;
    }

    boolean moveInBounds(Move move) {
        return !positionOutOfBounds(move.getStart().x(), move.getStart().y())
                && !positionOutOfBounds(move.getTarget().x(), move.getTarget().y());
    }

    public void placePiece(Piece piece, int row, int col) {
        pieces[row][col] = piece;
    }

    public Piece getPiece(Vector2D position) {
        return getPiece(position.y(), position.x());
    }

    public Piece getPiece(int row, int col) {
        if (positionOutOfBounds(row, col)) {
            return null;
        }
        return pieces[row][col];
    }
}
