package com.lucasborgesdev.presentediario;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Faz Download do arquivo de Áudio
 */
public class DownloadAudioActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // preparing date for URLs for redirect
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final File file_audio = new File("/sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3");
        final String url_download_audio = "http://104.236.27.118/presente_diario/presente"
                + dateFormatNoTraces + ".mp3";
        final String title_download_audio = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_audio);
        final String nameOfFile = URLUtil.guessFileName(url_download_audio, null,
                MimeTypeMap.getFileExtensionFromUrl(url_download_audio));


        // Se audio existe
        if (!file_audio.exists()) {
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


        // Impede o início de tela em branco
        finish();
    }
}
