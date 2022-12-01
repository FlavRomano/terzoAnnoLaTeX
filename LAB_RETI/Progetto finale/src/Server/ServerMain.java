package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

// compila:  javac -cp :./Server/gson-2.10.jar -d Out ./Server/*.java
// esegui:   java -cp :./Server/gson-2.10.jar:Out Server.ServerMain
public class ServerMain implements Runnable {
    static ServerSetup setup = new ServerSetup();
    static long wordTTL = setup.getWordTTL();
    static WordsReader wordReader = new WordsReader(wordTTL);
    String game;
    Socket socket;
    ServerUserAccess serverUserAccess;
    UDPNotifier udpNotifier;

    public ServerMain(Socket socket, UDPNotifier udpNotifier) {
        this.socket = socket;
        this.serverUserAccess = new ServerUserAccess();
        this.udpNotifier = udpNotifier;
    }

    public User accessHandler(Scanner in, PrintWriter out) throws IOException {
        User user = null;
        try {
            while (true) {
                String line = in.nextLine();
                if (line.equals("1")) {
                    // signup
                    String userInfo = in.nextLine();
                    String code = serverUserAccess.signup(userInfo);
                    out.println(code);
                } else if (line.equals("2")) {
                    // login
                    String userInfo = in.nextLine();
                    String username = userInfo.substring(0, userInfo.indexOf(":"));
                    user = serverUserAccess.getUser(username);
                    String code = serverUserAccess.login(userInfo);
                    out.println(code);
                    if (code.equals(username)) {
                        user.login();
                        serverUserAccess.updateUser(user);
                        break;
                    }
                }
            }
        } catch (NoSuchElementException ignored) {

        }
        return user;
    }

    public void gameHandler(Scanner in, PrintWriter out, User user) throws IOException {
        boolean stop = false;
        if (user == null)
            return;
        try {
            while (!stop) {
                String line = in.nextLine();
                String secretWord = wordReader.getExtractedWord();
                WordleHandler wordleHandler = new WordleHandler(secretWord);
                System.out.println(secretWord);
                switch (line) {
                    case "1":
                        // play
                        if (user.isPlayed(secretWord))
                            out.println("ko");
                        else {
                            user.addWord(secretWord);
                            user.statistics.numberOfPlays++;
                            serverUserAccess.updateUser(user);
                            int numberOfGuess = wordleHandler.playWordle(in, out, wordReader, secretWord);
                            game = wordleHandler.game;
                            if (numberOfGuess < 13)
                                user.win(numberOfGuess);
                            else
                                user.lost();
                            user.statistics.updateWinningPercentage();
                            serverUserAccess.updateUser(user);
                        }
                        break;
                    case "2":
                        // show statistics
                        String[] statistics = user.getStatistics();
                        for (String statistic : statistics) {
                            out.println(statistic);
                        }
                        break;
                    case "3":
                        // share results
                        if (game != null) {
                            serverUserAccess.postGame(user, game);
                            udpNotifier.sendToGroup(user.username, false);
                            game = null;
                            out.println("ok");
                        } else {
                            out.println("ko");
                        }
                        break;
                    case "4":
                        serverUserAccess.sendSocial(out);
                        break;
                    case "5":
                        // logout
                        user.logout();
                        serverUserAccess.updateUser(user);
                        udpNotifier.sendToGroup(user.username, true);
                        stop = true;
                        break;
                }
            }
        } catch (NoSuchElementException e) {
            user.logout();
            serverUserAccess.updateUser(user);
        }
    }

    public void run() {
        try (Scanner in = new Scanner(socket.getInputStream())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            User user = accessHandler(in, out);
            gameHandler(in, out, user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        int port = setup.getPort();
        int nThreads = setup.getNThreads();
        ExecutorService service = Executors.newFixedThreadPool(nThreads);
        UDPNotifier udpNotifier = new UDPNotifier();
        service.execute(wordReader);
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.format("Wordle server listening at port %d%n", port);
            while (true) {
                service.execute(new ServerMain(listener.accept(), udpNotifier));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}