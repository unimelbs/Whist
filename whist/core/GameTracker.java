package core;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import players.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class GameTracker {
    private Deck deck;
    private WhistGame.Suit currentTrump;
    private int nbPlayers;
    private ArrayList<PlayerHistory> playerHistories;


    public GameTracker(Deck deck, int nbPlayers){
        //Initialises GameTracker instance variables
        this.deck = deck;
        this.nbPlayers = nbPlayers;
        this.playerHistories = new ArrayList<PlayerHistory>();
        for (int i = 0; i<nbPlayers; i++){
            playerHistories.add(new PlayerHistory(deck, i));
        }
    }


    public void addToHistory(Hand trick, int startingPlayer, int winningPlayer){
        WhistGame.Suit lead = (WhistGame.Suit) trick.getFirst().getSuit();
        playerHistories.get(winningPlayer).increaseScore();
        int player=startingPlayer;
        for (Card card : trick.getCardList()){
            if (! card.getSuit().equals(lead)){
                playerHistories.get(player).addMissedSuit(lead);
            }
            playerHistories.get(player).addPlayedCard(card.clone());
            player++;
            if (player==nbPlayers){player=0;} // reset the player to zero
        }
    }

    /* Determines whether the given card is the highest card remaining
     */
    public int getNumberOfRemainingCardsHigherThan(Card card){
        /* look to see if there is still a un-played card higher than the inputted card
         * assume card is the highest card the player has in suit
         */
        int numCardsHigherThan = card.getRankId();
        for (Card playedCard : getTotalPlayedCards().getCardsWithSuit((WhistGame.Suit)card.getSuit())){
            if (card.getSuit().equals(playedCard.getSuit()) && playedCard.getRankId()<card.getRankId()){
                numCardsHigherThan--;
            }
            if (numCardsHigherThan == 0){
                break;
            }
        }
        return numCardsHigherThan;
    }
    public int getNumberOfCardsRemainingInSuit(WhistGame.Suit suit){

        int numCardsInSuit = WhistGame.Rank.values().length;
        for (Card playedCard : getTotalPlayedCards().getCardsWithSuit(suit)){
            numCardsInSuit--;
        }
        return numCardsInSuit;
    }

    public boolean isShortedSuitedIn(int player, WhistGame.Suit suit){
        return playerHistories.get(player).shortSuitedIn(suit);
    }


    public Hand getTotalPlayedCards(){
        Hand playedCards = new Hand(deck);
        for (PlayerHistory player : playerHistories){
            for (Card card : player.getPlayedCards().getCardList()){
                playedCards.insert(card.clone(), false);
            }
        }
        return playedCards;
    }


    /**
     * Gets the ID of the current Winner (player with highest score).
     *
     * @return
     */
    public int getCurrentWinnerId()
    {
        int highestScore = 0;
        int winningId=0;
        for (PlayerHistory player : playerHistories){
            if (player.getScore()>highestScore){
                highestScore=player.getScore();
                winningId=player.getPlayerNb();
            }
        }
        return winningId;
    }

    //Getters
    public WhistGame.Suit getCurrentTrump(){return this.currentTrump;}

    public void newRound(WhistGame.Suit currentTrump){
        this.currentTrump = currentTrump;
        this.playerHistories = new ArrayList<PlayerHistory>();
        for (int i = 0; i<nbPlayers; i++){
            playerHistories.add(new PlayerHistory(deck, i));
        }
    }
}
