import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ClientSetup {
    private final HashMap<String, String> config;
    public ClientSetup() {
        config = new HashMap<>();
        read();
    }
    public void read() {
        String config_path = "configClient.txt";
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
    public String getHost() {
        return config.get("host");
    }
    public int getServerPort() {
        return Integer.parseInt(config.get("serverPort"));
    }
}
