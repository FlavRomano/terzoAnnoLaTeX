package Client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @desc Class that reads client configuration parameters.
 */
public class ClientSetup {
    private final HashMap<String, String> config;
    public ClientSetup() {
        config = new HashMap<>();
        read();
    }

    /**
     * @desc Reads the client configuration file and stores the information in a HashMap.
     */
    public void read() {
        String config_path = "Config/configClient.txt";
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
        return config.get("host");
    }

    /**
     * @return Port where Wordle server is listening.
     */
    public int getServerPort() {
        return Integer.parseInt(config.get("serverPort"));
    }
}
