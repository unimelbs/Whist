package strategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import core.WhistGame;
import players.NPCPlayer;

public class SmartStrategy implements IGameStrategy {

    @Override
    public String toString()
    {
        return "Probability Based Smart Strategy v1.";
    }

    @Override
    public Card getLeadCard(NPCPlayer player)
    {
        for (WhistGame.Suit suit : WhistGame.Suit.values()){
            Card card = getTopCardInSuit(player.getHand(),suit);
            boolean canTrump=false;
            if (card!=null && player.getGameHistory().getNumberOfRemainingCardsHigherThan(card)==0){
                int otherPlayer = player.getId()+1;
                for (int i=0; i<WhistGame.nbPlayers-1; i++){
                    if (otherPlayer==WhistGame.nbPlayers){otherPlayer=0;} // reset the player

                    if (player.getGameHistory().isShortedSuitedIn(otherPlayer, (WhistGame.Suit) card.getSuit(), player.getHand())
                        && !player.getGameHistory().isShortedSuitedIn(otherPlayer, player.getGameHistory().getCurrentTrump(), player.getHand())) {
                        canTrump = true;
                        break;
                    }
                    otherPlayer++;
                }
                if (!canTrump) {
                    return card;
                }
            }
        }
        return discard(player.getHand(),player.getGameHistory().getCurrentTrump());
    }

    @Override
    public Card getTurnCard(NPCPlayer player, Hand trick) {

        WhistGame.Suit lead = getLeadSuit(trick);
        Card winningCard = getWinningCard(trick,player.getGameHistory().getCurrentTrump());
        // if player has suit
        if (player.getHand().getNumberOfCardsWithSuit(lead)>0){
            // if trick has not been trumped, player has a better card than winning card play the top card in lead suit
            if (winningCard.getSuit().equals(lead) && getTopCardInSuit(player.getHand(),lead).getRankId()<winningCard.getRankId()){
                return getTopCardInSuit(player.getHand(),lead);
            } else {
                return getLowestCardInSuit(player.getHand(),lead);
            }

        // try to trump
        } else {
            if (player.getHand().getNumberOfCardsWithSuit(player.getGameHistory().getCurrentTrump()) > 0){
                return trump(player.getHand(), player.getGameHistory().getCurrentTrump());
            } else {
                return discard(player.getHand(),player.getGameHistory().getCurrentTrump());
            }
        }
    }

    private Card trump(Hand hand, WhistGame.Suit trumps) {
        return getLowestCardInSuit(hand,trumps);
    }


    // finds the lead suit in a trick
    private WhistGame.Suit getLeadSuit(Hand trick){
        return (WhistGame.Suit) trick.getFirst().getSuit();
    }

    // finds the winning card in the trick
    private Card getWinningCard(Hand trick, WhistGame.Suit trumps){
        // if a trump has been played the winning card is the top trump
        if (getTopCardInSuit(trick, trumps) != null) {
            return getTopCardInSuit(trick, trumps);

        // with winning card is the top card in leading suit
        } else {
            return getTopCardInSuit(trick,getLeadSuit(trick));
        }
    }

    // finds the top card in a suit
    private Card getTopCardInSuit(Hand hand, WhistGame.Suit suit){
        if (hand.getNumberOfCardsWithSuit(suit)>0){
            int topRankId= 14;
            Card topCard=null;
            for (Card card : hand.getCardsWithSuit(suit)){
                if (card.getRankId()<topRankId){
                    topRankId = card.getRankId();
                    topCard=card;
                }
            }
            return topCard;
        } else {
            return null;
        }
    }
    // finds the lowest card in a suit
    private Card getLowestCardInSuit(Hand hand, WhistGame.Suit suit){
        if (hand.getNumberOfCardsWithSuit(suit)>0){
            int lowestRankId= -1;
            Card worstCard=null;
            for (Card card : hand.getCardsWithSuit(suit)){
                if (card.getRankId()>lowestRankId){
                    lowestRankId = card.getRankId();
                    worstCard=card;
                }
            }
            return worstCard;
        } else {
            return null;
        }
    }
    private Card discard(Hand hand, WhistGame.Suit trumps){
        int lowRankId=-1;
        Card badCard=null;
        // first try to discard worse non trump
        for (WhistGame.Suit suit: WhistGame.Suit.values()){
            if (!suit.equals(trumps)){
                Card card = getLowestCardInSuit(hand, suit);
                if ( card != null && card.getRankId()>lowRankId){
                    lowRankId=card.getRankId();
                    badCard = card;
                }
            }
        }
        if (badCard != null){
            return badCard;
        }
        return getLowestCardInSuit(hand, trumps);
    }
}
