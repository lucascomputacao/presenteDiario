package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mostrar texto textview
        final Context context_text = this;
        Button button_textViewActivity = (Button) findViewById(R.id.btnReadSDFile);

        button_textViewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context_text, TextViewActivity.class);
                startActivity(intent);
            }
        });

//        final EditText txtData = (EditText) findViewById(R.id.txtData);
//        Button btnReadSDFile = (Button) findViewById(R.id.btnReadSDFile);
//        btnReadSDFile.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // write on SD card file data in the text box
//                try {
//                    File myFile = new File("/sdcard/PresenteDiario/presente"+ dateFormatTraces +".txt");
//                    FileInputStream fIn = new FileInputStream(myFile);
//                    BufferedReader myReader = new BufferedReader(
//                            new InputStreamReader(fIn));
//                    String aDataRow = "";
//                    String aBuffer = "";
//                    while ((aDataRow = myReader.readLine()) != null) {
//                        aBuffer += aDataRow + "\n";
//                    }
//                    txtData.setText(aBuffer);
//                    myReader.close();
//                    Toast.makeText(getBaseContext(),
//                            "Done reading SD 'mysdfile.txt'",
//                            Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Toast.makeText(getBaseContext(), e.getMessage(),
//                            Toast.LENGTH_SHORT).show();
//                }
//            }// onClick
//        }); // btnReadSDFile

        // Redirection button to text page
        Button button = (Button) findViewById(R.id.texto);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserInternet = new Intent(Intent.ACTION_VIEW,
                        // Uri.parse("http://www.transmundial.org.br/presente-diario/03-08-2015"));
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
                        //Uri.parse("http://transmundial.org.br/podcast/presente_diario/18/presente03082015.mp3"));
                        Uri.parse(url_audio_redirect));
                startActivity(audioBrowserInternet);
            }
        });

        // Download do áudio
        final String title_download_audio = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_audio_redirect);
        Button buttonDownload = (Button) findViewById(R.id.download_audio);

        // Setando listener para o botão
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
                request.setDestinationInExternalPublicDir("/PresenteDiario", nameOfFile);
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                // Dialog Abrir diretório
                final AlertDialog.Builder dialog_download = new AlertDialog.Builder(MainActivity.this);
                dialog_download.setTitle("Download");
                dialog_download.setMessage("Arquivo de áudio sendo baixado"
                        + "\n"
                        + "Mostrar na pasta?");
                dialog_download.setCancelable(true);
                dialog_download.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ação positiva
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        Uri uri = Uri.parse(Environment.DIRECTORY_DOWNLOADS);
                        intent.setDataAndType(uri, "text/csv");
                        startActivity(Intent.createChooser(intent, "Open folder"));
                    }
                });
                dialog_download.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ação negativa
                    }
                });
                dialog_download.show();
            }
        });

        // WebActivity show text
        final Context context = this;
        Button button_webactivity = (Button) findViewById(R.id.buttonUrl);

        button_webactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                startActivity(intent);
            }
        });

        // Download de arquivo de texto
        final String url_download_texto = "http://104.236.27.118/presente_diario/presente"
                + dateFormatTraces + ".txt";
        final String title_download_texto = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_texto);
        Button button_downloadTexto = (Button) findViewById(R.id.download_texto);
        button_downloadTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Download do arquivo de Texto
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
//                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
                request.setDestinationInExternalPublicDir("/PresenteDiario", nameOfFile);

                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                // Dialog Abrir diretório
                final AlertDialog.Builder dialog_download = new AlertDialog.Builder(MainActivity.this);
                dialog_download.setTitle("Download");
                dialog_download.setMessage("Arquivo de texto sendo baixado"
                        + "\n"
                        + "Mostrar na pasta?");
                dialog_download.setCancelable(true);
                dialog_download.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ação positiva
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        Uri uri = Uri.parse(Environment.DIRECTORY_DOWNLOADS);
                        intent.setDataAndType(uri, "text/csv");
                        startActivity(Intent.createChooser(intent, "Open folder"));
                    }
                });
                dialog_download.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ação negativa
                    }
                });
                dialog_download.show();
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

        // Share text
        if (id == R.id.menu_item_share_text) {
//
//            Intent shareIntent = new Intent();
//            shareIntent.setAction(Intent.ACTION_SEND);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
//            shareIntent.setType("image/jpeg");
//            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

            // Intent funcionando
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

            share.putExtra(Intent.EXTRA_SUBJECT,
                    "Teste de compartilhamento Presente Diario");
            share.putExtra(Intent.EXTRA_TEXT, "Texto teste de compartilhamento do Presente Diário");

            startActivity(Intent.createChooser(share, "Compartilhar"));
            startActivity(Intent.createChooser(share, getResources().getText(R.string.send_text_to)));

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}