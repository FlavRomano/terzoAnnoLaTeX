package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc Class that uses google's Gson library to read, update the
 *       file with Wordle users and manage the social page.
 * @userJsonPath database/WordleUsers.json
 * @socialPath database/Social.txt
 * @userList List of users that it updates based on what it finds in the
 *           database.
 */
public class UserData {
    final String userJsonPath = "database/wordleUsers.json";
    final String socialPath = "database/social.txt";
    Gson gson;
    private List<User> userList = new ArrayList<>();

    public UserData() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * @desc Reads from JSON and updates its list.
     */
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

    /**
     * @param username
     * @param password
     * @desc Adds a new user to the Json.
     */
    public void add(String username, String password) {
        User user = new User(username, password);
        this.userList.add(user);
        try (Writer writer = new FileWriter(userJsonPath)) {
            gson.toJson(this.userList, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logOutAllUsers() {
        if (userList != null) {
            for (User user : userList) {
                user.logout();
            }
            try (Writer writer = new FileWriter(userJsonPath)) {
                gson.toJson(this.userList, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param user
     * @desc Update the user's information in the Json.
     */
    public void updateUser(User user) {
        userList.removeIf(user1 -> user1.username.equals(user.username));
        this.userList.add(user);
        try (Writer writer = new FileWriter(userJsonPath)) {
            gson.toJson(this.userList, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param username
     * @return User object from the Json.
     */
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
     * @desc Check if the username is in the database.
     */
    public boolean containsUser(String username) {
        if (userList != null) {
            for (User user : userList)
                if (user.username.equals(username))
                    return true;
        }
        return false;
    }

    /**
     * @param post New social post.
     * @desc Adds the post with the match and statistics of the user who pressed
     *       share to Social.
     */
    public void postOnSocial(String post) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(socialPath, true))) {
            bw.write(post);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @desc Send the social page to the client.
     */
    public void sendSocial(PrintWriter out) {
        try (BufferedReader br = new BufferedReader(new FileReader(socialPath))) {
            String socialLine;
            while ((socialLine = br.readLine()) != null) {
                out.println(socialLine);
            }
            out.println("EOF");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
