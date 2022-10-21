import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;

public class NetworkLogAnalyser {
    public static String[] weblogReader(String filePath) {
        File f = new File(filePath);
        char[] buf = new char[(int) f.length()];
        try {
            FileReader fr = new FileReader(filePath);
            fr.read(buf);
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(buf).split("\n");
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Inserire un percorso file");
        } else {
            String[] weblogs = weblogReader(args[0]);
            for (String weblog : weblogs) {
                int delimiter = weblog.indexOf("-");
                String[] weblogSliced = new String[]{weblog.substring(0, delimiter - 1),
                                                     weblog.substring(delimiter)};
                try {
                    InetAddress address = InetAddress.getByName(weblogSliced[0]);
                    System.out.format("%s %s%n",address.getHostName(), weblogSliced[1]);
                } catch (UnknownHostException ignored) {
                    System.err.format("Impossibile determinare l'host dell'ip %s%n", weblogSliced[0]);
                }
            }
        }
    }
}
