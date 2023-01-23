package pl.agh.edu.kis;

import org.junit.jupiter.api.Test;
import pl.edu.agh.kis.ClientRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ClientRunnerTest {
    @Test
    public void testConstructor() throws IOException, InterruptedException {
        ClientRunner clientRunner = new ClientRunner();
        assertNotEquals(null, clientRunner);
        ClientRunner.main(new String[0]);
    }
}
