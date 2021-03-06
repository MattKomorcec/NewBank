package newbank.server;

import newbank.client.TestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TransactionTest {
    TestClient client = new TestClient();

    @Before
    //Before each test starts connection and logs in
    public void setUp() throws IOException {
        client.startConnection("localhost", 14002);
        client.loginMessage();
    }

    @After
    //After each test, stops connection
    public void complete() throws IOException {
        client.stopConnection();
    }

    @Test
    public void moveFunds_checkingToNew_returns_validResponse() throws IOException {
        //starts by creating second account
        client.secondAccount();
        client.sendMessage("3");
        client.readMessage();
        client.readMessage();
        String response1 = client.sendMessage("100.0");
        String expectedOutput1 = "Please enter name of account to transfer funds from ('q' to quit):";

        String response2 = client.sendMessage("Checking");
        String expectedOutput2 = "Please enter name of account to transfer funds to ('q' to quit):";

        client.sendMessage("new");
        client.readMessage();
        client.readMessage();
        client.readMessage();

        client.sendMessage("Y");
        client.readMessage();
        client.readMessage();
        String response3 = client.readMessage();
        String expectedOutput3 = "SUCCESS";

        assertEquals(expectedOutput1, response1);
        assertEquals(expectedOutput2, response2);
        assertEquals(expectedOutput3, response3);
    }

    @Test
    public void payFunds_fromJohnToBhagy_returns_validResponse() throws IOException {
        client.sendMessage("4");
        client.readMessage();
        String response1 = client.sendMessage("100.0");
        String expectedOutput1 = "Please enter name of your account to transfer funds from ('q' to quit):";

        String response2 = client.sendMessage("Checking");
        String expectedOutput2 = "Please enter name of payee ('q' to quit):";

        String response3 = client.sendMessage("Bhagy");
        String expectedOutput3 = "Please enter name of payee's account ('q' to quit):";

        client.sendMessage("Main");
        client.readMessage();
        client.readMessage();
        client.readMessage();

        client.sendMessage("Y");
        client.readMessage();
        client.readMessage();
        client.readMessage();
        String response4 = client.readMessage();
        String expectedOutput4 = "SUCCESS";

        assertEquals(expectedOutput1, response1);
        assertEquals(expectedOutput2, response2);
        assertEquals(expectedOutput3, response3);
        assertEquals(expectedOutput4, response4);
    }
}