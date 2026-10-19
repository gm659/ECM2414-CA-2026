import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {

    @Test (expected = IllegalArgumentException.class)
    public void negativeCardTest() {
        new Card(-1);
    }

    @Test
    public void storesValueTest() {
        Card card = new Card(5);
        assertEquals(5, card.getValue());
    }

    @Test 
    public void toStringTest() {
        Card card = new Card(5);
        assertEquals("5", card.toString());
    }
    
}
