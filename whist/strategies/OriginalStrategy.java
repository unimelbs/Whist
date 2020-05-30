package strategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import players.NPCPlayer;

public class OriginalStrategy implements IGameStrategy {

    @Override
    public Card getLeadCard(NPCPlayer player) {
        return null;
    }

    @Override
    public Card getTurnCard(NPCPlayer player, Hand trick) {
        return null;
    }
}