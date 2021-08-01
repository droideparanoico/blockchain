package model;

import util.Security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class ChatClient implements Runnable {

    private final int clientId;
    private final BlockChain blockChain;
    private static final Random random = new Random();

    public ChatClient(final int chatClientId, final BlockChain blockChain) {
        this.clientId = chatClientId;
        this.blockChain = blockChain;
    }

    @Override
    public void run() {
        try {
            blockChain.acceptMessage(createMessage());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage()
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException, InvalidKeySpecException {
        return new Message(
                blockChain.getLastMessageId() + 1,
                clientId,
                generateRandomAlphabeticString(),
                Security.getPrivate(),
                Security.getPublic()
        );
    }

    private String generateRandomAlphabeticString() {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        final int targetStringLength = 10;

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}