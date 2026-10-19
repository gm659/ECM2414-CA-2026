import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class DeckTest {

    private Deck deck;

    @Before
    public void setUp() {
        deck = new Deck(1);
    } 

    @Test
    public void newDeckIsEmptyTest() {
        List<Card> cards = deck.returnCards();
        assertTrue(cards.isEmpty());
    }

    @Test 
    public void drawCardFifoTest() {
        Card card1 = new Card(5);
        Card card2 = new Card(10);
        deck.addCard(card1);
        deck.addCard(card2);

        Card drawnCard1 = deck.drawCard();
        Card drawnCard2 = deck.drawCard();
        assertEquals(card1, drawnCard1);
        assertEquals(card2, drawnCard2);
    }

    @Test
    public void drawFromEmptyDeckReturnsNullTest() {
        Card drawnCard = deck.drawCard();
        assertNull(drawnCard);
    }

    @Test
    public void getDeckNumberTest() {
        assertEquals(1, deck.getDeckNumber());
    }

    @Test
    public void addCardIncreasesDeckSizeTest() {
        Card card = new Card(5);
        deck.addCard(card);
        List<Card> cards = deck.returnCards();
        assertEquals(1, cards.size());
    }

    @Test 
    public void drawRemovesCardFromDeckTest() {
        Card card = new Card(5);
        deck.addCard(card);
        deck.drawCard();
        List<Card> cards = deck.returnCards();
        assertTrue(cards.isEmpty());
    }

    @Test (expected = IllegalArgumentException.class)
    public void addNullCardThrowsExceptionTest() {
        deck.addCard(null);
    }

    
}
