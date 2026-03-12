package OOPS_Practice;
import java.util.*;

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

    abstract double[] basicAttack();

    abstract void basicDefense(double[] opponentAttack);

    abstract double[] specialAttack(); // [1/0 (ignore or not ignore armour), attackDamage]

    abstract void assignWeapon(Weapon weapon);
}

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

    public void basicDefense(double[] opponentAttack) {
        double currDefense = this.getDefensePower() + 20;
        double currHealth = this.getHealthPoints();

        if (opponentAttack[0] == 1) {
            currHealth = this.getHealthPoints() - opponentAttack[1];
        } else {
            currHealth = this.getHealthPoints() - (opponentAttack[1] - currDefense);
        }

        this.setHealthPoints(Math.round(currHealth * 1000.0)/1000.0);
    }

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
        double randomExtra = (int) Math.floor(Math.random() * 50) + 1;
        double attackDamage = this.getAttackPower() + randomExtra;

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 0, attackDamage };
    }

    public void basicDefense(double[] opponentAttack) {
        if (opponentAttack[0] == 1) {
            this.setHealthPoints(Math.round((this.getHealthPoints() - opponentAttack[1]) * 1000.0)/1000.0);
        } else {
            this.setHealthPoints(Math.round((this.getHealthPoints() - (0.5 * opponentAttack[1])) * 1000.0)/1000.0);
        }
    }

    public double[] specialAttack() {
        if (this.manaPoints < 30) {
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
        int chance = (int) Math.floor((Math.random() * 10) + 1);
        double attackDamage = chance <= 3 ? 1.35 * this.getAttackPower() : this.getAttackPower(); // critical hit chance

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 0, attackDamage };
    }

    public void basicDefense(double[] opponentAttack) {
        if (opponentAttack[0] == 1) { // ignore defense/armour
            this.setHealthPoints(this.getHealthPoints() - opponentAttack[1]);
        } else {
            int chance = (int) Math.floor((Math.random() * 10) + 1);
            double currHealth = chance <= 4 ? this.getHealthPoints() - 0 : this.getHealthPoints() - opponentAttack[1];

            this.setHealthPoints(currHealth);
        }
    }

    public double[] specialAttack() {
        int chance = (int) Math.floor((Math.random() * 10) + 1);
        double attackDamage = (chance <= 3) ? 1.35 * this.getAttackPower() : this.getAttackPower(); // critical hit

        attackDamage += 1.5 * this.getAttackPower(); // special hit

        if (this.weapon != null) {
            attackDamage += this.weapon.getDamageBonusPoints();
        }

        return new double[] { 1, attackDamage };
    }

    public void assignWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}

public class DualPlayerGameCombatSystem {
    private static String fight(Character player1, Character player2, List<String> fightlog) {
        boolean player1Chance = true;
        String winner = "";

        while (player1.getHealthPoints() > 0 && player2.getHealthPoints() > 0) {
            int specialChance = (int) Math.floor(Math.random() * 10 + 1);

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

            player1Chance = !player1Chance;
        }

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

        for (String event : fightLog) {
            System.out.println(event);
        }

        System.out.println("Winner is: " + winner);

        sc.close();
    }
}