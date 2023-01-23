package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CryptUtilsTest {
    @Test
    public void testSHA_512_SecurePassword() {
        CryptUtils cryptUtils = new CryptUtils();
        String passwordToHash;
        passwordToHash = cryptUtils.SHA_512_SecurePassword("Password", "Salt");
        assertNotEquals("Password", passwordToHash);
    }
}
