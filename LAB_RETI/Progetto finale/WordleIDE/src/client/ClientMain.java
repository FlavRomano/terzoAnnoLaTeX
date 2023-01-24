package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

// compila:  javac -d out ./client/*.java
// esegui:   java -cp out client.ClientMain
public class ClientMain {
    static ClientSetup setup = new ClientSetup();
    static String username;
    static boolean quit = false;

    /**
     * @desc Send username and password
     *       to Server in the format "username:password"
     */
    public static String sendInfo(Scanner in, Scanner scanner, PrintWriter out) {
        System.out.format("Username: ");
        String username = scanner.nextLine();
        System.out.format("Password: ");
        String password = scanner.nextLine();
        out.println(String.format("%s:%s", username, password));
        return in.nextLine();
    }

    public static void printGameMenu() {
        System.out.println(setup.GAMEMENU);
    }

    public static void printInstruction() {
        System.out.println("> Welcome to Wordle! You have 12 attempts to guess the word.");
        System.out.println(
                "\t- If a character in my message is \u001B[32mgreen\u001B[0m then it's in the right position.");
        System.out.println(
                "\t- If it's \u001B[33myellow\u001B[0m then it appears in the secret word but not in that position.");
        System.out.println("\t- Otherwise it's wrong.");
    }

    /**
     * @desc Handles user sign in, login and client termination.
     */
    public static void accessPhase(Scanner in, Scanner scanner, PrintWriter out) {
        boolean stop = false;
        while (!stop) {
            System.out.println(setup.ACCESSMENU);
            String line = scanner.nextLine();
            switch (line) {
                case "1": {
                    // signup
                    System.out.println("--- Sign up ---");
                    out.println("1");
                    String serverResponse = sendInfo(in, scanner, out);
                    switch (serverResponse) {
                        case "INVUSR":
                            System.out.println("> Invalid username");
                            break;
                        case "INVPSW":
                            System.out.println("> Invalid password");
                            break;
                        case "YETREG":
                            System.out.println("> User already registered");
                            break;
                        default:
                            System.out.println("> Successfully signed in...");
                            System.out.println("> Login now, press 2");
                    }
                    break;
                }
                case "2": {
                    // login
                    System.out.println("--- Login ---");
                    out.println("2");
                    String serverResponse = sendInfo(in, scanner, out);
                    switch (serverResponse) {
                        case "NOTREG":
                            System.out.println("> User not listed in the system");
                            break;
                        case "WRGPSW":
                            System.out.println("> Wrong password, please retry");
                            break;
                        case "YETLOG":
                            System.out.println("> User already logged in");
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

    /**
     * @desc It manages the entire Wordle phase, notifying the server
     *       of user interactions and acting appropriately.
     */
    public static void gamePhase(Scanner in, Scanner scanner, PrintWriter out) {
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
                                String guessedWord = scanner.nextLine().toLowerCase();
                                out.println(guessedWord);
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
                    else
                        System.out.println("> Game successfully posted!");
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
        String host = setup.getHost();
        int port = setup.getServerPort();
        try (
                Socket socket = new Socket(host, port);
                Scanner in = new Scanner(socket.getInputStream());
                Scanner scanner = new Scanner(System.in);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
            accessPhase(in, scanner, out);
            if (!quit) {
                printInstruction();
                String multicastGroup = setup.getMulticastGroup();
                int multicastGroupPort = setup.getMulticastGroupPort();
                Thread udpReceiver = new Thread(new UDPReceiver(multicastGroup, multicastGroupPort, username));
                udpReceiver.start();
                gamePhase(in, scanner, out);
            }
        } catch (ConnectException e) {
            System.err.println("Not connected, can't reach server.");
        }
    }
}