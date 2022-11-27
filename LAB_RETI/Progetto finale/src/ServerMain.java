import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerMain implements Runnable {
    static ServerSetup setup = new ServerSetup();
    Socket socket;
    ServerUserAccess serverUserAccess;

    public ServerMain(Socket socket) {
        this.socket = socket;
        this.serverUserAccess = new ServerUserAccess();
    }

    public void accessHandler(Scanner in, PrintWriter out) throws IOException {
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.equals("1")) {
                // signup
                String userInfo = in.nextLine();
                String code = serverUserAccess.signup(userInfo);
                out.println(code);
            } else if (line.equals("2")) {
                // login
                String userInfo = in.nextLine();
                String code = serverUserAccess.login(userInfo);
                String username = userInfo.substring(0, userInfo.indexOf(":"));
                out.println(code);
                gameHandler(in, out, username);
                break;
            }
        }
    }

    public void gameHandler(Scanner in, PrintWriter out, String username) throws IOException {
        while (in.hasNextLine()) {
            String line = in.nextLine();
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
                    String code = serverUserAccess.logout(username);
                    out.println(code);
                    break;
            }
        }
    }

    public void run() {
        try (Scanner in = new Scanner(socket.getInputStream())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            accessHandler(in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        int port = setup.getPort();
        int nThreads = setup.getNThreads();
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.format("Wordle server listening at port %d%n", port);
            ExecutorService service = Executors.newFixedThreadPool(nThreads);
            while (true) {
                service.execute(new ServerMain(listener.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}