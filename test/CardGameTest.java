import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.rules.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import Java.util.Random;

public class CardGameTest {


    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private String createRandomPack(int length) throws IOException{
        List<String> text = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < length; i++){
            text.add(String.valueOf(random.nextInt(9)+1));
        }

        Path file = tempDir.resolve("testingPack.txt");
        Files.write(file,text);
        return file.toString();
    }

    private String createPrecisePack(Object...lines) throws IOExcpetion{
        List<String> text = new ArrayList<>();
        for (Object line : lines){
            text.add(String.valueOf(line));
        }
        Path file = tempFolder.getRoot().getPath().resolve("testingPack.txt");
        Files.write(file, text);
        return file.toString();
    }

    @Test
    public void acceptCorrectPackToPlayerSize() throws IOException {
        //checks if a 3 player game generates a 24 card Pack
        List<Card> pack = CardGame.getPack(3, createRandomPack(24));
        assertEquals(24,pack.size());
    }

    @Test
    public void readsCardsInOrder() throws IOException {
        // checks if the cards are taken from the text file into the pack in order
        List<Card> pack = CardGame.getPack(1,createPrecisePack( "1","2","3","4","5","6","7","8"));
        assertEquals(List.of(1,2,3,4,5,6,7,8), values(pack));
    }

    @Test
    public void whiteSpaceChecker() throws IOException {
        // checks if the method ignores whitespace
        List<Card> pack = CardGame.getPack(1,createPrecisePack( "    1","2","   3","4","    5","6    ","7","   8    "));
        assertEquals(List.of(1,2,3,4,5,6,7,8), values(pack));

    }

    @Test
    public void negativeNumberChecker() throws IOException{
        //Checks for negative numbers
        String file = CardGame.getPack(1,createPrecisePack( -1,2,3,4,5,6,7,8));
        assertThrows(IllegalArgumentException.class, () -> CardGame.getPack(1,file));
    }

    @Test
    public void missingFileCheck(){
        String missingFile = "notARealFile.txt";
        assertThrows(IO.exception.class, () -> CardGame.getPack(1,missingFile));
    }



}
