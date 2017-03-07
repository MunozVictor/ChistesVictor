package com.example.victor.chistesvictor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.Random;

public class PantallaChistes extends AppCompatActivity {
    String [] chistes = {"chiste1.mp3","chiste2.mp3","chiste3.mp3","chiste4.mp3"
            ,"chiste5.mp3","chiste6.mp3","chiste7.mp3","chiste8.mp3"
            ,"chiste9.mp3","chiste10.mp3"};
    AssetManager assetManager;
    MediaPlayer mediaPlayer;

    Button btnAleatorioOrdenado , btnBucle ,btnSiguiente , btnFav;
    boolean modoAleatorio = false;
    boolean modoBucle =false;
    int chisteFav = 0;
    int pos = 0;
    int posRndm=0;
    Random r ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_chistes);
        btnAleatorioOrdenado = (Button) findViewById(R.id.btnModoAleatorio);
        btnBucle = (Button) findViewById(R.id.btnChistesEnBucle);
        btnSiguiente = (Button) findViewById(R.id.btnSigChistes);
        btnFav = (Button) findViewById(R.id.btnRepChisteFav);

        assetManager = this.getAssets();
        mediaPlayer = new MediaPlayer();
        r = new Random();

    }
    public void siguienteChiste(View v){
        Log.i("metodo:","siguiente chiste");
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        if(!modoAleatorio){

            Log.i("pos:",pos+"");
            loadDescriptor(chistes[pos]);
            mediaPlayer.start();
            Log.i("REPRODUCIENDO:",chistes[pos]);
            if(pos==chistes.length-1){
                pos =0;
            }else{
                pos++;
            }

        }else {
            Log.i("pos:",pos+"");
            posRndm = r.nextInt(chistes.length);
            loadDescriptor(chistes[posRndm]);
            mediaPlayer.start();
            Log.i("REPRODUCIENDO:",chistes[posRndm]);


        }
        showDialog();

    }
    public void modoAleatorio(View v){
        if(!modoAleatorio){
            modoAleatorio=true;
            btnAleatorioOrdenado.setText("PASAR A MODO ORDENADO");
        }else {
            modoAleatorio=false;
            btnAleatorioOrdenado.setText("PASAR A MODO ALEATORIO");
        }
    }
    public void pressbtnBucle(View v){
        if(!modoBucle){
            btnFav.setVisibility(View.INVISIBLE);
            btnSiguiente.setVisibility(View.INVISIBLE);
            btnBucle.setText("PASAR CHISTES DE UNO EN UNO");
            modoBucle=true;
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            if(modoAleatorio){
                posRndm = r.nextInt(chistes.length);
                loadDescriptorBucle(chistes[posRndm]);
            }else{
                loadDescriptorBucle(chistes[pos]);
            }



        }else{
            btnFav.setVisibility(View.VISIBLE);
            btnSiguiente.setVisibility(View.VISIBLE);
            btnBucle.setText("PASAR A CHISTES EN BUCLE");
            modoBucle=false;
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }
    }

    private void loadDescriptor(String archivo) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    Log.i("SALTA","SALTA ON COMPLETION");
                    mediaPlayer = new MediaPlayer();

                }
            });

            AssetFileDescriptor descriptor = assetManager.openFd(archivo);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadDescriptorBucle(String archivo) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    Log.i("SALTA","SALTA ON COMPLETION");

                    if(modoAleatorio){
                        posRndm = r.nextInt(chistes.length);
                        loadDescriptorBucle(chistes[posRndm]);
                        Log.i("REPRODUCIENDO:",chistes[posRndm]);
                    }else{
                        if(pos==chistes.length-1){
                            pos =0;
                        }else{
                            pos++;
                        }
                        loadDescriptorBucle(chistes[pos]);
                        Log.i("REPRODUCIENDO:",chistes[pos]);
                    }
                }
            });

            AssetFileDescriptor descriptor = assetManager.openFd(archivo);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void chisteFavorito(View v){
        Log.i("metodo:","chiste favorito");
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        Log.i("pos:",chisteFav+"");
        loadDescriptor(chistes[chisteFav]);
        mediaPlayer.start();
        Log.i("REPRODUCIENDO:",chistes[chisteFav]);

    }
    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("¿Quiere convertir este chiste como favorito?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(!modoAleatorio){
                                    if(pos ==0){
                                        chisteFav=0;
                                    }else{
                                        chisteFav=pos-1;
                                    }

                                }else {
                                    chisteFav=posRndm;
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        alertDialogBuilder.show();
    }
}
