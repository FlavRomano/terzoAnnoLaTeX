package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ServerSetup {
    private final HashMap<String, String> config;
    private String config_path = "Config/configServer.txt";

    public ServerSetup() {
        config = new HashMap<>();
        read();
    }

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

    public int getPort() {
        return Integer.parseInt(config.get("port"));
    }

    public int getNThreads() {
        return Integer.parseInt(config.get("nThreads"));
    }

    public long getWordTTL() {
        return Long.parseLong(config.get("wordTTL"));
    }
}
