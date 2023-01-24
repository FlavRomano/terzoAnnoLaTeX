package server;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @desc Class that manages and maintains consistent user information as a
 *       response to
 *       actions performed by the client.
 */
public class ServerUserAccess {
    UserData userData;

    public ServerUserAccess() {
        userData = new UserData();
    }

    /**
     * @param userInfo User's username and password
     * @return A response code for the client:
     * @code1 "ok" User successfully registered.
     * @code2 "INVUSR" If username is not valid.
     * @code3 "INVPSW" If password is not valid.
     * @code4 "YETREG" If the username is already in the game database.
     */
    public String signup(String userInfo) throws IOException {
        userData.fetchUsers();
        String username = userInfo.substring(0, userInfo.indexOf(":"));
        String password = userInfo.substring(username.length() + 1);
        if (username.length() == 0 || username.contains(" ") || username.contains("-"))
            return "INVUSR";
        if (password.length() == 0 || password.contains(" "))
            return "INVPSW";
        if (userData.containsUser(username)) {
            return "YETREG";
        }
        userData.add(username, password);
        return "ok";
    }

    /**
     * @param userInfo User's username and password
     * @return A response code for the client or username:
     * @code1 "YETLOG" If user already logged in another client.
     * @code2 "NOTREG" If user not yet registered.
     * @code3 "WRGPSW" If the password sent does not match the password in the
     *        Database.
     */
    public String login(String userInfo) throws IOException {
        userData.fetchUsers();
        String username = userInfo.substring(0, userInfo.indexOf(":"));
        String password = userInfo.substring(username.length() + 1);
        User user = getUser(username);
        if (user == null)
            return "NOTREG";
        if (user.active)
            return "YETLOG";
        if (!user.password.equals(password))
            return "WRGPSW";
        return username;
    }

    /**
     * @param user User interested in posting match on social.
     * @param game Last game, without spoilers, played by the user.
     * @desc Make a post with the user's statistics and the last game played.
     */
    public void postGame(User user, String game) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm");
        User.Statistics statistics = user.statistics;
        String header = String.format("posted by %s%n", user.username);
        String footnote = String.format("> Matches played:         %d\n" +
                "> Winning percentage:     %.2f%%\n" +
                "> Recent winning streak:  %d\n" +
                "> Alltime winning streak: %d" +
                "\n                - %s%n",
                statistics.numberOfPlays, statistics.winningPercentage,
                statistics.recentWinningStreak, statistics.allTimeWinningStreak, formatter.format(new Date()));
        String separator = "——————————————————————————————————\n";
        userData.postOnSocial(header + game + footnote + separator);
    }

    /**
     * @desc Send the social page to the client.
     */
    public void sendSocial(PrintWriter out) {
        userData.sendSocial(out);
    }

    /**
     * @param username
     * @return Retrieves the User object, using the username, from the Json
     *         Database.
     */
    public User getUser(String username) throws IOException {
        userData.fetchUsers();
        return userData.getUser(username);
    }

    /**
     * @param user
     * @desc Updates user information in the database.
     */
    public void updateUser(User user) throws IOException {
        userData.fetchUsers();
        userData.updateUser(user);
    }

    /**
     * @desc If the server suddenly shuts down then on the next boot
     *       it must restore the active state of users to false.
     */
    public void restartingServer() throws IOException {
        userData.fetchUsers();
        userData.logOutAllUsers();
    }
}
