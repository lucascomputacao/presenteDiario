package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.WebView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WebActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.webcontent);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // set the current datetime in a Date-object
        String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)

        final String url_text = "http://www.transmundial.org.br/presente-diario/" + dateFormatTraces;

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url_text);

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation change
        if (newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            super.onConfigurationChanged(newConfig);
        }
    }

}

