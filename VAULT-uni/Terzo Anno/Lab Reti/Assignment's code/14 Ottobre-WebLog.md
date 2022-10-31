## Consegna
Il log file di un web server contiene un insieme di linee, con il seguente formato: 

 `150.108.64.57 - - [15/Feb/2001:09:40:58 -0500] "GET / HTTP 1.0" 200 2511` in cui: 

-   150.108.64.57 indica l'host remoto, in genere secondo la dotted quad form 
-   `[date]` 
-   "HTTP request" 
-   status 
-   bytes sent 
-   eventuale tipo del client "Mozilla/4.0......." 

Scrivere un'applicazione Weblog che prende in input il nome del log file (che è pubblicato sulla pagina del corso) e ne stampa ogni linea, in cui ogni indirizzo IP è sostituito con l'hostname 

Sviluppare due versioni del programma, la prima single-threaded, la seconda invece utilizza un thread pool, in cui il task assegnato ad ogni thread riguarda la traduzione di un insieme di linee del file. Confrontare i tempi delle due versioni.
## Codice
Soluzione sequenziale
```java
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
```

Soluzione Concorrente
```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class ConcurrentNetworkLogPrinter {
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
```
Comparatore
```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimeNetworkLogComparator {
    public static class Task implements Runnable {
        String[] args;
        boolean concurrent;
        long time;
        public Task(String[] args, boolean concurrent) {
            this.args = args;
            this.concurrent = concurrent;
        }
        public void run() {
            time = System.currentTimeMillis();
            if (concurrent) {
                ConcurrentNetworkLogPrinter.main(args);
            } else {
                NetworkLogPrinter.main(args);
            }
            long time2 = System.currentTimeMillis();
            time = (time2 - time);
        }
    }
    public static void main(String[] args) {
        Task taskConcurrent = new Task(args, true);
        Task taskSingleThread = new Task(args, false);
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            service.submit(i == 0 ? taskConcurrent : taskSingleThread);
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignored) {;}
        System.out.format("Concurrent Network Log Analyser tempo: %d ms.%n", taskConcurrent.time);
        System.out.format("Single Thread Network Log Analyser tempo: %d ms.%n", taskSingleThread.time);
    }
}
```
### Risultati
| | Single | Concurrent |
| ---- | ------ | ---------- |
| **Tempo** | 8 min. | 1.05 min.  |