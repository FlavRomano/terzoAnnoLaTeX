import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static class Consumer implements Runnable {
        boolean consumaPari;
        Dropbox dropbox;

        public Consumer(boolean consumaPari, Dropbox dropbox) {
            this.consumaPari = consumaPari;
            this.dropbox = dropbox;
        }

        public void run() {
            while (true) {
                this.dropbox.take(this.consumaPari);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Producer implements Runnable {
        Dropbox dropbox;

        public Producer(Dropbox dropbox) {
            this.dropbox = dropbox;
        }

        public void run() {
            while (true) {
                int randomInt = (int) (Math.random() * 5);
                this.dropbox.put(randomInt);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        Dropbox dropbox = new Dropbox();
        Consumer c1 = new Consumer(true, dropbox);  // c1 consuma pari
        Consumer c2 = new Consumer(false, dropbox); // c2 consuma dispari
        Producer p = new Producer(dropbox);
        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        Thread t3 = new Thread(p);
        t3.start();
        t1.start();
        t2.start();
    }
}