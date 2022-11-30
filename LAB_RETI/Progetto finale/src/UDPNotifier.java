import java.io.IOException;
import java.net.*;

public class UDPNotifier {
    String host = "226.226.226.226";
    int port = 4000;
    int groupPort = 4444;

    public void sendToGroup(String username, boolean quit) {
        try (MulticastSocket multicastSocket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(host);
            multicastSocket.joinGroup(group);
            String message = quit ? String.format("%s QUITTING", username) : String.format("> %s shared a game!", username);
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, groupPort);
            multicastSocket.send(packet);
            multicastSocket.leaveGroup(group);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}