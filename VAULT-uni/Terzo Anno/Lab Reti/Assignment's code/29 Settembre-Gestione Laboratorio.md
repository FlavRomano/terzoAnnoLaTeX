## Consegna
Il laboratorio di Informatica del Polo Marzotto è utilizzato da tre tipi di utenti (studenti, tesisti e professori) ed ogni utente deve fare una richiesta al tutor per accedere al laboratorio. I computer del laboratorio sono numerati da 1 a 20. Le richieste di accesso sono diverse a seconda del tipo dell'utente:

1.  I professori accedono in modo esclusivo a tutto il laboratorio, poichè hanno necessità di utilizzare tutti i computer per effettuare prove in rete.
2.  I tesisti richiedono l'uso esclusivo di un solo computer, identificato dall'indice i, poiché su quel computer è installato un particolare software necessario per lo sviluppo della tesi.
3.  Gli studenti richiedono l'uso esclusivo di un qualsiasi computer.

I professori hanno priorità su tutti nell'accesso al laboratorio, i tesisti hanno priorità sugli studenti. Nessuno però può essere interrotto mentre sta usando un computer.  
Scrivere un programma Java che simuli il comportamento degli utenti e del tutor.

-   Il programma riceve in ingresso il numero di studenti, tesisti e professori che utilizzano il laboratorio ed attiva un thread per ogni utente.
-   Ogni utente accede `k` volte al laboratorio, con `k` generato casualmente.
-   Simulare l'intervallo di tempo che intercorre tra un accesso ed il successivo e l'intervallo di permanenza in laboratorio mediante il metodo `sleep()`.
-   Il tutor deve coordinare gli accessi al laboratorio.
-   Il programma deve terminare quando tutti gli utenti hanno completato i loro accessi al laboratorio.
-   Simulare gli utenti con dei thread e incapsulare la logica di gestione del laboratorio all'interno di un monitor.
## Codice
Main.java
```java
import java.util.Comparator;
import java.util.concurrent.*;

public class Main {
    static protected final int MAXPRIORITY = 2;
    static protected final int MIDPRIORITY = 1;
    static protected final int MINPRIORITY = 0;

    static public class Persona implements Runnable {
        int priority;
        int computerID = ThreadLocalRandom.current().nextInt(20);
        int k = ThreadLocalRandom.current().nextInt(1, 9);
        Laboratorio lab;

        public Persona(Laboratorio lab) {
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
                    try {
                        service.execute(queueUtenti.peek());
                        queueUtenti.remove();
                        --size;
                    } catch (RejectedExecutionException ignored) {;}
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            service.shutdown();
            try {
                service.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) {;}
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
            int i = size;
            while (i > 0) {
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
                --i;
            }
        }
    }
}
```

Laboratorio.java
```java
public class Laboratorio {
    boolean professoreInAula = false;
    int[] computers = new int[20]; // bitmap: 1 se acquisito altrimenti 0
    int postiLiberi = 20;
    public synchronized void accesso(Main.Persona p) {
        if (p.priority == 0) {
            while (postiLiberi == 0 || professoreInAula) {
                System.out.printf("Studente %s aspetta l'acquisizione di un PC%n",
                        Thread.currentThread().getName());
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            --postiLiberi;
            System.out.format("Studente %s acquisisce un PC%n",
                    Thread.currentThread().getName());
        }
        if (p.priority == 1) {
            while (computers[p.computerID] == 1 || postiLiberi == 0 || professoreInAula) {
                System.out.printf("Tesista %s aspetta l'acquisizione di PC%d%n",
                        Thread.currentThread().getName(), p.computerID);
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            --postiLiberi;
            computers[p.computerID] = 1;
            System.out.format("Tesista %s acquisisce PC%d%n",
                    Thread.currentThread().getName(), p.computerID);
        }
        if (p.priority == 2) {
            while (postiLiberi != 20 || professoreInAula) {
                System.out.printf("Professore %s aspetta l'acquisizione dell'aula%n",
                        Thread.currentThread().getName());
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            professoreInAula = true;
            System.out.format("Professore %s acquisisce l'aula%n",
                    Thread.currentThread().getName());
        }
        notifyAll();
    }

    public synchronized void uscita(Main.Persona p) {
        if (p.priority == 0) {
            ++postiLiberi;
            System.out.format("Studente %s libera un PC%n",
                    Thread.currentThread().getName());
        }
        if (p.priority == 1) {
            ++postiLiberi;
            computers[p.computerID] = 0;
            System.out.format("Tesista %s libera PC%d%n",
                    Thread.currentThread().getName(), p.computerID);
        }
        if (p.priority == 2) {
            professoreInAula = false;
            System.out.format("Professore %s libera l'aula%n",
                    Thread.currentThread().getName());
        }
        notifyAll();
    }
}

```