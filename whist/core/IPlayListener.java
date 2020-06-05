package core;

import ch.aplu.jcardgame.Hand;

/**
 * Represents a listener to WhistGame play.
 */
public interface IPlayListener {

    void onEndTrick(int startingPlayer, int winningPlayer, Hand trick);
    void onNewRound(WhistGame.Suit trumps);

}
