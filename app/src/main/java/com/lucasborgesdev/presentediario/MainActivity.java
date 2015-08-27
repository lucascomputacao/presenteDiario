package com.lucasborgesdev.presentediario;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    boolean dialogDownload = true;
    // preparing date for URLs for redirect
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdfNoTrace = new SimpleDateFormat("ddMMyyyy");
    Calendar cal = Calendar.getInstance();
    Date now = cal.getTime(); // set the current datetime in a Date-object
    final String dateFormatTraces = sdf.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
    String dateFormatNoTraces = sdfNoTrace.format(now); // contains dd-MM-yyyy (e.g. 15-03-2015 for March 15, 2015)
    // Calculando a versão
    int year = cal.get(Calendar.YEAR); // get the current year
    int norma = 1997;
    int versao = year - norma;

    // making URLs
    final String url_text_redirect = "http://www.transmundial.org.br/presente-diario/" + dateFormatTraces;
    final String url_audio_redirect = "http://transmundial.org.br/podcast/presente_diario/" + versao
            + "/" + "presente" + dateFormatNoTraces + ".mp3";


    // Bloqueia dialog de download de arquivos
    protected void setDialogDownloadFalse() {
        dialogDownload = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Colocando ícone na ActionBar
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher_presente_diario);
//        }


        // Redirection button to text page
        Button button = (Button) findViewById(R.id.texto);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getBaseContext(), "Você escolheu ler o Texto na internet.\nSerá aberta uma página no seu navegador.",
                        Toast.LENGTH_SHORT).show();
                Intent browserInternet = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url_text_redirect));
                startActivity(browserInternet);
            }
        });

        // Redirection button to audio page
        Button audio = (Button) findViewById(R.id.audio_internet);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getBaseContext(),
                        "Você escolheu ouvir o Áudio na internet.\nSerá aberta uma página no seu navegador.",
                        Toast.LENGTH_SHORT).show();
                Intent audioBrowserInternet = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url_audio_redirect));
                startActivity(audioBrowserInternet);
            }
        });


        // Show Text button
        final Context context = this;
        Button button_textViewActivity = (Button) findViewById(R.id.button_show_text);

        button_textViewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "O Texto será exibido dentro do aplicativo (Não abre navegador).",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, TextViewActivity.class);
                startActivity(intent);
            }
        });

        // Play Audio button
        final Context audio_context = this;
        Button button_audio = (Button) findViewById(R.id.audio);

        button_audio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Play Audio / Abir áudio
                File file_audio = new File("/sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3");
                Uri uri_audio = Uri.fromFile(file_audio);
                if (file_audio.exists()) {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(uri_audio, "audio/*");
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Áudio será baixado\nEspero o término do dowload e tente novamente",
                            Toast.LENGTH_SHORT).show();

                    // Download de Áudio
                    Intent intent_audio = new Intent(getApplicationContext(), DownloadAudioActivity.class);
                    startActivity(intent_audio);

                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final File file_audio = new File("/sdcard/PresenteDiario/presente" + dateFormatNoTraces + ".mp3");
        final File file_texto = new File("/sdcard/PresenteDiario/presente" + dateFormatTraces + ".txt");

        // Impede o empilhamento dos dialog de download após perder e ganhar foco
        if (dialogDownload) {
            // Verificando existência de arquivos
            if (!file_audio.exists() || !file_texto.exists()) {

                // Impede que dialog de download de arquivos seja mostrada mais de uma vez
                setDialogDownloadFalse();
                // criar dialog do menu sobre este app
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

                // Setando Título do dialog
                alertDialog.setTitle("Download de Arquivos");
                // Mensagem do dialog
                alertDialog.setMessage("Arquivos não encontrados!\nDeseja baixar os arquivos agora?");
                //
                alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dowload Data to App (Audio and Text)
                        Toast textToast = Toast.makeText(getBaseContext(), "Baixando Arquivos.", Toast.LENGTH_LONG);
                        textToast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        textToast.show();

                        // Verificar preferências do usuário
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        if (sharedPreferences.getBoolean("perform_save", false)) {
                            // Verificar escolhas
                            PreferenceFilesActivity pref = new PreferenceFilesActivity();
                            switch (sharedPreferences.getString("save_interval", "-1")) {
                                //salvar todos
                                case "0":
                                    // Nada a fazer
                                    Toast.makeText(getApplicationContext(), "Sem Limpeza", Toast.LENGTH_LONG).show();
                                    break;
                                // últimos sete
                                case "7":
                                    pref.checkFirstDayOfWeek();
                                    Toast.makeText(getApplicationContext(), "Limpeza Semanal", Toast.LENGTH_LONG).show();
                                    break;
                                // todos do mês corrente
                                case "30":
                                    pref.checkFirstDayOfMonth();
                                    Toast.makeText(getApplicationContext(), "Limpeza Mensal", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } else {
                            // limpar pasta
                            PreferenceFilesActivity preFile = new PreferenceFilesActivity();
                            preFile.cleanDirectory();
                        }

                        // Verificando existência do áudio
                        if (!file_audio.exists()) {
                            // Download de Áudio
                            Intent intent = new Intent(getApplicationContext(), DownloadAudioActivity.class);
                            startActivity(intent);
                        }
                        // Download do arquivo de Texto
                        // Verificando existência de texto
                        if (!file_texto.exists()) {
                            // Download texto
                            Intent intent = new Intent(getApplicationContext(), DownloadTextoActivity.class);
                            startActivity(intent);
                        } //final existe texto

                        Toast mytoast = Toast.makeText(getBaseContext(), "Escolha uma das opções ou clique no Menu Compartilhar.", Toast.LENGTH_LONG);
                        mytoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        mytoast.show();
                    } // final onclick
                }); // final positiveButton

                alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        Toast mytoast = Toast.makeText(getBaseContext(), "Escolha uma das opções ou clique no Menu Compartilhar.", Toast.LENGTH_LONG);
                        mytoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        mytoast.show();

                    }
                });
                // Mostrar Dialog
                alertDialog.show();
            }// final da verificação de existência de arquivos
        }


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
        if (id == R.id.action_about) {
            final String url_site = "http://www.transmundial.org.br/";
            String dialog_title = "Presente Diário - Versão 1.0 "
                    + "LucasBorgesDev & GabrieLima";

            String dialog_message = "Este aplicativo redireciona/baixa o contéudo do Presente Diário."
                    + "\n" + "Disponível em :" + "\n"
                    + "http://www.transmundial.org.br/";

            // criar dialog do menu sobre este app
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setando Título do dialog
            alertDialog.setTitle(dialog_title);
            // Mensagem do dialog
            alertDialog.setMessage(dialog_message);
            //
            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ação ao clicar no botão OK
                }
            });
            alertDialog.setPositiveButton("Ir ao site", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ação ir ao site
                    Intent browserInternet = new Intent(Intent.ACTION_VIEW, Uri.parse(url_site));
                    startActivity(browserInternet);
                }
            });
            alertDialog.show();

            return true;
        }

        // Share Text
        if (id == R.id.menu_item_share_text) {

            final Context context = this;
            Intent intent = new Intent(context, ShareTextActivity.class);
            startActivity(intent);

            return true;
        }

        // Share Audio
        if (id == R.id.menu_item_share_audio) {

            final Context context_audio = this;
            Intent intent_audio = new Intent(context_audio, ShareAudioActivity.class);
            startActivity(intent_audio);

            return true;
        }

        // Share Audio and Text
        if (id == R.id.menu_item_share_both) {

            final Context context_both = this;
            Intent intent_both = new Intent(context_both, ShareBothActivity.class);
            startActivity(intent_both);

            return true;
        }

        // Share at the same time
        if (id == R.id.menu_item_share_sametime) {
            final Context context_sametime = this;
            Intent intent_sametime = new Intent(context_sametime, ShareSameTimeActivity.class);
            startActivity(intent_sametime);
            return true;
        }

        // Configs
        if (id == R.id.settings) {

            final Context context_settings = this;
            Intent intent_settings = new Intent(context_settings, QuickPrefsActivity.class);
            startActivity(intent_settings);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}