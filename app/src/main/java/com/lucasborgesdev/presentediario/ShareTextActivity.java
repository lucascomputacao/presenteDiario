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
//        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
        final String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)

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

            // Intent compartilhar texto do arquivo
            String stringApp = " - Compartilhado Via PresenteDiárioApp";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            share.putExtra(Intent.EXTRA_TEXT, aBuffer + stringApp);
            startActivity(Intent.createChooser(share, "Compartilhar Texto com:"));

            // fechar myReader
            myReader.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }
}
