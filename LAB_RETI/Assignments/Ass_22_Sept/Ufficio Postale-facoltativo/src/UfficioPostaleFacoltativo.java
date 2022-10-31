import java.util.concurrent.*;

public class UfficioPostaleFacoltativo {
    final static int K = 4;
    final static int TIME = 500;
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
            // System.out.format("Cliente %d esce%n", this.ticket);
        }
    }

    public static class Pusher implements Runnable {
        LinkedBlockingQueue<Runnable> queue;
        public Pusher(LinkedBlockingQueue<Runnable> queue) {
            this.queue = queue;
        }
        public void run() {
            int i = 1;
            while (true) {
                this.queue.add(new Persona(i++));
            }
        }
    }

    public static void main(String[] args) {
        LinkedBlockingQueue<Runnable> queueGrande = new LinkedBlockingQueue<>();
        ArrayBlockingQueue<Runnable> queuePiccola = new ArrayBlockingQueue<>(K);
        Thread t = new Thread(new Pusher(queueGrande));
        t.start();
        ThreadPoolExecutor service = new ThreadPoolExecutor(4,
                4,
                100,
                TimeUnit.MILLISECONDS,
                queuePiccola,
                new ThreadPoolExecutor.AbortPolicy());
        service.allowCoreThreadTimeOut(true);
        while (queueGrande.peek() != null) {
            try {
                service.execute(queueGrande.peek());
                queueGrande.remove();
                Thread.sleep(150);
            } catch (RejectedExecutionException ignored) {;} catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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