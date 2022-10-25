import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DungeonAdventuresClient {
    public final static String TITLE =
            "█▀▄ █░█ █▄░█ █▀▀ █▀▀ █▀█ █▄░█   ▄▀█ █▀▄ █░█ █▀▀ █▄░█ ▀█▀ █░█ █▀█ █▀▀ █▀\n" +
            "█▄▀ █▄█ █░▀█ █▄█ ██▄ █▄█ █░▀█   █▀█ █▄▀ ▀▄▀ ██▄ █░▀█ ░█░ █▄█ █▀▄ ██▄ ▄█";
    public final static String COMMANDS = "+----------------+-------------------------------------------------+\n" +
            "| commands       | description                                     |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'fight', 'f'   | fight the monster                               |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'heal', 'h'    | heal the player                                 |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'rematch', 'r' | request a rematch (only in case of win or draw) |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'quit', 'q'    | quit the game                                   |\n" +
            "+----------------+-------------------------------------------------+";
    public final static int PORT = 1313;
    public static boolean stringValidator(String line){
        return "fight".equals(line) || "f".equals(line) ||
                "rematch".equals(line) || "r".equals(line) ||
                "heal".equals(line) || "h".equals(line) ||
                "exit".equals(line) || "q".equals(line);
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Immettere l'IP del server");
        } else {
            Scanner scanner = null;
            Scanner in = null;
            try (Socket socket = new Socket(args[0], PORT)) {
                System.out.println(TITLE);
                System.out.println(COMMANDS);
                scanner = new Scanner(System.in);
                in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                boolean end = false;
                while (!end) {
                    String line = scanner.nextLine();
                    if (stringValidator(line)) {
                        if (line.equals("quit") || line.equals("q")) {
                            end = true;
                        } else if (line.equals("help")) {
                            System.out.println(COMMANDS);
                        } else {
                            out.println(line);
                            String nextLine = in.nextLine();
                            if (nextLine.contains("wins")) {
                                end = true;
                            }
                            System.out.println(nextLine);
                        }
                    } else {
                        System.out.println("Not a command");
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
