import java.util.Calendar;

public class DatePrinter {
    public static void main(String[] args) {
        while(true) {
            String date = String.valueOf(Calendar.getInstance().getTime());
            System.out.printf("%s: %s%n", Thread.currentThread().getName(), date);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getName());
    }
}