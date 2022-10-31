import java.util.Calendar;

public class DatePrinterThread extends Thread {
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
        DatePrinterThread datePrinter = new DatePrinterThread();
        datePrinter.start();
        String s = Thread.currentThread().getName();
        System.out.printf("%s%n", s);
    }
}