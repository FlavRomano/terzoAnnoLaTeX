import java.util.concurrent.*;

// 5 emettitrici, sala con al massimo 10 posti, 50 viaggiatori
// 5 emettitrici = #di thread core del pool
// 10 posti massimo in sala = lunghezza della BlockingQueue
// 50 viaggiatori = #di task da passare al threadPool

public class Biglietteria {

    public static class SalaDattesaPiena implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Viaggiatore viaggiatore = (Viaggiatore) r;
            System.out.format("%s: Viaggatore no. %d respinto sala piena%n",
                    Thread.currentThread().getName(), viaggiatore.id);
        }
    }

    public static class Viaggiatore implements Runnable {
        int id;

        public Viaggiatore(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.printf("Sono il viaggiatore %d, sto acquistando il biglietto%n", this.id);
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("Sono il viaggiatore %d, ho acquistato il biglietto%n", this.id);
        }
    }

    public static void main(String[] args) {
        ArrayBlockingQueue<Runnable> salaDattesa = new ArrayBlockingQueue<>(10);
        ThreadPoolExecutor service = new ThreadPoolExecutor(5,
                5,
                0,
                TimeUnit.SECONDS,
                salaDattesa,
                new SalaDattesaPiena());
        for (int i = 0; i < 50; i++) {
            Viaggiatore viaggiatore = new Viaggiatore(i);
            service.execute(viaggiatore);
        }
        service.shutdown();
    }
}