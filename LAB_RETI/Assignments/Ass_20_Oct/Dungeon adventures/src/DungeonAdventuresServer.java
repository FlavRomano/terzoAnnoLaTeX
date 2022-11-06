import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class DungeonAdventuresServer implements Runnable {
    public final static int PORT = 1313;
    private final Socket socket;

    public DungeonAdventuresServer(Socket socket) {
        this.socket = socket;
    }

    public static void restoreGame(int[] combatLog) {
        combatLog[0] = 100;
        combatLog[1] = 100;
        combatLog[2] = 75;
        combatLog[3] = 0;
    }

    public static class CombatLog {
        int playerHP;
        int monsterHP;
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
                play = false;
            }
            if (Math.max(playerHP, monsterHP) == 0) {
                status = "Draw";
            }
            if (playerHP > 0 && monsterHP > 0) {
                status = "Fight";
            }
        }

        public void fight() {
            if (status.equals("Fight")) {
                int damagePlayer = ThreadLocalRandom.current().nextInt(0, 100);
                int damageMonster = ThreadLocalRandom.current().nextInt(0, 100);
                playerHP = makeDamage(playerHP, damageMonster);
                monsterHP = makeDamage(monsterHP, damagePlayer);
                winOrDraw();
            }
        }

        public boolean heal() {
            if (potion > 0) {
                int potionConsume = ThreadLocalRandom.current().nextInt(1, 76);
                playerHP = (potionConsume > potion ? playerHP + potion : playerHP + potionConsume);
                potion = Math.max((potion - potionConsume), 0);
                return true;
            }
            return false;
        }
    }
    public boolean isQuitting(Scanner in, PrintWriter out, CombatLog combatLog, int wins) {
        while (true) {
            String line = in.nextLine();
            if (line.equals("r")) {
                combatLog.restorePG();
                out.format("Let's fight! Player: %d HP, Monster: %d HP, Potion: %d%n",
                        combatLog.playerHP, combatLog.monsterHP, combatLog.potion);
                return false;
            } else if (line.equals("q")) {
                out.format("Total wins: %d%n", wins);
                return true;
            } else {
                out.println("Press 'q' to quit or 'r' to restart.");
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
            out.format("Ready to fight | Player: %d HP, Monster: %d HP, Potion: %d%n",
                    combatLog.playerHP, combatLog.monsterHP, combatLog.potion);
            while (in.hasNextLine() && combatLog.play) {
                String line = in.nextLine();
                if ("f".equals(line) || "fight".equals(line)) {
                    switch (combatLog.status) {
                        case "Fight":
                            combatLog.fight();
                            out.format("Fighting... Player: %d HP, Monster: %d HP, Potion: %d%n",
                                    combatLog.playerHP, combatLog.monsterHP, combatLog.potion);
                            break;
                        case "Win":
                            out.println("Player won, press 'q' to quit or 'r' to restart.");
                            ++wins;
                            if (isQuitting(in, out, combatLog, wins)) {
                                combatLog.play = false;
                            }
                            break;
                        case "Draw":
                            out.println("Player drew, press 'q' to quit or 'r' to restart.");
                            if (isQuitting(in, out, combatLog, wins)) {
                                combatLog.play = false;
                            }
                            break;
                    }
                } else if ("h".equals(line) || "heal".equals(line)) {
                    if (combatLog.heal() && combatLog.status.equals("Fight")) {
                        out.format("Fighting... Player: %d HP, Monster: %d HP, Potion: %d%n",
                                combatLog.playerHP, combatLog.monsterHP, combatLog.potion);
                    } else {
                        out.println("Cannot heal");
                    }
                } else if ("q".equals(line) || "quit".equals(line)) {
                    break;
                } else {
                    out.println("Fight, heal or quit.");
                }
            }
            out.format("Total wins: %d%n", wins);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.format("Socket w/port=%d quitted%n", socket.getPort());
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