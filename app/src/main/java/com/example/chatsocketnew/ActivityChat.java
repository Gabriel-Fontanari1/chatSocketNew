package com.example.chatsocketnew;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.Manifest;


public class ActivityChat extends AppCompatActivity {

    //declaração dos componentes
    RecyclerView recyclerViewMessages;
    EditText inputMensagem;
    Button btnEnviar;
    TextView textViewIp;
    List<Message> messageList;
    RecyclerViewAdapter recyclerViewAdapter;

    //declaração dos sockets e variaveis para determinar o usuario
    ClienteSocket clienteSocket;
    ServidorSocket servidorSocket;
    boolean isServer;
    String username;

    //construtor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //permiti que o conteúdo da tela ocupe toda a área disponível
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        //verifica a permissão para acessar o estado do wifi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
        }

        //inicializa o recyclerview
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        //inicializa a lista de msg para e o adapter
        messageList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(messageList);
        recyclerViewMessages.setAdapter(recyclerViewAdapter);

        //inicializa os componentes da interface
        inputMensagem = findViewById(R.id.inputMensagem);
        btnEnviar = findViewById(R.id.btnEnviar);
        textViewIp = findViewById(R.id.textViewIp);

        isServer = getIntent().getBooleanExtra("isServer", false);

        username = getIntent().getStringExtra("username");

        //se for o dispositivo host, inicia o servidor de socket na porta 8080 e exibe o IP local
        if (isServer) {
            servidorSocket = new ServidorSocket(this, username);
            servidorSocket.startServer(8080);

            String localIpAddress = getLocalIpAddress();
            textViewIp.setText("Seu IP: " + localIpAddress);

            //se for o cliente, conecta ao servidor com o IP fornecido
        } else {
            String serverIp = getIntent().getStringExtra("serverIp");
            System.out.println("IP do servidor recebido: " + serverIp);
            clienteSocket = new ClienteSocket(this, username);
            clienteSocket.connectToServer(serverIp, 8080);
        }


        //botao enviar msg
        btnEnviar.setOnClickListener(v -> {
            String mensagem = inputMensagem.getText().toString().trim();
            String nomeUsuario = username;

            //verifica se não está vazio
            if (!mensagem.isEmpty()) {
                Message message = new Message(mensagem, true, nomeUsuario);
                addMessage(message);

                //envia via socket dependendo se for servidor ou cliente
                if (isServer && servidorSocket != null) {
                    servidorSocket.sendMessage(mensagem);
                } else if (clienteSocket != null) {
                    clienteSocket.sendMessage(mensagem);
                }
                inputMensagem.setText("");
            }
        });
    }

    //add msg na lista e atualiza o recycler
    public void addMessage(Message message) {
        runOnUiThread(() -> {
            messageList.add(message);
            recyclerViewAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
        });
    }

    //pega o ip do dispositivo usando o wifimanager
    private String getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        return Formatter.formatIpAddress(ipAddress);
    }

    //verifica o resultado da solicitação de permissões
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }
}
