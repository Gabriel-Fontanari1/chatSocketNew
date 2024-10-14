package com.example.chatsocketnew;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ActivityChat extends AppCompatActivity {

    RecyclerView recyclerViewMessages;
    EditText inputMensagem;
    Button btnEnviar;
    List<String> messageList;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        messageList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(messageList);
        recyclerViewMessages.setAdapter(recyclerViewAdapter);

        inputMensagem = findViewById(R.id.inputMensagem);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(v -> {
            String mensagem = inputMensagem.getText().toString().trim();
            if (!mensagem.isEmpty()) {
                addMessage(mensagem);
                //tem que colocar a logica para enviar a mensagem ao servidor/cliente aqui
            }
        });
    }

    public void addMessage(String message) {
        runOnUiThread(() -> {
            messageList.add(message);
            recyclerViewAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
        });
    }
}