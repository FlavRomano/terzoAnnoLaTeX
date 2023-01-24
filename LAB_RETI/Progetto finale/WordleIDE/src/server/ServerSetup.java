package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @desc Class that reads server configuration parameters.
 */
public class ServerSetup {
    private final HashMap<String, String> config;
    private String config_path = "config/server.txt";

    public ServerSetup() {
        config = new HashMap<>();
        read();
    }

    /**
     * @desc Reads the server configuration file and stores the information in a
     *       HashMap.
     */
    public void read() {
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
     * @return Port where Wordle server will listen.
     */
    public int getPort() {
        return Integer.parseInt(config.get("port").trim());
    }

    /**
     * @return Number of threads that will handle client connections.
     */
    public int getNThreads() {
        return Integer.parseInt(config.get("nThreads").trim());
    }

    /**
     * @return Wordle secret word lifetime.
     */
    public long getWordTTL() {
        return Long.parseLong(config.get("wordTTL").trim());
    }

    /**
     * @return Port needed by UDP Notifier.
     */
    public int getNotifierPort() {
        return Integer.parseInt(config.get("notifierPort").trim());
    }

    /**
     * @return Multicast group needed by UDP Notifier.
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
