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
        // making URLs
        final String url_text_redirect = "http://www.transmundial.org.br/presente-diario/" + dateFormatTraces;
        String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        // Calculando a versão
        int year = cal.get(Calendar.YEAR); // get the current year
        int norma = 1997;
        int versao = year - norma;
        final String url_audio_redirect = "http://transmundial.org.br/podcast/presente_diario/" + versao
                + "/" + "presente" + dateFormatNoTraces + ".mp3";

        final String title_download_audio = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_audio_redirect);

        try {
            // Download do arquivo de Áudio
            String nameOfFile = URLUtil.guessFileName(url_audio_redirect, null,
                    MimeTypeMap.getFileExtensionFromUrl(url_audio_redirect));
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_audio_redirect));
            request.setTitle(title_download_audio);
            String description = "Áudio Presente Diário " + dateFormatTraces;
            request.setDescription(description);
            // use a linha abaixo se quiser limitar o download por wifi / tem opção de dados tbm
            //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir("/PresenteDiario", nameOfFile);
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            // Intent compartilhar audio
            Intent shareAudio = new Intent(Intent.ACTION_SEND);
            shareAudio.setType("audio/mp3");
            shareAudio.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse("sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3"));
            startActivity(Intent.createChooser(shareAudio, "Compartilhar Áudio com:"));

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }
    }
}