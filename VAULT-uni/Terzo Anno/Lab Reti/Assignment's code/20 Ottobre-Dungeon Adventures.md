## Dinamiche del gioco
Il gioco si svolge in round, ad ogni round un giocatore può:
- **Combattere col mostro.** il combattimento si conclude *decrementando* la salute del mostro e del giocatore. Sia $SG$ il livello di salute attuale del giocatore, tale livello viene decrementato di un valore random $X$ con $0\leq X<SG$. Stessa cosa per la salute del mostro $SM$, si genera un valore casuale $K$ con $0\leq K <SM$ che viene sottratto alla salute.
- **Bere una parte della pozione.** la salute del giocatore viene incrementata di un valore proporzionale alla quantità di pozione bevuta $P$ generato randomicamente.
- **Uscire dal gioco.** in questo caso la partita viene considerata persa per il giocatore.

1. Il combattimento si conclude quando il giocatore o il mostro (anche entrambi) muoiono (hanno un valore di salute pari a 0).
2. Se il giocatore ha **vinto** o **pareggiato** $\implies$ può chiedere di giocare nuovamente.
3. Se il giocatore ha **perso** $\implies$ deve uscire dal gioco.

## Consegna
Sviluppare un'applicazione client-server che implementi il gioco descritto sopra.
```ad-info
title: Server
- Il server riceve richieste di gioco da parte dei client.
- Gestisce ogni connessione in un diverso thread.
```

```ad-info
title: Server's threads
Ogni thread riceve comandi dal client e li esegue.
- Nel caso del comando `combattere` simula il comportamento del mostro assegnato al client.
- Dopo aver eseguito ogni comando ne comunica al client l'esito.
- Comunica al client l'eventuale terminazione del gioco, insieme all'esito.
```

```ad-info
title: Clients
Il client si connette al server.
- Chiede iterativamente all'utente il comando da eseguire e lo invia al server. 
I comandi sono:
	1. combatti
	2. bevi pozione
	3. esci dal gioco
- Attende un messaggio che segnala l'esito del comando.
- In caso di vittoria, chiede all'utente se intende continuare a giocare e lo comunica al server.
```
## Codice
Server
```java
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
```

Client 
```java
import java.io.IOException;  
import java.io.PrintWriter;  
import java.net.Socket;  
import java.util.Scanner;  
  
public class DungeonAdventuresClient {  
    public final static String TITLE =  
            "█▀▄ █░█ █▄░█ █▀▀ █▀▀ █▀█ █▄░█   ▄▀█ █▀▄ █░█ █▀▀ █▄░█ ▀█▀ █░█ █▀█ █▀▀ █▀\n" +  
                    "█▄▀ █▄█ █░▀█ █▄█ ██▄ █▄█ █░▀█   █▀█ █▄▀ ▀▄▀ ██▄ █░▀█ ░█░ █▄█ █▀▄ ██▄ ▄█";  
    public final static String COMMANDS = "+----------------+-------------------------------------------------+\n" +  
            "| command        | description                                     |\n" +  
            "+----------------+-------------------------------------------------+\n" +  
            "| 'fight', 'f'   | fight the monster                               |\n" +  
            "+----------------+-------------------------------------------------+\n" +  
            "| 'heal', 'h'    | heal the player                                 |\n" +  
            "+----------------+-------------------------------------------------+\n" +  
            "| 'rematch', 'r' | request a rematch (only in case of win or draw) |\n" +  
            "+----------------+-------------------------------------------------+\n" +  
            "| 'quit', 'q'    | quit the game                                   |\n" +  
            "+----------------+-------------------------------------------------+\n" +  
            "| 'help'         | print this table                                |\n" +  
            "+----------------+-------------------------------------------------+";  
    public final static int PORT = 1313;  
  
    public static boolean stringValidator(String line) {  
        return "fight".equals(line) || "f".equals(line) ||  
                "rematch".equals(line) || "r".equals(line) ||  
                "heal".equals(line) || "h".equals(line) ||  
                "exit".equals(line) || "q".equals(line) ||  
                "help".equals(line);  
    }  
  
    public static void main(String[] args) {  
        System.out.println(TITLE);  
        System.out.println(COMMANDS);  
        try (  
                Socket socket = new Socket("0.0.0.0", PORT);  
                Scanner in = new Scanner(socket.getInputStream());  
                Scanner scanner = new Scanner(System.in)  
        ) {  
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  
            String connString = in.nextLine();  
            System.out.println(connString);  
            String initialState = in.nextLine();  
            System.out.println(initialState);  
            int i = System.in.available();  
            // for consuming random commands user may type before the game started  
            while (i > 0) {  
                int commandLen = scanner.nextLine().length() + 1; 
                // length() + 1 we must consider the CR (e.g "f\n", "aaa\n",)  
                i -= len;  
            }  
            boolean end = false;  
            while (!end) {  
                String line = scanner.nextLine();  
                if (stringValidator(line)) {  
                    if (line.equals("quit") || line.equals("q")) {  
                        out.println(line);  
                        String nextLine = in.nextLine();  
                        end = true;  
                        System.out.println(nextLine);  
                    } else if (line.equals("help")) {  
                        System.out.println(COMMANDS);  
                    } else {  
                        out.println(line);  
                        String nextLine = in.nextLine();  
                        if (nextLine.contains("wins")) {  
                            end = true;  
                        }  
                        System.out.println(nextLine);  
                    }  
                } else {  
                    System.out.println("Not a command");  
                }  
            }  
        } catch (IOException e) {  
            throw new RuntimeException(e);  
        }  
    }  
}
```