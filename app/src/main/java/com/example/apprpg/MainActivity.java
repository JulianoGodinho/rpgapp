package com.example.apprpg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button btnIniciar;
    public static MediaPlayer mp;
    public static SensorManager mSensores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mSensores = (SensorManager) getSystemService(SENSOR_SERVICE);

        mp = MediaPlayer.create(this, R.raw.batalha);

        btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirP1();
            }
        });

    }

    private void abrirP1() {
        Intent janela = new Intent(this, ActivityP1.class);
        startActivity(janela);
        finish();
    }

}