// LegalPlayer.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public class LegalPlayer extends Player{

	public LegalPlayer(int playerNb) {
		super(playerNb);
	}

	@Override
	public Card takeLead() {
		UI.getInstance().setStatus("Player " + playerNb + " thinking...");
		UI.getInstance().delay(THINKING_TIME);
		return Whist.randomCard(hand);
	}

	@Override
	public Card takeTurn(Whist.Suit lead) {
		if (hand.getNumberOfCardsWithSuit(lead) > 0){
			return hand.getCardsWithSuit(lead).get(Whist.random.nextInt(hand.getNumberOfCardsWithSuit(lead)));
		}
		else{
			return hand.get(Whist.random.nextInt(hand.getNumberOfCards()));
		}
	}


}
