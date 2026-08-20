package in.co.idbibank.etreasury.database.security;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptDecryptUtil {

    private static final String PREFIX = "ENC(";
    private static final String SUFFIX = ")";
    private static final String CIPHER = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH_BITS = 128;

    private final SecureRandom secureRandom = new SecureRandom();

    /** Encrypts a datasource credential and returns an ENC(...) property value. */
    public String encrypt(String plainCredential, String encryptionKey) {
        validateEncryptionKey(encryptionKey);
        if (plainCredential == null || plainCredential.isBlank()) {
            throw new IllegalArgumentException("Datasource credential must not be blank");
        }

        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    createKey(encryptionKey),
                    new GCMParameterSpec(TAG_LENGTH_BITS, iv)
            );

            byte[] encrypted =
                    cipher.doFinal(plainCredential.getBytes(StandardCharsets.UTF_8));
            byte[] payload = ByteBuffer.allocate(iv.length + encrypted.length)
                    .put(iv)
                    .put(encrypted)
                    .array();

            return PREFIX + Base64.getEncoder().encodeToString(payload) + SUFFIX;
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to encrypt datasource credential", exception);
        }
    }

    /** Decrypts a datasource credential in memory during connection-pool creation. */
    public String decrypt(String encryptedCredential, String encryptionKey) {
        validateEncryptionKey(encryptionKey);

        if (encryptedCredential == null
                || !encryptedCredential.startsWith(PREFIX)
                || !encryptedCredential.endsWith(SUFFIX)) {
            throw new IllegalStateException(
                    "Datasource credential must use the ENC(...) format. "
                            + "Generate it with EncryptDecryptUtilTool."
            );
        }

        try {
            String encoded = encryptedCredential.substring(
                    PREFIX.length(),
                    encryptedCredential.length() - SUFFIX.length()
            );
            byte[] payload = Base64.getDecoder().decode(encoded);
            if (payload.length <= IV_LENGTH) {
                throw new IllegalArgumentException("Encrypted database password is invalid");
            }

            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[payload.length - IV_LENGTH];
            System.arraycopy(payload, 0, iv, 0, IV_LENGTH);
            System.arraycopy(payload, IV_LENGTH, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    createKey(encryptionKey),
                    new GCMParameterSpec(TAG_LENGTH_BITS, iv)
            );
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "Unable to decrypt datasource credential. Check its value and encryption key.",
                    exception
            );
        }
    }

    private SecretKeySpec createKey(String encryptionKey) throws GeneralSecurityException {
        byte[] key = MessageDigest.getInstance("SHA-256")
                .digest(encryptionKey.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, "AES");
    }

    private void validateEncryptionKey(String encryptionKey) {
        if (encryptionKey == null || encryptionKey.length() < 16) {
            throw new IllegalArgumentException(
                    "APP_DB_ENCRYPTION_KEY must contain at least 16 characters"
            );
        }
    }
}
