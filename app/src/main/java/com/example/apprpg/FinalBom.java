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

public class FinalBom extends AppCompatActivity {

    Button btnSair, btnReiniciar;
    VideoView vdvTrofeu;

    MediaPlayer vitoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bom);
        getSupportActionBar().hide();

        vitoria = MediaPlayer.create(this, R.raw.vitoria);
        btnSair = findViewById(R.id.btnSair);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        vdvTrofeu = findViewById(R.id.vdvTrofeu);

        vitoria.start();

        Uri caminho = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.trofeu);
        vdvTrofeu.setVideoURI(caminho);
        vdvTrofeu.start();

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vitoria.stop();
                finish();
            }
        });

        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vitoria.stop();
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
        Toast.makeText(FinalBom.this, "Você não pode voltar.", Toast.LENGTH_SHORT).show();
    }
}
