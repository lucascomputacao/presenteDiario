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
 * Faz o Download do Arquivo de Texto caso o mesmo não exista.
 */
public class DownloadTextoActivity extends Activity {

    public void onCreate(Bundle savedInstaceState) {

        super.onCreate(savedInstaceState);

        // preparing date for URLs for redirect
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)

        // URLs
        final String url_download_texto = "http://104.236.27.118/presente_diario/presente"
                + dateFormatTraces + ".txt";
        final String title_download_texto = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_texto);
        String nameOfFileText = URLUtil.guessFileName(url_download_texto, null,
                MimeTypeMap.getFileExtensionFromUrl(url_download_texto));

        File file_texto = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");

        if (!file_texto.exists()) {
            // Download texto
            DownloadManager.Request request_text = new DownloadManager.Request(Uri.parse(url_download_texto));
            request_text.setTitle(title_download_texto);
            String description_text = "Texto Presente Diário " + dateFormatTraces;
            request_text.setDescription(description_text);
            // use a linha abaixo se quiser limitar o download por wifi / tem opção de dados tbm
            //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request_text.allowScanningByMediaScanner();
            request_text.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request_text.setDestinationInExternalPublicDir("PresenteDiario", nameOfFileText);
            // Download manager
            DownloadManager manager_text = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager_text.enqueue(request_text);
        }


        // Impede tela em branco ao startar Activity
        finish();
    }
}
