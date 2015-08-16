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
import java.util.Calendar;
import java.util.Date;

public class ShareBothActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final String url_download_audio = "http://104.236.27.118/presente_diario/presente"
                + dateFormatNoTraces + ".mp3";
        final String title_download_audio = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_audio);
        final String nameOfFile = URLUtil.guessFileName(url_download_audio, null,
                MimeTypeMap.getFileExtensionFromUrl(url_download_audio));
        final String url_download_texto = "http://104.236.27.118/presente_diario/presente"
                + dateFormatTraces + ".txt";
        final String title_download_texto = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_texto);
        final String nameOfFileText = URLUtil.guessFileName(url_download_texto, null,
                MimeTypeMap.getFileExtensionFromUrl(url_download_texto));


        // Verificando existência do áudio
        File file_audio = new File("/sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3");
        Uri uri = Uri.fromFile(file_audio);
        File file_texto = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");

        // Se audio existe
        if (file_audio.exists()) {
            // Se áudio e texto existem
            if (file_texto.exists()) {
                try {

                    Toast.makeText(getBaseContext(), "Você escolheu compartilhar Áudio e Texto.", Toast.LENGTH_LONG).show();
                    int i = 0;
                    while (i < 4) {

                        Toast.makeText(getBaseContext(),
                                "Compartilha o Áudio e depois o Texto." +
                                        "\nVocê pode compartilhar com aplicativos diferentes, se preferir.",
                                Toast.LENGTH_LONG).show();
                        i++;
                    }
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

                    // Compartilhar Texto
                    String stringApp = " - Compartilhado Via PresenteDiárioApp";
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                    share.putExtra(Intent.EXTRA_TEXT, aBuffer + stringApp);
                    startActivity(Intent.createChooser(share, "Compartilhar Texto com:"));


                    // fechar myReader
                    myReader.close();

                    // Compartilhar Audio
                    Intent shareAudio = new Intent(Intent.ACTION_SEND);
                    shareAudio.setType("audio/mp3");
                    shareAudio.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(shareAudio, "Compartilhar Áudio com:"));

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            } else {
                // Baixar apenas o texto
                // Mensagem para usuário
                Toast.makeText(getBaseContext(),
                        "Arquivo de texto será baixado\nClique em compatilhar após o término do download!'",
                        Toast.LENGTH_LONG).show();
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
        } else {
            // Baixar o audio
            // Mensagem para usuário
            Toast.makeText(getBaseContext(),
                    "Arquivo de áudio será baixado\nClique em compatilhar após o término do download!",
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

            // Se não houver arquivo de texto faz o download do texto
            if (!file_texto.exists()) {
                Toast.makeText(getBaseContext(),
                        "Arquivo de texto será baixado\nClique em compatilhar após o término do download!",
                        Toast.LENGTH_LONG).show();
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
        }


        // Finaliza View enquanto faz o download do(s) arquivo(s)
        finish();
    }

    protected void onPause() {
        super.onPause();
        // Resolvendo tela em branco após compartilhar
        finish();
    }
}
