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

        String settings;

        settings = sharedPrefs.getBoolean("perform_save", false) + "\n" +
                sharedPrefs.getString("save_interval", "-1") + "\n" +
                sharedPrefs.getInt("count_save",0);

        TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
        settingsTextView.setText(settings);

    }

}



