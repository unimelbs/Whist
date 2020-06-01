package players;// LegalPlayer.java

import ch.aplu.jcardgame.*;
import core.GameTracker;
import core.UI;
import core.WhistGame;
import strategies.IGameStrategy;

import java.util.Random;


@SuppressWarnings("serial")
public class NPCPlayer extends Player {
	//The strategy specified for the NPC player
	private IGameStrategy strategy;
	private WhistGame.Suit trump = null;
	public NPCPlayer(int playerNb) {
		super(playerNb);

	}
	public NPCPlayer(int playerNb, Random random) {
		super(playerNb,random);
	}

	@Override
	public Card takeLead() {
		return strategy.getLeadCard(this);
	}

	@Override
	public Card takeTurn(Hand trick) {
		// TODO remove
		return strategy.getTurnCard(this,trick);
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

	public void setTrump(WhistGame.Suit trump){
		this.trump = trump;
	}

	// TODO do we need to create a copy?
	public WhistGame.Suit getTrump(){
		return trump;
	}

	public void think(){
		UI.getInstance().setStatus("players.Player " + playerNb + " thinking...");
		UI.getInstance().delay(Player.THINKING_TIME);
	}
}
