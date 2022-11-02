import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DungeonAdventuresClient {
    public final static String TITLE =
            "█▀▄ █░█ █▄░█ █▀▀ █▀▀ █▀█ █▄░█   ▄▀█ █▀▄ █░█ █▀▀ █▄░█ ▀█▀ █░█ █▀█ █▀▀ █▀\n" +
            "█▄▀ █▄█ █░▀█ █▄█ ██▄ █▄█ █░▀█   █▀█ █▄▀ ▀▄▀ ██▄ █░▀█ ░█░ █▄█ █▀▄ ██▄ ▄█";
    public final static String COMMANDS = "+----------------+-------------------------------------------------+\n" +
            "| command        | description                                     |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'fight', 'f'   | fight the monster                               |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'heal', 'h'    | heal the player                                 |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'rematch', 'r' | request a rematch (only in case of win or draw) |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'quit', 'q'    | quit the game                                   |\n" +
            "+----------------+-------------------------------------------------+\n" +
            "| 'help'         | print this table                                |\n" +
            "+----------------+-------------------------------------------------+";
    public final static int PORT = 1313;
    public static boolean stringValidator(String line){
        return "fight".equals(line) || "f".equals(line) ||
                "rematch".equals(line) || "r".equals(line) ||
                "heal".equals(line) || "h".equals(line) ||
                "exit".equals(line) || "q".equals(line) ||
                "help".equals(line);
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Immettere l'IP del server");
        } else {
            System.out.println(TITLE);
            System.out.println(COMMANDS);
            try (
                 Socket socket = new Socket(args[0], PORT);
                 Scanner in = new Scanner(socket.getInputStream());
                 Scanner scanner = new Scanner(System.in)
                ) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                System.out.println(in.nextLine());
                System.out.println(in.nextLine()); // Take the initial stats
                boolean end = false;
                while (!end) {
                    String line = scanner.nextLine();
                    if (stringValidator(line)) {
                        if (line.equals("quit") || line.equals("q")) {
                            out.println(line);
                            String nextLine = in.nextLine();
                            end = true;
                            System.out.println(nextLine);
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
            }
        }
    }
}
