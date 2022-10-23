import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class DungeonAdventuresServer implements Runnable {
    public final static int PORT = 1313;
    private Socket socket;

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

        public boolean winOrDraw() {
            if (playerHP > 0 && monsterHP < 1) {
                status = "Win";
                return true;
            } else if (playerHP == 0 && playerHP == monsterHP) {
                status = "Draw";
                return true;
            } else if (playerHP > 0 && monsterHP > 0) {
                status = "Fight";
                return false;
            }
            status = "Lost";
            return false;
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
                potion = (potionConsume > potion ? 0 : potion - potionConsume);
                return true;
            }
            return false;
        }
    }
    public void run() {
        System.out.format("Connected socket w/port=%d managed by %s%n", socket.getPort(), Thread.currentThread().getName());
        CombatLog combatLog = new CombatLog();
        boolean play = true;
        int wins = 0;
        try (Scanner in = new Scanner(socket.getInputStream())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine() && play) {
                String line = in.nextLine();
                if ("f".equals(line)) {
                    switch (combatLog.status) {
                        case "Fight":
                            combatLog.fight();
                            out.format("Fighting... Player: %d HP, Monster: %d HP, Potion: %d%n",
                                    combatLog.playerHP, combatLog.monsterHP, combatLog.potion);
                            break;
                        case "Lost":
                            out.format("Player lost. Total wins: %d.", wins);
                            play = false;
                            break;
                        case "Win":
                            out.println("Player won, press 'q' to quit or 'r' to restart.");
                            ++wins;
                            while (true) {
                                if (in.nextLine().equals("r")) {
                                    combatLog.restorePG();
                                    out.format("Let's fight! Player: %d HP, Monster: %d HP, Potion: %d%n",
                                            combatLog.playerHP, combatLog.monsterHP, combatLog.potion);
                                    break;
                                }
                                out.println("Press 'q' to quit or 'r' to restart.");
                            }
                            break;
                        case "Draw":
                            out.println("Player drew, press 'q' to quit or 'r' to restart.");
                            while (true) {
                                if (in.nextLine().equals("r")) {
                                    combatLog.restorePG();
                                    out.format("Let's fight! Player: %d HP, Monster: %d HP, Potion: %d%n",
                                            combatLog.playerHP, combatLog.monsterHP, combatLog.potion);
                                    break;
                                }
                                out.println("Press 'q' to quit or 'r' to restart.");
                            }
                            break;
                    }
                } else if ("h".equals(line)) {
                    if (combatLog.heal() && combatLog.status.equals("Fight")) {
                        out.format("Fighting... Player: %d HP, Monster: %d HP, Potion: %d%n",
                                combatLog.playerHP, combatLog.monsterHP, combatLog.potion);
                    } else {
                        out.println("Cannot heal");
                    }
                } else {
                    out.println("Fight, heal or quit.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.format("Socket w/port=%d quitted%n", socket.getPort());
    }

    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket(PORT)) {
            System.out.format("Server is running at %s%n", listener.getInetAddress().getHostAddress());
            ExecutorService service = Executors.newFixedThreadPool(5);
            while (true) {
                service.execute(new DungeonAdventuresServer(listener.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}