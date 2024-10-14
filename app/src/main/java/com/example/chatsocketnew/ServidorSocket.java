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
                System.out.println("Mensagem recebida: " + receivedMessage);
                Message message = new Message(receivedMessage, false);
                activityChat.addMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao receber a mensagem.");
        }
    }

    public void sendMessage(String message) {
        try {
            if (clientSocket != null && clientSocket.isConnected()) {
                System.out.println("Cliente está conectado. Enviando mensagem...");
                output.write((message + "\n").getBytes());
                output.flush();
            } else {
                System.out.println("Cliente não está conectado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar mensagem para o cliente.");
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