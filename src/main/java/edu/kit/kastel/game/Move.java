package edu.kit.kastel.game;

public class Move {
    private final Vector2D start;
    private final Vector2D target;

    public Move(Vector2D start, Vector2D target) {
        this.start = start;
        this.target = target;
    }

    public Vector2D getStart() {
        return start;
    }

    public Vector2D getTarget() {
        return target;
    }
}
