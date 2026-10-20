import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.file.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class CardGame{

    private final int numberOfPlayers;

    private final List<Card> pack;
    private final Player[] players;
    private final Deck[] decks;
    private final AtomicInteger winner;

    public CardGame(int numberOfPlayers, String packtxt) throws IOException{
        if (numberOfPlayers < 1){
            throw new IllegalArgumentException("Game has to have one or more characters");
        }




        this.winner = new AtomicInteger(0);
        this.pack = getPack(numberOfPlayers, packtxt);
        this.numberOfPlayers = numberOfPlayers;


        this.decks = new Deck[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++){
            decks[i] = new Deck(i+1);

        }

        //make hands for the players

        //have to put the first set of lists inside of the list of list
        List<List<Card>> hand = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++){
            hand.add(new ArrayList<>());
        }
        // adds cards into the lists
        int index = 0;
        for (int i = 0; i < 4; i++){
            for (int p = 0; p < numberOfPlayers; p++){
                hand.get(p).add(pack.get(index++)); // takes the card at the current potisition and puts in in the players hand
            }
        }

        //put the rest of the cards into the decks
        for (int i = 0; i < 4; i++){
            for (int d = 0; d < numberOfPlayers; d++){
                decks[d].addCard(pack.get(index++)); // adds a card into the deck from the pack
            }
        }



        this.players = new Player[numberOfPlayers];
        for (int i = 0; i<numberOfPlayers; i++){
           players[i] = new Player(i+1, numberOfPlayers, hand.get(i), decks[i], decks[(i+1) % numberOfPlayers], winner);}


    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers = 0;
        String pack = "pack.txt";
        CardGame game;
        while (true){
            System.out.println("Enter in number of players");
            String input = scanner.nextLine().trim();
            try{
                numberOfPlayers = Integer.parseInt(input);
                if (numberOfPlayers < 1){
                    System.out.print("Has to be more than one player!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.print("Number Format Exception " + e);
            }
        }
        while (true){
            System.out.println("Enter in the pack location");
            pack = scanner.nextLine().trim();
            try{
                game = new CardGame(numberOfPlayers, pack);
                break;
            } catch (IOException e) {
                System.out.print("IO Exception " + e);
            }
        }
        scanner.close();
        game.play();




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
        if (pack.size() != n*8){
            throw new IllegalArgumentException("Pack must have " + 8*n + " cards. Instead has "+pack.size());
        }

        return pack;



    }

}