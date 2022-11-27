import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// compila:  javac -cp :gson-2.10.jar ClientMain.java
// esegui:   java -cp :gson-2.10.jar ClientMain


// java -cp :gson-2.10.jar ServerMain
public class ClientMain {
    static ClientSetup setup = new ClientSetup();
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
    public static String sendInfo(Scanner in, Scanner scanner, PrintWriter out) {
        System.out.format("Username: ");
        String username = scanner.nextLine();
        System.out.format("Password: ");
        String password = scanner.nextLine();
        out.println(String.format("%s:%s", username, password));
        return in.nextLine();
    }
    public static void accessPhase(Scanner in, Scanner scanner, PrintWriter out) throws IOException {
        System.out.println(ACCESSMENU);
        boolean stop = false;
        while (!stop) {
            String line = scanner.nextLine();
            switch (line) {
                case "1": {
                    // signup
                    System.out.println("--- Sign up ---");
                    out.println("1");
                    String serverResponse = sendInfo(in, scanner, out);
                    switch (serverResponse) {
                        case "ok":
                            System.out.println("> Successfully signed in...");
                            System.out.println("> Login now, press 2");
                            break;
                        case "ko":
                            System.out.println("> Invalid password");
                            break;
                        case "YETREG":
                            System.out.println("> User already registered");
                            break;
                    }
                    break;
                }
                case "2": {
                    // login
                    System.out.println("--- Login ---");
                    out.println("2");
                    String serverResponse = sendInfo(in, scanner, out);
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
                    break;
                }
            }
        }
    }
    public static void gamePhase(Scanner in, Scanner scanner, PrintWriter out) throws IOException {
        System.out.println(GAMEMENU);
        boolean stop = false;
        while (!stop) {
            String line = scanner.nextLine();
            switch (line) {
                case "1":
                    // play
                    break;
                case "2":
                    // show statistics
                    break;
                case "3":
                    // share results
                    break;
                case "4":
                    // show players progress
                    break;
                case "5":
                    // logout
                    out.println("5");
                    String serverResponse = in.nextLine();
                    if (serverResponse.equals("ok")) {
                        System.out.println("> Successfully logged out, have a nice one");
                        stop = true;
                    }
                    break;
            }
        }
    }
    public static void main(String[] args) {
        int port = setup.getServerPort();
        String host = setup.getHost();
        try (
                Socket socket = new Socket(host, port);
                Scanner in = new Scanner(socket.getInputStream());
                Scanner scanner = new Scanner(System.in);
        ) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            accessPhase(in, scanner, out);
            gamePhase(in, scanner, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}