package com.lucasborgesdev.presentediario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ShareActionProvider;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    // preparing date for URLs for redirect
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
    Calendar cal = Calendar.getInstance();
    Date now = cal.getTime(); // set the current datetime in a Date-object
    final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
    String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
    // Calculando a versão
    int year = cal.get(Calendar.YEAR); // get the current year
    int norma = 1997;
    int versao = year - norma;

    // making URLs
    final String url_text_redirect = "http://www.transmundial.org.br/presente-diario/" + dateFormatTraces;
    final String url_audio_redirect = "http://transmundial.org.br/podcast/presente_diario/" + versao
            + "/" + "presente" + dateFormatNoTraces + ".mp3";
    // Strings para download de áudio
    final String url_download_audio = "http://104.236.27.118/presente_diario/presente"
            + dateFormatNoTraces + ".mp3";
    final String title_download_audio = "Presente_Diário_" + dateFormatTraces + "."
            + MimeTypeMap.getFileExtensionFromUrl(url_audio_redirect);
    final String nameOfFile = URLUtil.guessFileName(url_audio_redirect, null,
            MimeTypeMap.getFileExtensionFromUrl(url_audio_redirect));
    // Strings para download de texto do servidor
    final String url_download_texto = "http://104.236.27.118/presente_diario/presente"
            + dateFormatTraces + ".txt";
    final String title_download_texto = "Presente_Diário_" + dateFormatTraces + "."
            + MimeTypeMap.getFileExtensionFromUrl(url_download_texto);

    String nameOfFileText = URLUtil.guessFileName(url_download_texto, null,
            MimeTypeMap.getFileExtensionFromUrl(url_download_texto));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dowload Data to App (Audio and Text)
        // Verificando existência do áudio
        File file_audio = new File("/sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3");

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

        // Download do arquivo de Texto
        File file_texto = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");
        // Verificando existência de texto
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

        // Redirection button to text page
        Button button = (Button) findViewById(R.id.texto);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserInternet = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url_text_redirect));
                startActivity(browserInternet);
            }
        });

        // Redirection button to audio page
        Button audio = (Button) findViewById(R.id.audio);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audioBrowserInternet = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url_audio_redirect));
                startActivity(audioBrowserInternet);
            }
        });


        // WebActivity
        final Context context = this;
        Button button_webactivity = (Button) findViewById(R.id.buttonUrl);

        button_webactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            final String url_site = "http://www.transmundial.org.br/";
            String dialog_title = "Presente Diário - Versão 0.1 "
                    + "\n"
                    + "web site: lucasborgesdev.com";
            String dialog_message = "Este aplicativo redireciona/baixa o contéudo do Presente Diário."
                    + "\n" + "Disponível em :" + "\n"
                    + "http://www.transmundial.org.br/";

            // criar dialog do menu sobre este app
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setando Título do dialog
            alertDialog.setTitle(dialog_title);
            // Mensagem do dialog
            alertDialog.setMessage(dialog_message);
            //
            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ação ao clicar no botão OK
                }
            });
            alertDialog.setPositiveButton("Ir ao site", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ação ir ao site
                    Intent browserInternet = new Intent(Intent.ACTION_VIEW, Uri.parse(url_site));
                    startActivity(browserInternet);
                }
            });
            alertDialog.show();

            return true;
        }

        // Share Text
        if (id == R.id.menu_item_share_text) {

            final Context context = this;
            Intent intent = new Intent(context, ShareTextActivity.class);
            startActivity(intent);

            return true;
        }

        // Share Audio
        if (id == R.id.menu_item_share_audio) {

            final Context context_audio = this;
            Intent intent_audio = new Intent(context_audio, ShareAudioActivity.class);
            startActivity(intent_audio);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}