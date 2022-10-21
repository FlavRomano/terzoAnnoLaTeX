import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class ConcurrentNetworkLogAnalyser {
    public static class Task implements Runnable {
        String[] weblog;

        public Task(String weblog) {
            int delimiter = weblog.indexOf("-");
            this.weblog = new String[]{weblog.substring(0, delimiter - 1), weblog.substring(delimiter)};
        }

        public void run() {
            try {
                InetAddress address = InetAddress.getByName(weblog[0]);
                System.out.format("%s: %s %s%n", Thread.currentThread().getName(), address.getHostName(), weblog[1]);
            } catch (UnknownHostException e) {
                System.err.format("Impossibile determinare l'host dell'ip %s%n", weblog[0]);
            }
        }
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Inserire un percorso file");
        } else {
            ExecutorService service = Executors.newFixedThreadPool(5);
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(args[0]));
                String line = reader.readLine();
                while (line != null) {
                    service.submit(new Task(line));
                    line = reader.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ignored) {;}
        }
    }
}