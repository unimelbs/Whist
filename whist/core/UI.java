package core;

import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.util.Optional;

public class UI extends CardGame {

    Font bigFont = new Font("Serif", Font.BOLD, 36);

    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(650, 575)
    };
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);
    private Location hideLocation = new Location(-500, - 500);
    private Location trumpsActorLocation = new Location(50, 50);
    private final int handWidth = 400;
    private final int trickWidth = 40;
    private Actor[] scoreActors = {null, null, null, null };
    final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};
    private Actor trumpsActor;
    private static UI ui;

    protected UI(String version, int nbPlayers){
        super(700, 700, 30);
        if (ui != null){
            System.out.println("Singleton core.UI screwed up");
        }
        ui = this;
        setTitle("Whist (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatus("Initializing...");
        // Initialize score
        for (int i = 0; i < nbPlayers; i++) {
            scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    public static UI getInstance(){
        if (ui == null){
            System.out.println("Singleton core.UI screwed up");
        }
        return ui;
    }

    public void setStatus(String string) { setStatusText(string); }

    protected void initRound(int nbPlayers, Hand[] hands){
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            // layouts[i].setStepDelay(10);
            hands[i].setView(this, layouts[i]);
            hands[i].setTargetArea(new TargetArea(trickLocation));
            hands[i].draw();
        }
//	    for (int i = 1; i < nbPlayers; i++)  // This code can be used to visually hide the cards in a hand (make them face down)
//	      hands[i].setVerso(true);
        // End graphics
    }

    protected void updateScore(int player, int[] scores) {
        removeActor(scoreActors[player]);
        scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    protected void displayTrumpSuit(WhistGame.Suit trumps){
        trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
        addActor(trumpsActor, trumpsActorLocation);
    }

    protected void clearTrumpSuit(){
        removeActor(trumpsActor);
    }

    protected void updateTrick(Hand trick){
        trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
        trick.draw();
    }

    protected void endTrick(Hand trick, int winner){
        delay(600);
        trick.setView(this, new RowLayout(hideLocation, 0));
        trick.draw();
        setStatusText("players.Player " + winner + " wins trick.");
    }

    protected void endGame(Optional<Integer> winner){
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatus("Game over. Winner is player: " + winner.get());
        refresh();
    }

}
