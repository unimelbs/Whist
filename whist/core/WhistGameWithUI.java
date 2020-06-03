package core;

import ch.aplu.jcardgame.*;
import exceptions.BrokeRuleException;
import players.NPCPlayer;
import players.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Represents a Whist game. Contains the game's logic and related data.
 */
@SuppressWarnings("serial")
public class WhistGameWithUI extends WhistGame {
    private UI ui;

    public WhistGameWithUI(int nbPlayers, int winningScore, int nbStartCards, Random random, boolean enforceRules) {
        super(nbPlayers, winningScore, nbStartCards, random, enforceRules);
    }


    @Override
    protected void initRound() {
        super.initRound();
        ui.initRound(nbPlayers, hands);
    }

    @Override
    protected void initializeTrump(){
       super.initializeTrump();
       ui.displayTrumpSuit(trump);
    }

    @Override
    protected void getLeadFromPlayer(){
        super.getLeadFromPlayer();
        players.get(nextPlayer).think();
        ui.updateTrick(trick);
    }

    @Override
    protected void getTurnFromPlayer(){
        super.getTurnFromPlayer();
        players.get(nextPlayer).think();
        ui.updateTrick(trick);
    }

    @Override
    protected void endTrick(){
        super.endTrick();
        ui.endTrick(trick, winner);
        ui.updateScore(nextPlayer, scores);
    }

    @Override
    protected Optional<Integer> playRound(){
        Optional<Integer> winner = super.playRound();
        ui.clearTrumpSuit();
        return winner;
    }

    @Override
    public void start()
    {
       super.start();
       ui.endGame(Optional.of(winner));
    }

}
