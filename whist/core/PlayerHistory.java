package core;

import java.util.ArrayList;

public class PlayerHistory {
    private ArrayList<WhistGame.Suit> missedSuits;
    public PlayerHistory(){
        missedSuits = new ArrayList<WhistGame.Suit>();
    }

    public void addMissedSuit(WhistGame.Suit suit){
        missedSuits.add((WhistGame.Suit) suit);
    }
    public boolean shortSuitedIn(WhistGame.Suit suit){
        if (missedSuits.contains(suit)){
            return true;
        }
        return false;
    }
}
