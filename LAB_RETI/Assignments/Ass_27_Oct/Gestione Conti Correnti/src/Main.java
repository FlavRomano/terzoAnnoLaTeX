import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.*;
import java.util.ArrayList;

public class Main {
    public class Record {
        String date;
        String reason;
    }
    public class Wrapper {
        String owner;
        Record[] records;
    }
    public static void main(String[] args) {
        File f = new File("test.txt");
        try (JsonReader reader = new JsonReader(new FileReader(f))) {
            ArrayList<Wrapper> gsonList = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                JsonToken peek = reader.peek();
                if (peek == JsonToken.BEGIN_OBJECT) {
                    Gson gson = new Gson();
                    gsonList.add(gson.fromJson(reader, Wrapper.class));
                }
            }
            reader.endArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello world!");
    }
}