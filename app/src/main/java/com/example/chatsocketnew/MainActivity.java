package com.example.chatsocketnew;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonBeHost;
    Button buttonConfirmarIp;
    EditText editTextIPconectar;
    ServidorSocket servidorSocket;
    ClienteSocket clienteSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBeHost = findViewById(R.id.buttonBeHost);
        buttonConfirmarIp = findViewById(R.id.buttonConfirmarIp);
        editTextIPconectar = findViewById(R.id.editTextIPconectar);

        buttonBeHost.setOnClickListener(v -> {
            servidorSocket = new ServidorSocket();
            servidorSocket.startServer(12345);
            Intent intent = new Intent(MainActivity.this, ActivityChat.class);
            startActivity(intent);
            //tem que passar um intent extra, com os valores do hoster
        });

        buttonConfirmarIp.setOnClickListener(v -> {
            String ip = editTextIPconectar.getText().toString();
            clienteSocket = new ClienteSocket();
            clienteSocket.connectToServer(ip, 12345);
            Intent intent = new Intent(MainActivity.this, ActivityChat.class);
            startActivity(intent);
        });
    }
}