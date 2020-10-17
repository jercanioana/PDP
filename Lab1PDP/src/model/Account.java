package model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", initialBalance=" + initialBalance +
                ", operations=" + operations +
                '}';
    }

    public AtomicInteger balance;
    public Semaphore operationsMutex;
    public int initialBalance;
    public ArrayList<Operation> operations;

    public Account(int balance) {
        this.balance = new AtomicInteger(balance);
        this.initialBalance = balance;
        this.operationsMutex = new Semaphore(1);
        this.operations = new ArrayList<>();
    }

    public AtomicInteger getBalance() {
        return balance;
    }

    public void setBalance(AtomicInteger balance) {
        this.balance = balance;
    }

    public Semaphore getOperationsMutex() {
        return operationsMutex;
    }

    public void setOperationsMutex(Semaphore operationsMutex) {
        this.operationsMutex = operationsMutex;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void addOperation(Operation operation){
        this.operations.add(operation);
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }


}
