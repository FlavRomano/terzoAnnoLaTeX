package client;

import java.io.IOException;
import java.net.*;

/**
 * @desc Class that receives UDP messages sent in multicast from the server.
 *       Notifies the user when a player shares a game on the social (press
 *       share).
 * @implNote The following information may change due client configuration file.
 * @multicastGroup 226.226.226.226
 * @port 4444
 */
public class UDPReceiver implements Runnable {
    String username;
    String groupName;
    int port;

    public UDPReceiver(String groupName, int port, String username) {
        this.groupName = groupName;
        this.port = port;
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
                if (message.contains(username) && message.contains("QUIT NOW")) {
                    multicastSocket.leaveGroup(group);
                    break;
                }
                if (!message.contains(username) && !message.contains("QUIT NOW"))
                    System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}