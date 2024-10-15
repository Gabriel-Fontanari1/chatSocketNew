package com.example.chatsocketnew;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket extends Thread {
    //atributos
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader input;
    private OutputStream output;
    private boolean running;
    private ActivityChat activityChat;
    private String username;

    //construtor
    public ServidorSocket(ActivityChat activity, String username) {
        this.activityChat = activity;
        this.username = username;
    }

    //inicia o servidor na porta do dispositivo host
    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true; //define o servidor como ativo
            System.out.println("Servidor iniciado na porta: " + port);
            start();//inicia a thread do servidor
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao iniciar o servidor.");
        }
    }

    @Override
    public void run() {
        try {
            clientSocket = serverSocket.accept(); //aceita a conexão do cliente
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //le os dados do cliente
            output = clientSocket.getOutputStream(); //prepara o canal de saida para enviar mensagens ao cliente

            String receivedMessage;
            //loop para que vai continuar enq o servidor estiver rodando
            while (running && (receivedMessage = input.readLine()) != null) {
                //divide a msg em nome de usuario e msg
                String[] parts = receivedMessage.split(": ", 2);
                String username = parts[0];
                String messageContent = parts.length > 1 ? parts[1] : "";

                System.out.println("Mensagem recebida: " + receivedMessage);

                //cria uma nova msg e manda para a activityChat
                Message message = new Message(messageContent, false, username);
                activityChat.addMessage(message); //adiciona a msg no recyclerview
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro para receber a mensagem.");
        }
    }

    //enviar msg ao cliente
    public void sendMessage(String message) {
        new Thread(new Runnable() { //executa oq a thread deve fazer, e depois executa essa tarefa em paralelo a execução do resto do programa
            @Override               //não bloquei o fluxo, nesse caso podia fazer o envio de mensagens demorar
            public void run() {
                try {
                    String messageToSend = username + ": " + message;
                    System.out.println("Enviando mensagem: " + messageToSend); //formata
                    output.write((messageToSend + "\n").getBytes()); //envia a msg ao usuario
                    output.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Erro para enviar mensagem.");
                }
            }
        }).start();
    }
}
