package core;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

public class PlayerHistory {
    private ArrayList<WhistGame.Suit> missedSuits;
    private ArrayList<Card> playedCards;
    public PlayerHistory()
    {
        missedSuits = new ArrayList<WhistGame.Suit>();
        playedCards = new ArrayList<Card>();
    }

    public void addMissedSuit(WhistGame.Suit suit){
        missedSuits.add((WhistGame.Suit) suit);
    }
    public void addPlayedCard(Card card)
    {
        this.playedCards.add(card);
    }
    public boolean shortSuitedIn(WhistGame.Suit suit){
        if (missedSuits.contains(suit)){
            return true;
        }
        return false;
    }
}
