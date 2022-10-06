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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main {

    public static class Professore implements Runnable {
        Laboratorio lab;
        int priority, accessi;

        public Professore(Laboratorio lab) {
            this.lab = lab;
            this.priority = 2;
            this.accessi = (int) (Math.random() * 8);
        }

        public void run() {
            for (int i = 0; i < this.accessi; i++) {
                lab.accessoProf();
            }
        }
    }

    public static class Tesista implements Runnable {
        Laboratorio lab;
        int priority, idComputer, accessi;

        public Tesista(Laboratorio lab) {
            this.lab = lab;
            this.priority = 1;
            this.idComputer = (int) (Math.random() * 20);
            this.accessi = (int) (Math.random() * 9);
        }

        public void run() {
            for (int i = 0; i < this.accessi; i++) {
                lab.accessoTesista(this);
            }
        }
    }

    public static class Studente implements Runnable {
        Laboratorio lab;
        int priority, accessi;

        public Studente(Laboratorio lab) {
            this.lab = lab;
            this.priority = 0;
            this.accessi = (int) (Math.random() * 8);
        }

        public void run() {
            for (int i = 0; i < this.accessi; i++) {
                lab.accessoStudente();
            }
        }
    }

    public static void main(String[] args) {
        Laboratorio lab = new Laboratorio();
        int numeroStudenti = 0;
        int numeroTesisti = 30;
        int numeroProfessori = 2;
        if (args.length > 0) {
            numeroStudenti = Integer.parseInt(args[0]);
            numeroTesisti = Integer.parseInt(args[1]);
            numeroProfessori = Integer.parseInt(args[2]);
        }
        int totUtenti = numeroStudenti + numeroTesisti + numeroProfessori;
        ExecutorService service = Executors.newFixedThreadPool(totUtenti);
        for (int i = 0; i < totUtenti; i++) {
            if (numeroTesisti > 0) {
                Tesista t = new Tesista(lab);
                service.execute(t);
                numeroTesisti--;
            }
            if (numeroProfessori > 0) {
                Professore p = new Professore(lab);
                service.execute(p);
                numeroProfessori--;
            }
            if (numeroStudenti > 0) {
                Studente s = new Studente(lab);
                service.execute(s);
                numeroStudenti--;
            }
        }
        service.shutdown();
    }
}