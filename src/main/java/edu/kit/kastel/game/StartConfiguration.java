package edu.kit.kastel.game;

import edu.kit.kastel.game.pieces.*;

public enum StartConfiguration {
    CLASSIC {
        private void setMainRow(Board board, Player player, int row) {
            board.placePiece(new Piece(player, PieceType.ROOK), row, 0);
            board.placePiece(new Piece(player, PieceType.KNIGHT), row, 1);
            board.placePiece(new Piece(player, PieceType.BISHOP), row, 2);
            board.placePiece(new Piece(player, PieceType.QUEEN), row, 3);
            board.placePiece(new Piece(player, PieceType.KING), row, 4);
            board.placePiece(new Piece(player, PieceType.BISHOP), row, 5);
            board.placePiece(new Piece(player, PieceType.KNIGHT), row, 6);
            board.placePiece(new Piece(player, PieceType.ROOK), row, 7);
        }

        @Override
        public void setupPieces(Board board) {
            for (int i = 0; i < Board.SIZE; i++) {
                board.placePiece(new Piece(Player.BLACK, PieceType.PAWN), 1, i);
                board.placePiece(new Piece(Player.WHITE, PieceType.PAWN), 6, i);
            }
            setMainRow(board, Player.BLACK, 0);
            setMainRow(board, Player.WHITE, 7);
        }
    };

    public abstract void setupPieces(Board board);
}
