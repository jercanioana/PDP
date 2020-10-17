package model;

public class Operation {

    private int serialNumberForOperation;
    private int amount;
    private Account associatedAccount;

    @Override
    public String toString() {
        return "Operation{" +
                "serialNumberForOperation=" + serialNumberForOperation +
                ", amount=" + amount +
                '}';
    }

    public Operation(int serialNumberForOperation, int amount, Account associatedAccount) {

        this.serialNumberForOperation = serialNumberForOperation;
        this.amount = amount;
        this.associatedAccount = associatedAccount;
    }



    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int getSerialNumberForOperation() {
        return serialNumberForOperation;
    }

    public Account getAssociatedAccount() {
        return associatedAccount;
    }
}
