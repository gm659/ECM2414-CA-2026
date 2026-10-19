import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.*;



public class Player implements Runnable {

    private final int id;
    private final List<Card> hand;
    private final Random random = new Random();
    private final int numberOfPlayers;
    private final PrintWriter writer;
    private final Deck drawDeck;
    private final Deck discardDeck;
    private final AtomicInteger winner;


    public Player(int id, int numberOfPlayers, List<Card> hand, Deck drawDeck, Deck discarDeck, AtomicInteger winner) throws FileNotFoundException{
        this.id = id;
        this.hand = hand;
        this.numberOfPlayers = numberOfPlayers;
        this.drawDeck = drawDeck;
        this.winner = winner;
        this.discardDeck = discarDeck;
        this.writer = new PrintWriter("player" + id + "_output.txt");




    }

    public synchronized void addCard(Card card){
        if (hand.size() > 4){
            throw new IllegalArgumentException("Already 4 cards in the deck");
        }
        hand.add(card);
    }

    public synchronized Card removeCard(){
        List<Integer> trashList = new ArrayList<>(); //holds potential values to remove from the hand
        for (int i = 0; i < 4; i++){
            if (hand.get(i).getValue() != id){ //adds all cards that aren't the player's number to the trashList for random removal
                trashList.add(i);
            }
        }
        int randomNumber =  random.nextInt(trashList.size()); //picks number at random
        int discardedCard = trashList.get(randomNumber);
        return hand.remove(discardedCard);
    }

    public synchronized boolean checkHand(){
        int count = 0;
        for (int i = 1; i < 4; i++){
            if (hand.get(0).getValue() == hand.get(i).getValue()){
                count++;
            }
        }
        if (count == 3){
            return true;
        } else{
            return false;
        }

    }

    public synchronized void turn(){
        Deck Deck1;
        Deck Deck2;
        if (drawDeck.getDeckNumber() > discardDeck.getDeckNumber()){
            Deck1 = drawDeck;
        } else {
            Deck1 = discardDeck;
        }
        if (Deck1 == drawDeck){
            Deck2 = discardDeck;
        } else{
            Deck2 = drawDeck;
        }

        // conditional statements needed to find the proper


        synchronized(Deck1){
            synchronized(Deck2){
                if (drawDeck.isEmpty()){
                    return;
                }

                Card discardedCard;
                Card newCard = drawDeck.drawCard();



                addCard(newCard);
                discardedCard = removeCard(); //removes the card from the hand and stores it in the discardedCard variable
                discardDeck.addCard(discardedCard); // adds card to the discardDeck



                writer.println("player"+id+" drew "+ newCard.getValue());
                writer.println("player"+id+" discarded "+ discardedCard.getValue());
                writer.println("player"+id+" current hand is "+hand);
            }
        }
        }

    @Override
    public void run(){
        // game logic goes here

        String message1 = String.format("player%d initial hand %d %d %d %d", id, hand.get(0).getValue(),
        hand.get(1).getValue(),           hand.get(2).getValue(),                hand.get(3).getValue());
        // orginal message to be put in the text file

        writer.println(message1);


        while ((winner.get() == 0)){

                if (checkHand() == true){
                    crownWinner();
                    break;
                }
                turn();
                Thread.onSpinWait();}

        if (winner.get() == id){
            writer.println("player"+id+" wins");
        } else {
            writer.println("player"+winner.get()+" has won!");
        }
        writer.println("player"+id+" final hand: "+hand);
        writer.close();
    }

    public synchronized void crownWinner(){
        winner.compareAndSet(0,id); //checks if a player hasn't won then changes the value to the player's id
    }






}
