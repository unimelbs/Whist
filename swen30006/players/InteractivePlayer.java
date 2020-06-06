package players;// LegalPlayer.java

import ch.aplu.jcardgame.*;
import core.UI;

/**
 * Represents an Interactive player (a human player) that provides input to the game.
 */
@SuppressWarnings("serial")
public class InteractivePlayer extends Player {

	private Card selected;

	public InteractivePlayer(int playerNb) {
		super(playerNb);
	}

	@Override
	public void initRound(Hand hand) {
		super.initRound(hand);
		// Set up human player for interaction
		CardListener cardListener = new CardAdapter()  // Human players.Player plays card
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
	public Card takeTurn(Hand trick) {return getCardInput(); }


	private Card getCardInput(){
		selected = null;
		hand.setTouchEnabled(true);
		UI.getInstance().setStatus("Double-click on a card to play it.");
		while (null == selected) UI.getInstance().delay(100);
		return selected;
	}

	@Override
	public String toString() {
		return "Player["+this.playerNb+"](Human) uses (no strategy).";
	}
}
