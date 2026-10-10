import java.util.List;


public class Player implements Runnable {

    private final int id;
    private final Card[] hand;
    private final int higherDeck;


    public Player(int id, String hand, int higherDeck){
        this.id = id;
        this.hand = new Card[4];
        this.higherDeck = higherDeck;
    
    }


    @Override
    public void run(){
        // game logic goes here
    }






}
