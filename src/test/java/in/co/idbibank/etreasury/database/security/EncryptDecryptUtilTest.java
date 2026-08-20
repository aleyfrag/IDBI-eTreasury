package in.co.idbibank.etreasury.database.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EncryptDecryptUtilTest {

    private final EncryptDecryptUtil encryptor = new EncryptDecryptUtil();

    @Test
    void encryptsAndDecryptsPassword() {
        String password = "test-oracle-password";
        String key = "test-encryption-key-123456789";

        String encrypted = encryptor.encrypt(password, key);

        assertTrue(encrypted.startsWith("ENC("));
        assertNotEquals(password, encrypted);
        assertEquals(password, encryptor.decrypt(encrypted, key));
    }

    @Test
    void encryptsAndDecryptsUsername() {
        String username = "test_database_user";
        String key = "test-encryption-key-123456789";

        String encrypted = encryptor.encrypt(username, key);

        assertTrue(encrypted.startsWith("ENC("));
        assertNotEquals(username, encrypted);
        assertEquals(username, encryptor.decrypt(encrypted, key));
    }
}
