package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

// compila:  javac -d Out ./Client/*.java
// esegui:   java -cp Out Client.ClientMain
public class ClientMain {
    static ClientSetup setup = new ClientSetup();
    final static String ACCESSMENU = "+---------+------------------------------+\n" +
            "| command |          description         |\n" +
            "+---------+------------------------------+\n" +
            "|    1    |     Create a new account     |\n" +
            "+---------+------------------------------+\n" +
            "|    2    | Login to an existing account |\n" +
            "+---------+------------------------------+\n" +
            "|    q    |         Close client         |\n" +
            "+---------+------------------------------+";
    final static String GAMEMENU = "+---------+------------------------------+\n" +
            "| command |          description         |\n" +
            "+---------+------------------------------+\n" +
            "|    1    | Play Wordle, guess the word! |\n" +
            "+---------+------------------------------+\n" +
            "|    2    |        Show statistics       |\n" +
            "+---------+------------------------------+\n" +
            "|    3    |      Share your results      |\n" +
            "+---------+------------------------------+\n" +
            "|    4    |       Show WordleBook        |\n" +
            "+---------+------------------------------+\n" +
            "|    5    |            Logout            |\n" +
            "+---------+------------------------------+\n" +
            "|    h    |    Show game instructions    |\n" +
            "+---------+------------------------------+\n" +
            "|    m    |           Show menu          |\n" +
            "+---------+------------------------------+";
    static String username;
    static boolean quit = false;

    public static String sendInfo(Scanner in, Scanner scanner, PrintWriter out) {
        System.out.format("Username: ");
        String username = scanner.nextLine();
        System.out.format("Password: ");
        String password = scanner.nextLine();
        out.println(String.format("%s:%s", username, password));
        return in.nextLine();
    }

    public static void printGameMenu() {
        System.out.println(GAMEMENU);
    }

    public static void printInstruction() {
        System.out.println("> Welcome to Wordle! You have 12 attempts to guess the word.");
        System.out.println("\t- If a character in my message is \u001B[32mgreen\u001B[0m then it's in the right position.");
        System.out.println("\t- If it's \u001B[33myellow\u001B[0m then it appears in the secret word but not in that position.");
        System.out.println("\t- Otherwise it's wrong.");
    }

    public static void accessPhase(Scanner in, Scanner scanner, PrintWriter out) throws IOException {
        boolean stop = false;
        while (!stop) {
            System.out.println(ACCESSMENU);
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
                        case "INVUSR":
                            System.out.println("> Invalid username");
                            break;
                        case "INVPSW":
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
                        case "ko":
                            System.out.println("> User already logged in");
                            break;
                        case "NOTREG":
                            System.out.println("> User not listed in the system");
                            break;
                        case "WRGPSW":
                            System.out.println("> Wrong password, please retry");
                            break;
                        default:
                            System.out.println("> Successfully logged in...");
                            username = serverResponse;
                            stop = true;
                    }
                    break;
                }
                case "q": {
                    // quit
                    stop = quit = true;
                    break;
                }
            }
        }
    }

    public static void gamePhase(Scanner in, Scanner scanner, PrintWriter out) throws IOException {
        boolean stop = false;
        printGameMenu();
        while (!stop) {
            String line = scanner.nextLine();
            String serverResponse;
            switch (line) {
                case "1":
                    out.println("1");
                    serverResponse = in.nextLine();
                    if (serverResponse.equals("ko")) {
                        System.out.println("> You've played with this word before");
                    } else {
                        boolean gameOver = false;
                        while (!gameOver) {
                            System.out.println(serverResponse);
                            if (serverResponse.contains("✓") || serverResponse.contains("✗")) {
                                serverResponse = in.nextLine();
                                System.out.println(serverResponse);
                                printGameMenu();
                                gameOver = true;
                            } else {
                                out.println(scanner.nextLine());
                                serverResponse = in.nextLine();
                            }
                        }
                    }
                    break;
                case "2":
                    // show statistics
                    out.println("2");
                    for (int i = 0; i < 17; i++) {
                        serverResponse = in.nextLine();
                        System.out.println(serverResponse);
                    }
                    break;
                case "3":
                    // share results
                    out.println("3");
                    serverResponse = in.nextLine();
                    if (serverResponse.equals("ko"))
                        System.out.println("> You must have played at least one word first");
                    break;
                case "4":
                    // show me sharing
                    out.println("4");
                    serverResponse = in.nextLine();
                    while (!serverResponse.equals("EOF")) {
                        System.out.println(serverResponse);
                        serverResponse = in.nextLine();
                    }
                    break;
                case "5":
                    // logout
                    out.println("5");
                    System.out.println("> Successfully logged out, have a nice one");
                    stop = true;
                    break;
                case "m":
                    printGameMenu();
                    break;
                case "h":
                    printInstruction();
                    break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        int port = setup.getServerPort();
        String host = setup.getHost();
        try (
                Socket socket = new Socket(host, port);
                Scanner in = new Scanner(socket.getInputStream());
                Scanner scanner = new Scanner(System.in);
        ) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            accessPhase(in, scanner, out);
            if (!quit) {
                printInstruction();
                Thread udpReceiver = new Thread(new UDPReceiver(username));
                udpReceiver.start();
                gamePhase(in, scanner, out);
            }
        } catch (ConnectException e) {
            System.err.println("Not connected, can't reach server.");
        }
    }
}