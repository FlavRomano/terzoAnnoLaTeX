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