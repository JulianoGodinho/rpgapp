package com.example.apprpg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

public class FinalRuim extends AppCompatActivity {

    Button btnSair, btnReiniciar;
    MediaPlayer derrota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_ruim);


        derrota = MediaPlayer.create(this, R.raw.derrota);
        btnSair = findViewById(R.id.btnSair);
        btnReiniciar = findViewById(R.id.btnReiniciar);

        derrota.start();

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                derrota.stop();
                finish();
            }
        });

        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                derrota.stop();
                abrirMain();
            }
        });
    }

    private void abrirMain(){
        Intent janela = new Intent(this, MainActivity.class);
        startActivity(janela);
        finish();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(FinalRuim.this, "Você não pode voltar.", Toast.LENGTH_SHORT).show();
    }
}