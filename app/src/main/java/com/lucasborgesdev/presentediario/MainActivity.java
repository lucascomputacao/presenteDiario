package com.lucasborgesdev.presentediario;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // preparing date for URLs for download
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
                Intent browerInternet = new Intent(Intent.ACTION_VIEW,
                        // Uri.parse("http://www.transmundial.org.br/presente-diario/03-08-2015"));
                        Uri.parse(url_text));
                startActivity(browerInternet);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
