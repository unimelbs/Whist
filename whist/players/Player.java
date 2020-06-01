package players;// LegalPlayer.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import core.GameTracker;
import core.WhistGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public abstract class Player {

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
	public GameTracker getGameHistory(){
		return gameHistory;
	}
	public void updateGameHistory(Hand trick, int startingPlayer){
		this.gameHistory.addToHistory(trick, startingPlayer);
	}
}
