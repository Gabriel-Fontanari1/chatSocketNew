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

    RecyclerView recyclerViewMessages;
    EditText inputMensagem;
    Button btnEnviar;
    TextView textViewIp;
    List<Message> messageList;
    RecyclerViewAdapter recyclerViewAdapter;

    ClienteSocket clienteSocket;
    ServidorSocket servidorSocket;
    boolean isServer;
    String username; // Armazenar username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
        }

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        messageList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(messageList);
        recyclerViewMessages.setAdapter(recyclerViewAdapter);

        inputMensagem = findViewById(R.id.inputMensagem);
        btnEnviar = findViewById(R.id.btnEnviar);
        textViewIp = findViewById(R.id.textViewIp);

        isServer = getIntent().getBooleanExtra("isServer", false);

        username = getIntent().getStringExtra("username"); // Busca o username digitado

        if (isServer) {
            servidorSocket = new ServidorSocket(this, username);  // Adiciona o username como parâmetro
            servidorSocket.startServer(8080);

            String localIpAddress = getLocalIpAddress();
            textViewIp.setText("Seu IP: " + localIpAddress);
        } else {
            String serverIp = getIntent().getStringExtra("serverIp");
            System.out.println("IP do servidor recebido: " + serverIp);
            clienteSocket = new ClienteSocket(this, username);
            clienteSocket.connectToServer(serverIp, 8080);
        }


        btnEnviar.setOnClickListener(v -> {
            String mensagem = inputMensagem.getText().toString().trim();
            String nomeUsuario = username; // Usa o username correto

            if (!mensagem.isEmpty()) {
                Message message = new Message(mensagem, true, nomeUsuario); // Mensagem enviada pelo próprio usuário
                addMessage(message);

                if (isServer && servidorSocket != null) {
                    servidorSocket.sendMessage(mensagem);
                } else if (clienteSocket != null) {
                    clienteSocket.sendMessage(mensagem);
                }
                inputMensagem.setText("");
            }
        });
    }

    public void addMessage(Message message) {
        runOnUiThread(() -> {
            messageList.add(message);
            recyclerViewAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
        });
    }

    private String getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        return Formatter.formatIpAddress(ipAddress);
    }

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
