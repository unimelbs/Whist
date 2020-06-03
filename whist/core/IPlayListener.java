package core;

import ch.aplu.jcardgame.Card;
import players.Player;

/**
 * Represents a listener to WhistGame play.
 */
public interface IPlayListener {
    void onCardPlayed(Card card, Player player);
    void onTrickWon(Player player);
    void onTrumpChange(WhistGame.Suit trump);
}
