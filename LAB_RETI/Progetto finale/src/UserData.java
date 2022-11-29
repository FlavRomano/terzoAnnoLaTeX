import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    final String userJsonPath = "WordleUsers.json";
    final String socialPath = "Social.txt";
    Gson gson;
    private List<User> userList = new ArrayList<>();

    public UserData() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    public void fetchUsers() throws IOException {
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
    public void addUser(User user) {
        userList.removeIf(user1 -> user1.username.equals(user.username));
        this.userList.add(user);
        try (Writer writer = new FileWriter(userJsonPath)) {
            gson.toJson(this.userList, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public User getUser(String username) {
        if (userList != null) {
            for (User user : userList) {
                if (user.username.equals(username))
                    return user;
            }
        }
        return null;
    }
    /**
     * @param username
     * @return password | ""
     */
    public String approveUser(String username, boolean login, boolean logout) {
        if (userList != null) {
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
    public void postOnSocial(String post) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(socialPath, true))) {
            bw.write(post);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String readSocial() {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(socialPath))) {
            String line;
            while ((line = br.readLine()) != null)
                result.append(line).append("\n");
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
