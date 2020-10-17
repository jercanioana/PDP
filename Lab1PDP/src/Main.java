import model.Account;
import model.Operation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static ArrayList<Account> listOfAccounts = new ArrayList<>();
    public static boolean stopCond = false;
    public static int serialNumber = 0;
    static class ThreadTransfer extends Thread {


        @Override
        public void run() {
            for(int i = 0; i < 3; i++){
                try {
                    int sleep = ThreadLocalRandom.current().nextInt(6001);
                    Thread.sleep(sleep);
                    int randomPos1 = ThreadLocalRandom.current().nextInt(listOfAccounts.size());
                    int randomPos2 = ThreadLocalRandom.current().nextInt(listOfAccounts.size());
                    Account a = listOfAccounts.get(randomPos1);
                    Account b = listOfAccounts.get(randomPos2);
                    while(a.equals(b)){
                        randomPos2 = ThreadLocalRandom.current().nextInt(listOfAccounts.size());
                        b = listOfAccounts.get(randomPos2);
                    }
                    int sum = ThreadLocalRandom.current().nextInt(150);

                    if(b.balance.intValue() < sum){
                        System.out.println("Insufficient funds");
                    }else {
                        a.operationsMutex.acquire();
                        b.operationsMutex.acquire();

                        b.balance.getAndSet(b.balance.intValue() - sum);
                        a.balance.addAndGet(sum);
                        Operation operationForAccountA = new Operation(serialNumber, sum, b);
                        Operation operationForAccountB = new Operation(serialNumber,-1 * sum, a);
                        a.addOperation(operationForAccountA);
                        b.addOperation(operationForAccountB);
                        listOfAccounts.set(randomPos1, a);
                        listOfAccounts.set(randomPos2, b);
                        a.operationsMutex.release();
                        b.operationsMutex.release();
                        System.out.println(this.getId() + " transfered " + sum + "from account " + randomPos2 + " to " + randomPos1
                                        + " with serial number: " + operationForAccountA.getSerialNumberForOperation() + " "
                        + operationForAccountB.getSerialNumberForOperation());
                        serialNumber++;
                    }





                }catch(Exception e){
                    e.printStackTrace();
                }

            }

        }
    }
    static class CheckingThread extends Thread{
        @Override
        public void run() {

            while(!stopCond){
                try {
                    int sleep = ThreadLocalRandom.current().nextInt(4000);
                    Thread.sleep(sleep);
                    boolean ok = true;
                    for (Account a: listOfAccounts) {
                        int sumToCheck = 0;
                        a.operationsMutex.acquire();
                        for(Operation o:a.operations){
                            sumToCheck += o.getAmount();
                            Account accountToCheck = o.getAssociatedAccount();
                            for(Operation op: accountToCheck.operations){
                                if(op.getSerialNumberForOperation() == o.getSerialNumberForOperation()) {
                                    if (op.getAssociatedAccount() != a)
                                        ok = false;
                                }
                            }
                        }

                        if(a.balance.get() != a.initialBalance + sumToCheck){
                            ok = false;
                        }
                        a.operationsMutex.release();
                    }
                    if (ok)
                        System.out.println("All accounts are good");
                    else System.out.println("Issues found");

                }catch(Exception e){
                    e.printStackTrace();
                }

            }

        }

    }
    public static void main(String[] args) throws InterruptedException {
        Account a1 = new Account(100);
        Account a2 = new Account(150);
        Account a3 = new Account(160);
        Account a4 = new Account(110);
        listOfAccounts.add(a1);
        listOfAccounts.add(a2);
        listOfAccounts.add(a3);
        listOfAccounts.add(a4);

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        ArrayList<ThreadTransfer> threads = new ArrayList<>();
        CheckingThread thread = new CheckingThread();
        for(int i = 0; i <= n; i++){
            ThreadTransfer transfer = new ThreadTransfer();
            threads.add(transfer);
        }
        for (ThreadTransfer t:threads) {
            t.start();
        }
        thread.start();
        for (ThreadTransfer t:threads) {
            t.join();
        }
//        ThreadTransfer transfer1 = new ThreadTransfer();
//        ThreadTransfer transfer2 = new ThreadTransfer();



//        transfer1.start();
//        transfer2.start();

//        transfer2.join();
//        transfer2.join();
        stopCond = true;
        thread.join();
        System.out.println(listOfAccounts.toString());

    }
}