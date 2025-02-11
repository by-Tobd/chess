package edu.kit.kastel.game;

public enum Player {
    BLACK("\u001B[30m", Direction.DOWN), WHITE("\u001B[97m", Direction.UP);

    private final String ansiCode;
    private final Direction pawnDirection;

    Player(String ansiCode, Direction direction) {
        this.ansiCode = ansiCode;
        pawnDirection = direction;
    }

    public String getAnsiCode() {
        return ansiCode;
    }

    public Player next() {
        return Player.values()[(ordinal() + 1) % Player.values().length];
    }

    public Direction getPawnDirection() {
        return pawnDirection;
    }
}
