import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class DayTimeServerUDP {
    public static void main(String[] args) {
        // 1. Aprire un DatagramSocket su una porta nota
        try (DatagramSocket socket = new DatagramSocket(13)) {
            System.out.println("Server listening on port 13");
            // 2. Creare un pacchetto in cui ricevere la richiesta del client
            DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
            socket.receive(request);
            // 3. Creare un pacchetto di risposta
            String daytime = new Date().toString();
            byte[] data = daytime.getBytes(StandardCharsets.US_ASCII);
            InetAddress host = request.getAddress();
            int port = request.getPort();
            DatagramPacket response = new DatagramPacket(data, data.length, host, port);
            // 4. Inviare la sposta usando la stessa socket da cui si è ricevuto il pacchetto
            socket.send(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
