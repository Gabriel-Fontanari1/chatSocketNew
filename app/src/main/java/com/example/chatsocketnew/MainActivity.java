package com.example.chatsocketnew;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonBeHost;
    Button buttonConfirmarIp;
    EditText editTextIPconectar;
    EditText editTextUsername; // Username
    ServidorSocket servidorSocket;
    ClienteSocket clienteSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBeHost = findViewById(R.id.buttonBeHost);
        buttonConfirmarIp = findViewById(R.id.buttonConfirmarIp);
        editTextIPconectar = findViewById(R.id.editTextIPconectar);

        editTextUsername = findViewById(R.id.editTextUsername); // Username


        buttonBeHost.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            // Verifica se o campo tá vazio
            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Insira um nome.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ActivityChat.class);
                intent.putExtra("isServer", true);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });

        buttonConfirmarIp.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim(); // Username
            String ip = editTextIPconectar.getText().toString();
            System.out.println("IP digitado: " + ip);
            // Verifica se o campo tá vazio
            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Insira um nome.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ActivityChat.class);
                intent.putExtra("isServer", false);
                intent.putExtra("serverIp", ip);
                intent.putExtra("username", username); // Username
                startActivity(intent);
                finish();
            }
        });
    }
}