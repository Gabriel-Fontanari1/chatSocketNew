package com.example.chatsocketnew;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClienteSocket extends Thread {
    private Socket socket;
    private BufferedReader input;
    private OutputStream output;
    private boolean running;

    public void connectToServer(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = socket.getOutputStream();
            running = true;
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String receivedMessage;
            while (running && (receivedMessage = input.readLine()) != null) {
                //Passa a mensagem recebida para o recyclerView
                System.out.println("Mensagem recebida: " + receivedMessage);
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

    public void disconnect() {
        running = false;
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}