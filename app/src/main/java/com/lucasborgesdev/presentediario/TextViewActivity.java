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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TextViewActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.textview);
        setContentView(R.layout.textview);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)

        // Download de arquivo de texto
        final String url_download_texto = "http://104.236.27.118/presente_diario/presente"
                + dateFormatTraces + ".txt";
        final String title_download_texto = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_texto);

//        TextView textView = (TextView) findViewById(R.id.show_text);
        EditText textView = (EditText) findViewById(R.id.textView);
        try {
            // Download de arquivo
            String nameOfFile = URLUtil.guessFileName(url_download_texto, null,
                    MimeTypeMap.getFileExtensionFromUrl(url_download_texto));
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_download_texto));
            request.setTitle(title_download_texto);
            String description = "Texto Presente Diário " + dateFormatTraces;
            request.setDescription(description);
            // use a linha abaixo se quiser limitar o download por wifi / tem opção de dados tbm
            //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir("PresenteDiario", nameOfFile);

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

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
            textView.setText(aBuffer);

            // Intent compartilhar texto
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            share.putExtra(Intent.EXTRA_TEXT, aBuffer + " - Compartilhado Via PresenteDiárioApp");
            startActivity(Intent.createChooser(share, "Compartilhar Texto com:"));

            // Intent compartilhar audio
            Intent shareAudio = new Intent(Intent.ACTION_SEND);
            String sharePath = Environment.getExternalStorageDirectory()
                    + "PresenteDiario/presente"+ dateFormatNoTraces + ".mp3";
            shareAudio.setType("audio/mp3");
            shareAudio.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse("sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3") );
            startActivity(Intent.createChooser(shareAudio, "Compartilhar Áudio"));

            myReader.close();
            Toast.makeText(getBaseContext(),
                    "Done reading SD 'mysdfile.txt'",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }
}