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
public class WhistGame {
    protected final String version = "1.0";

    // TODO check this, made static final to be accessed by npc players
    public static int nbPlayers = 4;
    public static final Deck deck = new Deck(WhistGame.Suit.values(), WhistGame.Rank.values(), "cover");
    public int nbStartCards = 13;
    public int winningScore = 11;
    private boolean enforceRules = false;
    private int seed;


    protected ArrayList<Player> players = new ArrayList<Player>();
    protected Hand[] hands;
    protected Optional<Integer> roundWinner;
    protected WhistGame.Suit trump;
    protected Hand trick;
    protected int winner;
    private Card winningCard;
    private WhistGame.Suit lead;
    protected int nextPlayer;
    private int leadingPlayer;
    protected int[] scores = new int[nbPlayers];
    private Card selected;

    private UI ui;
    //FIXME Change modified
    public final Random random;
    //public static final Random random = ThreadLocalRandom.current();
    public enum Suit {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }
    public enum Rank {
        // Reverse order of rank importance (see rankGreater() below)
        // Order of cards is tied to card images
        ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
    }

    // return random Enum value
    public  <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    // return random Card from Hand
    public Card randomCard(Hand hand) {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    // return random Card from ArrayList
    public Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    protected void initRound() {
        //TODO I added false to below dealingOut call to prevent shuffling.
        // Needs revision, shuffling is needed otherwise we always end up with the same game.
        // SET SHUFFLE TO TRUE TO TEST PLAYERS
        hands = deck.dealingOut(nbPlayers, nbStartCards,true); // Last element of hands is leftover cards; these are ignored
        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            players.get(i).initRound(hands[i]);
        }
        ui.initRound(nbPlayers, hands);
    }

    protected Optional<Integer> playRound() {  // Returns winner, if any
        initializeTrump();
        nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
        for (int i = 0; i < nbStartCards; i++) {
            getLeadFromPlayer();
            // End Lead
            for (int j = 1; j < nbPlayers; j++) {
               getTurnFromPlayer();
            }
            endTrick();
            if (scores[nextPlayer] == winningScore) return Optional.of(nextPlayer);
        }
        return Optional.empty();
    }

    /**
     * Adds Players to a Whist game
     * @param players
     */
    public void addPlayers(ArrayList<Player> players)
    {
        this.players = players;
    }

    /**
     * Select and display trump suit
     */
    protected void initializeTrump(){
        trump = randomEnum(WhistGame.Suit.class);
        // TODO DO THIS PROPERLY
        for (int j=1; j<nbPlayers; j++){
            NPCPlayer player = (NPCPlayer) players.get(j);
            player.setTrump(trump);
        }
    }

    /**
     * Gets lead card from random player
     */
    protected void getLeadFromPlayer(){
        trick = new Hand(deck);
        selected = players.get(nextPlayer).takeLead();
        leadingPlayer = nextPlayer;
        // Lead with selected card
        selected.setVerso(false);
        // No restrictions on the card being lead
        lead = (WhistGame.Suit) selected.getSuit();
        selected.transfer(trick, false); // transfer to trick (includes graphic effect)
        winner = nextPlayer;
        winningCard = selected;
    }

    /**
     * Gets card from a player who is not playing the lead card
     */
    protected void getTurnFromPlayer(){
        if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
        selected = players.get(nextPlayer).takeTurn(trick);
        selected.setVerso(false);  // In case it is upside down
        // Check: Following card must follow suit if possible
        if (selected.getSuit() != lead && players.get(nextPlayer).getHand().getNumberOfCardsWithSuit(lead) > 0) {
            // Rule violation
            String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
            System.out.println(violation);
            if (enforceRules)
                try {
                    throw (new BrokeRuleException(violation));
                } catch (BrokeRuleException e) {
                    e.printStackTrace();
                    System.out.println("A cheating player spoiled the game!");
                    System.exit(0);
                }
        }
        // End Check
        selected.transferNonBlocking(trick, false); // transfer to trick (includes graphic effect)
        System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
        System.out.println(" played: suit = " + selected.getSuit() + ", rank = " + selected.getRankId());
        if ( // beat current winner with higher card
                (selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
                        // trumped when non-trump was winning
                        (selected.getSuit() == trump && winningCard.getSuit() != trump)) {
            System.out.println("NEW WINNER");
            winner = nextPlayer;
            winningCard = selected;
        }
        // End Follow
    }

    /**
     * Update score and player gamehistory to reflect the trick plaued
     */
    protected void endTrick(){
        nextPlayer = winner;
        scores[nextPlayer]++;
        // update player's histories
        // TODO DO THIS PROPERLY
        for (int j=1; j<nbPlayers; j++){
            NPCPlayer player = (NPCPlayer) players.get(j);
            player.updateGameHistory(trick, leadingPlayer);
        }
    }

    //public WhistGame(int nbPlayers, int winningScore, int nbStartCards, int seed, boolean enforceRules)
    public WhistGame(int nbPlayers, int winningScore, int nbStartCards, Random random, boolean enforceRules)
    {
        this.nbPlayers = nbPlayers;
        this.winningScore = winningScore;
        this.nbStartCards = nbStartCards;
        //this.seed = seed;
        this.enforceRules = enforceRules;
        this.random = random; //new Random(seed);
        ui = new UI(version, nbPlayers);

    }
    public void start()
    {
        // initialize players and scores
        for (int i = 0; i < nbPlayers; i++) {
            scores[i] = 0;
        }
        do {
            initRound();
            roundWinner = playRound();
        } while (!roundWinner.isPresent());
    }

    //Returns one random object to all players, to play the original play
    public Random getRandom()
    {
        return this.random;
    }

    public Optional<Integer> getRoundWinner() { return roundWinner;}

}
