import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// compila:  javac -cp :gson-2.10.jar ClientMain.java
// esegui:   java -cp :gson-2.10.jar ClientMain

public class ClientMain {
    final static String ACCESSMENU = "+--------+------------------------------+\n" +
            "| numpad |          description         |\n" +
            "+========+==============================+\n" +
            "|    1   |     Create a new account     |\n" +
            "+--------+------------------------------+\n" +
            "|    2   | Login to an existing account |\n" +
            "+--------+------------------------------+";
    final static String GAMEMENU = "+--------+------------------------------+\n" +
            "| numpad |          description         |\n" +
            "+========+==============================+\n" +
            "|    1   | Play Wordle, guess the word! |\n" +
            "+--------+------------------------------+\n" +
            "|    2   |        Show statistics       |\n" +
            "+--------+------------------------------+\n" +
            "|    3   |      Share your results      |\n" +
            "+--------+------------------------------+\n" +
            "|    4   |     Show players progress    |\n" +
            "+--------+------------------------------+\n" +
            "|    5   |            Logout            |\n" +
            "+--------+------------------------------+";
    final static int PORT = 1313;

    public static String sendInfo(Scanner scanner, Scanner in, PrintWriter out) {
        System.out.format("Username: ");
        String username = scanner.nextLine();
        System.out.format("Password: ");
        String password = scanner.nextLine();
        out.println(String.format("%s:%s", username, password));
        return in.nextLine();
    }

    public static void accessPhase(Socket socket, Scanner in, Scanner scanner, PrintWriter out) throws IOException {
        System.out.println(ACCESSMENU);
        boolean stop = false;
        while (!stop) {
            String line = scanner.nextLine();
            if (line.equals("1")) {
                // signup
                System.out.println("--- Sign up ---");
                out.println("1");
                String serverResponse = sendInfo(scanner, in, out);
                switch (serverResponse) {
                    case "ok":
                        System.out.println("> Successfully signed in...");
                        System.out.println("> Login now, press 2");
                        break;
                    case "YETREG":
                        System.out.println("> User already registered");
                        break;
                }
            }
            if (line.equals("2")) {
                // login
                System.out.println("--- Login ---");
                out.println("2");
                String serverResponse = sendInfo(scanner, in, out);
                switch (serverResponse) {
                    case "ok":
                        System.out.println("> Successfully logged in...");
                        stop = true;
                        break;
                    case "NOTREG":
                        System.out.println("> User not listed in the system");
                        break;
                    case "WRGPSW":
                        System.out.println("> Wrong password, please retry");
                        break;
                }
            }
        }
    }
    public static void gamePhase(Socket socket, Scanner in, Scanner scanner, PrintWriter out) throws IOException {
        System.out.println(GAMEMENU);
    }
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", PORT);
                Scanner in = new Scanner(socket.getInputStream());
                Scanner scanner = new Scanner(System.in);
        ) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            accessPhase(socket, in, scanner, out);
            gamePhase(socket, in, scanner, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}