package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
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
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)


        TextView textView = (TextView) findViewById(R.id.textView);
        File file_texto = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");
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
            // Download texto
            Intent intent = new Intent(getApplicationContext(), DownloadTextoActivity.class);
            startActivity(intent);

            finish();
        }



    }
}
