import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerMain implements Runnable {
    final static int PORT = 1313;
    Socket socket;
    ServerUserAccess serverUserAccess;
    public ServerMain(Socket socket) {
        this.socket = socket;
        this.serverUserAccess = new ServerUserAccess();
    }
    public void eventHandler(Scanner in, PrintWriter out) throws IOException {
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
                out.println(code);
            }
        }
    }

    public void run() {
        try(Scanner in = new Scanner(socket.getInputStream())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            eventHandler(in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket(PORT)) {
            System.out.format("Wordle server listening at port %d%n", PORT);
            ExecutorService service = Executors.newFixedThreadPool(25);
            while (true) {
                service.execute(new ServerMain(listener.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}