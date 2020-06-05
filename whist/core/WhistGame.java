package core;

import ch.aplu.jcardgame.*;
import exceptions.BrokeRuleException;
import players.NPCPlayer;
import players.Player;

import java.util.*;


/**
 * Represents a Whist game. Contains the game's logic and related data.
 */
@SuppressWarnings("serial")
public class WhistGame {
    private final String version = "1.0";
    public static int nbPlayers = 4;
    public static final Deck deck = new Deck(WhistGame.Suit.values(), WhistGame.Rank.values(), "cover");
    public int nbStartCards = 13;
    public int winningScore = 10;
    private boolean enforceRules = false;
    private ArrayList<IPlayListener> playListeners;
    private ArrayList<Player> players = new ArrayList<Player>();
    private UI ui;
    private int[] scores = new int[nbPlayers];
    private Card selected;
    private final Random random;
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

    private void initRound() {
        Hand[] hands = dealCards();
        hands.toString();
        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            players.get(i).initRound(hands[i]);
        }
        // graphics
        ui.initRound(nbPlayers, hands);
    }


    /**
     * Deals out cards using random from seed
     * @return
     */
    private Hand[] dealCards(){
        // Convert to hand for ease of access
        Hand deckAsHand = deck.toHand(false);
        Hand[] hands = new Hand[nbPlayers];
        for (int i = 0; i<nbStartCards; i++){
            for (int j = 0; j<nbPlayers; j++){
                int randomInt = random.nextInt(deckAsHand.getNumberOfCards());
                if (hands[j] == null){
                    hands[j] = new Hand(deck);
                }
                hands[j].insert(deckAsHand.get(randomInt), false);
                deckAsHand.remove(randomInt, false);
            }
        }
        return hands;
    }

    private Optional<Integer> playRound() {  // Returns winner, if any
        // Select and display trump suit
        final WhistGame.Suit trumps = randomEnum(WhistGame.Suit.class);
        publishNewRound(trumps);
        ui.displayTrumpSuit(trumps);
        // End trump suit
        Hand trick;
        int winner;
        Card winningCard;
        WhistGame.Suit lead;
        int leadingPlayer;
        int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
        for (int i = 0; i < nbStartCards; i++) {
            trick = new Hand(deck);
            selected = players.get(nextPlayer).takeLead();
            leadingPlayer = nextPlayer;
            // Lead with selected card
            ui.updateTrick(trick);
            selected.setVerso(false);
            // No restrictions on the card being lead
            lead = (WhistGame.Suit) selected.getSuit();
            selected.transfer(trick, true); // transfer to trick (includes graphic effect)
            //Publishing CardPlayed event.
            winner = nextPlayer;
            winningCard = selected;
            // End Lead
            for (int j = 1; j < nbPlayers; j++) {
                if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
                selected = players.get(nextPlayer).takeTurn(trick);
                // Follow with selected card
                ui.updateTrick(trick);
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
                selected.transfer(trick, true); // transfer to trick (includes graphic effect)
                System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
                System.out.println(" played: suit = " + selected.getSuit() + ", rank = " + selected.getRankId());
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
                    System.out.println("NEW WINNER");
                    winner = nextPlayer;
                    winningCard = selected;
                }
                // End Follow
            }
            //Publish trickWon event
            publishEndTrick(leadingPlayer, winner, trick);
            ui.endTrick(trick, winner);
            nextPlayer = winner;
            scores[nextPlayer]++;
            ui.updateScore(nextPlayer, scores);
            if (winningScore == scores[nextPlayer]) return Optional.of(nextPlayer);

        }

        ui.clearTrumpSuit();
        return Optional.empty();
    }

    /**
     * Adds Players to a Whist game
     * @param players
     */
    public void addPlayers(ArrayList<Player> players)
    {
        this.players = players;
        //Subscribing new players to WhistGame events.
        for (Player p: players)
        {
            this.addPlayListener(p);
        }
    }

    public WhistGame(int nbPlayers, int winningScore, int nbStartCards, Random random, boolean enforceRules, boolean hideCards)
    {
        this.playListeners = new ArrayList<IPlayListener>();
        this.nbPlayers = nbPlayers;
        this.winningScore = winningScore;
        this.nbStartCards = nbStartCards;
        this.enforceRules = enforceRules;
        this.random = random;
        ui = new UI(version, nbPlayers, hideCards);

    }

    /**
     * Starts the game.
     */
    public void start()
    {
        // initialize players and scores
        for (int i = 0; i < nbPlayers; i++) {
            scores[i] = 0;
        }

        Optional<Integer> winner;
        do {
            initRound();
            winner = playRound();
        } while (!winner.isPresent());

        ui.endGame(winner);
    }

    /**
     * Adds PlayListeners to the game to be notified when a plan takes place.
     * @param pl
     */
    public void addPlayListener(IPlayListener pl)
    {
        if (playListeners.contains(pl))
        {
            System.out.println("Listener already added!");
        }
        else
        {
            playListeners.add(pl);
        }
    }

    /**
     * Updates subscribers on Trump change.
     * @param newTrump
     */
    private void publishNewRound(Suit newTrump)
    {
        for (IPlayListener listener: playListeners)
        {
            listener.onNewRound(newTrump);
        }
    }

    /**
     * Updates subscribers on which Player won the trick.
     * @param startingPlayer
     * @param trick
     */
    private void publishEndTrick(int startingPlayer, int winningPlayer, Hand trick)
    {
        for (IPlayListener listener: playListeners)
        {
            listener.onEndTrick(startingPlayer, winningPlayer, trick);
        }
    }
}
