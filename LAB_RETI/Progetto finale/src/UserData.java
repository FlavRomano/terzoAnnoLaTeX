import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    final String userJsonPath = "WordleUsers.json";
    Gson gson;
    private List<User> userList = new ArrayList<>();

    public UserData() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
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

    public void add(String username, String password, boolean login) {
        User user = new User(username, password);
        if (login)
            user.login();
        this.userList.add(user);
        try (Writer writer = new FileWriter(userJsonPath)) {
            gson.toJson(this.userList, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param username
     * @return password | ""
     */
    public String get(String username, boolean login, boolean logout) {
        if (this.userList != null) {
            for (User user : userList) {
                if (user.username.equals(username)) {
                    String password = user.password;
                    if (login) {
                        userList.remove(user);
                        add(username, password, true);
                    }
                    if (logout) {
                        userList.remove(user);
                        add(username, password, false);
                    }
                    return user.password;
                }
            }
        }
        return "";
    }
}
