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

    public void run() {
        System.out.format("Connected: %s%n", socket);
        int playerHP = 100;
        int monsterHP = 100;
        int potion = 75;
        int rematch = 0;
        int wins = 0;
        int[] combatLog = {playerHP, monsterHP, potion, rematch};
        try (Scanner in = new Scanner(socket.getInputStream())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if ("exit".equals(line) && combatLog[3] == 1) {
                    out.format("Bye bye. Total wins: %d%n", wins);
                }
                if ("rematch".equals(line) && combatLog[3] == 1) {
                    restoreGame(combatLog);
                    out.println("Let's fight!");
                }
                if ("fight".equals(line)) {
                    combatLog = combat(combatLog[0], combatLog[1], combatLog[2], false);
                    if (combatLog[0] == combatLog[1]) {
                        out.println("Draw, ready for a rematch? Type 'rematch' otherwise exit");
                    }
                    else if (combatLog[1] < 1) {
                        out.println("Player wins, ready for a rematch? Type 'rematch' otherwise 'exit'");
                        combatLog[3] = 1;
                        wins++;
                    }
                    else if (combatLog[0] < 1) {
                        if (wins == 0) out.println("Player loses, bye bye.");
                        else out.format("Player loses, bye bye. Total wins: %d%n", wins);
                    } else {
                        out.format("Fighting... - PlayerHP: %d, MonsterHP: %d, Potion left: %d%n",
                                combatLog[0], combatLog[1], combatLog[2]);
                    }
                } else if ("heal".equals(line)) {
                    if (combatLog[2] == 0) {
                        out.println("Cannot heal");
                    } else {
                        combatLog = combat(combatLog[0], combatLog[1], combatLog[2], true);
                        out.format("Healing... - PlayerHP: %d, MonsterHP: %d, Potion left: %d%n",
                                combatLog[0], combatLog[1], combatLog[2]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] combat(int playerHP, int monsterHP, int potion, boolean healing) {
        int[] combatResults = new int[5];
        int playerDamage = ThreadLocalRandom.current().nextInt(0, 100);
        int monsterDamage = ThreadLocalRandom.current().nextInt(0, 100);
        if (!healing) {
            combatResults[0] = playerHP - monsterDamage;
            combatResults[1] = monsterHP - playerDamage;
            combatResults[2] = potion;
        }
        if (healing && potion > 0) {
            int potionConsume = ThreadLocalRandom.current().nextInt(1, 76);
            playerHP = (potionConsume > potion ? playerHP + potion : playerHP + potionConsume);
            potion = (potionConsume > potion ? 0 : potion - potionConsume);
            combatResults[0] = playerHP;
            combatResults[1] = monsterHP;
            combatResults[2] = potion;
        } else {
            if (monsterHP - playerDamage < 1) {
                combatResults[0] = playerHP - monsterDamage;
                combatResults[1] = 0;
                combatResults[2] = potion;
                combatResults[3] = 1;
            }
            if (playerHP - monsterDamage < 1) {
                combatResults[0] = 0;
                combatResults[1] = monsterHP - playerDamage;
                combatResults[2] = potion;
                combatResults[3] = 0;
            }
        }
        return combatResults;
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