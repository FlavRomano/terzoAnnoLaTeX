public class WordleHandler {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    static String secretWord;
    String censor;

    public WordleHandler(String secretWord) {
        this.secretWord = secretWord;
        this.censor = "░░ ░░ ░░ ░░ ░░ ░░ ░░ ░░ ░░ ░░";
    }

    public static String formatGW(String guessWord) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            char gwChar = guessWord.charAt(i);
            char swChar = secretWord.charAt(i);
            if (gwChar == swChar) {
                String rightChar = ANSI_GREEN + gwChar + ANSI_RESET;
                res.append(rightChar).append(" ");
            } else if (secretWord.contains(String.valueOf(gwChar))) {
                String halfRightChar = ANSI_YELLOW + gwChar + ANSI_RESET;
                res.append(halfRightChar).append(" ");
            } else if (i < secretWord.length() - 1)
                res.append(gwChar).append(" ");
            else
                res.append(gwChar);
        }
        return res.toString();
    }

    public static void main(String[] args) {
        secretWord = "abdominous";
        System.out.println(formatGW("aberdevine"));
    }
}
