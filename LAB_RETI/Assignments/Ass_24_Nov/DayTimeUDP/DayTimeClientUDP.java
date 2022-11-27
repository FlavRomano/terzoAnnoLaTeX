import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class DayTimeClientUDP {
    static final String HOSTNAME = "localhost";
    static final int PORT = 13;
    public static void main(String[] args) {
        // 1. Apri la socket
        try (DatagramSocket socket = new DatagramSocket(0)) {
            // 2. Setta il timeout
            socket.setSoTimeout(15000);
            // 3. Costruire due pacchetti: uno per inviare la richiesta al server,
            //    uno per ricevere la risposta
            InetAddress host = InetAddress.getByName(HOSTNAME);
            DatagramPacket request = new DatagramPacket(new byte[1], 1, host, PORT);
            byte[] data = new byte[1024];
            DatagramPacket response = new DatagramPacket(data, data.length);
            // 4. Mandare la richiesta ed aspettare la risposta
            socket.send(request);
            socket.receive(response);
            // 5. Estrarre i byte dalla risposta e convertirli in String
            String daytime = new String(response.getData(), 0, response.getLength(), StandardCharsets.US_ASCII);
            System.out.println(daytime);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
