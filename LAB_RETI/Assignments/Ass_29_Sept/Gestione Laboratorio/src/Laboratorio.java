public class Laboratorio {
    boolean professoreInAula = false;
    int[] computers = new int[20];
    int postiLiberi = 20;

    public Laboratorio() {
        for (int i = 0; i < 20; i++) {
            computers[i] = 0;
        }
    }

    public synchronized void accesso(Main.Persona p) {
        boolean full = (postiLiberi == 0);
        if (p.priority == 0) {
            while (full || professoreInAula) {
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
            notifyAll();
        }
        if (p.priority == 1) {
            while (computers[p.computerID] == 1 || full || professoreInAula) {
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
            System.out.format("Tesista %s acquisisce PC%d%n", Thread.currentThread().getName(), p.computerID);
            notifyAll();
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
            notifyAll();
        }
    }

    public synchronized void uscita(Main.Persona p) {
        if (p.priority == 0) {
            ++postiLiberi;
            System.out.format("Studente %s libera un PC%n",
                    Thread.currentThread().getName());
            notifyAll();
        }
        if (p.priority == 1) {
            ++postiLiberi;
            computers[p.computerID] = 0;
            System.out.format("Tesista %s libera PC%d%n",
                    Thread.currentThread().getName(), p.computerID);
            notifyAll();
        }
        if (p.priority == 2) {
            professoreInAula = false;
            System.out.format("Professore %s libera l'aula%n",
                    Thread.currentThread().getName());
            notifyAll();
        }
    }
}
