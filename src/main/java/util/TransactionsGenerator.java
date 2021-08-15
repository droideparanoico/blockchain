package util;

import java.util.Arrays;
import java.util.stream.Stream;
import model.Blockchain;
import java.util.Random;

public class TransactionsGenerator implements Runnable {

    private static final Random random = new Random();
    private static final int INITIAL_COINS = 100;
    private static final String[] userNames = Stream.of(
        "Thom", "Jonny", "Ed", "Colin", "Phil"
    ).toArray(String[]::new);

    private final Blockchain blockchain;

    public TransactionsGenerator(final Blockchain blockchain) {
        this.blockchain = blockchain;
        if (blockchain.getUsersCoins().isEmpty()) {
            setInitialUserCoins();
        }
    }

    @Override
    public void run() {
        try {
            blockchain.acceptTransaction(
                blockchain.getNextTransactionId(),
                getRandomUserName(),
                getRandomAmount(),
                getRandomUserName()
            );
/*
        Catching throwable instead of exception to avoid ScheduledExecutorService from stop working
        because any thrown exception or error reaching the executor causes the executor to halt.
*/
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    private String getRandomUserName() {
        return userNames[random.nextInt(userNames.length)];
    }

    private int getRandomAmount(){
        final int MIN_AMOUNT = 1;
        final int MAX_AMOUNT = 50;

        return random.nextInt((MAX_AMOUNT - MIN_AMOUNT) + 1) + MIN_AMOUNT;
    }

    private void setInitialUserCoins() {
        Arrays.stream(userNames).forEach(userName -> blockchain.acceptUser(userName, INITIAL_COINS));
    }

}