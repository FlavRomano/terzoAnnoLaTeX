## Consegna
Scrivere un programma che dato in input una lista di directories, comprima tutti i file in esse contenuti, con l'utility gzip.

Ipotesi semplificativa:

-   zippare solo i file contenuti nelle directories passate in input,  non considerare ricorsione su eventuali soIttodirectories

Il  riferimento ad ogni file individuato viene passato ad un task, che deve essere eseguito in un threadpool. individuare nelle API JAVA la classe di supporto adatta per la compressione

NOTA: l'utilizzo dei threadpool è indicato, perchè I task presentano un buon mix tra I/O e computazione

-  I/O heavy: tutti i file devono essere letti e scritti

- CPU-intensive: la compressione richiede molta computazione

Facoltativo: comprimere ricorsivamente I file in tutte le sottodirectories
## Codice
```java
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Main {
    public static void zippingFile(String filePath) throws IOException {
        File f = new File(filePath);
        String zippedPath = filePath + ".gz";
        FileInputStream fis = new FileInputStream(filePath);
        FileOutputStream fos = new FileOutputStream(zippedPath);
        GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
        byte[] buffer = new byte[(int) f.length()];
        fis.read(buffer);
        gzipOS.write(buffer, 0, (int) f.length());
        System.out.printf("%s %s zipped%n", 
	        Thread.currentThread().getName(), f.getName());
        gzipOS.close();
        fos.close();
        fis.close();
    }

    public static class Task implements Runnable {

        String fileName;

        public Task(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void run() {
            try {
                zippingFile(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void zipping(String fileName, ExecutorService service) throws IOException {
        List<Path> pathList;
        File f = new File(fileName);
        if (f.isDirectory()) {
            try (Stream<Path> stream = Files.walk(Paths.get(f.getAbsolutePath()))) {
                pathList = stream.map(Path::normalize)
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());
            }
            for (Path path : pathList) {
                service.execute(new Task(path.toString()));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (String fileName : args) {
            zipping(fileName, service);
        }
        service.shutdown();
    }
}
```