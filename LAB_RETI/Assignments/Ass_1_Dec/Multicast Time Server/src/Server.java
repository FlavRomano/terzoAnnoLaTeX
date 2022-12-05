import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;

public class Server {
    static final int PORT = 4000;
    static final int MCPORT = 4444;
    public static InetAddress getDateGroup(String[] args) {
        InetAddress dateGroup = null;
        try {
            dateGroup = InetAddress.getByName(args[0]);
        } catch (UnknownHostException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java Server MULTICASTADDRESS");
        }
        return dateGroup;
    }
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        InetAddress group = getDateGroup(args);
        if (group != null) {
            try (MulticastSocket multicastSocket = new MulticastSocket(PORT)) {
                System.out.println("Server running...");
                while (true) {
                    byte[] date = new Date().toString().getBytes();
                    DatagramPacket time = new DatagramPacket(date, 0, date.length, group, MCPORT);
                    multicastSocket.send(time);
                    Thread.sleep(750);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}