package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)

        Toast.makeText(getBaseContext(), "Você escolheu compartilhar Texto.", Toast.LENGTH_SHORT).show();

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
                String stringApp = " - Compartilhado Via Download_PresenteDiário";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, aBuffer + stringApp);
                startActivity(Intent.createChooser(share, "Compartilhar Texto com:"));

                // fechar buffer de leitura
                myReader.close();
            } else {
                // Mensagem para usuário
                Toast.makeText(getBaseContext(),"Arquivo de texto será baixado." +
                                "\nClique no botão compatilhar após o término do download!",
                        Toast.LENGTH_LONG).show();
                // Download texto
                Intent intent = new Intent(getApplicationContext(), DownloadTextoActivity.class);
                startActivity(intent);
            }

            // Finaliza View enquanto faz o download do arquivo
            finish();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }


}
