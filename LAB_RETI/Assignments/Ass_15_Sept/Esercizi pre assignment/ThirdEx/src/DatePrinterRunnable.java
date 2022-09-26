import java.util.Calendar;

public class DatePrinterRunnable implements Runnable {
    @Override
    public void run() {
        while(true) {
            System.out.printf("%s %s%n", Thread.currentThread().getName(), Calendar.getInstance().getTime());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        DatePrinterRunnable printer = new DatePrinterRunnable();
        Thread t = new Thread(printer);
        t.start();
        String s = Thread.currentThread().getName();
        System.out.println(s);
    }
}