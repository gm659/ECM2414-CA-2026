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
        writer.println("player"+id+"current hand: "+hand);



        Card discardedCard = removeCard();
        discardDeck.addCard(discardedCard); //removes a card from the hand and adds it to the discard deck

        Card newCard = this.drawDeck.drawCard();
        addCard(newCard);



        writer.println("player"+id+" drew "+ newCard.getValue());
        writer.println("player"+id+" discarded "+ discardedCard.getValue());


        }






    @Override
    public void run(){
        // game logic goes here

        String message1 = String.format("player %d intial hand %d %d %d %d", id, hand.get(0).getValue(),
        hand.get(1).getValue(),           hand.get(2).getValue(),                hand.get(3).getValue());
        // orginal message to be put in the text file




        writer.println(message1);



        if (checkHand() == false){
            while (!(winner.get() == id)){

                if (checkHand() == true){
                    crownWinner();
                }
                turn();


        }} else{
            System.out.println("game is over");
        }


    }

    public synchronized void crownWinner(){
        winner.addAndGet(id);
    }






}
