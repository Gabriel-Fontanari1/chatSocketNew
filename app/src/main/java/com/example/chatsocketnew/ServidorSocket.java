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
    private String username; // Adiciona o username do host

    public ServidorSocket(ActivityChat activity, String username) {
        this.activityChat = activity;
        this.username = username;
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Servidor iniciado na porta: " + port);
            start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao iniciar o servidor.");
        }
    }

    @Override
    public void run() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = clientSocket.getOutputStream();

            String receivedMessage;
            while (running && (receivedMessage = input.readLine()) != null) {
                String[] parts = receivedMessage.split(": ", 2); // Divide a mensagem
                String username = parts[0];
                String messageContent = parts.length > 1 ? parts[1] : "";

                System.out.println("Mensagem recebida: " + receivedMessage);

                Message message = new Message(messageContent, false, username);
                activityChat.addMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao receber a mensagem.");
        }
    }

    public void sendMessage(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String messageToSend = username + ": " + message;  // Adiciona o username à mensagem
                    System.out.println("Enviando mensagem: " + messageToSend);
                    output.write((messageToSend + "\n").getBytes());
                    output.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Erro ao enviar mensagem.");
                }
            }
        }).start();
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
