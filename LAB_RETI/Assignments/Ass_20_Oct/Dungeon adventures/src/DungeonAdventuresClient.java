import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DungeonAdventuresClient {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    public final static String TITLE = ANSI_PURPLE +
            "█▀▄ █░█ █▄░█ █▀▀ █▀▀ █▀█ █▄░█   ▄▀█ █▀▄ █░█ █▀▀ █▄░█ ▀█▀ █░█ █▀█ █▀▀ █▀\n" +
            "█▄▀ █▄█ █░▀█ █▄█ ██▄ █▄█ █░▀█   █▀█ █▄▀ ▀▄▀ ██▄ █░▀█ ░█░ █▄█ █▀▄ ██▄ ▄█"
            + ANSI_RESET;
    public final static String COMMANDS = "+--------+----------------+-------------------------------------------------+\n" +
            "| \033[0;1mnumpad\u001B[0m |    \033[0;1mcommand\u001B[0m     |                  \033[0;1m description\u001B[0m                   |\n" +
            "+========+================+=================================================+\n" +
            "|    1   | 'fight', 'f'   | fight the monster or action                     |\n" +
            "+--------+----------------+-------------------------------------------------+\n" +
            "|    2   | 'heal', 'h'    | heal the player                                 |\n" +
            "+--------+----------------+-------------------------------------------------+\n" +
            "|    3   | 'quit', 'q'    | quit the game                                   |\n" +
            "+--------+----------------+-------------------------------------------------+\n" +
            "|    4   | 'rematch', 'r' | request a rematch (only in case of win or draw) |\n" +
            "+--------+----------------+-------------------------------------------------+\n" +
            "|    5   | 'help'         | print this table                                |\n" +
            "+--------+----------------+-------------------------------------------------+";
    public final static int PORT = 1313;

    public static boolean stringValidator(String line) {
        return "fight".equals(line) || "f".equals(line) || "1".equals(line) ||
                "heal".equals(line) || "h".equals(line) || "2".equals(line) ||
                "quit".equals(line) || "q".equals(line) || "3".equals(line) ||
                "rematch".equals(line) || "r".equals(line) || "4".equals(line) ||
                "help".equals(line) || "5".equals(line);
    }

    public static void main(String[] args) {
        System.out.println(TITLE);
        System.out.println(COMMANDS);
        try (
                Socket socket = new Socket("",PORT);
                Scanner in = new Scanner(socket.getInputStream());
                Scanner scanner = new Scanner(System.in)
        ) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            int faultyInputs = System.in.available();
            while (faultyInputs > 0) {
                // for skipping random commands user may type before the game started
                int commandLen = scanner.nextLine().length() + 1; // we must consider the CR (e.g "f\n", "aaa\n",)
                faultyInputs -= commandLen;
            }
            String connString = in.nextLine();
            System.out.println(connString);
            String initialState = in.nextLine();
            System.out.println(initialState);
            boolean end = false;
            while (!end) {
                String line = scanner.nextLine();
                if (stringValidator(line)) {
                    if (line.equals("help") || line.equals("5")) {
                        System.out.println(COMMANDS);
                    } else {
                        out.println(line);
                        String nextLine = in.nextLine();
                        if (nextLine.contains("lost")) {
                            end = true;
                            System.out.println(nextLine);
                            nextLine = in.nextLine();
                            System.out.println(nextLine);
                        } else if (nextLine.contains("wins")) {
                            end = true;
                            System.out.println(nextLine);
                        } else {
                            System.out.println(nextLine);
                        }
                    }
                } else {
                    System.out.println(ANSI_RED+"Not a command"+ANSI_RESET);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}