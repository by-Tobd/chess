package edu.kit.kastel.game;

import edu.kit.kastel.game.pieces.Piece;
import edu.kit.kastel.game.pieces.PieceType;

import java.util.*;

public class Board {
    public static final int SIZE = 8;

    private final Piece[][] pieces;
    private Vector2D enPassantSquare;
    private boolean isMate = false;

    private final Map<Player, BitSet> attackedFields;
    private final Map<Player, List<Piece>> ownedPieces;

    public Board(StartConfiguration startConfiguration) {
        pieces = new Piece[SIZE][SIZE];
        attackedFields = new HashMap<>();
        ownedPieces = new HashMap<>();
        for (Player player : Player.values()) {
            attackedFields.put(player, new BitSet(SIZE * SIZE));
            ownedPieces.put(player, new ArrayList<>());
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

    private Piece getKing(Player player) {
        for (Piece candidate : ownedPieces.get(player)) {
            if (candidate.getPieceType() == PieceType.KING) {
                return candidate;
            }
        }
        return null;
    }

    public boolean isInCheck(Player player, Move move) {
        Piece king = getKing(player);
        if (king == null) {
            return false;
        }

        for (Player other : Player.values()) {
            if (other == player) continue;
            if (Board.isPositionSet(attackedFields.get(other), king.getPosition())) {
                for (Piece piece : ownedPieces.get(other)) {
                    BitSet accessibleFields = piece.getAccessibleFields(this);
                    if (Board.isPositionSet(accessibleFields, king.getPosition())) {
                        if (piece.getPieceType() == PieceType.KNIGHT || piece.getPieceType() == PieceType.PAWN) {
                            return true;
                        }
                        Vector2D delta = piece.getPosition().subtract(king.getPosition()).signum();
                        Vector2D current = king.getPosition().add(delta);
                        boolean blocked = false;
                        while (!current.equals(piece.getPosition())) {
                            if (getPiece(current) != null) {
                                blocked = true;
                                continue;
                            }
                            current = current.add(delta);
                        }
                        if (!blocked) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isPiecePinned(Piece piece) {
        if (piece.getPieceType() == PieceType.KING) return false;

        Piece king = getKing(piece.getPlayer());
        if (king == null) {
            return false;
        }

        Vector2D delta = piece.getPosition().subtract(king.getPosition()).signum();
        Vector2D current = piece.getPosition().add(delta);
        while (getPiece(current) == null && !positionOutOfBounds(current.x(), current.y())) {
            current = current.add(delta);
        }
        Piece target = getPiece(current);

        if (Math.abs(delta.x()) == Math.abs(delta.y())) {
            return target != null && target.getPlayer() != piece.getPlayer() && (target.getPieceType() == PieceType.BISHOP || target.getPieceType() == PieceType.QUEEN);
        } else if (delta.x() == 0 || delta.y() == 0) {
            return target != null && target.getPlayer() != piece.getPlayer() && (target.getPieceType() == PieceType.ROOK || target.getPieceType() == PieceType.QUEEN);
        }
        return false;
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

    public boolean isMate() {
        return isMate;
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
        Piece target = getPiece(move.getTarget());

        if (target != null) {
            target.setPosition(null);
        }
        piece.setPosition(move.getTarget());

        pieces[move.getStart().y()][move.getStart().x()] = null;
        pieces[move.getTarget().y()][move.getTarget().x()] = piece;

        if (piece.getPieceType() == PieceType.KING && move.getDistance() > 1) {
            Piece rook;
            Vector2D delta = move.getTarget().subtract(move.getStart()).signum();
            Vector2D current = move.getStart().add(delta);
            while (true) {
                if (getPiece(current) != null && getPiece(current).getPieceType() == PieceType.ROOK) {
                    rook = getPiece(current);
                    break;
                }
                current = current.add(delta);
            }

            pieces[rook.getPosition().y()][rook.getPosition().x()] = null;
            Vector2D newPosition = move.getStart().add(delta);

            pieces[newPosition.y()][newPosition.x()] = rook;
            rook.setPosition(newPosition);
        }

        if (move.getTarget().equals(enPassantSquare)) {
            Vector2D piecePosition = enPassantSquare.subtract(piece.getPlayer().getPawnDirection().getVector());
            getPiece(piecePosition).setPosition(null);
            pieces[piecePosition.y()][piecePosition.x()] = null;
        }

        if (piece.getPieceType() == PieceType.PAWN && Math.abs(move.getStart().y() - move.getTarget().y()) == 2) {
            enPassantSquare = move.getStart().add(piece.getPlayer().getPawnDirection().getVector());
        } else {
            enPassantSquare = null;
        }
        updateAttackedFields();
    }

    public BitSet getAttackedFields(Player player) {
        return attackedFields.get(player);
    }

    private void updateAttackedFields() {
        for (Player player : Player.values()) {
            BitSet set = new BitSet(SIZE * SIZE);
            for (Piece piece : ownedPieces.get(player)) {
                if (piece.getPosition() == null) {
                    continue;
                }
                set.or(piece.getAccessibleFields(this));
            }
            attackedFields.put(player, set);
        }

        for (Player player : Player.values()) {
            Piece king = getKing(player);
            if (king == null) continue;

            for (Player other : Player.values()) {
                if (other == player) continue;

                if (Board.isPositionSet(attackedFields.get(other), king.getPosition())) {
                    if (isCheckMate(king, other)) {
                        isMate = true;
                        return;
                    }
                }
            }
        }
    }

    private boolean isCheckMate(Piece king, Player attacker) {
        if (king.getPieceType() != PieceType.KING) return false;

        BitSet accessibleFields = king.getAccessibleFields(this);
        BitSet both = new BitSet();
        both.or(accessibleFields);
        both.andNot(attackedFields.get(attacker));

        if (!both.isEmpty()) {
            return false;
        }

        for (Piece target : ownedPieces.get(attacker)) {
            if (!Board.isPositionSet(target.getAccessibleFields(this), king.getPosition())) continue;

            //TODO: Check how many different moves are needed to defend
            boolean blocked = false;
            outer: for (Piece piece : ownedPieces.get(king.getPlayer())) {
                if (Board.isPositionSet(piece.getAccessibleFields(this), target.getPosition())) {
                    blocked = true;
                    break;
                }

                if (!target.getAccessibleFields(this).intersects(piece.getAccessibleFields(this))) {
                    continue;
                }
                Vector2D delta = target.getPosition().subtract(king.getPosition()).signum();
                Vector2D current = king.getPosition().add(delta);
                while (!current.equals(target.getPosition())) {
                    if (Board.isPositionSet(piece.getAccessibleFields(this), current)) {
                        blocked = true;
                        break outer;
                    }
                    current = current.add(delta);
                }

            }
            if (!blocked) {
                return true;
            }
        }
        return false;
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
        ownedPieces.get(piece.getPlayer()).add(piece);
        piece.setPosition(new Vector2D(col, row));
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
