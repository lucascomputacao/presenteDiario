package com.lucasborgesdev.presentediario;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShareAudioActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)

        Toast.makeText(getBaseContext(), "Você escolheu compartilhar Áudio.", Toast.LENGTH_SHORT ).show();

        try {
            // Verificando existência do áudio
            File file_audio = new File("/sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3");
            Uri uri = Uri.fromFile(file_audio);
            if (file_audio.exists()) {
                // Intent compartilhar audio
                Intent shareAudio = new Intent(Intent.ACTION_SEND);
                shareAudio.setType("audio/mp3");
                shareAudio.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareAudio, "Compartilhar Áudio com:"));
            } else {
                // Mensagem para usuário
                Toast.makeText(getBaseContext(),
                        "Arquivo de áudio será baixado.\nClique no Botão Compatilhar Após o Término do Download!",
                        Toast.LENGTH_LONG).show();
                // Download de Áudio
                Intent intent = new Intent(getApplicationContext(), DownloadAudioActivity.class);
                startActivity(intent);
            }

            // Não mostrar tela em branco
            finish();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }
    }

}