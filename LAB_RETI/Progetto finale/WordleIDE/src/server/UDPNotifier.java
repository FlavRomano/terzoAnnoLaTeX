package server;

import java.io.IOException;
import java.net.*;

/**
 * @desc Class that sends notifications to the multicast group that all clients
 *       join.
 *       The notifications are used to let users know that someone has just
 *       posted a game session.
 * @implNote The following information may change due server configuration file.
 * @host 226.226.226.226
 * @port 4000
 * @groupPort 4444
 */
public class UDPNotifier {
    int port;
    String host;
    int groupPort;

    public UDPNotifier(String multicastGroup, int multicastGroupPort, int notifierPort) {
        this.port = notifierPort;
        this.host = multicastGroup;
        this.groupPort = multicastGroupPort;
    }

    public void sendToGroup(String username, boolean quit) {
        try (MulticastSocket multicastSocket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(host);
            multicastSocket.joinGroup(group);
            String message = quit ? String.format("%s QUIT NOW", username)
                    : String.format("> %s shared a game!", username);
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, groupPort);
            multicastSocket.send(packet);
            multicastSocket.leaveGroup(group);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}