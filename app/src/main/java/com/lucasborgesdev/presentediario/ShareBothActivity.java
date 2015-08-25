package com.lucasborgesdev.presentediario;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ShareBothActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Intent Texto
        Intent intent_texto = new Intent(getApplicationContext(), ShareTextActivity.class);
        startActivity(intent_texto);

        // Download de Áudio
        Intent intent_audio = new Intent(getApplicationContext(), ShareAudioActivity.class);
        startActivity(intent_audio);

        // Finaliza View enquanto faz o download do(s) arquivo(s)
        finish();
    }

}
