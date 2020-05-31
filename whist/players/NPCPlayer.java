package players;// LegalPlayer.java

import ch.aplu.jcardgame.*;
import core.UI;
import core.WhistGame;
import strategies.IGameStrategy;

import java.util.Random;


@SuppressWarnings("serial")
public class NPCPlayer extends Player {
	//The strategy specified for the NPC player
	private IGameStrategy strategy;


	public NPCPlayer(int playerNb) {
		super(playerNb);
	}
	public NPCPlayer(int playerNb, Random random) {
		super(playerNb,random);
	}

	@Override
	public Card takeLead() {
		UI.getInstance().setStatus("players.Player " + playerNb + " thinking...");
		UI.getInstance().delay(Player.THINKING_TIME);
		return strategy.getLeadCard(this);
		//return WhistGame.randomCard(hand);
	}

	@Override
	public Card takeTurn(WhistGame.Suit lead) {
		return strategy.getTurnCard(this,hand);
		//Take turn logic should be implemented in the corresponding strategy.
		/*
		if (hand.getNumberOfCardsWithSuit(lead) > 0){
			return hand.getCardsWithSuit(lead).get(WhistGame.random.nextInt(hand.getNumberOfCardsWithSuit(lead)));
		}
		else{
			return hand.get(WhistGame.random.nextInt(hand.getNumberOfCards()));
		}
		 */
	}

	//TODO to review, not so sure if we need this

	public void setStrategy(IGameStrategy strategy)
	{
		this.strategy = strategy;
	}

	public Random getRandomGenerator()
	{
		return random;
	}

}
