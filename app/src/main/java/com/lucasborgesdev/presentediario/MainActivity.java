package com.lucasborgesdev.presentediario;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // preparing date for URLs for redirect
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)


        int year = cal.get(Calendar.YEAR); // get the current year
        // acertando a versao
        int norma = 1997;
        int versao = year - norma;

        // making URLs
        final String url_text = "http://www.transmundial.org.br/presente-diario/" + dateFormatTraces;
        final String url_audio = "http://transmundial.org.br/podcast/presente_diario/" + versao
                + "/" + "presente" + dateFormatNoTraces + ".mp3";

        // Redirection button to text page
        Button button = (Button) findViewById(R.id.texto);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserInternet = new Intent(Intent.ACTION_VIEW,
                        // Uri.parse("http://www.transmundial.org.br/presente-diario/03-08-2015"));
                        Uri.parse(url_text));
                startActivity(browserInternet);
            }
        });

        // Redirection button to audio page
        Button audio = (Button) findViewById(R.id.audio);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audioBrowserInternet = new Intent(Intent.ACTION_VIEW,
                        //Uri.parse("http://transmundial.org.br/podcast/presente_diario/18/presente03082015.mp3"));
                        Uri.parse(url_audio));
                startActivity(audioBrowserInternet);
            }
        });


        // download do áudio
        final String url_download_audio = "http://transmundial.org.br/podcast/presente_diario/" + versao
                + "/" + "presente" + dateFormatNoTraces + ".mp3";

        // criando botão
        Button buttonDownload = (Button) findViewById(R.id.download_audio);
        // Setando listener para o botão
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfFile = URLUtil.guessFileName(url_download_audio, null,
                        MimeTypeMap.getFileExtensionFromUrl(url_download_audio));
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_download_audio));
                request.setTitle("PresenteDiario");
                request.setDescription("Arquivo sendo baixado...");
                // use a linha abaixo se quiser limitar o download por wifi / tem opção de dados tbm
                //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);

                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
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
        // if (id == R.id.action_settings) {
        //   return true;
        //}
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            final String url_site = "http://www.transmundial.org.br/";
            String dialog_title = "Presente Diário - versão 0.1 "
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

        return super.

                onOptionsItemSelected(item);
    }
}
