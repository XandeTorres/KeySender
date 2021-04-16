package com.keysender.client;

import com.keysender.network.TCPConnection;
import com.keysender.network.TCPConnectionListener;
import com.keysender.rsaencryption.RSAEncryption;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;


public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final String IP_ADDR = "";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private TCPConnection connection;

    private RSAEncryption encryption = new RSAEncryption();
    private BigInteger ownPublicKey, ownModulus;
    private ArrayList<BigInteger> publicKeyStorage = new ArrayList<>();
    private ArrayList<BigInteger> modulesStorage = new ArrayList<>();


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });

    }

    private  JTextArea log = new JTextArea(); // area for displaying messages
    private  JTextField fieldNickName = new JTextField("Type your nickname: ");
    private  JTextField fieldInput = new JTextField(); // field for message typing


    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("KeySender");

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickName, BorderLayout.NORTH);

        setVisible(true);

        ownPublicKey = encryption.publicKey;
        ownModulus = encryption.modulus;


        // creating a new connection, when a new client started
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            System.out.println("Connection exception: "+ e);
            //e.printStackTrace();
        }


    }



    @Override
    public void actionPerformed(ActionEvent e) {
        // method for sending message to the server

        String msg = fieldInput.getText();
        if(msg.equals(" ")) return;

        fieldInput.setText(null);
        connection.sendString(fieldNickName.getText()+ ": "+msg);

    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");

    }



    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        //method for receiving incoming message and sending it to .printMSg() method, that displaying it.
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close...");

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception... "+ e);

    }

    private synchronized void printMsg(String msg) {
        // method for displaying information at all (user's messages, logging of connections)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
