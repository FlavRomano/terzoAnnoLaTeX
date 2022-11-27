import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    final String userJsonPath = "src/WordleUsers.json";
    Gson gson;
    private List<User> userList = new ArrayList<>();

    public UserData() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static class User {
        String username;
        String password;
        boolean loggedIn;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
            this.loggedIn = false;
        }
        public void login() {
            this.loggedIn = true;
        }
        public void logout() {
            this.loggedIn = false;
        }
    }

    public void read() throws IOException {
        try (Reader reader = new FileReader(userJsonPath)) {
            Type type = new TypeToken<ArrayList<User>>() {
            }.getType();
            List<User> newUserList = gson.fromJson(reader, type);
            if (newUserList != null) {
                this.userList = newUserList;
            }
        }
    }

    public void add(String username, String password) {
        User user = new User(username, password);
        this.userList.add(user);
        try (Writer writer = new FileWriter(userJsonPath)) {
            gson.toJson(this.userList, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param username
     * @return password
     */
    public String get(String username, boolean login) {
        if (this.userList != null) {
            for (User user : userList) {
                if (user.username.equals(username))
                    if (login)
                        user.login();
                return user.password;
            }
        }
        return "";
    }
}
