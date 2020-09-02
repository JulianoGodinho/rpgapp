package com.example.apprpg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class ActivityP1 extends AppCompatActivity {

    TextView txtHpBoss, txtHpPersonagem, txtInfoPersonagem, txtInfoBoss, txtBoss, txtPersonagem, txtTotalHpBoss, txtTotalHpPersonagem;
    Button btnAtaqueInvestida, btnAtaqueChifrada, btnAtaqueArremesso, btnVoz;
    VideoView vdvLuta;
    Sensor sLuminosidade, sAcelerometro;
    LinearLayout layoutPrincipal;
    TextToSpeech tts;

    int hpBoss = 15000, ataqueVideoPersonagem, ataqueVideoBoss, controle;
    int hpPersonagem = 9000;
    int sacudidasX = 0, sacudidasY = 0, sacudidasZ = 0;
    double vLuminosidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1);
        getSupportActionBar().hide();

        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        MainActivity.mp.start();

        txtBoss = findViewById(R.id.txtBoss);
        txtPersonagem = findViewById(R.id.txtPersonagem);
        txtTotalHpBoss = findViewById(R.id.txtTotalHpBoss);
        txtTotalHpPersonagem = findViewById(R.id.txtTotalHpPersonagem);
        layoutPrincipal = findViewById(R.id.layoutPrincipal);
        txtInfoBoss = findViewById(R.id.txtInfoBoss);
        txtInfoPersonagem = findViewById(R.id.txtInfoPersonagem);
        txtHpBoss = findViewById(R.id.txtHpBoss);
        txtHpPersonagem = findViewById(R.id.txtHpPersonagem);
        btnVoz = findViewById(R.id.btnVoz);
        btnAtaqueInvestida = findViewById(R.id.btnAtaqueInvestida);
        btnAtaqueArremesso = findViewById(R.id.btnAtaqueArremesso);
        btnAtaqueChifrada = findViewById(R.id.btnAtaqueChifrada);
        vdvLuta = findViewById(R.id.vdvLuta);

        btnAtaqueChifrada.setEnabled(false);

        btnAtaqueInvestida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jogar(1);
            }
        });

        btnAtaqueArremesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jogar(2);
            }
        });

        btnAtaqueChifrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jogar(3);
            }
        });

        btnVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturar();
            }
        });

        //Pegando os sensores pro tipo
        sLuminosidade = MainActivity.mSensores.getDefaultSensor(Sensor.TYPE_LIGHT);
        sAcelerometro = MainActivity.mSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Vinculando os sensores com suas classes
        MainActivity.mSensores.registerListener(
                new Luminosidade(),
                sLuminosidade,
                SensorManager.SENSOR_DELAY_GAME
        );

        MainActivity.mSensores.registerListener(
                new Acelerometro(),
                sAcelerometro,
                SensorManager.SENSOR_DELAY_GAME
        );

    }

    class Acelerometro implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            if(x<-15||x>25){
                sacudidasX++;
            }

            if(y<-10||y>20){
                sacudidasY++;
            }
            //Habilitando um novo ataque atraves dos sensores e mudando o nome dos buttons
            if(sacudidasY>=10){
                btnAtaqueInvestida.setText("Investida Melhorada");
            }

            if (sacudidasX >=10){
                btnAtaqueChifrada.setText("Chifrada melhorada");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    class Luminosidade implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            double lux = event.values[0];
            vLuminosidade = lux;
            //habilitando novo ataque e mudando as cores de background e dos textview
            if(lux <= 2){
                layoutPrincipal.setBackgroundColor(Color.BLACK);
                txtBoss.setTextColor(Color.WHITE);
                txtPersonagem.setTextColor(Color.WHITE);
                txtHpBoss.setTextColor(Color.WHITE);
                txtHpPersonagem.setTextColor(Color.WHITE);
                txtTotalHpBoss.setTextColor(Color.WHITE);
                txtTotalHpPersonagem.setTextColor(Color.WHITE);
                txtInfoBoss.setTextColor(Color.WHITE);
                txtInfoPersonagem.setTextColor(Color.WHITE);
                btnAtaqueArremesso.setText("Arremesso furtivo");

            } else {
                layoutPrincipal.setBackgroundColor(Color.WHITE);
                txtBoss.setTextColor(Color.BLACK);
                txtPersonagem.setTextColor(Color.BLACK);
                txtHpBoss.setTextColor(Color.BLACK);
                txtHpPersonagem.setTextColor(Color.BLACK);
                txtTotalHpBoss.setTextColor(Color.BLACK);
                txtTotalHpPersonagem.setTextColor(Color.BLACK);
                txtInfoBoss.setTextColor(Color.BLACK);
                txtInfoPersonagem.setTextColor(Color.BLACK);
                btnAtaqueArremesso.setText("Arremesso");
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

//criando um ataque sorteado com chance de critico e erro,verificando o sensor para habilitar novo ataque,
    //escolhendo o video,narrando o txt com o dano causado
    private void ataqueInvestidaPersonagem() {

        Random rd = new Random(new Date().getTime());
        controle++;
        int sorteado = rd.nextInt(10) + 1;

        if (sacudidasY >=10){
            if(sorteado == 1){
                ataqueVideoPersonagem = 1;
                txtInfoPersonagem.setText("O boss defendeu o ataque.");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if(sorteado >=2 && sorteado <=9){
                hpBoss = hpBoss - 1200;
                ataqueVideoPersonagem = 1;
                txtInfoPersonagem.setText("Ataque melhorado. -1200");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if(sorteado == 10){
                hpBoss = hpBoss - 1700;
                ataqueVideoPersonagem = 1;
                txtInfoPersonagem.setText("Dano critico melhorado. -1700");
                narrar(txtInfoPersonagem.getText().toString());
            }
            sacudidasY = 0;
            btnAtaqueInvestida.setText("Investida");
        } else {
            if (sorteado == 1) {
                ataqueVideoPersonagem = 1;
                txtInfoPersonagem.setText("O boss defendeu o ataque.");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if (sorteado >= 2 && sorteado <= 9) {
                hpBoss = hpBoss - 1000;
                ataqueVideoPersonagem = 1;
                txtInfoPersonagem.setText("Ataque normal. -1000");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if (sorteado == 10) {
                hpBoss = hpBoss - 1500;
                ataqueVideoPersonagem = 1;
                txtInfoPersonagem.setText("Dano Critico. -1500");
                narrar(txtInfoPersonagem.getText().toString());
            }
        }
    }
    //criando um ataque sorteado com chance de critico e erro,verificando o sensor para habilitar novo ataque,
    //escolhendo o video,narrando o txt com o dano causado

    private void ataqueArremessoPersonagem() {
        Random rd = new Random(new Date().getTime());
        int sorteado = rd.nextInt(10) + 1;
        controle++;

        if (vLuminosidade > 2){
            if (sorteado == 1) {
                ataqueVideoPersonagem = 2;
                txtInfoPersonagem.setText("O boss defendeu o ataque.");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if (sorteado >= 2 && sorteado <= 8) {
                hpBoss = hpBoss - 800;
                ataqueVideoPersonagem = 2;
                txtInfoPersonagem.setText("Ataque normal. -800");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if (sorteado >= 9 && sorteado <=10) {
                hpBoss = hpBoss - 2000;
                ataqueVideoPersonagem = 2;
                txtInfoPersonagem.setText("Dano Critico. -2000");
                narrar(txtInfoPersonagem.getText().toString());
            }
        }

        if (vLuminosidade <= 2){
            if (sorteado >= 1 && sorteado <= 8) {
                hpBoss = hpBoss - 1000;
                ataqueVideoPersonagem = 2;
                txtInfoPersonagem.setText("Ataque furtivo. -1000");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if (sorteado >= 9 && sorteado <=10) {
                hpBoss = hpBoss - 2200;
                ataqueVideoPersonagem = 2;
                txtInfoPersonagem.setText("Dano Critico e furtivo. -2200");
                narrar(txtInfoPersonagem.getText().toString());
            }
        }
    }
//criando um ataque sorteado com chance de critico e erro,verificando o sensor para habilitar novo ataque,
    //escolhendo o video,narrando o txt com o dano causado
    private void ataqueChifradaPersonagem(){
        Random rd = new Random(new Date().getTime());
        int sorteado = rd.nextInt(10) + 1;

        if (sacudidasX >=10){
            if (sorteado == 1) {
                ataqueVideoPersonagem = 3;
                txtInfoPersonagem.setText("O boss defendeu o ataque.");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if (sorteado >= 2 && sorteado <= 9) {
                hpBoss = hpBoss - 2500;
                ataqueVideoPersonagem = 3;
                txtInfoPersonagem.setText("Ataque melhorado -2500");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if (sorteado == 10) {
                hpBoss = hpBoss - 3000;
                ataqueVideoPersonagem = 3;
                txtInfoPersonagem.setText("Dano critico melhorado. -3000");
                narrar(txtInfoPersonagem.getText().toString());
            }
            sacudidasX = 0;
            btnAtaqueChifrada.setText("Chifrada");
        } else {
            if (sorteado == 1) {
                ataqueVideoPersonagem = 3;
                txtInfoPersonagem.setText("O boss defendeu o ataque.");
                narrar(txtInfoPersonagem.getText().toString());
            }
            if (sorteado >= 2 && sorteado <= 9) {
                hpBoss = hpBoss - 2000;
                ataqueVideoPersonagem = 3;
                txtInfoPersonagem.setText("Ataque normal. -2000");
                narrar(txtInfoPersonagem.getText().toString());

            }
            if (sorteado == 10) {
                hpBoss = hpBoss - 2500;
                ataqueVideoPersonagem = 3;
                txtInfoPersonagem.setText("Dano Critico. -2500");
                narrar(txtInfoPersonagem.getText().toString());
            }
        }
        controle = 0;
    }

//controlando a variavel para habilitar o ataque chifrada
    private void controleAtaques(){

        if (controle == 2) {
            btnAtaqueChifrada.setEnabled(true);
        }
        if (controle > 2) {
            controle = 0;
        }
    }
   //metodo sorteia qual sera o ataque do boss e seleciona o video

    private void ataqueBoss(){

        Random rd = new Random(new Date().getTime());
        int sorteado2 = rd.nextInt(2) + 1;


        if (sorteado2 == 1){
            hpPersonagem = hpPersonagem - 500;
            ataqueVideoBoss = 1;
        }
        if (sorteado2 == 2){
            hpPersonagem = hpPersonagem - 800;
            ataqueVideoBoss = 2;
        }


    }

    //vincula o ataque com o video

    private void verificaVideoPersonagem(){

        if (ataqueVideoPersonagem == 1){
            Uri caminho = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.investida);
            vdvLuta.setVideoURI(caminho);
            vdvLuta.start();
        } if (ataqueVideoPersonagem == 2){
            Uri caminho = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.arremesso);
            vdvLuta.setVideoURI(caminho);
            vdvLuta.start();
        } if (ataqueVideoPersonagem == 3){
            Uri caminho = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chifrada);
            vdvLuta.setVideoURI(caminho);
            vdvLuta.start();
        }
    }

   //vincula o ataque com o video
    private void verificaVideoBoss(){

        btnAtaqueInvestida.setEnabled(false);
        btnAtaqueArremesso.setEnabled(false);

        if (ataqueVideoBoss == 1){
            Uri caminho = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.soco);
            vdvLuta.setVideoURI(caminho);
            vdvLuta.start();
            txtInfoBoss.setText("O boss causou 500 de dano");
            narrar(txtInfoBoss.getText().toString());
        } if (ataqueVideoBoss == 2){
            Uri caminho = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.raio);
            vdvLuta.setVideoURI(caminho);
            vdvLuta.start();
            txtInfoBoss.setText("O boss causou 800 de dano");
            narrar(txtInfoBoss.getText().toString());
        }

        vdvLuta.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnAtaqueInvestida.setEnabled(true);
                btnAtaqueArremesso.setEnabled(true);
            }
        });
    }

    private void videoBom(){
        Uri caminho = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.morteboss);
        vdvLuta.setVideoURI(caminho);
        vdvLuta.start();
    }

    private void videoRuim(){
        Uri caminho = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.morte);
        vdvLuta.setVideoURI(caminho);
        vdvLuta.start();
    }


    private void abrirFinalBom() {
        Intent janela = new Intent(this, FinalBom.class);
        startActivity(janela);
        MainActivity.mp.stop();
        finish();
    }

    private void abrirFinalRuim() {
        Intent janela = new Intent(this, FinalRuim.class);
        startActivity(janela);
        MainActivity.mp.stop();
        finish();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(ActivityP1.this, "Você não pode voltar.", Toast.LENGTH_SHORT).show();
    }

//recebe um parametro relacionado aos metodos de ataque
// verifica o hp para abrir as janelas de vitoria ou derrota
//usando o metodo onCompletion para sincronizar os videos

    private void jogar(int acao){
        //desabilita os btn para não serem usados inumeras vezes
        btnAtaqueInvestida.setEnabled(false);
        btnAtaqueArremesso.setEnabled(false);
        btnAtaqueChifrada.setEnabled(false);
        btnVoz.setEnabled(false);

        if(acao==1) ataqueInvestidaPersonagem();
        if(acao==2) ataqueArremessoPersonagem();
        if(acao==3) ataqueChifradaPersonagem();

        verificaVideoPersonagem();

        vdvLuta.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(hpBoss<=0){
                    hpBoss = 0;
                    txtHpBoss.setText("HP " + hpBoss);
                    videoBom();
                    vdvLuta.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            abrirFinalBom();
                        }
                    });

                } else {
                    txtHpBoss.setText("HP " + hpBoss);
                    ataqueBoss();
                    verificaVideoBoss();

                    vdvLuta.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if(hpPersonagem<=0){
                                hpPersonagem = 0;
                                txtHpPersonagem.setText("HP " + hpPersonagem);
                                videoRuim();
                                vdvLuta.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        abrirFinalRuim();
                                    }
                                });

                            } else {
                                txtHpPersonagem.setText("HP " + hpPersonagem);
                                btnAtaqueInvestida.setEnabled(true);
                                btnAtaqueArremesso.setEnabled(true);
                                btnVoz.setEnabled(true);
                                controleAtaques();

                            }
                        }
                    });
                }
            }
        });
    }

    private void narrar(String texto) {
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void capturar(){
        Intent iSTT = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        iSTT.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        iSTT.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        iSTT.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale seu ataque!");
        startActivityForResult(iSTT, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> resultado =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String textoReconhecido = resultado.get(0);

                if(textoReconhecido.toUpperCase().contains("INVESTIDA")) {
                    jogar(1);
                }


                if(textoReconhecido.toUpperCase().contains("ARREMESSO")) {
                    jogar(2);
                }


                if(textoReconhecido.toUpperCase().contains("CHIFRADA") && controle ==2) {
                    jogar(3);
                }

            }
        }
    }


}