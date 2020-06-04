package players;// LegalPlayer.java

import ch.aplu.jcardgame.*;
import core.GameTracker;
import core.IPlayListener;
import core.WhistGame;

import java.util.*;

@SuppressWarnings("serial")
public abstract class Player implements IPlayListener {

	protected Hand hand;
	protected Random random;
	protected int playerNb;
	private GameTracker gameHistory;
	protected static final int THINKING_TIME = 2000;

	public Player(int playerNb){
		this.playerNb = playerNb;
		gameHistory = new GameTracker(WhistGame.deck,WhistGame.nbPlayers);
	}
	public Player(int playerNb, Random random){
		this.playerNb = playerNb; this.random=random;
		gameHistory = new GameTracker(WhistGame.deck,WhistGame.nbPlayers);
	}
	public void initRound(Hand hand){
		this.hand = hand;
	}

	public Hand getHand(){
		return hand;
	}

	public abstract Card takeLead();

	public abstract Card takeTurn(Hand trick);
	public GameTracker getGameHistory()
	{
		return gameHistory;
	}

	@Override
	public void onEndTrick(int startingPlayer, int winningPlayer, Hand trick)
	{
		this.gameHistory.addToHistory(trick, startingPlayer, winningPlayer);
	}
	@Override
	public void onNewRound(WhistGame.Suit trumps){
		this.gameHistory.newRound(trumps);
	}

	public int getId(){return this.playerNb;}

}
