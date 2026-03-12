package OOPS_Practice;
import java.util.*;

abstract class Account  {
    // all the private properties will be accessed by their respective getter functions
    private double balance;
    protected int accno;
    String acc_holder_name;
    private boolean freeze;
    String account_type;
    List<String> transaction_history;

    protected Account() {
        this.transaction_history = new ArrayList<>();
    }

    protected double getBalance() { return this.balance; }
    protected void setBalance(double balance) { this.balance = balance; }
    protected boolean isFrozen() { return this.freeze; }

    public void deposit(double amount) throws Exception{
        if (this.freeze) throw new Exception("Account is freezed, Deposit not allowed");

        if (amount <= 0) {
            throw new Exception("Can not add balance which is less than 0");
        }
        this.balance += amount;

        String curr_history = "Deposit: " + amount + ", final balance: " + this.balance;
        transaction_history.add(curr_history);
    }

    public List<String> viewHistory() {
        return this.transaction_history;
    }

    public void freezeAccount() {
        this.freeze = true;
    }

    public void unfreezeAccount() {
        this.freeze = false;
    }

    public HashMap<String, String> accountStatus() {
        HashMap<String, String> map = new HashMap<>();

        map.put("Account Type", this.account_type);
        map.put("Account Holder Name", this.acc_holder_name);
        map.put("Account Number", ""+this.accno);
        map.put("Balance", ""+this.balance);
        
        if (freeze) {
            map.put("Freeze status", "True");
        } else {
            map.put("Freeze status", "False");
        }

        return map;
    }

    abstract double withdraw(double amount) throws Exception;
}

class SavingsAccount extends Account {
    int interest_rate = 7;
    int monthly_withdrawl_limit = 3;
    int count_monthly_withdrwals = 0;

    public SavingsAccount(String acc_holder_name, int accno) {
        this.acc_holder_name = acc_holder_name;
        this.accno = accno;
        this.account_type = "Savings Account";
    } 

    public double withdraw(double amount) throws Exception{
        if (isFrozen()) throw new Exception("Account is freezed, Deposit not allowed");

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

    public void addInterestToBalance() {
        setBalance(getBalance() + getBalance() * (double) interest_rate / 100);

        String curr_history = "Interest added: " + interest_rate + "% to the current balance, final balance: " + getBalance();
        transaction_history.add(curr_history);
    }

}

class CurrentAccount extends Account {
    double overdraft_limit;

    public CurrentAccount(String acc_holder_name, int accno, double overdraft_limit) {
        this.acc_holder_name = acc_holder_name;
        this.accno = accno;
        this.overdraft_limit = overdraft_limit;
        this.account_type = "Current Account";
    }

    public double withdraw(double amount) throws Exception {
        if (isFrozen()) throw new Exception("Account is freezed, Deposit not allowed");

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
        
    }
}
