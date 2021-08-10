package util;

import model.BlockChain;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class ChatClient implements Runnable {

    private static final Random random = new Random();

    private final BlockChain blockChain;

    public ChatClient(final BlockChain blockChain) {
        this.blockChain = blockChain;
    }

    @Override
    public void run() {
        try {
            blockChain.acceptText(blockChain.getNextMessageId(), generateRandomName(), generateRandomText());
        } catch (final NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private String generateRandomName() {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        final int targetStringLength = 5;

        return random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    private String generateRandomText() {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        final int targetStringLength = 20;

        return random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

}
