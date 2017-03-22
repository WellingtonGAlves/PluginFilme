package com.example.wellclbo.apiomd;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private TextView title;
    private Button pesquisar;
    private EditText filme;
    private TextView genero;
    private TextView ano;
    private String filmePadrao = "http://www.omdbapi.com/?t=kill+bill";
    private String teste ;
    private RatingBar nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (TextView) findViewById(R.id.txtViewTitulo);
        ano = (TextView) findViewById(R.id.txtViewAno);
        genero = (TextView) findViewById(R.id.txtViewGenero);
        filme = (EditText) findViewById(R.id.editFilme);
        pesquisar = (Button) findViewById(R.id.btnProcurar);
        nota = (RatingBar) findViewById(R.id.ratingBar);

        Intent intent = getIntent();

        Bundle params = intent.getExtras();

        if(params!=null)
        {
            String mostraTexto = params.getString("teste");
            filme.setText(mostraTexto);
            //setContentView(filme);

        }


        teste = (String) filme.getText().toString();


        new DownloadFromOpenWeather().execute();

    }
    public void onClick1(View v)
    {
        Intent intent = new Intent(v.getContext(), Main2Activity.class);
        Bundle params = new Bundle();

        String filmedigitado = (String) filme.getText().toString();
        params.putString("teste", filmedigitado);
        intent.putExtras(params);
        startActivity(intent);
    }


    public void conectar(View v){
        new MainActivity.DownloadFromOpenWeather().execute();
    }

    private class DownloadFromOpenWeather extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url;
                if(teste.equals("")){
                    url = new URL(filmePadrao);
                }else{
                    url = new URL("http://www.omdbapi.com/?t="+teste.toString());
                }


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                String result = Util.webToString(urlConnection.getInputStream());

                return result;
            } catch (Exception e) {
                Log.e("Error", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String dada;
            float avaliacao;
            Filme filme = Util.JSONtoFilme(s);
            if (filme != null) {
                dada = filme.getTitle().toString();
                title.setText(dada);
                avaliacao = Float.parseFloat(filme.getAvaliacao());
                nota.setRating(avaliacao);
                genero.setText(filme.getGenero().toString());
                ano.setText(filme.getAno().toString());

            }       }
    }
}