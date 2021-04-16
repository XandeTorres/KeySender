import com.keysender.network.TCPConnection;
import com.keysender.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    public static void main(String[] args) {
        new ChatServer();
    }

    private ArrayList<TCPConnection> connections = new ArrayList<>();
   // public ArrayList<BigInteger> publicKeys = new ArrayList<>();
   // public ArrayList<BigInteger> modules = new ArrayList<>();

    private ChatServer(){
        System.out.println("Server running... ");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true){
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e){
                    System.out.println("TCPConnection exception: "+ e);
                }
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
       // publicExponents.add(rsaencryption.publicKey);
       // modules.add(rsaencryption.modulus);
        sendToAllConnections("Client connected: "+ tcpConnection);
    }




    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        //here, message is received by the server, than the server sends message to all clients
        sendToAllConnections(value);


    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: "+ tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception:" + e);
    }
    private void sendToAllConnections(String value){
        //this method sends information to all users (messages, logging of connection)
        System.out.println(value);
        int connectionsSize =connections.size();
        for (int i = 0; i< connectionsSize; i++)
            connections.get(i).sendString(value);

    }
}
