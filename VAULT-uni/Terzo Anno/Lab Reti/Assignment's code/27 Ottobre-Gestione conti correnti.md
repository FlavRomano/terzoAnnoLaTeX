## Consegna
Viene dato un file JSON compresso (in formato GZIP) contenente i conti correnti di una banca.  

Ogni conto corrente contiene il nome del correntista ed una lista di movimenti.  
I movimenti registrati per un conto corrente possono essere molto numerosi.  
Per ogni movimento vengono registrati la data e la causale del movimento.  
L'insieme delle causali possibili è fissato: Bonifico, Accredito, Bollettino, F24, PagoBancomat.  
  
Progettare un'applicazione che attiva un insieme di thread.  
Uno di essi legge dal file gli oggetti "conto corrente" e li passa, uno per volta, ai thread presenti in un thread pool.  
Si vuole trovare, per ogni possibile causale, quanti movimenti hanno quella causale.   
La lettura dal file deve essere fatta utilizzando l'API GSON per lo streaming.
### Struttura File Json Conti correnti
```java
[
  {
    "owner": "Luca",
    "records": [
      {
        "date": "Dec 28, 2020, 4:23:34 PM",
        "reason": "F24"
      },
      {
        "date": "Jan 23, 2019, 4:47:51 AM",
        "reason": "BOLLETTINO"
      }
    ]
  },
  {
    "owner": "Andrea",
    "records": [
      {
        "date": "Dec 28, 1992, 4:23:34 PM",
        "reason": "F24"
      },
      {
        "date": "Jan 23, 2002, 4:47:51 AM",
        "reason": "BONIFICO"
      }
    ]
  },
  {
    "owner": "Giovanni",
    "records": [
      {
        "date": "Dec 28, 1999, 4:23:34 PM",
        "reason": "ACCREDITO"
      },
      {
        "date": "Jan 23, 2012, 4:47:51 AM",
        "reason": "BONIFICO"
      },
      {
        "date": "Jan 23, 2012, 4:47:51 AM",
        "reason": "PAGOBANCOMAT"
      }
    ]
  }
]
```
## Codice
```java
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
    public static void printResults() {
        System.out.println("\nRisultati:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.format("%s,%d%n", entry.getKey(), entry.getValue());
        }
    }
    public static void main(String[] args) {
        if (args.length % 2 == 0) {
            System.err.format("Inserire un path di file json%n java Main path/example.json");
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
                    JsonToken peek = reader.peek();
                    if (peek == JsonToken.BEGIN_OBJECT) {
                        Conto conto = (new Gson()).fromJson(reader, Conto.class);
                        Task t = new Task(conto);
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
            printResults();
        }
    }
}
```
### Risultato JSON grosso
| Causale      | \#Movimenti |
| ------------ | ----------- |
| BOLLETTINO   | 4003958     |
| F24          | 3998118     |
| PAGOBANCOMAT | 3996664     |
| BONIFICO     | 4001414     |
| ACCREDITO    | 3999846     |

