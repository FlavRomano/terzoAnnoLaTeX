import java.util.Comparator;
import java.util.concurrent.*;

public class Main {
    static protected final int MAXPRIORITY = 2;
    static protected final int MIDPRIORITY = 1;
    static protected final int MINPRIORITY = 0;
    static public class Persona implements Runnable {
        int priority;
        int computerID = ThreadLocalRandom.current().nextInt(20);
        int k = ThreadLocalRandom.current().nextInt(9);
        Laboratorio lab;
        public Persona (Laboratorio lab) {
            this.lab = lab;
        }
        public void run() {
            for (int i = 0; i < k; i++) {
                lab.accesso(this);
                try {
                    Thread.sleep((long) (Math.random() * 1001));
                    lab.uscita(this);
                    Thread.sleep((long) (Math.random() * 851));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void setPriority(int priority) {
            this.priority = priority;
        }
    }
    static public class Professore extends Persona {
        public Professore(Laboratorio lab) {
            super(lab);
            setPriority(MAXPRIORITY);
        }
    }
    static public class Tesista extends Persona {
        public Tesista(Laboratorio lab) {
            super(lab);
            setPriority(MIDPRIORITY);
        }
    }
    static public class Studente extends Persona {
        public Studente(Laboratorio lab) {
            super(lab);
            setPriority(MINPRIORITY);
        }
    }
    static public class Tutor implements Runnable {
        int size;
        PriorityBlockingQueue<Runnable> queueUtenti;
        ExecutorService service;
        public Tutor(int size, PriorityBlockingQueue<Runnable> queueUtenti) {
            this.size = size;
            this.queueUtenti = queueUtenti;
            this.service = Executors.newFixedThreadPool(size);
        }
        public void run() {
            while (size > 0) {
                if (queueUtenti.peek() != null) {
                    service.execute(queueUtenti.peek());
                    queueUtenti.remove();
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                --size;
            }
            service.shutdown();
        }
    }
    public static void main(String[] args) {
        Laboratorio lab = new Laboratorio();
        if (args.length == 0) {
            System.out.println("Non hai immesso #Studenti #Tesisti #Professori");
        } else {
            int nStudenti = Integer.parseInt(args[0]);
            int nTesisti = Integer.parseInt(args[1]);
            int nProfessori = Integer.parseInt(args[2]);
            int size = nStudenti + nTesisti + nProfessori;
            PriorityBlockingQueue<Runnable> queue =
                    new PriorityBlockingQueue<>(size, Comparator.comparingInt(p -> ((Persona) p).priority).reversed());
            new Thread(new Tutor(size, queue)).start();
            while (size > 0) {
                if (nStudenti > 0) {
                    queue.add(new Studente(lab));
                    --nStudenti;
                }
                if (nTesisti > 0) {
                    queue.add(new Tesista(lab));
                    --nTesisti;
                }
                if (nProfessori > 0) {
                    queue.add(new Professore(lab));
                    --nProfessori;
                }
                --size;
            }
        }
    }
}