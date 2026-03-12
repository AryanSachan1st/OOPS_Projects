package OOPS_Practice;

import java.util.*;

// Base class for all account types; holds shared fields and common banking operations
abstract class Account {
    // private fields are accessed only through their getter/setter methods
    private double balance;
    protected int accno;
    String acc_holder_name;
    private boolean freeze;
    String account_type;
    List<String> transaction_history;

    protected Account() {
        this.transaction_history = new ArrayList<>();
    }

    protected double getBalance() {
        return this.balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    protected boolean isFrozen() {
        return this.freeze;
    }

    // Adds amount to balance; blocked if account is frozen or amount is invalid
    public void deposit(double amount) throws Exception {
        if (this.freeze)
            throw new Exception("Account is freezed, Deposit not allowed");

        if (amount <= 0) {
            throw new Exception("Can not add balance which is less than 0");
        }
        this.balance += amount;

        String curr_history = "Deposit: " + amount + ", final balance: " + this.balance;
        transaction_history.add(curr_history);
    }

    // Returns the full list of past transactions
    public List<String> viewHistory() {
        return this.transaction_history;
    }

    public void freezeAccount() {
        this.freeze = true;
    }

    public void unfreezeAccount() {
        this.freeze = false;
    }

    // Returns a summary of account details as a key-value map
    public HashMap<String, String> accountStatus() {
        HashMap<String, String> map = new HashMap<>();

        map.put("Account Type", this.account_type);
        map.put("Account Holder Name", this.acc_holder_name);
        map.put("Account Number", "" + this.accno);
        map.put("Balance", "" + this.balance);

        if (freeze) {
            map.put("Freeze status", "True");
        } else {
            map.put("Freeze status", "False");
        }

        return map;
    }

    // Each account type enforces its own withdrawal rules
    abstract double withdraw(double amount) throws Exception;
}

// Savings account with a 7% interest rate and a monthly withdrawal limit of 3
class SavingsAccount extends Account {
    int interest_rate = 7;
    int monthly_withdrawl_limit = 3;
    int count_monthly_withdrwals = 0;

    public SavingsAccount(String acc_holder_name, int accno) {
        this.acc_holder_name = acc_holder_name;
        this.accno = accno;
        this.account_type = "Savings Account";
    }

    // Withdraws amount; blocked if frozen, balance is insufficient, or monthly
    // limit is reached
    public double withdraw(double amount) throws Exception {
        if (isFrozen())
            throw new Exception("Account is freezed, Deposit not allowed");

        if (amount > getBalance()) {
            throw new Exception("Insufficient balance");
        }

        if (count_monthly_withdrwals >= monthly_withdrawl_limit) {
            throw new Exception("Your monthly withdrawl limit expired");
        }

        setBalance(getBalance() - amount);
        count_monthly_withdrwals += 1;

        String curr_history = "Withdrawl: " + amount + ", final balance: " + getBalance();
        transaction_history.add(curr_history);

        return getBalance();
    }

    // Applies interest to current balance and logs it in transaction history
    public void addInterestToBalance() {
        setBalance(getBalance() + getBalance() * (double) interest_rate / 100);

        String curr_history = "Interest added: " + interest_rate + "% to the current balance, final balance: "
                + getBalance();
        transaction_history.add(curr_history);
    }

}

// Current account for business use; allows balance to go negative up to the
// overdraft limit
class CurrentAccount extends Account {
    double overdraft_limit;

    public CurrentAccount(String acc_holder_name, int accno, double overdraft_limit) {
        this.acc_holder_name = acc_holder_name;
        this.accno = accno;
        this.overdraft_limit = overdraft_limit;
        this.account_type = "Current Account";
    }

    // Withdraws amount; allowed up to (balance + overdraft_limit), balance can go
    // negative
    public double withdraw(double amount) throws Exception {
        if (isFrozen())
            throw new Exception("Account is freezed, Deposit not allowed");

        if (amount > getBalance() + overdraft_limit) {
            throw new Exception("Insufficent balance, overdraft limit applied");
        }

        setBalance(getBalance() - amount); // balance can go -ive (considering: amount > bal + odl)

        String curr_history = "Withdrawl: " + amount + ", final balance: " + getBalance();
        transaction_history.add(curr_history);

        return getBalance();
    }
}

public class BankAccounts {
    public static void main(String[] args) {
        // Savings Account Demo
        SavingsAccount saving = new SavingsAccount("Aryan", 101);

        try {
            saving.deposit(10000); // deposit 10,000
            saving.deposit(5000); // deposit 5,000 -> balance: 15,000

            saving.withdraw(2000); // withdraw 2,000 -> balance: 13,000
            saving.withdraw(3000); // withdraw 3,000 -> balance: 10,000
            saving.withdraw(1000); // withdraw 1,000 -> balance: 9,000

            saving.addInterestToBalance(); // +7% interest -> balance: 9,630

            // 4th withdrawal should throw -> monthly limit is 3
            saving.withdraw(500);
        } catch (Exception e) {
            System.out.println("SavingsAccount error: " + e.getMessage());
        }

        System.out.println("Savings Account Status-");
        saving.accountStatus().forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("Savings Transaction History-");
        saving.viewHistory().forEach(System.out::println);

        // Current Account Demo
        CurrentAccount current = new CurrentAccount("Rohan", 202, 5000); // overdraft limit: 5,000

        try {
            current.deposit(8000); // deposit 8,000 -> balance: 8,000

            current.withdraw(10000); // withdraw 10,000 (uses overdraft) -> balance: -2,000
            current.withdraw(2000); // withdraw 2,000 -> balance: -4,000

            // this should throw -> exceeds balance + overdraft limit (-4000 + 5000 = 1000
            // left)
            current.withdraw(2000);
        } catch (Exception e) {
            System.out.println("CurrentAccount error: " + e.getMessage());
        }

        // Freeze and attempt a deposit
        current.freezeAccount();
        try {
            current.deposit(500); // should throw -> account is frozen
        } catch (Exception e) {
            System.out.println("CurrentAccount error: " + e.getMessage());
        }

        current.unfreezeAccount(); // unfreeze and resume
        try {
            current.deposit(500); // deposit 500 -> balance: -3500
        } catch (Exception e) {
            System.out.println("CurrentAccount error: " + e.getMessage());
        }

        System.out.println("Current Account Status-");
        current.accountStatus().forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("Current Transaction History-");
        current.viewHistory().forEach(System.out::println);
    }
}
