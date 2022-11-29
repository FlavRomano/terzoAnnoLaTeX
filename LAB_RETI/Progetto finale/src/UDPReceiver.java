import java.io.IOException;
import java.net.*;

public class UDPReceiver implements Runnable {
    String groupName = "226.226.226.226";
    int port = 4444;
    // gestisci il fatto che ad un utente non debba arrivare la sua stessa notifica di post
    public void receiveUDPMessage() {
        byte[] buffer = new byte[1024];
        try (MulticastSocket multicastSocket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(groupName);
            multicastSocket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);
                String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void run() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        receiveUDPMessage();
    }
}