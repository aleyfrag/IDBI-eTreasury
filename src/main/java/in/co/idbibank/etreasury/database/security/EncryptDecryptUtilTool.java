package in.co.idbibank.etreasury.database.security;

import java.io.Console;
import java.util.Arrays;

public final class EncryptDecryptUtilTool {

    private EncryptDecryptUtilTool() {
    }

    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException(
                    "Run this class from a system terminal so the password is not displayed"
            );
        }

        char[] username = console.readPassword("Enter actual Oracle username: ");
        char[] password = console.readPassword("Enter actual Oracle password: ");
        char[] key = console.readPassword("Enter encryption key (minimum 16 characters): ");

        try {
            EncryptDecryptUtil encryptor = new EncryptDecryptUtil();
            console.printf("%napp.datasource.username=%s%n",
                    encryptor.encrypt(new String(username), new String(key)));
            console.printf("app.datasource.password=%s%n",
                    encryptor.encrypt(new String(password), new String(key)));
        } finally {
            Arrays.fill(username, '\0');
            Arrays.fill(password, '\0');
            Arrays.fill(key, '\0');
        }
    }
}
