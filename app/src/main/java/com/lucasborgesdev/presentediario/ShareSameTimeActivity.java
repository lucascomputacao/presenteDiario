package com.lucasborgesdev.presentediario;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShareSameTimeActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)


        // Verificando existência do áudio
        File file_audio = new File("/sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3");
        Uri uri_audio = Uri.fromFile(file_audio);
        File file_texto = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");

        // Se existe texto e áudio
        if (file_audio.exists() && file_texto.exists()) {
                try {
                    Toast.makeText(getBaseContext(),
                            "Compartilha o Áudio e o Texto no mesmo Aplicativo.", Toast.LENGTH_LONG).show();

                    // Leitura de arquivo
                    File myFile = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");
                    FileInputStream fIn = new FileInputStream(myFile);
                    BufferedReader myReader = new BufferedReader(
                            new InputStreamReader(fIn));
                    String aDataRow = "";
                    String aBuffer = "";
                    while ((aDataRow = myReader.readLine()) != null) {
                        aBuffer += aDataRow + "\n";
                    }

                    myReader.close();

                    // Compartilhar Texto e Áudio
                    String stringApp = " - Compartilhado Via Download_PresenteDiário";
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, aBuffer + stringApp);
                    share.setType("audio/*");
                    share.putExtra(Intent.EXTRA_STREAM, uri_audio);
                    startActivity(Intent.createChooser(share, "Compartilhar Áudio e Texto ao mesmo tempo com:"));

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

        } else {
            Toast.makeText(getBaseContext(),
                    "Arquivos sendo baixados\nClique em compatilhar após o término do download!'",
                    Toast.LENGTH_LONG).show();
            // Download texto
            Intent intent = new Intent(getApplicationContext(), DownloadTextoActivity.class);
            startActivity(intent);

            // Download áudio
            Intent intent_audio = new Intent(getApplicationContext(), DownloadAudioActivity.class);
            startActivity(intent_audio);
        }

        // Finaliza View enquanto faz o download do(s) arquivo(s)
        finish();
    }

}
