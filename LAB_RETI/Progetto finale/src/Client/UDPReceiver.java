package Client;

import java.io.IOException;
import java.net.*;

public class UDPReceiver implements Runnable {
    String username;
    String groupName = "226.226.226.226";
    int port = 4444;
    public UDPReceiver(String username) {
        this.username = username;
    }
    public void run() {
        byte[] buffer = new byte[1024];
        try (MulticastSocket multicastSocket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(groupName);
            multicastSocket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);
                String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                if (message.contains(username) && message.contains("QUITTING"))
                    break;
                if (!message.contains(username) && !message.contains("QUITTING"))
                    System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}