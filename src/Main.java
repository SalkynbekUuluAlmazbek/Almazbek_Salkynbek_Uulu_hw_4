import java.util.Random;

public class Main {
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int medicHealth = 220;
    public static int medicHeal = 10;
    public static int golemHealth = 300;
    public static int golemDamage = 5;
    public static String golemAttackType = "Physical";
    public static int luckyHealth = 200;
    public static double luckyAvoidProbability = 0.2;
    public static int[] heroesHealth = {270, 260, 250, medicHealth, golemHealth, luckyHealth};
    public static int[] heroesDamage = {10, 15, 20, 0, golemDamage, 0};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medical",
            golemAttackType, "Dodge"};

    public static int roundNumber = 0;
    public static String message = "";

    public static boolean isLuckyDodges() {
        Random random = new Random();
        double chance = random.nextDouble();
        return chance < luckyAvoidProbability;
    }

    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        message = "";
        chooseBossDefence();
        bossHits();
        heroesHit();
        medicHeal();
        printStatistics();

    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void bossHits() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (heroesAttackType[i].equals(golemAttackType)) {
                    int golemDamageTaken = bossDamage / 5;
                    if (golemHealth - golemDamageTaken < 0) {
                        golemHealth = 0;
                    } else {
                        golemHealth = golemHealth - golemDamageTaken;
                    }
                } else if (heroesAttackType[i].equals("Dodge")) {
                    if (isLuckyDodges()) {
                        message = "Lucky dodged boss's attack!";
                    } else {
                        if (heroesHealth[i] - bossDamage < 0) {
                            heroesHealth[i] = 0;
                        } else {
                            heroesHealth[i] = heroesHealth[i] - bossDamage;
                        }
                    }
                } else {
                    if (heroesHealth[i] - bossDamage < 0) {
                        heroesHealth[i] = 0;
                    } else {
                        heroesHealth[i] = heroesHealth[i] - bossDamage;
                    }
                }
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (heroesAttackType[i] == bossDefence) {
                    Random random = new Random();
                    int coefficient = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10;
                    damage = damage * coefficient;
                    message = "Critical damage: " + damage;
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }
            }
        }
    }

    public static void medicHeal() {

        if (medicHealth > 0) {
            int minHealth = heroesHealth[0];
            int minIndex = 0;
            for (int i = 1; i < heroesHealth.length - 1; i++) {
                if (heroesHealth[i] < minHealth && heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                    minHealth = heroesHealth[i];
                    minIndex = i;
                }
            }
            if (minHealth < 100) {
                heroesHealth[minIndex] += medicHeal;
                if (medicHealth <= 0) {
                    System.out.println("Medic is dead and can no longer heal.");
                }

            }
        }
    }


    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 && !heroesAttackType[i].equals(golemAttackType)) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead || bossHealth <= 0) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }


    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ----------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage
                + " defence: " + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesAttackType[i].equals(golemAttackType)) {
                System.out.println("Golem health: " + golemHealth
                        + " damage: " + golemDamage);
            }
            if (heroesAttackType[i].equals("Dodge")) {
                System.out.println("Lucky health: " + heroesHealth[i]);
            } else {
                System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]
                        + " damage: " + heroesDamage[i]);
            }


        }
        System.out.println(message);
    }

}
