package com.example.victor.chistesvictor;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    SoundPool sp;
    AssetManager assetManager;
    int soundId;
    AssetFileDescriptor descriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assetManager=this.getAssets();
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            sp = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(aa).build();
        }else{
            sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        }
    }

    public void entrar(View v){
        lanzarSonido("aplausos.mp3");
        Intent i = new Intent(this,PantallaChistes.class);
        startActivity(i);

    }
    public void salir(View v){
        lanzarSonido("game over.mp3");
        finish();

    }

    private void lanzarSonido(String archivo) {
        try{
            descriptor = assetManager.openFd(archivo);
            soundId = sp.load(descriptor, 1);
            sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    sp.play(soundId,1,1,0,0,1);
                    //loop en bucle el 4º
                }
            });
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
