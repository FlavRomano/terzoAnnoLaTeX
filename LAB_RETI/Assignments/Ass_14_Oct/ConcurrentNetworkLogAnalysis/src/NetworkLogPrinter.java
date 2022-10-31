import java.io.*;
import java.net.*;

public class NetworkLogPrinter {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Inserire un percorso file");
        } else {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(args[0]));
                String line = reader.readLine();
                while (line != null) {
                    int delimiter = line.indexOf("-");
                    String[] weblogSliced = new String[]{line.substring(0, delimiter - 1), line.substring(delimiter)};
                    try {
                        InetAddress address = InetAddress.getByName(weblogSliced[0]);
                        System.out.format("%s %s%n", address.getHostName(), weblogSliced[1]);
                    } catch (UnknownHostException e) {
                        System.err.format("Impossibile determinare l'host dell'ip %s%n", weblogSliced[0]);
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
