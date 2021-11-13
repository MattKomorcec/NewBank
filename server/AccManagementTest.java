package newbank.server;

import static org.junit.Assert.*;

import newbank.client.TestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class AccManagementTest {
    TestClient client = new TestClient();

    @Before
    //starts by logging in
    public void setUp() throws IOException {
        client.startConnection("localhost", 14002);
        client.loginMessage();
    }

    @After
    //stops connection after each test to reset back to main menu
    public void complete() throws IOException {
        client.stopConnection();
    }

    @Test
    //Test1 - test showAccounts`
    public void test1() throws IOException {
        String response = client.sendMessage("1");

        String expectedOutput = "Checking: 250.0";
        assertEquals(expectedOutput, response);
    }

    @Test
    //Test 2 - test newAccount(), with name "new"
    public void test2() throws IOException {
        client.sendMessage("2");
        client.readMessage();
        String response = client.sendMessage("new");

        String expectedOutput = "SUCCESS";
        assertEquals(expectedOutput, response);
    }

    @Test
    //Test 3 - test existing account fail
    public void test3() throws IOException {
        client.sendMessage("2");
        client.readMessage();
        String response = client.sendMessage("Checking");

        String expectedOutput = "Account name is taken, please try again.";
        assertEquals(expectedOutput, response);
    }

    @Test
    //Test 4 - test blank account name
    public void test4() throws IOException {
        client.sendMessage("2");
        client.readMessage();
        String response = client.sendMessage("");

        String expectedOutput = "Account name must not be blank, please try again.";
        assertEquals(expectedOutput, response);
    }

    @Test
    //Test 5 - test blank account name
    public void test5() throws IOException {
        String response1 = client.sendMessage("1");
        String response2 = client.readMessage();

        String expectedOutput = "Checking: 250.0";
        String expectedOutput2 = "new: 0.0";
        assertEquals(expectedOutput, response1);
        assertEquals(expectedOutput2, response2);
    }

}