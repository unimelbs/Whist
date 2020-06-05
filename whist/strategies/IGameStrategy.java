package strategies;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import players.NPCPlayer;
public interface  IGameStrategy {
    Card getLeadCard(NPCPlayer player);
    Card getTurnCard(NPCPlayer player, Hand trick);
}
