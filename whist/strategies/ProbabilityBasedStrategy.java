package strategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import players.NPCPlayer;

public class ProbabilityBasedStrategy implements IGameStrategy {

    @Override
    public String toString() {
        return "TE10: Probability Based Strategy smart v1.";//super.toString();
    }

    @Override
    public Card getLeadCard(NPCPlayer player) {
        return null;
    }

    @Override
    public Card getTurnCard(NPCPlayer player, Hand trick) {
        return null;
    }
}
