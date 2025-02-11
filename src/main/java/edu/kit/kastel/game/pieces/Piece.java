package edu.kit.kastel.game.pieces;

import edu.kit.kastel.game.*;

import java.util.BitSet;

public class Piece {
    private final Player player;
    private final PieceType pieceType;
    private Vector2D position;
    private boolean wasMoved = false;

    public Piece(Player player, PieceType pieceType) {
        this.player = player;
        this.pieceType = pieceType;
    }

    public void setPosition(Vector2D position) {
        if (this.position != null) {
            wasMoved = true;
        }
        this.position = position;
    }

    public boolean wasMoved() {
        return wasMoved;
    }

    public Vector2D getPosition() {
        return position;
    }

    public BitSet getAccessibleFields(Board board) {
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
