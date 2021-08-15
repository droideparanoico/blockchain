package model;

import util.Security;

import java.io.Serializable;
import java.security.*;

public class Transaction implements Serializable  {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String text;
    private final byte[] signature;
    private final PublicKey publicKey;

    public Transaction(
        final int id,
        final String sender,
        final int amount,
        final String receiver,
        final PrivateKey privateKey,
        final PublicKey publicKey
    ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.id = id;
        this.text = sender + " sent " + amount + " KarmaCoins to " + receiver;
        this.signature = Security.sign(text, privateKey);
        this.publicKey = publicKey;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

}
