package edu.kit.kastel.game.pieces;

import edu.kit.kastel.game.*;

import java.util.BitSet;
import java.util.List;

public class Piece {
    private final Player player;
    private final PieceType pieceType;

    public Piece(Player player, PieceType pieceType) {
        this.player = player;
        this.pieceType = pieceType;
    }

    public BitSet getAccessibleFields(Vector2D position, Board board) {
        return pieceType.accessiblePositions(position, player, board);
    }

    public Player getPlayer() {
        return player;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public String toString() {
        return String.valueOf(pieceType);
    }
}
