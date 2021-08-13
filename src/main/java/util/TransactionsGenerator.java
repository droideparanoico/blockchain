package util;

import java.util.Arrays;
import java.util.stream.Stream;
import model.BlockChain;
import exceptions.NotEnoughCoinsException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class TransactionsGenerator implements Runnable {

    private static final Random random = new Random();
    private static final int INITIAL_COINS = 100;
    private static final String[] userNames = Stream.of(
        "Thom", "Jonny", "Ed", "Colin", "Phil"
    ).toArray(String[]::new);

    private final BlockChain blockChain;

    public TransactionsGenerator(final BlockChain blockChain) {
        this.blockChain = blockChain;
        if (blockChain.getUsersCoins().isEmpty()) {
            setInitialUserCoins();
        }
    }

    @Override
    public void run() {
        try {
            blockChain.acceptTransaction(
                blockChain.getNextTransactionId(),
                getRandomUserName(),
                getRandomAmount(),
                getRandomUserName()
            );
        } catch (final NoSuchAlgorithmException |
            SignatureException |
            InvalidKeyException |
            IOException |
            InvalidKeySpecException |
            NotEnoughCoinsException e)
        {
            e.printStackTrace();
        }
    }

    private String getRandomUserName() {
        return userNames[random.nextInt(userNames.length)];
    }

    private int getRandomAmount(){
        final int MIN_AMOUNT = 1;
        final int MAX_AMOUNT = 100;

        return random.nextInt((MAX_AMOUNT - MIN_AMOUNT) + 1) + MIN_AMOUNT;
    }

    private void setInitialUserCoins() {
        Arrays.stream(userNames).forEach(userName -> blockChain.acceptUser(userName, INITIAL_COINS));
    }

}