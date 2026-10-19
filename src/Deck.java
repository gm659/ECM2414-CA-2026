import java.util.concurrent.atomic.*;
import java.util.ArrayList;
import java.util.List;

public class Deck {

    private final int deckNumber;
    private final List<Card> cards = new ArrayList<>();

    public Deck(int deckNumber){
        this.deckNumber = deckNumber;

    }

    public synchronized void addCard(Card newCard){
        // could add an exception to check validity of card
        cards.add(newCard);
    }

    public synchronized Card drawCard(){
        Card drawnCard = cards.remove(0);
        return drawnCard;
    }

    public int getDeckNumber(){
        return deckNumber;
    }

    public List<Card> returnCards(){
        return cards;
    }




}
