package Server;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class WordleHandler {
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_GREEN = "\u001B[32m";
    final String ANSI_YELLOW = "\u001B[33m";
    String secretWord;
    String game;
    String censor = "0. ░ ░ ░ ░ ░ ░ ░ ░ ░ ░";


    public WordleHandler(String secretWord) {
        this.secretWord = secretWord;
        this.game = "> Wordle game.\n";
    }

    public String formatGW(String guessWord, int attempt) {
        guessWord = guessWord.toLowerCase();
        StringBuilder res = new StringBuilder().append(attempt).append(". ");
        game += attempt + ".";
        HashMap<Character, Integer> letters = new HashMap<>();
        for (int i = 0; i < secretWord.length(); i++) {
            Character key = secretWord.charAt(i);
            letters.put(key, letters.getOrDefault(key, 0) + 1);
        }
        for (int i = 0; i < secretWord.length(); i++) {
            char gwChar = guessWord.charAt(i);
            char swChar = secretWord.charAt(i);
            if (gwChar == swChar) {
                String rightChar = ANSI_GREEN + gwChar + ANSI_RESET;
                res.append(rightChar).append(" ");
                game += " \uD83D\uDFE9";
                letters.compute(swChar, (key, val) -> val - 1);
            } else if (secretWord.contains(String.valueOf(gwChar)) && letters.get(gwChar) > 0) {
                String halfRightChar = ANSI_YELLOW + gwChar + ANSI_RESET;
                res.append(halfRightChar).append(" ");
                game += " \uD83D\uDFE8";
                letters.compute(gwChar, (key, val) -> val - 1);
            } else if (i < secretWord.length() - 1) {
                game += " ⬜";
                res.append(gwChar).append(" ");
            }
            else {
                game += " ⬜";
                res.append(gwChar);
            }
        }
        game += "\n";
        return res.toString();
    }

    public int playWordle(Scanner in, PrintWriter out, WordsReader wordReader, String secretWord) {
        int i = 1;
        out.println(censor);
        while (i <= 12) {
            String guessedWord = in.nextLine();
            String response;
            if (guessedWord.length() != 10)
                out.println("> Your guess must be long 10 character");
            else if (!wordReader.checkWord(guessedWord)) {
                out.println("> Not in the dictionary");
            } else if (guessedWord.equals(secretWord)) {
                response = String.format("%s ✓", formatGW(guessedWord, i));
                out.println(response);
                out.format("> Congratulations, you make it in %d attempts!%n", i);
                return i;
            } else if (i == 12) {
                response = String.format("%s ✗", formatGW(guessedWord, i));
                out.println(response);
                out.println("> Sorry, you ran out of 12 attempts");
                return i;
            } else {
                response = formatGW(guessedWord, i);
                out.println(response);
                i++;
            }
        }
        return i;
    }
}
