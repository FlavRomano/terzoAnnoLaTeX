package server;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @secretWord
 * @game Spoiler-less match
 */
public class WordleHandler {
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_GREEN = "\u001B[32m";
    final String ANSI_YELLOW = "\u001B[33m";
    final String ANSI_GREEN_SQUARE = "\uD83D\uDFE9";
    final String ANSI_YELLOW_SQUARE = "\uD83D\uDFE8";
    final String ANSI_WHITE_SQUARE = "⬜";

    String secretWord;
    String game;
    String censor = "0. ░ ░ ░ ░ ░ ░ ░ ░ ░ ░";

    public WordleHandler(String secretWord) {
        this.secretWord = secretWord;
        this.game = "> Wordle game.\n";
    }

    /**
     * @param guessWord User's submitted word
     * @param attempt   User's current attempt number
     * @return Line formatted with colors representing hints to the user.
     */
    public String formatGW(String guessWord, int attempt) {
        guessWord = guessWord.toLowerCase();
        StringBuilder res = new StringBuilder().append(attempt).append(". ");
        game += String.format("%2d.", attempt);

        String[] resultWord = new String[secretWord.length()];
        HashMap<Character, Integer> letters = new HashMap<>();

        for (int i = 0; i < secretWord.length(); i++) {
            char gwChar = guessWord.charAt(i);
            char swChar = secretWord.charAt(i);
            letters.put(swChar, letters.getOrDefault(swChar, 0) + 1);
            if (gwChar == swChar) {
                String rightChar = ANSI_GREEN + gwChar + ANSI_RESET;
                resultWord[i] = rightChar;
                letters.put(gwChar, letters.get(gwChar) - 1);
            } else
                resultWord[i] = String.valueOf(gwChar);
        }
        for (int i = 0; i < secretWord.length(); i++) {
            char gwChar = guessWord.charAt(i);
            if (resultWord[i].contains(ANSI_GREEN)) {
                String rightChar = resultWord[i];
                res.append(rightChar).append(" ");
                game += " " + ANSI_GREEN_SQUARE;
            } else if (secretWord.contains(String.valueOf(gwChar)) && letters.get(gwChar) > 0) {
                String halfRightChar = ANSI_YELLOW + gwChar + ANSI_RESET;
                res.append(halfRightChar).append(" ");
                letters.put(gwChar, letters.get(gwChar) - 1);
                game += " " + ANSI_YELLOW_SQUARE;
            } else {
                game += " " + ANSI_WHITE_SQUARE;
                res.append(gwChar).append(" ");
            }
        }
        game += "\n";
        return res.toString();
    }

    /**
     * @param wordReader To check that the word entered by the user is present in
     *                   the Wordle dictionary.
     * @return Number of attempts spent by the user
     * @desc Implements the actual game. Sends, as responses to the user, the
     *       guessed
     *       word with hints and any errors.
     */
    public int playWordle(Scanner in, PrintWriter out, WordsReader wordReader, String secretWord) {
        int i = 1;
        out.println(censor);
        while (true) {
            String guessedWord = in.nextLine();
            String response;
            if (guessedWord.length() != 10)
                out.println("> Your guess must be long 10 character");
            else if (!wordReader.checkWord(guessedWord))
                out.println("> Not in the dictionary");
            else if (guessedWord.equals(secretWord)) {
                response = String.format("%s ✓", formatGW(guessedWord, i));
                out.println(response);
                out.format("> Congratulations, you make it in %d attempts!%n", i);
                return i;
            } else if (i == 12) {
                response = String.format("%s ✗", formatGW(guessedWord, i));
                out.println(response);
                out.println("> Sorry, you ran out of 12 attempts");
                i++;
                return i;
            } else {
                response = formatGW(guessedWord, i);
                out.println(response);
                i++;
            }
        }
    }
}