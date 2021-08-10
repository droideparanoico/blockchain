package model;

import util.Security;

import java.io.Serializable;
import java.security.*;

public class Message implements Serializable  {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String text;
    private final byte[] signature;
    private final PublicKey publicKey;

    public Message(final int id, final String name, final String text, final PrivateKey privateKey, final PublicKey publicKey)
        throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.id = id;
        this.text = "Chatter " + name + " says: " + text;
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
