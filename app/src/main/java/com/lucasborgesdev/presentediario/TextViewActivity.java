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
        final String url_download_texto = "http://104.236.27.118/presente_diario/presente"
                + dateFormatTraces + ".txt";
        final String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)

        final String title_download_texto = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_texto);
        String nameOfFile = URLUtil.guessFileName(url_download_texto, null,
                MimeTypeMap.getFileExtensionFromUrl(url_download_texto));

        TextView textView = (TextView) findViewById(R.id.textView);
        File file_texto = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");
        Uri uri_texto = Uri.fromFile(file_texto);

        // Baixa o arquivo se ele não existir
        if (file_texto.exists()) {
            try {
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
                textView.setText(aBuffer);

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Arquivo de Texto sendo baixado.\nTente novamente após o download.",
                    Toast.LENGTH_LONG).show();
            // Download de arquivo
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

            finish();
        }


    }
}
