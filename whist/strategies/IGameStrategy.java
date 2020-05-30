package strategies;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import players.NPCPlayer;
public interface IGameStrategy {
    public Card getLeadCard(NPCPlayer player);
    public Card getTurnCard(NPCPlayer player, Hand trick);
}
