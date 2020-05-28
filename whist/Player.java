// LegalPlayer.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public abstract class Player {

	protected Hand hand;
	protected int playerNb;
	protected static final int THINKING_TIME = 2000;

	public Player(int playerNb){
		this.playerNb = playerNb;
	}

	public void initRound(Hand hand){
		this.hand = hand;
	}

	public Hand getHand(){
		return hand;
	}

	public abstract Card takeLead();

	public abstract Card takeTurn(Whist.Suit lead);


}