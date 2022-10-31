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