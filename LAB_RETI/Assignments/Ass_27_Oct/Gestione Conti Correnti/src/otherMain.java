import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.*;
import java.util.concurrent.*;

public class otherMain {
    static final File f = new File("src/test.json");
    public static class Record {
        String date;
        String reason;
    }
    public static class Wrapper {
        String owner;
        Record[] records;
    }
    public static class Task implements Runnable {
        Record objRecords;
        ConcurrentHashMap<String, Integer> map;

        public Task(Record objRecords, ConcurrentHashMap<String, Integer> map) {
            this.objRecords = objRecords;
            this.map = map;
        }
        public void run() {
            System.out.println(objRecords);
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
//                    Gson gson = new Gson();
//                    gsonList.add(gson.fromJson(reader, Wrapper.class));
                    reader.beginObject();
                    boolean isRecord = false;
                    while (!isRecord) {
                        System.out.println(reader.peek());
                        if (reader.peek() == JsonToken.NAME) {
                            System.out.println(reader.nextName()); // owner
                            System.out.println(reader.nextString());
                            System.out.println(reader.nextName()); // records
                            reader.beginArray();
                            while (reader.hasNext()) {
                                if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                                    Gson gson = new Gson();
                                    Task t = new Task(gson.fromJson(reader, Record.class), map);
                                    service.submit(t);
                                }
                            }
                            reader.endArray();
                            isRecord = true;
                        }
                    }
                    reader.endObject();
                }
            }
            reader.endArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(map);
        service.shutdown();
    }
}