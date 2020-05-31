package strategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import players.NPCPlayer;

public class LegalStrategy implements IGameStrategy{

    @Override
    public String toString() {
        return "TE10: Legal Strategy v1.";//super.toString();
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
