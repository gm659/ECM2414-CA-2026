import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.file.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class Game{

    private final int numberOfPlayers;
    private final String packtxt;
    private final List<Card> pack;
    private final Player[] players;
    private final Deck[] decks;
    private final AtomicInteger winner;

    public Game(int numberOfPlayers, String packtxt){
        if (numberOfPlayers < 1){
            throw new IllegalArgumentException("Game has to have one or more characters");
        }
        List<Card> pack = new ArrayList<>();
        try{
            pack = getPack(numberOfPlayers, packtxt);
        } catch (IOException e){
            System.out.println("IOException: " + e);
        }


        this.winner = new AtomicInteger(0);
        this.pack = pack;
        this.numberOfPlayers = numberOfPlayers;
        this.packtxt = packtxt;

        this.decks = new Deck[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++){
            decks[i] = new Deck(i);
        }


        this.players = new Player[numberOfPlayers];
        for (int i = 0; i<numberOfPlayers; i++){
            try{
            players[i] = new Player(i, numberOfPlayers, pack, decks[i], decks[(i+1) % numberOfPlayers], winner);}
            catch (FileNotFoundException e){System.out.println("The writer in player class is broken most likely" + e);}
        }



    }

    public static void main(String[] args) throws Exception {
        new Game(3, "pack.txt").play();






    }

    public void play() throws InterruptedException{
        Thread[] threads = new Thread[numberOfPlayers];
        for (int i=0 ; i<numberOfPlayers; i++){
            threads[i] = new Thread(players[i], "player" +(i+1));

        }

        for (Thread t : threads){ // for in loop that starts all the threads
            t.start();
        }

        for (Thread t : threads){ // for in loop that joins all the threads
            t.join();
        }



    }

    public static List<Card> getPack(int n /* number of players */, String packtxt)throws IOException{
        List<Card> pack = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(packtxt))){
            int value = Integer.parseInt(line.trim()); // remove any accidently white space from both ends of the line
            if (value < 0){
                throw new IllegalArgumentException("Cards cannot have negative values");
            }
            pack.add(new Card(value));
        }
        if (pack.size() < n*8){
            throw new IllegalArgumentException("Pack must have " + 8*n + " cards. Instead has "+pack.size());
        }

        return pack;



    }
    // first the players, then the decks
    public static Card getCardFromPack(List<Card> pack){
        //cards are created by the order of the pack
        Card card = pack.remove(0);
        return card;
    }



    public static List<Card> makeDeck(List<Card> pack, int n){
        List<Card> Deck = new ArrayList<>();
        for (int i = 0; i<n;i++){
        Deck.add(getCardFromPack(pack));
        }
        return Deck;

    }


}