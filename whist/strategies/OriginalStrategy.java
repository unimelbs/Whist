package strategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import players.NPCPlayer;
import java.util.Random;

public class OriginalStrategy implements IGameStrategy {

    // return random Card from Hand
    public Card randomCard(Hand hand, Random random) {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    @Override
    public Card getLeadCard(NPCPlayer player)
    {
        return randomCard(player.getHand(),player.getRandomGenerator());
    }

    @Override
    public Card getTurnCard(NPCPlayer player, Hand trick)
    {
        return randomCard(trick,player.getRandomGenerator());
    }

    @Override
    public String toString() {
        return "TE10: Original Based Strategy smart v1.";//super.toString();
    }
}
