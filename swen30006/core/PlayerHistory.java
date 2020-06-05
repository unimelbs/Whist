package core;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class PlayerHistory {
    private ArrayList<WhistGame.Suit> missedSuits; // the suits a player is short suited in
    private Hand playedCards;
    private int score;
    private int playerNb;
    public PlayerHistory(Deck deck, int playerNb)
    {
        missedSuits = new ArrayList<WhistGame.Suit>();
        playedCards = new Hand(deck);
        score = 0;
        this.playerNb = playerNb;
    }
    public void addMissedSuit(WhistGame.Suit suit){
        if (! missedSuits.contains(suit)){
            missedSuits.add(suit);
        }
    }
    public void addPlayedCard(Card card)
    {
        playedCards.insert(card,false);

    }
    public void increaseScore(){
        score++;
    }
    public int getScore(){
        return score;
    }
    public Hand getPlayedCards(){
        return playedCards;
    }
    public boolean shortSuitedIn(WhistGame.Suit suit){
        if (missedSuits.contains(suit)){
            return true;
        }
        return false;
    }

    public int getPlayerNb() {
        return playerNb;
    }
}
