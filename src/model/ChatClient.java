package model;

import java.util.Random;

public class ChatClient implements Runnable {

    private final int clientId;
    private final BlockChain blockChain;
    private static final Random random = new Random();

    public ChatClient(int chatClientId, BlockChain blockChain) {
        this.clientId = chatClientId;
        this.blockChain = blockChain;
    }

    @Override
    public void run() {
        blockChain.acceptMessage(generateRandomAlphabeticString());
    }

    private String generateRandomAlphabeticString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;

        return "Client " + clientId + " says: " + random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}