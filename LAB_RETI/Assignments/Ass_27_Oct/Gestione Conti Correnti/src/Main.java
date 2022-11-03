import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;

public class Main {
    private static final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    public class Record {
        String date;
        String reason;
    }

    public class Conto {
        String owner;
        Record[] records;
    }

    public static class Task implements Runnable {
        Conto conto;

        public Task(Conto conto) {
            this.conto = conto;
        }

        public void run() {
            System.out.format("%s controlla il conto di %s%n",
                    Thread.currentThread().getName(), conto.owner);
            for (Record r : conto.records) {
                map.compute(r.reason, (key, value) -> value + 1);
            }
        }
    }

    public static String csvResults() {
        StringBuilder s = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            s.append(String.format("%s,%d%n", entry.getKey(), entry.getValue()));
        }
        return String.valueOf(s);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.format("Inserire un path di file json%n\tjava -cp :gson-2.10.jar Main path/example.json%n");
        } else {
            map.put("F24", 0);
            map.put("BONIFICO", 0);
            map.put("ACCREDITO", 0);
            map.put("BOLLETTINO", 0);
            map.put("PAGOBANCOMAT", 0);
            ExecutorService service = Executors.newFixedThreadPool(5);
            File f = new File(args[0]);
            try (JsonReader reader = new JsonReader(new FileReader(f))) {
                reader.beginArray();
                while (reader.hasNext()) {
                    Gson gson = new Gson();
                    Conto conto = gson.fromJson(reader, Conto.class);
                    Task t = new Task(conto);
                    service.execute(t);
                }
                reader.endArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ignored) {;}
            String results = csvResults();
            System.out.format("%nRisultati:%n%s", results);
        }
    }
}
/*
Risultati:
BOLLETTINO,4003958
F24,3998118
PAGOBANCOMAT,3996664
BONIFICO,4001414
ACCREDITO,3999846
*/