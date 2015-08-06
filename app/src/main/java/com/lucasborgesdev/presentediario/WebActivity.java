package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lucas on 06/08/15.
 */
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

        //webView.loadUrl("http://www.javacodegeeks.com");
        //webView.loadUrl("http://www.transmundial.org.br/presente-diario/06-08-2015");
        webView.loadUrl(url_text);

    }

}

