import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WordsReader {
    final String wordsPath = "words.txt";
    public String randomWord() {
        String word = null;
        try (RandomAccessFile f = new RandomAccessFile(new File(wordsPath), "r")) {
            long randomPosition = (long) (Math.random() * f.length());
            f.seek(randomPosition);
            f.readLine();
            word = f.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return word;
    }
}
