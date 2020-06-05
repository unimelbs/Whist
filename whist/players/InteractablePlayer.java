package players;// LegalPlayer.java

import ch.aplu.jcardgame.*;
import core.UI;
import core.WhistGame;

@SuppressWarnings("serial")
public class InteractablePlayer extends Player {

	private Card selected;

	public InteractablePlayer(int playerNb) {
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
}
