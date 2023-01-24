package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @desc Class that reads client configuration parameters.
 */
public class ClientSetup {
    final String ACCESSMENU = "+---------+------------------------------+\n" +
            "| command |          description         |\n" +
            "+---------+------------------------------+\n" +
            "|    1    |     Create a new account     |\n" +
            "+---------+------------------------------+\n" +
            "|    2    | Login to an existing account |\n" +
            "+---------+------------------------------+\n" +
            "|    q    |         Close client         |\n" +
            "+---------+------------------------------+";
    final String GAMEMENU = "+---------+------------------------------+\n" +
            "| command |          description         |\n" +
            "+---------+------------------------------+\n" +
            "|    1    | Play Wordle, guess the word! |\n" +
            "+---------+------------------------------+\n" +
            "|    2    |        Show statistics       |\n" +
            "+---------+------------------------------+\n" +
            "|    3    |      Share your results      |\n" +
            "+---------+------------------------------+\n" +
            "|    4    |       Show WordleBook        |\n" +
            "+---------+------------------------------+\n" +
            "|    5    |            Logout            |\n" +
            "+---------+------------------------------+\n" +
            "|    h    |    Show game instructions    |\n" +
            "+---------+------------------------------+\n" +
            "|    m    |           Show menu          |\n" +
            "+---------+------------------------------+";
    final String INSTRUCTION = "> Welcome to Wordle! You have 12 attempts to guess the word.\n" +
            "\t- If a character in my message is \u001B[32mgreen\u001B[0m then it's in the right position.\n" +
            "\t- If it's \u001B[33myellow\u001B[0m then it appears in the secret word but not in that position.\n" +
            "\t- Otherwise it's wrong.";
    private final HashMap<String, String> config;

    public ClientSetup() {
        config = new HashMap<>();
        read();
    }

    /**
     * @desc Reads the client configuration file and stores the information in a
     *       HashMap.
     */
    public void read() {
        String config_path = "config/client.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(config_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(":");
                config.put(splitLine[0], splitLine[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Host found in the configuration file.
     */
    public String getHost() {
        return config.get("host").trim();
    }

    /**
     * @return Port where Wordle server is listening.
     */
    public int getServerPort() {
        return Integer.parseInt(config.get("serverPort").trim());
    }

    /**
     * @return Multicast group needed by UDP Receiver.
     */
    public String getMulticastGroup() {
        return config.get("multicastGroup").trim();
    }

    /**
     * @return Multicast group port needed by UDP Receiver.
     */
    public int getMulticastGroupPort() {
        return Integer.parseInt(config.get("multicastGroupPort").trim());
    }
}
