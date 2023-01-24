package server;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    public static class Statistics {
        int numberOfPlays;
        boolean wonBefore;
        double winningPercentage;
        int recentWinningStreak;
        int allTimeWinningStreak;
        HashMap<Integer, Integer> guessDistribution;

        public Statistics() {
            numberOfPlays = 0;
            wonBefore = false;
            winningPercentage = 0.0;
            recentWinningStreak = 0;
            allTimeWinningStreak = 0;
            guessDistribution = new HashMap<>();
            for (int i = 1; i < 13; i++)
                guessDistribution.put(i, 0);
        }

        public void updateWinningPercentage() {
            winningPercentage = (double) allTimeWinningStreak / numberOfPlays * 100;
        }
    }

    String username;
    String password;
    boolean active;
    ArrayList<String> playedWords = new ArrayList<>();
    Statistics statistics = new Statistics();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = false;
    }

    public boolean isPlayed(String secretWord) {
        return playedWords.contains(secretWord);
    }

    public void addWord(String secretWord) {
        playedWords.add(secretWord);
    }

    public void login() {
        this.active = true;
    }

    public void logout() {
        this.active = false;
    }

    public void win(int numberOfGuess) {
        if (!statistics.wonBefore) {
            statistics.wonBefore = true;
        }
        statistics.recentWinningStreak++;
        if (statistics.allTimeWinningStreak < statistics.recentWinningStreak)
            statistics.allTimeWinningStreak = statistics.recentWinningStreak;
        statistics.guessDistribution.compute(numberOfGuess, (key, val) -> val + 1);
        statistics.updateWinningPercentage();
    }

    public void lost() {
        statistics.wonBefore = false;
        statistics.recentWinningStreak = 0;
        statistics.updateWinningPercentage();
    }

    public String[] getStatistics() {
        String[] res = new String[17];
        res[0] = "> Guess distribution";
        for (int i = 1; i < 13; i++) {
            int val = statistics.guessDistribution.get(i);
            res[i] = String.format(" %2d. | %d", i, val);
        }
        res[13] = String.format("> Number of games started:  %d", statistics.numberOfPlays);
        res[14] = String.format("> Winning percentage:       %.2f%%", statistics.winningPercentage);
        res[15] = String.format("> Recent winning streak:    %d", statistics.recentWinningStreak);
        res[16] = String.format("> All time winning streak:  %d", statistics.allTimeWinningStreak);
        return res;
    }
}
