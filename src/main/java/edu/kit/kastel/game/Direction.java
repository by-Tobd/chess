package edu.kit.kastel.game;

public enum Direction {
    UP(new Vector2D(0, -1)),
    DOWN(new Vector2D(0, 1)),
    LEFT(new Vector2D(-1, 0)),
    RIGHT(new Vector2D(1, 0));

    private final Vector2D vector;
    Direction(Vector2D vector) {
        this.vector = vector;
    }

    public Vector2D getVector() {
        return vector;
    }

    public static Vector2D[] getDiagonalVectors() {
        return new Vector2D[] {
                UP.vector.add(LEFT.vector),
                UP.vector.add(RIGHT.vector),
                DOWN.vector.add(LEFT.vector),
                DOWN.vector.add(RIGHT.vector)
        };
    }
}
