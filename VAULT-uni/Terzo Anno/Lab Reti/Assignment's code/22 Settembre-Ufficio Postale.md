## Consegna
Simulare il flusso di clienti in un ufficio postale che ha 4 sportelli. Nell'ufficio esiste:

-   un'ampia sala d'attesa in cui ogni persona può entrare liberamente. Quando entra, ogni persona prende il numero dalla numeratrice e aspetta il proprio turno in questa sala.
-   una seconda sala, meno ampia, posta davanti agli sportelli, in cui si può entrare solo a gruppi di k persone
-   una persona si mette quindi prima in coda nella prima sala, poi passa nella seconda sala.

Ogni persona impiega un tempo differente per la propria operazione allo sportello. Una volta terminata l'operazione, la persona esce dall'ufficio.

Scrivere un programma in cui:

-   l'ufficio viene modellato come una classe JAVA, in cui viene attivato un ThreadPool di dimensione uguale al numero degli sportelli
-   la coda delle persone presenti nella sala d'attesa è gestita esplicitamente dal programma
-   la seconda coda (davanti agli sportelli) è quella gestita implicitamente dal ThreadPool
-   ogni persona viene modellata come un task, un task che deve essere assegnato ad uno dei thread associati agli sportelli
-   si preveda di far entrare tutte le persone nell'ufficio postale, all'inizio del programma

Facoltativo: prevedere il caso di un flusso continuo di clienti e la possibilità che l'operatore chiuda lo sportello stesso dopo che in un certo intervallo di tempo non si presentano clienti al suo sportello.
## Codice
```java
import java.util.concurrent.*;

public class UfficioPostale {
    final static int TIME = 1000;
    final static int terminationDelay = 5000;
    public static class Persona implements Runnable {
        int ticket;

        public Persona(int ticket) {
            this.ticket = ticket;
        }

        public void run() {
            System.out.format("Cliente %d allo sportello %s%n",
                    this.ticket, Thread.currentThread().getName());
            try {
                Thread.sleep((long) (Math.random() * TIME));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        int persone = 50;
        int k = 4;
        System.out.println("Per inserire parametri: java UfficioPostale numeroPersone postiSalaPiccola");
        if (args.length > 1) {
            persone = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);
        }
        System.out.format("Inizializzato con %d persone e %d posti nella sala piccola%n", persone, k);
        LinkedBlockingQueue<Runnable> queueGrande = new LinkedBlockingQueue<>();
        ArrayBlockingQueue<Runnable> queuePiccola = new ArrayBlockingQueue<>(k);
        ThreadPoolExecutor service = new ThreadPoolExecutor(4,
                4,
                0,
                TimeUnit.MILLISECONDS,
                queuePiccola,
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 1; i <= persone; i++) {
            queueGrande.add(new Persona(i));
        }
        while (!queueGrande.isEmpty()) {
            try {
                service.execute(queueGrande.peek());
                queueGrande.remove();
            } catch (RejectedExecutionException ignored) {;}
        }
        service.shutdown();
        try {
            if (!service.awaitTermination(terminationDelay, TimeUnit.MILLISECONDS))
                service.shutdownNow();
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
    }
}
```