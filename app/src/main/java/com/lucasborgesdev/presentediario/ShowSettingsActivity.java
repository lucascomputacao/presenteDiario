package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class ShowSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_settings_layout);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

//        StringBuilder builder = new StringBuilder();
        String builder = new String();

        builder = sharedPrefs.getBoolean("perform_save", false) + sharedPrefs.getString("save_interval", "-1");
//        builder.append("\n" + sharedPrefs.getBoolean("perform_save", false));
//        builder.append("\n" + sharedPrefs.getString("save_interval", "-1"));
//        builder.append("\n" + sharedPrefs.getString("welcome_message", "NULL"));

        TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
        settingsTextView.setText(builder.toString());

    }

}



