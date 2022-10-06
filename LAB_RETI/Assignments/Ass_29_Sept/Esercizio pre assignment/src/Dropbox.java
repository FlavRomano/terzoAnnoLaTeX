class Dropbox {
    /**
     full è uguale a true se il buffer è pieno, false altrimenti
     */
    protected boolean full = false;
    /**
     * num valore del buffer (utile solo se il buffer è pieno)
     */
    protected int num;

    /**
     * Attende che il buffer contenga un numero, poi lo recupera e lo ritorna
     * @param e indica l'interesse a consumare un numero pari o dispari
     * 			se e == True il numero contenuto è pari, altrimenti è dispari
     * @return numero consumato
     */
    public synchronized int take(boolean e) {
        /* La seguente espressione equivale a:
         * if (e == true) {
         * 		s = "Pari"
         * }
         * else {
         * 		s = "Dispari
         * }
         */
        String s = e ? "Pari" : "Dispari";

        while (!full || e == (num % 2 != 0)) { //num non è quello cercato
            System.out.printf("%s Attendi per: %s%n", Thread.currentThread().getName(), s);
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        System.out.printf("%s %s <-> %d%n", Thread.currentThread().getName(), s, num);
        full = false;
        notifyAll();
        return num;
    }

    /**
     * Attende che il buffer sia vuoto, poi inserisce n all'interno di esso
     * @param n intero da inserire nel buffer
     */
    public synchronized void put(int n) {
        while (full) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Producer ha inserito " + n);
        num = n;
        full = true;
        notifyAll();
    }
}