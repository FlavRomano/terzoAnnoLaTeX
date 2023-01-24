package server;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * @desc Class that reads Wordle's dictionary, draws the new word
 *       randomly and changes it when it expires.
 * @wordTTL Lifetime of the secret word.
 */
public class WordsReader implements Runnable {
    final String wordsPath = "database/words.txt";
    private String extractedWord;
    private ArrayList<String> dictionary;
    private int numberOfWords;
    long wordTTL;

    public WordsReader(long wordTTL) {
        this.wordTTL = wordTTL;
        this.dictionary = new ArrayList<>();
        this.numberOfWords = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(wordsPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line);
                numberOfWords++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String randomWord() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(numberOfWords);
        return dictionary.get(randomIndex);
    }

    public boolean checkWord(String guessedWord) {
        return dictionary.contains(guessedWord);
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
