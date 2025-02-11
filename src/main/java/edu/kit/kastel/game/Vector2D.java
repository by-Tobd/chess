package edu.kit.kastel.game;

public record Vector2D(int x, int y) {


    public Vector2D subtract(Vector2D vector) {
        return new Vector2D(x - vector.x, y - vector.y);
    }

    public Vector2D add(Vector2D vector) {
        return new Vector2D(x + vector.x, y + vector.y);
    }

    public Vector2D signum() {
        return new Vector2D(Integer.signum(x), Integer.signum(y));
    }
}
