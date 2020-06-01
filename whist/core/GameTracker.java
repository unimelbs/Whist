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
    private Hand playedCards;
    private HashMap<Player, ArrayList<Card>> playersHistory;
    private ArrayList<PlayerHistory> playerHistories;

    public GameTracker(Deck deck, int nbPlayers){
        this.playedCards = new Hand(deck);
        this.playerHistories = new ArrayList<PlayerHistory>();
        //TODO TE: I added this one to use the observer pattern
        this.playersHistory = new HashMap<Player,ArrayList<Card>>();
        for (int i = 0; i<nbPlayers; i++){
            playerHistories.add(new PlayerHistory());
        }
    }


    public void addToHistory(Hand trick, int startingPlayer){
        WhistGame.Suit lead = (WhistGame.Suit) trick.getFirst().getSuit();
        // TODO get number of players
        int nbPlayers=4;
        int player=startingPlayer;
        for (Card card : trick.getCardList()){
            // TODO check this equality
            if (! card.getSuit().equals(lead)){
                playerHistories.get(player).addMissedSuit(lead);
            }
            player++;
            if (player==nbPlayers){player=0;} // reset the player to zero
        }
        for (Card card : trick.getCardList()){
            // TODO CHECK WORKING WITH THE CLONE
            playedCards.insert(card.clone(),false);
        }
    }

    /* Determines whether the given card is the highest card remaining
     */
    public int getNumberOfRemainingCardsHigherThan(Card card){
        /* look to see if there is still a unplayed card higher than the inputted card
         * assume card is the highest card the player has in suit
         */
        int numCardsHigherThan = card.getRankId();
        // TODO this is not working (the committed code)
        System.out.println("game history is "+playedCards);
        //for (Card playedCard : playedCards.getCardsWithSuit(card.getSuit())){
        for (Card playedCard : playedCards.getCardList()){
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

        // TODO check this
        int numCardsInSuit = WhistGame.Rank.values().length;
        // TODO check this cast, seems dodgy
        for (Card playedCard : (ArrayList<Card>) playedCards.getCardsWithSuit(suit)){
            numCardsInSuit--;
        }
        return numCardsInSuit;
    }

    public boolean isShortedSuitedIn(int player, WhistGame.Suit suit){
        return playerHistories.get(player).shortSuitedIn(suit);
    }
    public void addPlayedCard(Card card, Player player)
    {
        if(playersHistory.get(player)==null)
        {
            ArrayList<Card> playerCards = new ArrayList<Card>();
            playerCards.add(card);
            playersHistory.put(player,playerCards);
        }
        else
        {
            playersHistory.get(player).add(card);
        }
    }

    //TODO TE: Remove, added for testing
    public String getGameTrackerState()
    {
        String state = "players history has "+this.playersHistory.size()+" entries.";
        return state;
    }
    public void printTrackerState()
    {
        String state="";
        for (Player p: playersHistory.keySet())
        {
            state+=p.getId()+ ": ";
            int i=0;
            for (Card c: playersHistory.get(p))
            {
                state+=i+":"+c.toString()+" ";
                i++;
            }
            state+="\n";
        }
        state+="\n";
        System.out.println(state);
    }

}
