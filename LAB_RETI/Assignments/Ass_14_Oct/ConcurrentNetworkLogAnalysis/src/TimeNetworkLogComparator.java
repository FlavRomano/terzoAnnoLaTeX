import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimeNetworkLogComparator {
    public static class Task implements Runnable {
        String[] args;
        boolean concurrent;
        long time;
        public Task(String[] args, boolean concurrent) {
            this.args = args;
            this.concurrent = concurrent;
            this.time = System.currentTimeMillis();
        }
        public void run() {
            if (concurrent) {
                ConcurrentNetworkLogAnalyser.main(args);
            } else {
                NetworkLogAnalyser.main(args);
            }
            long time2 = System.currentTimeMillis();
            time = (time2 - time);
        }
    }
    public static void main(String[] args) {
        Task taskConcurrent = new Task(args, true);
        Task taskSingleThread = new Task(args, false);
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            service.submit(i == 0 ? taskConcurrent : taskSingleThread);
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignored) {;}
        System.out.format("Concurrent Network Log Analyser tempo: %d ms.%n", taskConcurrent.time);
        System.out.format("Single Thread Network Log Analyser tempo: %d ms.%n", taskSingleThread.time);
    }
}
// - A casa
// Concurrent Network Log Analyser tempo:    63238 ms.     ~ 1.05  minuti
// Single Thread Network Log Analyser tempo: 484048 ms.    ~ 8     minuti

// - In dipartimento
// Concurrent Network Log Analyser tempo:    13387 ms.     ~ 0.22   minuti
// Single Thread Network Log Analyser tempo: 13421 ms.     ~ 0.22   minuti