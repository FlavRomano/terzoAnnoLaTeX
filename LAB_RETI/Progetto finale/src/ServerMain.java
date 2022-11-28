import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerMain implements Runnable {
    static ServerSetup setup = new ServerSetup();
    static long wordTTL = setup.getWordTTL();
    static WordsReader wr = new WordsReader(wordTTL);
    Socket socket;
    ServerUserAccess serverUserAccess;

    public ServerMain(Socket socket) {
        this.socket = socket;
        this.serverUserAccess = new ServerUserAccess();
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
                    if (code.equals("ok")) {
                        out.println(code);
                        break;
                    }
                    out.println(code);
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
                String secretWord = wr.getExtractedWord();
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
                            boolean win = wordleHandler.playWordle(in, out, secretWord);
                            if (win)
                                user.win();
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
                        break;
                    case "4":
                        // show me sharing
                        break;
                    case "5":
                        // logout
                        user.logout();
                        serverUserAccess.updateUser(user);
                        String code = "ok";
                        out.println(code);
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
        int port = setup.getPort();
        int nThreads = setup.getNThreads();
        ExecutorService service = Executors.newFixedThreadPool(nThreads);
        service.execute(wr);
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.format("Wordle server listening at port %d%n", port);
            while (true) {
                service.execute(new ServerMain(listener.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}