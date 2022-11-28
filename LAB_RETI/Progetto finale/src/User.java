import java.util.ArrayList;

public class User {
    public static class Statistics {
        int numberOfPlays;
        boolean wonBefore;
        double winningPercentage;
        int recentWinningStreak;
        int allTimeWinningStreak;
        public Statistics() {
            numberOfPlays = 0;
            wonBefore = false;
            winningPercentage = 0.0;
            recentWinningStreak = 0;
            allTimeWinningStreak = 0;
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
    public void win() {
        if (!statistics.wonBefore) {
            statistics.wonBefore = true;
        }
        statistics.recentWinningStreak++;
        if (statistics.allTimeWinningStreak < statistics.recentWinningStreak)
            statistics.allTimeWinningStreak = statistics.recentWinningStreak;
        statistics.updateWinningPercentage();
    }
    public void lost() {
        statistics.wonBefore = false;
        statistics.recentWinningStreak = 0;
        statistics.updateWinningPercentage();
    }
    public String[] getStatistics() {
        String[] res = new String[4];
        res[0] = String.format("> Number of games started:  %d", statistics.numberOfPlays);
        res[1] = String.format("> Winning percentage:       %.2f%%", statistics.winningPercentage);
        res[2] = String.format("> Recent winning streak:    %d", statistics.recentWinningStreak);
        res[3] = String.format("> All time winning streak:  %d", statistics.allTimeWinningStreak);
        return res;
    }
}