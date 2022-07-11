import controllers.Game;
import org.jsfml.graphics.RenderWindow;

import java.io.IOException;


public class Main
{
    public static void main(String[] args) throws IOException {
        // // Remove all loggers
        // GameLogger.get().setLevel(Level.OFF);
        // // Create Window
        RenderWindow window = new RenderWindow();
        Game mainGame = new Game(window);
        mainGame.start();
    }
}