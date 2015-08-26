package com.lucasborgesdev.presentediario;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Mantem Arquivos segundo preferência do Usuário
 */
public class PreferenceFilesActivity extends Activity {


    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
    Calendar cal = Calendar.getInstance();
    Date now = cal.getTime(); // set the current datetime in a Date-object
    final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
    String dateFormatNoTraces = sdfNoTrace.format(now);


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    /**
     * Deleta todos os arquivos do diretório
     */
    public void cleanDirectory() {
        File file = new File("/sdcard/PresenteDiario");
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

    }

    /**
     * Checa se e o primeiro dia da semana para limpar a pasta
     */
    public void checkFirstDayOfWeek() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        // Domingo - limpa a pasta todos os domingos
        if (dayOfWeek == 1){
            cleanDirectory();
        }
    }

    /**
     * Checa se é o primeiro dia do mês para limpar a pasta
     */
    public void checkFirstDayOfMonth(){
        Calendar today = Calendar.getInstance();
        int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        // limpa a pasta no primeiro dia do mês
        if (dayOfMonth == 1){
            cleanDirectory();
        }
    }


    public void incrementCount() {

        // Carrega preferencias do usuario
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // verifica se o contador foi inicializado
        if (counterZero()) {
            startCounter();
        } else {
            // incrementa contador de dias
            int contador = sharedPreferences.getInt("count_save", 0);
            contador++;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("count_save", contador);
            editor.apply();
        }
    }

    /**
     * Inicia Contador
     */
    public void startCounter() {

        // Carrega preferencias do usuario
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // inicia o contador
        editor.putInt("count_save", 1);
        // salva datas dos primeiros arquivos - usado para deletar mais tarde
        editor.putString("date_first_audio", dateFormatNoTraces);
        editor.putString("date_first_texto", dateFormatTraces);
        editor.apply();
    }

    public boolean counterZero() {

        // Carrega preferencias do usuario
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getInt("count_save", 0) == 0) {
            return true;
        } else {
            return false;
        }
    }


    // checa arquivos da semana
    public void checkSeven() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);   //adicionamos 1 dia a data atual

        String dataNovoArquivo = c.toString();

        // valor salvo
        int prefCount = sharedPreferences.getInt("count_save", 0);

        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, +1);

        if (sharedPreferences.getInt("count_save", 0) >= 8) {

            // deletar os primeiros e setar novos primeiros

            File file = new File("/sdcard/PresenteDiario" + sharedPreferences.getString("date_first_audio", ""));
        }

    }


}
