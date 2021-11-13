package newbank.client;

import java.io.*;
import java.net.Socket;

// Test Client - basic client with Socket that sends and receives messages from server.
public class TestClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    //Login with userName "John", password "John"
    public String loginMessage() throws IOException {
        out.println("John");
        String resp = in.readLine();
        out.println("John");
        resp = in.readLine();
        resp = in.readLine();
        resp = in.readLine();
        return resp;
    }
    //Creates second account under customer John
    public void secondAccount() throws IOException {
        out.println("2");
        in.readLine();
        in.readLine();
        out.println("new");
        return;
    }
    //sends a message, and reads single line reply
    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }
    //reads single line reply
    public String readMessage() throws IOException {
        String resp = in.readLine();
        return resp;
    }
    //ends connection
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

}