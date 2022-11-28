import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class WordleHandler {
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_GREEN = "\u001B[32m";
    final String ANSI_YELLOW = "\u001B[33m";
    String secretWord;
    String censor = "0. ░ ░ ░ ░ ░ ░ ░ ░ ░ ░";


    public WordleHandler(String secretWord) {
        this.secretWord = secretWord;
    }

    public String formatGW(String guessWord, int attempt) {
        guessWord = guessWord.toLowerCase();
        StringBuilder res = new StringBuilder().append(attempt).append(". ");
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
                letters.compute(swChar, (key, val) -> val - 1);
            } else if (secretWord.contains(String.valueOf(gwChar)) && letters.get(gwChar) > 0) {
                String halfRightChar = ANSI_YELLOW + gwChar + ANSI_RESET;
                res.append(halfRightChar).append(" ");
                letters.compute(gwChar, (key, val) -> val - 1);
            } else if (i < secretWord.length() - 1)
                res.append(gwChar).append(" ");
            else
                res.append(gwChar);
        }
        return res.toString();
    }

    public boolean playWordle(Scanner in, PrintWriter out, String secretWord) {
        int i = 1;
        out.println(censor);
        while (i <= 12) {
            String line = in.nextLine();
            if (line.equals(secretWord)) {
                String response = String.format("%s ✓", formatGW(line, i));
                out.println(response);
                out.format("> Congratulations, you make it in %d attempts!%n", i);
                return true;
            } else if (i == 12) {
                String response = String.format("%s ✗", formatGW(line, i));
                out.println(response);
                out.println("> Sorry, you ran out of 12 attempts");
                return false;
            } else if (line.length() == 10) {
                String response = formatGW(line, i);
                out.println(response);
                i++;
            } else {
                out.println("> Your guess must be long 10 character");
            }
        }
        return false;
    }
}
