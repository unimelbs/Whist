// LegalPlayer.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public class InteractablePlayer extends Player{

	private Card selected;

	public InteractablePlayer(int playerNb) {
		super(playerNb);
	}

	@Override
	public void initRound(Hand hand) {
		super.initRound(hand);
		// Set up human player for interaction
		CardListener cardListener = new CardAdapter()  // Human Player plays card
		{
			public void leftDoubleClicked(Card card) { selected = card; hand.setTouchEnabled(false); }
		};
		hand.addCardListener(cardListener);
	}

	@Override
	public Card takeLead() {
		return getCardInput();
	}

	@Override
	public Card takeTurn(Whist.Suit lead) {
		return getCardInput();
	}

	private Card getCardInput(){
		selected = null;
		hand.setTouchEnabled(true);
		UI.getInstance().setStatus("Player 0 double-click on card to lead.");
		while (null == selected) UI.getInstance().delay(100);
		return selected;
	}
}
