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
	public NPCPlayer(int playerNb) {
		super(playerNb);

	}
	public NPCPlayer(int playerNb, Random random) {
		super(playerNb,random);
	}

	@Override
	public Card takeLead() {
		think();
		return strategy.getLeadCard(this);
	}

	@Override
	public Card takeTurn(Hand trick) {
		think();
		return strategy.getTurnCard(this,trick);
	}

	private void think(){
		UI.getInstance().setStatus("Player " + playerNb + " thinking...");
		UI.getInstance().delay(Player.THINKING_TIME);
	}

	public Random getRandomGenerator()
	{
		return random;
	}

	//public WhistGame.Suit getTrumps(){
	//	return this.getGameHistory().getCurrentTrump();
	//}
	public void setStrategy(IGameStrategy strategy){
		this.strategy = strategy;
	}

}
