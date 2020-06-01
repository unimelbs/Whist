import core.GameFactory;
import core.WhistGame;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

// Whist.java
public class Simulation {

    private ArrayList<Optional<Integer>> winners;

    @Test
    public void WhistTest() throws IOException{
        WhistGame game = GameFactory.getInstance();
        for (int i=0; i<10; i++){
            game.start();
            Optional<Integer> winner = game.getRoundWinner();
            if (winner.isPresent()){
                winners.add(winner);
            }
        }
        System.out.print(winners.toString());
    }

}
