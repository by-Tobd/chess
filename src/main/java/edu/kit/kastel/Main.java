package edu.kit.kastel;

import edu.kit.kastel.commands.CommandHandler;
import edu.kit.kastel.game.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        CommandHandler handler = new CommandHandler(game);

        handler.handleInput();
    }
}