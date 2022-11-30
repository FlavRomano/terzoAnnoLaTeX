import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerUserAccess {
    UserData userData;

    public ServerUserAccess() {
        userData = new UserData();
    }

    public String signup(String userInfo) throws IOException {
        userData.fetchUsers();
        String username = userInfo.substring(0, userInfo.indexOf(":"));
        String password = userInfo.substring(username.length() + 1);
        if (username.length() == 0 || username.contains(" ") || username.contains("-"))
            return "INVUSR";
        if (password.length() == 0)
            return "INVPSW";
        if (!userData.approveUser(username, false, false).equals("")) {
            return "YETREG";
        }
        userData.add(username, password, false);
        return "ok";
    }

    public String login(String userInfo) throws IOException {
        userData.fetchUsers();
        String username = userInfo.substring(0, userInfo.indexOf(":"));
        String password = userInfo.substring(username.length() + 1);
        User user = getUser(username);
        if (user == null)
            return "NOTREG";
        if (user.active)
            return "ko";
        if (!user.password.equals(password))
            return "WRGPSW";
        user.login();
        updateUser(user);
        return username;
    }

    public void postGame(User user, String game) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm");
        User.Statistics statistics = user.statistics;
        String header = String.format("%s by %s%n", formatter.format(new Date()), user.username);
        String footnote = String.format("> Matches played:         %d\n> Winning percentage:     %.2f%%\n" +
                "> Recent winning streak:  %d\n> Alltime winning streak: %d%n",
                statistics.numberOfPlays, statistics.winningPercentage, statistics.recentWinningStreak, statistics.allTimeWinningStreak);
        String separator = "——————————————————————————————————\n";
        userData.postOnSocial(header + game + footnote + separator);
    }
    public void sendSocial(PrintWriter out) {
        userData.sendSocial(out);
    }
    public User getUser(String username) throws IOException {
        userData.fetchUsers();
        return userData.getUser(username);
    }

    public void updateUser(User user) throws IOException {
        userData.fetchUsers();
        userData.addUser(user);
    }
}
