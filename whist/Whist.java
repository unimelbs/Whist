// Whist.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public class Whist{
	
  public enum Suit
  {
    SPADES, HEARTS, DIAMONDS, CLUBS
  }

  public enum Rank
  {
    // Reverse order of rank importance (see rankGreater() below)
	// Order of cards is tied to card images
	ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
  }
  

  static final Random random = ThreadLocalRandom.current();
  
  // return random Enum value
  public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
      int x = random.nextInt(clazz.getEnumConstants().length);
      return clazz.getEnumConstants()[x];
  }
  
  // return random Card from Hand
  public static Card randomCard(Hand hand){
      int x = random.nextInt(hand.getNumberOfCards());
      return hand.get(x);
  }
 
  // return random Card from ArrayList
  public static Card randomCard(ArrayList<Card> list){
      int x = random.nextInt(list.size());
      return list.get(x);
  }
  
  public boolean rankGreater(Card card1, Card card2) {
	  return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
  }
	 
private final String version = "1.0";
public final int nbPlayers = 4;
public final int nbStartCards = 13;
public final int winningScore = 11;
private boolean enforceRules = false;

private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

private ArrayList<Player> players = new ArrayList<Player>();
private UI ui;
private int[] scores = new int[nbPlayers];
private Card selected;

private void initRound() {
		 Hand[] hands = deck.dealingOut(nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
		 for (int i = 0; i < nbPlayers; i++) {
			   hands[i].sort(Hand.SortType.SUITPRIORITY, true);
			   players.get(i).initRound(hands[i]);
		 }
		// graphics
		ui.initRound(nbPlayers, hands);
 }

private Optional<Integer> playRound() {  // Returns winner, if any
	// Select and display trump suit
	final Suit trumps = randomEnum(Suit.class);
	ui.displayTrumpSuit(trumps);
	// End trump suit
	Hand trick;
	int winner;
	Card winningCard;
	Suit lead;
	int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
	for (int i = 0; i < nbStartCards; i++) {
		trick = new Hand(deck);
    	selected = players.get(nextPlayer).takeLead();
        // Lead with selected card
		ui.updateTrick(trick);
		selected.setVerso(false);
		// No restrictions on the card being lead
		lead = (Suit) selected.getSuit();
		selected.transfer(trick, true); // transfer to trick (includes graphic effect)
		winner = nextPlayer;
		winningCard = selected;
		// End Lead
		for (int j = 1; j < nbPlayers; j++) {
			if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
			selected = players.get(nextPlayer).takeTurn(lead);
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
								 throw(new BrokeRuleException(violation));
								} catch (BrokeRuleException e) {
									e.printStackTrace();
									System.out.println("A cheating player spoiled the game!");
									System.exit(0);
								}  
					 }
				// End Check
				 selected.transfer(trick, true); // transfer to trick (includes graphic effect)
				 System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
				 System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " +    selected.getRankId());
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

		ui.endTrick(trick, winner);
		nextPlayer = winner;
		scores[nextPlayer]++;
		ui.updateScore(nextPlayer, scores);
		if (winningScore == scores[nextPlayer]) return Optional.of(nextPlayer);
	}

	ui.clearTrumpSuit();
	return Optional.empty();
}

  public Whist()
  {
	// initialize players and scores
  	for (int i = 0; i < nbPlayers; i++) {
		  scores[i] = 0;
	}

  	// Add players, currently Hardcoded
	  players.add(new InteractablePlayer(0));
	  players.add(new LegalPlayer(1));
	  players.add(new LegalPlayer(2));
	  players.add(new LegalPlayer(3));

	  ui = new UI(version, nbPlayers);

  	Optional<Integer> winner;
    do { 
      initRound();
      winner = playRound();
    } while (!winner.isPresent());

    ui.endGame(winner);
  }

  public static void main(String[] args)
  {
	// System.out.println("Working Directory = " + System.getProperty("user.dir"));
    new Whist();
  }

}
