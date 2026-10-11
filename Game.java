import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;

public class Game{

    private final int numberOfPlayers;
    private final String packtxt;
    private final List<Card> pack;


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



        this.pack = pack;
        this.numberOfPlayers = numberOfPlayers;
        this.packtxt = packtxt;

        Deck deck = new Deck(1);
        deck.addCard(getCardFromPack(pack));
        System.out.println(deck.returnCards());

    }

    public static void main(String[] args) {
        Game game = new Game(3, "pack.txt");




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