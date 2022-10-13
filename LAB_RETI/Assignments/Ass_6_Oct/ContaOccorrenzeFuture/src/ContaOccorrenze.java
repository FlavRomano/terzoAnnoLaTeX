import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ContaOccorrenze {
    public static class Task implements Callable<ConcurrentHashMap<Character,AtomicInteger>> {
        String filePath;
        public Task(String filePath) {
            this.filePath = filePath;
        }
        public ConcurrentHashMap<Character,AtomicInteger> call() throws IOException {
            File f = new File(filePath);
            char[] chars = new char[(int) f.length()];
            ConcurrentHashMap<Character, AtomicInteger> map = new ConcurrentHashMap<>();
            FileReader fr = new FileReader(f);
            if (fr.read(chars) == 0) {
                fr.close();
                throw new IOException();
            } else {
                fr.close();
                System.out.format("%s prende %s%n", Thread.currentThread().getName(), filePath);
                for (char c : chars) {
                    if ((c >= 97 && c <= 122) || (c >= 65 && c <= 90)) {
                        map.putIfAbsent(c, new AtomicInteger(0));
                        map.get(c).incrementAndGet();
                    }
                }
                return map;
            }
        }
    }
    public static String formatResults(HashMap<Character,Integer> map) {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<Character,Integer> entry : map.entrySet()) {
            Character c = entry.getKey();
            Integer v = entry.getValue();
            res.append(String.format("%c,%d%n", c, v));
        }
        return new String(res).trim();
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        if (args.length == 0) {System.out.println("Inserire almeno un percorso di file .txt");}
        else {
            ExecutorService service = Executors.newFixedThreadPool(5);
            List<Future<ConcurrentHashMap<Character,AtomicInteger>>> list = new CopyOnWriteArrayList<>();
            HashMap<Character,Integer> res = new HashMap<>();
            int validArgs = 0;
            for (String fileName : args) {
                if (fileName.contains(".txt")) {
                    list.add(service.submit(new Task(fileName)));
                    validArgs++;
                }
            }
            while(!list.isEmpty()) {
                for (Future<ConcurrentHashMap<Character, AtomicInteger>> f : list) {
                    if (f.isDone()) {
                        f.get().forEach((k, v) -> res.merge(k, v.intValue(), Integer::sum));
                        list.remove(f);
                    }
                }
            }
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
            service.shutdown();
        }
    }
}