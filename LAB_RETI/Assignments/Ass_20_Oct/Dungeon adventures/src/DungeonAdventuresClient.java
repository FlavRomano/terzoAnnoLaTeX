import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DungeonAdventuresClient {
    public final static int PORT = 1313;
    public static boolean stringValidator(String line){
        return "fight".equals(line) || "rematch".equals(line) || "heal".equals(line) || "exit".equals(line);
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Immettere l'IP del server");
        } else {
            Scanner scanner = null;
            Scanner in = null;
            try (Socket socket = new Socket(args[0], PORT)) {
                System.out.println("Immettere le righe di testo, 'exit' per uscire.");
                scanner = new Scanner(System.in);
                in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                boolean end = false;
                while (!end) {
                    String line = scanner.nextLine();
                    if (stringValidator(line)) {
                        if ("exit".equals(line)) {
                            end = true;
                        }
                        out.println(line);
                        String nextLine = in.nextLine();
                        if (nextLine.contains("loses")) {
                            end = true;
                        }
                        System.out.println(nextLine);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                assert scanner != null;
                assert in != null;
                scanner.close();
                in.close();
            }
        }
    }
}
