// RIASSUNTO
// Utenti: { Studenti, Tesisti, Professori }
// Tutor = colui che permette l'accesso al laboratorio
// Computer: {c0,...,c19}
// Richiesta professori = accesso a TUTTO il laboratorio.
// Richiesta tesisti = accesso al singolo computer identificato dall'indice i.
// Richiesta studenti = accesso a qualsiasi computer libero.
// Sia pX la priorità con X utente => pP > pT > pS.

// REQUISITI
// Input: { #Studenti, #Tesisti, #Professori }
// Il main attiva un thread per ogni utente
// Ogni utente accede k volte al laboratorio (k random da 0 a 8)
// simulare l'intervallo di permanenza in laboratorio con una Thread.sleep(random(0, 1000))
// Il programma termina quando tutti gli utenti hanno completato i loro accessi al laboratorio.
// Utenti = Threads ; Laboratorio = Monitor ; Tutor = Main.

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    static protected final int MAXPRIORITY = 2;
    static protected final int MIDPRIORITY = 1;
    static protected final int MINPRIORITY = 0;
    static public class Persona implements Runnable {
        int priority;
        int computerID = (int) (Math.random() * 20);
        int k = (int) (Math.random() * 9);
        Laboratorio lab;
        public Persona (Laboratorio lab) {
            this.lab = lab;
        }
        public void run() {
            for (int i = 0; i < k; i++) {
                lab.accesso(this);
                try {
                    Thread.sleep((long) (Math.random() * 1001));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lab.uscita(this);
                try {
                    Thread.sleep(850);
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

    public static void main(String[] args) {
        Laboratorio lab = new Laboratorio();
        int nStudenti = 20;
        int nTesisti = 30;
        int nProfessori = 0;
        if (args.length > 0) {
            nStudenti = Integer.parseInt(args[0]);
            nTesisti = Integer.parseInt(args[1]);
            nProfessori = Integer.parseInt(args[2]);
        }
        int size = nStudenti + nTesisti + nProfessori;
        PriorityBlockingQueue<Runnable> queue =
                new PriorityBlockingQueue<>(size, Comparator.comparingInt(p -> ((Persona) p).priority));
        ThreadPoolExecutor service = new ThreadPoolExecutor(size,
                size,
                0,
                TimeUnit.MILLISECONDS,
                queue);
        int i = size;
        while (i > 0) {
            if (nProfessori > 0) {
                service.execute(new Professore(lab));
                nProfessori--;
            }
            if (nStudenti > 0) {
                service.execute(new Studente(lab));
                nStudenti--;
            }
            if (nTesisti > 0) {
                service.execute(new Tesista(lab));
                nTesisti--;
            }
            i--;
        }
        service.shutdown();
    }
}