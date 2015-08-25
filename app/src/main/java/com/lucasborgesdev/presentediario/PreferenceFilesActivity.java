package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.io.File;

/**
 * Mantem Arquivos segundo preferência do Usuário
 */
public class PreferenceFilesActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    /**
     * Deleta o diretório
     */
    public void cleanDirectory() {
        File file = new File("/sdcard/PresenteDiario");
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if(f.delete()){
                    }

                }
            }
        }

    }

}
