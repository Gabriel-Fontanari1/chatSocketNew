package com.example.chatsocketnew;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteSocket extends Thread {
    private Socket socket;
    private BufferedReader input;
    private OutputStream output;
    private boolean running;
    private ActivityChat activityChat;

    private String username;

    public ClienteSocket(ActivityChat activity, String username) {
        this.activityChat = activity;
        this.username = username;
    }

    public void connectToServer(String ip, int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Tentando conectar ao servidor: " + ip + ":" + port);
                    socket = new Socket(ip, port);
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    output = socket.getOutputStream();
                    running = true;
                    System.out.println("Conectado ao servidor: " + ip + ":" + port);
                    start();
                } catch (UnknownHostException e) {
                    System.out.println("Erro: Endereço IP ou nome de host desconhecido.");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("Erro: Falha na conexão de rede (verifique se o IP/porta estão corretos).");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            String receivedMessage;
            while (running && (receivedMessage = input.readLine()) != null) {
                // Recebe a mensagem no formato: "username: mensagem"
                String[] parts = receivedMessage.split(": ", 2); // Divide a mensagem
                String senderName = parts[0]; // O primeiro pedaço é o nome do remetente
                String messageContent = parts.length > 1 ? parts[1] : ""; // O resto é a mensagem

                System.out.println("Mensagem recebida: " + receivedMessage);

                // Adiciona a mensagem ao chat como uma mensagem recebida
                Message message = new Message(messageContent, false, senderName); // Remetente é outro usuário
                activityChat.addMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Inclui o nome do usuário na mensagem
                    String messageToSend = username + ": " + message;
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


    public void disconnect() {
        running = false;
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
