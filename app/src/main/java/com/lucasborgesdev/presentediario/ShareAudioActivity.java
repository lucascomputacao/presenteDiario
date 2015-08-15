package com.lucasborgesdev.presentediario;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShareAudioActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final String url_download_audio = "http://104.236.27.118/presente_diario/presente"
                + dateFormatNoTraces + ".mp3";
        final String title_download_audio = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_audio);
        final String nameOfFile = URLUtil.guessFileName(url_download_audio, null,
                MimeTypeMap.getFileExtensionFromUrl(url_download_audio));

        try {
            // Verificando existência do áudio
            File file_audio = new File("/sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3");
            Uri uri = Uri.fromFile(file_audio);
            if (file_audio.exists()) {
                // Intent compartilhar audio
                Intent shareAudio = new Intent(Intent.ACTION_SEND);
                shareAudio.setType("audio/mp3");
                shareAudio.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(shareAudio, "Compartilhar Áudio com:"));
            } else {
                // Mensagem para usuário
                Toast.makeText(getBaseContext(),
                        "Arquivo de áudio será baixado\nClique em compatilhar após o término do download!'",
                        Toast.LENGTH_LONG).show();
                // Download de Áudio
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_download_audio));
                request.setTitle(title_download_audio);
                String description = "Áudio Presente Diário " + dateFormatTraces;
                request.setDescription(description);
                // use a linha abaixo se quiser limitar o download por wifi / tem opção de dados tbm
                //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir("/PresenteDiario", nameOfFile);
                // Download manager
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }


        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }
    }
}