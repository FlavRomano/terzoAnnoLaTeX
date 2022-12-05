import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Client {
    static final int PORT = 4444;
    public static InetAddress getDateGroup(String[] args) {
        InetAddress dateGroup = null;
        try {
            dateGroup = InetAddress.getByName(args[0]);
        } catch (UnknownHostException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java Client MULTICASTADDRESS");
        }
        return dateGroup;
    }
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        int count = 0;
        InetAddress group = getDateGroup(args);
        if (group != null) {
            try (MulticastSocket multicastSocket = new MulticastSocket(PORT)) {
                multicastSocket.joinGroup(group);
                while (count < 10) {
                    byte[] buffer = new byte[1024];
                    DatagramPacket date = new DatagramPacket(buffer, buffer.length);
                    multicastSocket.receive(date);
                    System.out.printf(" %d. %s%n", count + 1, new String(buffer));
                    ++count;
                }
                multicastSocket.leaveGroup(group);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
