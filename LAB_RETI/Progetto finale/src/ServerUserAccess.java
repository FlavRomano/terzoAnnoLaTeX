import java.io.*;

public class ServerUserAccess {
    UserData userData;
    public ServerUserAccess() {
         userData = new UserData();
    }

    public String signup(String userInfo) throws IOException {
        userData.read();
        String username = userInfo.substring(0, userInfo.indexOf(":"));
        String password = userInfo.substring(username.length() + 1);
        if (!userData.get(username, false).equals("")) {
            return "YETREG";
        }
        userData.add(username, password);
        return "ok";
    }

    public String login(String userInfo) throws IOException {
        userData.read();
        String username = userInfo.substring(0, userInfo.indexOf(":"));
        String password = userInfo.substring(username.length() + 1);
        String queryResult = userData.get(username, true);
        if (queryResult.equals(""))
            return "NOTREG";
        if (!queryResult.equals(password))
            return "WRGPSW";
        return "ok";
    }
}
