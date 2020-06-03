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

	/**
	 * Updates Player with a played card event published by the game.
	 * @param card
	 * @param player
	 */
	public void onCardPlayed(Card card, Player player)
	{
		this.gameHistory.addPlayedCard(card,player);
		//TODO TE: Remove
		/*
		System.out.printf("onCardPlayed.player[%s].Gamehistory state:\n",
				this.playerNb);
		this.gameHistory.printTrackerState();

		 */
	}

	/**
	 * Updates Player with a new Trump event published by the game.
	 * @param trump
	 */
	public void onTrumpChange(WhistGame.Suit trump){this.gameHistory.updateTrump(trump);}

	/**
	 * Updates each Player of the player who won the trick. This information is stored
	 * in Player's GameTracker and is used by Smart Players.
	 * @param player
	 */
	public void onTrickWon(Player player)
	{
		this.gameHistory.updateScores(player);
	}
	public void updateGameHistory(Hand trick, int startingPlayer)
	{
		this.gameHistory.addToHistory(trick, startingPlayer);
	}
	public int getId(){return this.playerNb;}

}
