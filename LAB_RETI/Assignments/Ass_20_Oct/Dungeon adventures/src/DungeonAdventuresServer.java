import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class DungeonAdventuresServer implements Runnable {
    public final static int PORT = 1313;
    private final Socket socket;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public DungeonAdventuresServer(Socket socket) {
        this.socket = socket;
    }

    public static class CombatLog {
        int playerHP;
        int monsterHP;
        int damagePlayer;
        int damageMonster;
        int potion;
        boolean play = true;
        String status;

        public CombatLog() {
            this.playerHP = 100;
            this.monsterHP = 100;
            this.potion = 75;
            this.status = "Fight";
        }

        public int makeDamage(int HP, int damage) {
            return Math.max((HP - damage), 0);
        }

        public void restorePG() {
            playerHP = 100;
            monsterHP = 100;
            potion = 75;
            status = "Fight";
        }

        public void winOrDraw() {
            if (playerHP > 0 && monsterHP < 1) {
                status = "Win";
            }
            if (playerHP < 1 && monsterHP > 0) {
                status = "Lost";
            }
            if (Math.max(playerHP, monsterHP) == 0) {
                status = "Draw";
            }
            if (playerHP > 0 && monsterHP > 0) {
                status = "Fight";
            }
        }

        public void fight() {
            damagePlayer = ThreadLocalRandom.current().nextInt(0, 100);
            damageMonster = ThreadLocalRandom.current().nextInt(0, 100);
            playerHP = makeDamage(playerHP, damageMonster);
            monsterHP = makeDamage(monsterHP, damagePlayer);
            winOrDraw();
        }

        public int heal() {
            int potionConsume = ThreadLocalRandom.current().nextInt(1, 76);
            int healing = Math.min(potionConsume, potion);
            playerHP = playerHP + healing;
            potion = Math.max((potion - potionConsume), 0);
            return healing;
        }
    }

    public boolean isQuitting(Scanner in, PrintWriter out, CombatLog combatLog, int wins) {
        while (true) {
            String line = in.nextLine();
            if (line.equals("r")) {
                combatLog.restorePG();
                out.format("%sLet's fight! Player: %d HP, Monster: %d HP, Potion: %d%s%n",
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
            out.format("%sReady to fight | Player: %d HP, Monster: %d HP, Potion: %d%s%n",
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
                            out.format("%sPlayer won, press 'q' to quit or 'r' to restart.%s%n", ANSI_GREEN, ANSI_RESET);
                            ++wins;
                            if (isQuitting(in, out, combatLog, wins)) {
                                combatLog.play = false;
                            }
                            break;
                        case "Draw":
                            out.format("%sPlayer drew, press 'q' to quit or 'r' to restart.%s%n", ANSI_CYAN, ANSI_RESET);
                            if (isQuitting(in, out, combatLog, wins)) {
                                combatLog.play = false;
                            }
                            break;
                    }
                } else if ("h".equals(line) || "heal".equals(line)) {
                    if (combatLog.potion > 0 && combatLog.status.equals("Fight")) {
                        int healed = combatLog.heal();
                        out.format("%sHealed %d | Player: %d HP, Monster: %d HP, Potion: %d%s%n",
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
            System.out.format("Server is running at %s%n", listener.getInetAddress().getHostAddress());
            ExecutorService service = Executors.newFixedThreadPool(25);
            while (true) {
                service.execute(new DungeonAdventuresServer(listener.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}