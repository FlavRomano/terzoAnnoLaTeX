public class User {
    public static class Statistics {
        int numberOfPlays;
        double winningPercentage;
        int recentWinningStreak;
        int allTimeWinningStreak;
        public Statistics() {
            numberOfPlays = 0;
            winningPercentage = 0.0;
            recentWinningStreak = 0;
            allTimeWinningStreak = 0;
        }
    }
    String username;
    String password;
    boolean active;
    Statistics statistics = new Statistics();
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = false;
    }
    public void login() {
        this.active = true;
    }
}