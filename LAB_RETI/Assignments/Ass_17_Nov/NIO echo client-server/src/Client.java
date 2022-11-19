import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);

        try (SocketChannel socketChannelClient = SocketChannel.open(inetSocketAddress);
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connecting to Server on port 8080");
            boolean run = true;
            while (run) {
                String line = scanner.nextLine();
                if (line.equals("quit")) {
                    // va bene anche un SIGINT per terminare
                    run = false;
                }
                else {
                    // scrittura
                    byte[] message = line.getBytes();
                    ByteBuffer buffer = ByteBuffer.wrap(message);
                    socketChannelClient.write(buffer);
                    // lettura
                    ByteBuffer outBuffer = ByteBuffer.allocate(1024);
                    socketChannelClient.read(outBuffer);
                    String response = new String(outBuffer.array()).trim();
                    System.out.println(response);
                }
            }
        }

    }
}
