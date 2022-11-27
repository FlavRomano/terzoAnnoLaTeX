import java.io.IOException;
import java.net.*;

public class PingClient {
    static String hostName;
    static int port;
    static int packetsReceived = 0;
    static long minRtt = 2000;
    static long totRtt = 0;
    static long maxRtt = -1;

    public static void printStat() {
        System.out.println("--- PING statistics ---");
        double percentage = (1 - ((float) packetsReceived / 10.0)) * 100;
        System.out.format("%d packets transmitted, %d packets received, %.1f%% packet loss%n",
                10, packetsReceived, percentage);
        System.out.format("round-trip (ms) min/avg/max = %d/%.2f/%d%n",
                minRtt, (double) totRtt / 10, maxRtt);
    }

    public static boolean checkArgs(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: PingClient HOSTNAME PORT");
            return false;
        } else {
            hostName = args[0];
            try {
                port = Integer.parseInt(args[1]);
                if (port < 1024 || port > 65535) {
                    System.err.println("ERR - arg 2");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.err.println("ERR - arg 2");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        if (checkArgs(args)) {
            try (DatagramSocket socket = new DatagramSocket(0)) {
                socket.setSoTimeout(15000);
                InetAddress host = InetAddress.getByName(hostName);
                for (int i = 0; i < 10; i++) {
                    long start = System.currentTimeMillis();
                    String message = String.format("PING %d %d", i, start);
                    System.out.format("%s RTT: ", message);
                    DatagramPacket request = new DatagramPacket(message.getBytes(), message.length(), host, port);
                    socket.send(request);
                    byte[] data = new byte[2];
                    DatagramPacket response = new DatagramPacket(data, data.length);
                    socket.receive(response);
                    String serverResponse = new String(data);
                    long end = System.currentTimeMillis();
                    long rtt = end - start;
                    if (serverResponse.equals("ko") || rtt >= 2000) {
                        System.out.format("*%n");
                    } else {
                        System.out.format("%d ms.%n", rtt);
                        packetsReceived++;
                        if (maxRtt < rtt) maxRtt = rtt;
                        if (minRtt > rtt) minRtt = rtt;
                        totRtt += rtt;
                    }
                }
                printStat();
            } catch (UnknownHostException e) {
                System.err.println("ERR -arg 1");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
