import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PlayerTest {
    private Deck drawDeck;
    private Deck discardDeck;
    private AtomicInteger winner;

    @Before
    public void setUp(){
        drawDeck = new Deck(1);
        discardDeck = new Deck(2);
        winner = new AtomicInteger(0);

    }

    private List<Card> hand(int... values){
        List<Card> hand = new ArrayList<>();
        for (int v: values){
            hand.add(new Card(v));

        }
        return hand;
    }

    private Player player(List<Card> hand) throws Exception{
        return new Player(1,2,hand,drawDeck,discardDeck,winner);
    }


    // addCard Tests
    @Test
    //checking what happens when the player has 5 cards in thier hand
    public void overflowingHandTest() throws Exception{
        List<Card> hand = hand(1,2,3,4);
        Player player = player(hand);
        player.addCard(new Card(5));
        assertEquals(5,hand.size());
    }

    @Test(expected = IllegalArgumentException.class)
    //Checks that adding null to a hand returns an IllegalArgumentException
    public void addNullCardThrowsExceptionTest() throws Exception{
        Player player = player(hand(1,2,3,4));
        player.addCard(null);
    }

    //RemoveCard

    @Test
    // checks that the player never discards the same card number as thier id
    // repeats 100 times as removeCard is random
    public void cardRemovalTest() throws Exception{
        for (int i = 0; i < 50; i++){
            List<Card> hand = hand(1,2,3,4,1);
            Player player = player(hand);
            Card removed = player.removeCard();
            assertNotEquals(1, removed.getValue());
            // makes sure hand size also goes back down to four
            assertEquals(4, hand.size());
        }
    }

    //Check Hand
    @Test

    public void checkForWinningHandFalse()throws Exception{
        Player player = player(hand(1,3,1,2));
        assertFalse(player.checkHand());

    }


    public void checkForWinningHandTrue()throws Exception{
            Player player = player(hand(1,1,1,1));
            assertTrue(player.checkHand());

    }

    //Turn

    @Test
    public void checkTurnWorks()throws Exception{
        List<Card> hand = hand(1,2,3,4);
        Player player = player(hand);
        drawDeck.addCard(new Card(5));

        player.turn();

        assertTrue(drawDeck.isEmpty());
        assertEquals(1,discardDeck.returnCards().size());
        assertEquals(4,hand.size());
    }

    //checks if the draw deck is empty and the turn has to be skipped
    @Test
    public void EmptyTurn()throws Exception{
        List<Card> hand = hand(1,2,3,4);
        Player player = player(hand);


        player.turn();



        assertEquals(4,hand.size());
        assertTrue(discardDeck.isEmpty());
    }

    // crownWinner


    @Test

    public void checkForWinner()throws Exception{
        Player player = player(hand(1,2,3,4));
        player.crownWinner();
        assertEquals(1,winner.get());

    }

    // Makes sure to not replace a player if someone else has already won
    @Test
    public void checkForReplacement()throws Exception{
        winner.set(2);
        Player player = player(hand(1,2,3,4));
        player.crownWinner();
        assertEquals(2,winner.get());
    }

    // Run

    @Test
    public void initialHandWinner()throws Exception{
        Player player = player(hand(1,1,1,1));
        player.run();
        assertEquals(1,winner.get());
        String output = Files.readString(Path.of("player1_output.txt"));
        assertTrue(output.contains("player1 initial hand 1 1 1 1"));
        assertTrue(output.contains("player1 wins"));

    }

    @Test
    public void stopWhenAnotherPlayerHasWon()throws Exception{
        winner.set(2);
        Player player=player(hand(1,2,3,4));
        player.run();
        assertEquals(2,winner.get());
        String output = Files.readString(Path.of("player1_output.txt"));
        assertTrue(output.contains("player2 has won!"));
    }








}
