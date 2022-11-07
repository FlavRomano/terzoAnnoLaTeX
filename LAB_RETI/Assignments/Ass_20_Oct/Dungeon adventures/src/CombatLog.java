import java.util.concurrent.ThreadLocalRandom;

public class CombatLog {
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