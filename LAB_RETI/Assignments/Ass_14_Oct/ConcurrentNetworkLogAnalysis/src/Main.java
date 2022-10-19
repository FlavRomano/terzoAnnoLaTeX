import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Main {
    public static class Task implements Runnable {
        String[] weblog;

        public Task(String weblog) {
            int delimiter = weblog.indexOf("-");
            this.weblog = new String[]{weblog.substring(0, delimiter - 1), weblog.substring(delimiter)};
        }

        public void run() {
            try {
                InetAddress address = InetAddress.getByName(weblog[0]);
                System.out.format("%s %s%n",address.getHostName(), weblog[1]);
            } catch (UnknownHostException ignored) {
                System.err.format("Impossibile determinare l'host dell'ip %s%n", weblog[0]);
            }
        }
    }
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
            ExecutorService service = Executors.newFixedThreadPool(10);
            for (String weblog : weblogs) {
                service.submit(new Task(weblog));
            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ignored) {;}
        }
    }
}