## Consegna
Scrivere un programma che conta le occorrenze dei caratteri alfabetici (lettere dalla "A" alla "Z") in un insieme di file di testo. Il programma prende in input una serie di percorsi di file testuali e per ciascuno di essi conta le occorrenze dei caratteri, ignorando eventuali caratteri non alfabetici (come per esempio le cifre da 0 a 9). Per ogni file, il conteggio viene effettuato da un apposito task e tutti i task attivati vengono gestiti tramite un pool di thread. I task registrano i loro risultati parziali all'interno di una `ConcurrentHashMap`. Prima di terminare, il programma stampa su un apposito file di output il numero di occorrenze di ogni carattere. Il file di output contiene una riga per ciascun carattere ed è formattato come segue:  
```
<carattere 1>,<numero occorrenze>   
<carattere 2>,<numero occorrenze>   
...   
<carattere N>,<numero occorrenze>  
```
  
Esempio di file di output:  `a,1281   b,315   c,261   d,302   ...`
## Codice
```java
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;

public class ContaOccorrenze {
    protected static ConcurrentHashMap<Character,Integer> res = new ConcurrentHashMap<>();

    public static class Task implements Runnable {
        String filePath;
        ConcurrentHashMap<Character,Integer> map;
        public Task(String filePath, ConcurrentHashMap<Character,Integer> map) {
            this.filePath = filePath;
            this.map = map;
        }
        public void run() {
            File f = new File(filePath);
            char[] chars = new char[(int) f.length()];
            try {
                FileReader fr = new FileReader(f);
                fr.read(chars);
                fr.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.format("%s prende %s%n", Thread.currentThread().getName(), filePath);
            for (char c : chars) {
                if (Character.isLetter(c)) {
                    map.merge(c, 1, Integer::sum);
                }
            }
        }
    }
    public static String formatResults(ConcurrentHashMap<Character,Integer> map) {
        StringBuilder res = new StringBuilder();
        for (ConcurrentHashMap.Entry<Character,Integer> entry : map.entrySet()) {
            int v = entry.getValue();
            if (v > 0) {
                char c = entry.getKey();
                res.append(String.format("%c,%d%n", c, v));
            }
        }
        return new String(res).trim();
    }
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {System.out.println("Inserire almeno un percorso di file .txt");}
        else {
            ExecutorService service = Executors.newFixedThreadPool(5);
            for (int i = 65; i <= 122; i++) {
                res.put(((char) i), 0);
            }
            int validArgs = 0;
            for (String fileName : args) {
                if (fileName.contains(".txt")) {
                    service.submit(new Task(fileName, res));
                    validArgs++;
                }
            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ignored) {;}
            if (validArgs > 0) {
                String resultToWrite = formatResults(res);
                String resultFileName = String.format("occorrenzeOut%d.txt", validArgs);
                File output = new File(resultFileName);
                FileWriter fw = new FileWriter(output);
                fw.write(resultToWrite);
                fw.close();
                System.out.printf("%n--Generato il file dei risultati: %s%n", resultFileName);
            } else {
                System.out.println("Non è stato inserito nessun percorso di file valido");
            }
        }
    }
}
```