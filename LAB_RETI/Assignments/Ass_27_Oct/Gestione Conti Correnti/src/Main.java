import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.*;
import java.util.concurrent.*;

public class Main {
    static final File f = new File("src/test.json");
    public static class Record {
        String date;
        String reason;
    }
    public static class Conto {
        String owner;
        Record[] records;
    }
    public static class Task implements Runnable {
        Conto conto;
        ConcurrentHashMap<String, Integer> map;

        public Task(Conto conto, ConcurrentHashMap<String, Integer> map) {
            this.conto = conto;
            this.map = map;
        }
        public void run() {
            System.out.format("%s controlla il conto di %s%n",
                    Thread.currentThread().getName(), conto.owner);
            for (Record r : conto.records) {
                map.compute(r.reason, (key,value) -> value + 1);
            }
        }
    }
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("BONIFICO", 0);
        map.put("ACCREDITO", 0);
        map.put("BOLLETTINO", 0);
        map.put("F24", 0);
        map.put("PAGOBANCOMAT", 0);
        ExecutorService service = Executors.newFixedThreadPool(5);
        try (JsonReader reader = new JsonReader(new FileReader(f))) {
            reader.beginArray();
            while (reader.hasNext()) {
                JsonToken peek = reader.peek();
                if (peek == JsonToken.BEGIN_OBJECT) {
                    Gson gson = new Gson();
                    Task t = new Task(gson.fromJson(reader, Conto.class), map);
                    service.submit(t);
                }
            }
            reader.endArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignored) {;}
        System.out.println(map);
    }
}