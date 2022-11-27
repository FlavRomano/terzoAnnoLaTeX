import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class PingServer {
    static long seed = 123;
    static int port;

    public static boolean checkArgs(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: PingServer PORT [SEED]");
            return false;
        } else {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("ERR - arg 1");
                return false;
            }
            try {
                if (args.length > 1) {
                    seed = Integer.parseInt(args[1]);
                }
            } catch (NumberFormatException e) {
                System.err.println("ERR - arg 2");
                return false;
            }
            return true;
        }
    }

    public static void main(String[] args) {
        if (checkArgs(args)) {
            int port = Integer.parseInt(args[0]);
            if (args.length > 1) {
                seed = Integer.parseInt(args[1]);
            }
            try (DatagramSocket socket = new DatagramSocket(port)) {
                System.out.format("PING Server listening on port %d%n", port);
                Random rand = new Random(seed);
                while (true) {
                    for (int i = 0; i < 10; i++) {
                        byte[] data = new byte[1024];
                        DatagramPacket request = new DatagramPacket(data, data.length);
                        socket.receive(request);
                        String clientMessage = new String(data);
                        InetAddress host = request.getAddress();
                        int cPort = request.getPort();
                        System.out.format("%s:%d> %s ACTION: ", host, cPort, clientMessage);
                        boolean sending = rand.nextInt(5) != 1; // 1 su 4 non viene mandato
                        String sent;
                        if (sending) {
                            sent = "ok";
                            DatagramPacket response = new DatagramPacket(sent.getBytes(), sent.length(), host, cPort);
                            long rLong = rand.nextInt(2001);
                            Thread.sleep(rLong);
                            socket.send(response);
                            System.out.format("delayed %d ms%n", rLong);
                        } else {
                            sent = "ko";
                            DatagramPacket response = new DatagramPacket(sent.getBytes(), sent.length(), host, cPort);
                            socket.send(response);
                            System.out.format("not sent%n");
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}