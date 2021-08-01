package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class Security {

    public static final String PRIVATE_KEY = "KeyPair/privateKey";
    public static final String PUBLIC_KEY = "KeyPair/publicKey";

    public static PublicKey getPublic()
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        final byte[] keyBytes = Files.readAllBytes(new File(PUBLIC_KEY).toPath());
        final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        final KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PrivateKey getPrivate()
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        final byte[] keyBytes = Files.readAllBytes(new File(PRIVATE_KEY).toPath());
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        final KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static byte[] sign(final String data, final PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException  {
        final Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        rsa.update(data.getBytes());
        return rsa.sign();
    }

    public static boolean verifySignature(final String data, final byte[] signature, final PublicKey publicKey) {
        try {
            final Signature verifier = Signature.getInstance("SHA1withRSA");
            verifier.initVerify(publicKey);
            verifier.update(data.getBytes());
            return verifier.verify(signature);
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void generateKeys()  {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            if (keyGen != null) {
                keyGen.initialize(2048);
                final KeyPair keyPair = keyGen.generateKeyPair();
                final PrivateKey privateKey = keyPair.getPrivate();
                final PublicKey publicKey = keyPair.getPublic();
                FileManagement.saveKey(PUBLIC_KEY, publicKey.getEncoded());
                FileManagement.saveKey(PRIVATE_KEY, privateKey.getEncoded());
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

}
