package Server;

import java.io.*;

/**
 * @desc Class that reads Wordle's dictionary, draws the new word
 * randomly and changes it when it expires.
 * @wordTTL Lifetime of the secret word.
 */
public class WordsReader implements Runnable {
    final String wordsPath = "Database/words.txt";
    private String extractedWord;
    long wordTTL;
    public WordsReader(long wordTTL) {
        this.wordTTL = wordTTL;
    }
    public String randomWord() {
        String word;
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
    public boolean checkWord(String guessedWord) {
        try (BufferedReader br = new BufferedReader(new FileReader(wordsPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(guessedWord))
                    return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public String getExtractedWord() {
        return extractedWord;
    }
    public void run() {
        while (true) {
            extractedWord = randomWord();
            try {
                Thread.sleep(wordTTL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
