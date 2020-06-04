package core;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import players.Player;

/**
 * Represents a listener to WhistGame play.
 */
public interface IPlayListener {

    void onEndTrick(int startingPlayer, int winningPlayer, Hand trick);
    void onNewRound(WhistGame.Suit trumps);

}
