import core.GameFactory;
import core.WhistGame;
import java.io.IOException;

// Whist.java
public class Whist {
    public static void main(String[] args) throws IOException {
        WhistGame game = GameFactory.getInstance().getWhistGame();
        game.start();
    }
}
