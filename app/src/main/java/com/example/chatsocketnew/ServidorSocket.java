package com.example.chatsocketnew;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader input;
    private OutputStream output;
    private boolean running;
    private ActivityChat activityChat;

    public ServidorSocket(ActivityChat activity) {
        this.activityChat = activity;
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            clientSocket = serverSocket.accept();
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = clientSocket.getOutputStream();

            String receivedMessage;
            while (running && (receivedMessage = input.readLine()) != null) {
                activityChat.addMessage(receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            output.write((message + "\n").getBytes());
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        try {
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}