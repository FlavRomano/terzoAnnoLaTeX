package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

// compila:  javac -cp :lib/gson-2.10.jar -d out ./server/*.java
// esegui:   java -cp :lib/gson-2.10.jar:out server.ServerMain
public class ServerMain implements Runnable {
    WordsReader wordReader;
    String game;
    Socket socket;
    ServerUserAccess serverUserAccess;
    UDPNotifier udpNotifier;

    public ServerMain(Socket socket, ServerUserAccess sUserAccer, UDPNotifier udpNotifier, WordsReader wReader) {
        this.socket = socket;
        this.serverUserAccess = sUserAccer;
        this.udpNotifier = udpNotifier;
        this.wordReader = wReader;
    }

    /**
     * @return User (who has successfully logged in)
     * @desc Manages the user's login phase, returning appropriate error or
     *       confirmation
     *       codes to the user for sign-in, or login, if successful.
     */
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

    /**
     * @desc Manages the user's game phase.
     * @implNote If the user exits unexpectedly a NoSuchElementException is raised,
     *           which will be handled by the server by successfully logging the
     *           user out.
     */
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
                        for (String statistic : statistics)
                            out.println(statistic);
                        break;
                    case "3":
                        // share results
                        if (game != null) {
                            serverUserAccess.postGame(user, game);
                            udpNotifier.sendToGroup(user.username, false);
                            game = null;
                            out.println("ok");
                        } else
                            out.println("ko");
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
        try (Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            User user = accessHandler(in, out);
            gameHandler(in, out, user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @desc Two threads are sent running before the server accepts user connection
     *       requests:
     *       one to handle Wordle's secret words and the other to send multicast UDP
     *       notifications to
     *       logged-in users (for social).
     * @implNote System.setProperty(...) is used to avoid errors on the
     *           UDPNotifier's multicast socket.
     */
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        ServerSetup setup = new ServerSetup();
        int port = setup.getPort();
        int nThreads = setup.getNThreads();
        long wordTTL = setup.getWordTTL();
        String multicastGroup = setup.getMulticastGroup();
        int multicastGroupPort = setup.getMulticastGroupPort();
        int notifierPort = setup.getNotifierPort();

        UDPNotifier udpNotifier = new UDPNotifier(multicastGroup, multicastGroupPort, notifierPort);
        ExecutorService service = Executors.newFixedThreadPool(nThreads);
        ServerUserAccess serverUserAccess = new ServerUserAccess();
        WordsReader wordsReader = new WordsReader(wordTTL);
        try (ServerSocket listener = new ServerSocket(port)) {
            serverUserAccess.restartingServer();
            System.out.format("Wordle server listening at port %d%n", port);
            new Thread(wordsReader).start();
            while (true)
                service.execute(new ServerMain(listener.accept(), serverUserAccess, udpNotifier, wordsReader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}