package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ActionMenuView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ShareTextActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final String url_download_texto = "http://104.236.27.118/presente_diario/presente"
                + dateFormatTraces + ".txt";
        final String title_download_texto = "Presente_Diário_" + dateFormatTraces + "."
                + MimeTypeMap.getFileExtensionFromUrl(url_download_texto);

        String nameOfFileText = URLUtil.guessFileName(url_download_texto, null,
                MimeTypeMap.getFileExtensionFromUrl(url_download_texto));


        Toast.makeText(getBaseContext(), "Você escolheu compartilhar Texto.", Toast.LENGTH_LONG).show();

        try {
            // Verficando existência de texto
            File file_texto = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");
            // Se existir, lê o arquivo e compartilha
            if (file_texto.exists()) {
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

                // Intent compartilhar texto do arquivo
                String stringApp = " - Compartilhado Via PresenteDiárioApp";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                share.putExtra(Intent.EXTRA_TEXT, aBuffer + stringApp);
                startActivity(Intent.createChooser(share, "Compartilhar Texto com:"));

                // fechar myReader
                myReader.close();
            } else {
                // Mensagem para usuário
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

                // Finaliza View enquanto faz o download do arquivo
                finish();

            }

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    protected void onPause() {
        super.onPause();
        // Resolvendo tela em branco após compartilhar
        finish();
    }

}
