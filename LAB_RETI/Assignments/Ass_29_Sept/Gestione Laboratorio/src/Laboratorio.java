public class Laboratorio {
    boolean accessoProfessore;
    int[] computers;
    int count, postiLiberi;

    public Laboratorio() {
        this.accessoProfessore = false;
        this.computers = new int[20]; // 0 libero, 1 occupato
        this.postiLiberi = 20;
    }
    public synchronized void accessoProf() {
        while (postiLiberi != 20 || accessoProfessore) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.accessoProfessore = true;
            System.out.format("Accesso professore %s%n", Thread.currentThread().getName());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.accessoProfessore = false;
        System.out.format("Fine professore %s%n", Thread.currentThread().getName());
        notifyAll();
    }

    public synchronized void accessoTesista(Main.Tesista r) {
        while (postiLiberi == 0 || accessoProfessore || computers[r.idComputer] == 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.count++;
            computers[r.idComputer] = 1;
            System.out.format("Accesso tesista %s al pc %d %n", Thread.currentThread().getName(), r.idComputer);
            Thread.sleep((long) (Math.random() * 500));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        computers[r.idComputer] = 0;
        this.count--;
        System.out.format("Fine tesista %s%n", Thread.currentThread().getName());
        notifyAll();
    }

    public synchronized void accessoStudente() {
        while (postiLiberi == 0 || accessoProfessore) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.count++;
            System.out.format("Accesso studente %s%n", Thread.currentThread().getName());
            Thread.sleep((long) (Math.random() * 500));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.count--;
        System.out.format("Fine studente %s%n", Thread.currentThread().getName());
        notifyAll();
    }
}
