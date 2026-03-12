package OOPS_Practice;
import java.util.*;

// Represents a weapon that a character can equip; adds bonus damage
class Weapon {
    String name;
    private double damageBonusPoints;

    public double getDamageBonusPoints() {
        return this.damageBonusPoints;
    }

    public Weapon(String name, double damageBonusPoints) {
        this.name = name;
        this.damageBonusPoints = damageBonusPoints;
    }
}

// Base class for all character types; holds shared stats and abstract combat methods
abstract class Character {
    String name;
    private double healthPoints, attackPower, defensePower;
    Weapon weapon;

    public double getHealthPoints() {
        return this.healthPoints;
    }

    public double getAttackPower() {
        return this.attackPower;
    }

    public double getDefensePower() {
        return this.defensePower;
    }

    public void setHealthPoints(double healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setAttackPower(double attackPower) {
        this.attackPower = attackPower;
    }

    public void setDefensePower(double defensePower) {
        this.defensePower = defensePower;
    }

    // Returns [armourIgnore (0/1), attackDamage]
    abstract double[] basicAttack();

    abstract void basicDefense(double[] opponentAttack);

    // Returns [armourIgnore (0/1), attackDamage] — 1 means bypasses opponent's armour
    abstract double[] specialAttack();

    abstract void assignWeapon(Weapon weapon);
}

// High HP, high defense; special attack doubles damage but reduces own defense
class Warrior extends Character {
    int attackPower = 280;
    int defensePower = 300;
    int healthPoints = 8000;

    public Warrior(String name) {
        this.name = name;
        setAttackPower();
        setDefensePower();
        setHealthPoints();
    }

    // Push subclass stats to parent fields
    private void setHealthPoints() {
        super.setHealthPoints(healthPoints);
    }

    private void setAttackPower() {
        super.setAttackPower(attackPower);
    }

    private void setDefensePower() {
        super.setDefensePower(defensePower);
    }

    public double[] basicAttack() {
        double attackDamage = this.getAttackPower() + 10;

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 0, attackDamage };
    }

    // Defense absorbs damage; if armour is bypassed (opponentAttack[0] == 1), full damage is taken
    public void basicDefense(double[] opponentAttack) {
        double currDefense = this.getDefensePower() + 20;
        double currHealth = this.getHealthPoints();

        if (opponentAttack[0] == 1) { // armour bypassed — take full damage
            currHealth = this.getHealthPoints() - opponentAttack[1];
        } else { // damage reduced by defense
            currHealth = this.getHealthPoints() - (opponentAttack[1] - currDefense);
        }

        this.setHealthPoints(Math.round(currHealth * 1000.0)/1000.0);
    }

    // Deals 2x damage but permanently -15 defense as a trade-off
    public double[] specialAttack() {
        double attackDamage = this.getAttackPower() * 2;
        double currDefensePower = this.getDefensePower() - 15;

        this.setDefensePower(currDefensePower);

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 0, attackDamage };
    }

    public void assignWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}

// High attack with mana-based special; takes half damage on normal hits
class Mage extends Character {
    int manaPoints;

    int attackPower = 400;
    int defensePower = 320;
    int healthPoints = 6500;

    public Mage(String name) {
        this.name = name;
        setAttackPower();
        setDefensePower();
        setHealthPoints();
        this.manaPoints = 80;
    }

    // Push subclass stats to parent fields
    private void setHealthPoints() {
        super.setHealthPoints(healthPoints);
    }

    private void setAttackPower() {
        super.setAttackPower(attackPower);
    }

    private void setDefensePower() {
        super.setDefensePower(defensePower);
    }

    // Random bonus damage (1–50) added on each basic attack
    public double[] basicAttack() {
        double randomExtra = (int) Math.floor(Math.random() * 50) + 1;
        double attackDamage = this.getAttackPower() + randomExtra;

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 0, attackDamage };
    }

    // Takes half damage on normal hits; full damage if armour is bypassed
    public void basicDefense(double[] opponentAttack) {
        if (opponentAttack[0] == 1) { // armour bypassed — take full damage
            this.setHealthPoints(Math.round((this.getHealthPoints() - opponentAttack[1]) * 1000.0)/1000.0);
        } else { // magic shield halves incoming damage
            this.setHealthPoints(Math.round((this.getHealthPoints() - (0.5 * opponentAttack[1])) * 1000.0)/1000.0);
        }
    }

    // Deals 3x damage; costs 30 mana. Falls back to basic attack power if mana is insufficient
    public double[] specialAttack() {
        if (this.manaPoints < 30) { // not enough mana — use fallback damage
            return new double[] { 0, this.getAttackPower() };
        }

        double attackDamage = 3 * this.getAttackPower();
        this.manaPoints -= 30;

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 0, attackDamage };
    }

    public void assignWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}

// Agile fighter with dodge chance on defense; special attack bypasses opponent's armour
class Archer extends Character {
    int attackPower = 330;
    int defensePower = 250;
    int healthPoints = 6000;

    public Archer(String name) {
        this.name = name;
        setAttackPower();
        setDefensePower();
        setHealthPoints();
    }

    // Push subclass stats to parent fields
    private void setHealthPoints() {
        super.setHealthPoints(healthPoints);
    }

    private void setAttackPower() {
        super.setAttackPower(attackPower);
    }

    private void setDefensePower() {
        super.setDefensePower(defensePower);
    }

