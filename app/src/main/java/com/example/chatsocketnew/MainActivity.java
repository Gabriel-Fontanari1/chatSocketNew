package com.example.chatsocketnew;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //atributos
    Button buttonBeHost;
    Button buttonConfirmarIp;
    EditText editTextIPconectar;
    EditText editTextUsername;

    //construtor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBeHost = findViewById(R.id.buttonBeHost);
        buttonConfirmarIp = findViewById(R.id.buttonConfirmarIp);
        editTextIPconectar = findViewById(R.id.editTextIPconectar);
        editTextUsername = findViewById(R.id.editTextUsername);

        //botao para hostear
        buttonBeHost.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            if (username.isEmpty()) {
                //avisa o usr, sobre inserir o nome
                Toast.makeText(MainActivity.this, "Insira um nome.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ActivityChat.class);
                intent.putExtra("isServer", true); //define o usr como servidor
                intent.putExtra("username", username);//manda os dados por um extra
                startActivity(intent);
                finish();
            }
        });

        //conectar a um servidor existente
        buttonConfirmarIp.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String ip = editTextIPconectar.getText().toString(); //pega o ip digitado
            System.out.println("IP digitado: " + ip);
            if (username.isEmpty()) {
                //aviso se o nome de usr estiver vazio
                Toast.makeText(MainActivity.this, "Insira um nome.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ActivityChat.class);
                intent.putExtra("isServer", false);
                intent.putExtra("serverIp", ip); //passa a ip do host
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });
    }
}