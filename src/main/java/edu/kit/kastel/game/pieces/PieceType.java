package edu.kit.kastel.game.pieces;

import edu.kit.kastel.game.Board;
import edu.kit.kastel.game.Direction;
import edu.kit.kastel.game.Player;
import edu.kit.kastel.game.Vector2D;

import java.util.BitSet;
import java.util.List;

public enum PieceType {
    PAWN('P') {
        @Override
        public BitSet accessiblePositions(Vector2D position, Player player, Board board) {
            BitSet bitSet = new BitSet();
            Vector2D front = position.add(player.getPawnDirection().getVector());
            if (board.getPiece(front) == null && !board.positionOutOfBounds(front.x(), front.y())) {
                Board.setPosition(bitSet, front);
            }

            if (position.y() == ((player == Player.WHITE) ? 6 : 1)) {
                Vector2D twoFront = front.add(player.getPawnDirection().getVector());
                if (board.getPiece(twoFront) == null && !board.positionOutOfBounds(twoFront.x(), twoFront.y())) {
                    Board.setPosition(bitSet, twoFront);
                }
            }

            Vector2D frontLeft = front.add(Direction.LEFT.getVector());
            Vector2D frontRight = front.add(Direction.RIGHT.getVector());

            if ((board.getPiece(frontLeft) != null && board.getPiece(frontLeft).getPlayer() != player) || board.isEnPassantSquare(frontLeft)) {
                Board.setPosition(bitSet, frontLeft);
            }
            if ((board.getPiece(frontRight) != null && board.getPiece(frontRight).getPlayer() != player) || board.isEnPassantSquare(frontRight)) {
                Board.setPosition(bitSet, frontRight);
            }

            return bitSet;
        }
    },
    KNIGHT('N') {
        private static final Vector2D[] KNIGHT_OFFSETS = new Vector2D[] { new Vector2D(2, 1), new Vector2D(2, -1), new Vector2D(-2, 1), new Vector2D(-2, -1), new Vector2D(1, 2), new Vector2D(1, -2), new Vector2D(-1, 2), new Vector2D(-1, -2)};

        @Override
        public BitSet accessiblePositions(Vector2D position, Player player, Board board) {
            BitSet bitSet = new BitSet();
            for (Vector2D offset : KNIGHT_OFFSETS) {
                if (board.isFieldAccessible(position.add(offset), player)) {
                    Board.setPosition(bitSet, position.add(offset));
                }
            }

            return bitSet;
        }
    },
    BISHOP('B') {
        @Override
        public BitSet accessiblePositions(Vector2D position, Player player, Board board) {
            return board.getDiagonalAccessibleFields(position, player);
        }
    },
    ROOK('R') {
        @Override
        public BitSet accessiblePositions(Vector2D position, Player player, Board board) {
            return board.getStraightAccessibleFields(position, player);
        }
    },
    QUEEN('Q') {
        @Override
        public BitSet accessiblePositions(Vector2D position, Player player, Board board) {
            BitSet set = board.getStraightAccessibleFields(position, player);
            set.or(board.getDiagonalAccessibleFields(position, player));
            return set;
        }
    },
    KING('K') {
        @Override
        public BitSet accessiblePositions(Vector2D position, Player player, Board board) {
            BitSet set = new BitSet();
            for (Direction direction : Direction.values()) {
                if (board.isFieldAccessible(position.add(direction.getVector()), player)) {
                    Board.setPosition(set, position.add(direction.getVector()));
                }
            }

            for (Vector2D diagonal : Direction.getDiagonalVectors()) {
                if (board.isFieldAccessible(position.add(diagonal), player)) {
                    Board.setPosition(set, position.add(diagonal));
                }
            }

            return set;
        }
    };

    private final char symbol;

    PieceType(char symbol) {
        this.symbol = symbol;
    }

    public abstract BitSet accessiblePositions(Vector2D position, Player player, Board board);

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