    // 30% chance of a critical hit (1.35x damage)
    public double[] basicAttack() {
        int chance = (int) Math.floor((Math.random() * 10) + 1);
        double attackDamage = chance <= 3 ? 1.35 * this.getAttackPower() : this.getAttackPower(); // critical hit chance

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 0, attackDamage };
    }

    // 40% chance to fully dodge the attack
    public void basicDefense(double[] opponentAttack) {
        if (opponentAttack[0] == 1) { // armour bypassed — take full damage, no dodge
            this.setHealthPoints(this.getHealthPoints() - opponentAttack[1]);
        } else {
            int chance = (int) Math.floor((Math.random() * 10) + 1);
            double currHealth = chance <= 4 ? this.getHealthPoints() - 0 : this.getHealthPoints() - opponentAttack[1]; // 40% dodge

            this.setHealthPoints(currHealth);
        }
    }

    // Bypasses opponent's armour (returns 1); also has a 30% critical hit chance on top of special damage
    public double[] specialAttack() {
        int chance = (int) Math.floor((Math.random() * 10) + 1);
        double attackDamage = (chance <= 3) ? 1.35 * this.getAttackPower() : this.getAttackPower(); // critical hit

        attackDamage += 1.5 * this.getAttackPower(); // special hit bonus

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 1, attackDamage }; // 1 = bypass armour
    }

    public void assignWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}

public class DualPlayerGameCombatSystem {

    // Runs the fight loop; players alternate turns until one's HP drops to 0
    private static String fight(Character player1, Character player2, List<String> fightlog) {
        boolean player1Chance = true;
        String winner = "";

        while (player1.getHealthPoints() > 0 && player2.getHealthPoints() > 0) {
            int specialChance = (int) Math.floor(Math.random() * 10 + 1); // >5 triggers special attack

            if (player1Chance) {
                // player1 attacks and player2 defends

                double[] attackDamage = new double[2];
                if (specialChance > 5) {
                    attackDamage = player1.specialAttack();
                } else {
                    attackDamage = player1.basicAttack();
                }

                player2.basicDefense(attackDamage);

                String curr = player1.name + " attacks " + player2.name + ", " + player2.name + "'s remaining health: "
                        + player2.getHealthPoints();
                fightlog.add(curr);

            } else {
                // player2 attacks and player1 defends

                double[] attackDamage = new double[2];
                if (specialChance > 5) {
                    attackDamage = player2.specialAttack();
                } else {
                    attackDamage = player2.basicAttack();
                }

                player1.basicDefense(attackDamage);

                String event = player2.name + " attacks " + player1.name + ", " + player1.name + "'s remaining health: "
                        + player1.getHealthPoints();
                fightlog.add(event);

            }

            player1Chance = !player1Chance; // alternate turns
        }

        // Clamp loser's HP to 0 and declare winner
        if (player1.getHealthPoints() <= 0) { // player1 lost
            player1.setHealthPoints(0);
            winner = player2.name;
        } else { // player2 lost
            player2.setHealthPoints(0);
            winner = player1.name;
        }

        return winner;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter 2 players and their weapon-");
        String player1 = sc.next();
        String weapon1Name = sc.next();
        String player2 = sc.next();
        String weapon2Name = sc.next();

        Weapon weapon1 = new Weapon(weapon1Name, 45);
        Weapon weapon2 = new Weapon(weapon2Name, 60);

        // Instantiate only the characters entered by the user
        Warrior knightWarrior = null;
        Mage superMage = null;
        Archer ninjaArcher = null;

        if (player1.equals("Warrior") || player2.equals("Warrior")) {
            knightWarrior = new Warrior("Knight Warrior");
        }
        if (player1.equals("Mage") || player2.equals("Mage")) {
            superMage = new Mage("Super Mage");
        }
        if (player1.equals("Archer") || player2.equals("Archer")) {
            ninjaArcher = new Archer("Ninja Archer");
        }

        String winner = "";
        List<String> fightLog = new ArrayList<>();

        // Assign weapons, display starting HP, and start the fight for the matched pair
        if (knightWarrior != null && superMage != null) {
            knightWarrior.assignWeapon(weapon1);
            superMage.assignWeapon(weapon2);

            System.out.println(knightWarrior.name + "'s health: " + knightWarrior.getHealthPoints());
            System.out.println(superMage.name + "'s health: " + superMage.getHealthPoints());

            winner = fight(knightWarrior, superMage, fightLog);
        } else if (knightWarrior != null && ninjaArcher != null) {
            knightWarrior.assignWeapon(weapon1);
            ninjaArcher.assignWeapon(weapon2);

            System.out.println(knightWarrior.name + "'s health: " + knightWarrior.getHealthPoints());
            System.out.println(ninjaArcher.name + "'s health: " + ninjaArcher.getHealthPoints());

            winner = fight(knightWarrior, ninjaArcher, fightLog);
        } else if (superMage != null && ninjaArcher != null) {
            superMage.assignWeapon(weapon1);
            ninjaArcher.assignWeapon(weapon2);

            System.out.println(superMage.name + "'s health: " + superMage.getHealthPoints());
            System.out.println(ninjaArcher.name + "'s health: " + ninjaArcher.getHealthPoints());

            winner = fight(superMage, ninjaArcher, fightLog);
        }

        // Print full fight log, then declare the winner
        for (String event : fightLog) {
            System.out.println(event);
        }

        System.out.println("Winner is: " + winner);

        sc.close();
    }
}