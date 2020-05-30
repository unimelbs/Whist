package players;// LegalPlayer.java

import ch.aplu.jcardgame.*;
import core.UI;
import core.WhistGame;
import strategies.IGameStrategy;


@SuppressWarnings("serial")
public class NPCPlayer extends Player {
	//The strategy specified for the NPC player
	private IGameStrategy strategy;


	public NPCPlayer(int playerNb) {
		super(playerNb);
	}

	@Override
	public Card takeLead() {
		UI.getInstance().setStatus("players.Player " + playerNb + " thinking...");
		UI.getInstance().delay(Player.THINKING_TIME);
		return WhistGame.randomCard(hand);
	}

	@Override
	public Card takeTurn(WhistGame.Suit lead) {
		if (hand.getNumberOfCardsWithSuit(lead) > 0){
			return hand.getCardsWithSuit(lead).get(WhistGame.random.nextInt(hand.getNumberOfCardsWithSuit(lead)));
		}
		else{
			return hand.get(WhistGame.random.nextInt(hand.getNumberOfCards()));
		}
	}

	public void setStrategy(IGameStrategy strategy)
	{
		this.strategy = strategy;
	}
}
