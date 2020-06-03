package strategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import core.WhistGame;
import players.NPCPlayer;

import java.util.Random;

/**
 * Represents a legal playing logic to be used by NPC Players
 */
public class LegalStrategy implements IGameStrategy{
    // return random Card from Hand
    @Override
    public String toString() {
        return "Legal Strategy v1.";//super.toString();
    }


    @Override
    public Card getLeadCard(NPCPlayer player) {
        int randomCard = player.getRandomGenerator().nextInt(player.getHand().getNumberOfCards());
        return player.getHand().get(randomCard);
    }
    @Override
    public Card getTurnCard(NPCPlayer player, Hand trick) {
        //WhistGame.Suit lead = (WhistGame.Suit) trick.getFirst().getSuit();
        WhistGame.Suit lead = (WhistGame.Suit) trick.getFirst().getSuit();

        int numberOfCardsInSuit = player.getHand().getNumberOfCardsWithSuit(lead);
        int randomCard;

        // if the player has suit, play suit
        if (numberOfCardsInSuit>0){

            randomCard = player.getRandomGenerator().nextInt(numberOfCardsInSuit);
            return player.getHand().getCardsWithSuit(lead).get(randomCard);

        // play a random card
        } else {

            randomCard = player.getRandomGenerator().nextInt(player.getHand().getNumberOfCards());
            return player.getHand().get(randomCard);
        }
    }

}
