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

    //construtor
    public ClienteSocket(ActivityChat activity, String username) {
        this.activityChat = activity;
        this.username = username;
    }

    //conecta ao servidor usando a porta e ip fornecida
    public void connectToServer(String ip, int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Tentando conectar ao servidor: " + ip + ":" + port);
                    //cria o socket e tenta se conectar ao sevidor
                    socket = new Socket(ip, port);
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    output = socket.getOutputStream();
                    running = true; //define o estado de execução do cliente
                    System.out.println("Conectado ao servidor: " + ip + ":" + port);
                    start(); //puxa a trhead para escutar as msgs do servidor
                } catch (UnknownHostException e) {
                    System.out.println("Endereço IP ou nome de host desconhecido.");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("Verifique se o IP ou a porta estão corretos.");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //metodo usado na thread para ouvir as msg do servidor
    @Override
    public void run() {
        try {
            String receivedMessage;
            //enquanto o cliente estiver rodando, ele vai continuar escutando as msg do servidor
            while (running && (receivedMessage = input.readLine()) != null) {
                //divide a mensagem recebida no formato "nomeusuario: mensagem"
                String[] parts = receivedMessage.split(": ", 2); //2 indica 2 partes
                String senderName = parts[0];
                String messageContent = parts.length > 1 ? parts[1] : "";
                //retorna um array de tamanho 1, vai verificar se a divisão gerou mais de uma parte, se sim, a mensagem real part[1] é atribuida na msg

                System.out.println("Mensagem recebida: " + receivedMessage);
                Message message = new Message(messageContent, false, senderName);
                activityChat.addMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //metodo para enviar msg ao servidor
    public void sendMessage(String message) {
        //roda em uma nova thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //formata a msg
                    String messageToSend = username + ": " + message;
                    System.out.println("Mensagem enviada: " + messageToSend);
                    //envia a msg ao servidor por output stream
                    output.write((messageToSend + "\n").getBytes());
                    output.flush(); //garante a entrega dos dados
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Erro para enviar msg.");
                }
            }
        }).start();
    }
}
