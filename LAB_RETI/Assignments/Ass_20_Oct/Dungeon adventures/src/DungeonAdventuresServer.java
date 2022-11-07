import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DungeonAdventuresServer implements Runnable {
    public final static int PORT = 1313;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    private final Socket socket;

    public DungeonAdventuresServer(Socket socket) {
        this.socket = socket;
    }

    public boolean isQuitting(Scanner in, PrintWriter out, CombatLog combatLog, int wins) {
        while (true) {
            String line = in.nextLine();
            if (line.equals("r")) {
                combatLog.restorePG();
                out.format("%s> Let's fight! Player: %d HP, Monster: %d HP, Potion: %d%s%n",
                        ANSI_YELLOW, combatLog.playerHP, combatLog.monsterHP, combatLog.potion, ANSI_RESET);
                return false;
            } else if (line.equals("q")) {
                out.format("%sTotal wins: %d%s%n", ANSI_PURPLE, wins, ANSI_RESET);
                return true;
            } else {
                out.format("%sPress 'q' to quit or 'r' to restart.%s%n", ANSI_RED, ANSI_RESET);
            }
        }
    }

    public void run() {
        System.out.format("Connected socket w/port=%d managed by %s%n",
                socket.getPort(), Thread.currentThread().getName());
        CombatLog combatLog = new CombatLog();
        int wins = 0;
        try (Scanner in = new Scanner(socket.getInputStream())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Connected to server");
            out.format("%s> Ready to fight | Player: %d HP, Monster: %d HP, Potion: %d%s%n",
                    ANSI_YELLOW, combatLog.playerHP, combatLog.monsterHP, combatLog.potion, ANSI_RESET);
            while (in.hasNextLine() && combatLog.play) {
                String line = in.nextLine();
                if ("f".equals(line) || "fight".equals(line)) {
                    switch (combatLog.status) {
                        case "Fight":
                            combatLog.fight();
                            out.format("%s-%d to Player and -%d to Monster | Player: %d HP, Monster: %d HP, Potion: %d%s%n",
                                    ANSI_YELLOW, combatLog.damageMonster, combatLog.damagePlayer,
                                    combatLog.playerHP, combatLog.monsterHP, combatLog.potion, ANSI_RESET);
                            break;
                        case "Lost":
                            out.format("%sPlayer lost.%s%n", ANSI_RED, ANSI_RESET);
                            out.format("%sTotal wins: %d%s%n", ANSI_PURPLE, wins, ANSI_RESET);
                            combatLog.play = false;
                            break;
                        case "Win":
                            out.format("%s> Player won, press 'q' to quit or 'r' to restart.%s%n", ANSI_GREEN, ANSI_RESET);
                            ++wins;
                            if (isQuitting(in, out, combatLog, wins)) {
                                combatLog.play = false;
                            }
                            break;
                        case "Draw":
                            out.format("%s> Player drew, press 'q' to quit or 'r' to restart.%s%n", ANSI_CYAN, ANSI_RESET);
                            if (isQuitting(in, out, combatLog, wins)) {
                                combatLog.play = false;
                            }
                            break;
                    }
                } else if ("h".equals(line) || "heal".equals(line)) {
                    if (combatLog.potion > 0 && combatLog.status.equals("Fight")) {
                        int healed = combatLog.heal();
                        out.format("%s> Healed %d | Player: %d HP, Monster: %d HP, Potion: %d%s%n",
                                ANSI_GREEN, healed, combatLog.playerHP, combatLog.monsterHP, combatLog.potion, ANSI_RESET);
                    } else {
                        out.format("%sCannot heal%s%n", ANSI_RED, ANSI_RESET);
                    }
                } else if ("q".equals(line) || "quit".equals(line)) {
                    break;
                }  else {
                    out.format("%sFight, heal or quit.%s%n", ANSI_RED, ANSI_RESET);
                }
            }
            out.format("%sTotal wins: %d%s%n", ANSI_PURPLE, wins, ANSI_RESET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.format("Socket w/port=%d quit%n", socket.getPort());
    }

    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket(PORT)) {
            System.out.format("Server is running, listening at port %s%n", PORT);
            ExecutorService service = Executors.newFixedThreadPool(25);
            while (true) {
                service.execute(new DungeonAdventuresServer(listener.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}