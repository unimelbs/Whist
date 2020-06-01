package core;

import ch.aplu.jcardgame.Card;
import players.Player;

public interface IPlayListener {
    void onCardPlayed(Card card, Player player);
}
