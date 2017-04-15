package com.example.wellclbo.tripcuritiba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LogadoActivity extends Activity {

    private String email;
    private ListView lView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado);
        lView = (ListView) findViewById(R.id.listView);
        Intent intent = getIntent();

        Bundle params = intent.getExtras();

        if (params != null) {
            String mostraTexto = params.getString("teste");
            email = mostraTexto;


        }
    }

    public void conectar(View v) {
        new DownloadRotasEmail().execute();
    }

    public class DownloadRotasEmail extends AsyncTask<Void, Void, List<Rota>> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LogadoActivity.this, "Aviso", "Carregando suas rotas");
        }

        @Override
        protected List<Rota> doInBackground(Void... params) {
            List<Rota> rotas = new ArrayList<Rota>();
            try {
                URL url = new URL("http://192.168.1.115:8080/artigo/RecuperaCarros");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer sBuffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        sBuffer.append(line);
                    }

                    JSONObject obj = new JSONObject(sBuffer.toString());
                    JSONArray array = obj.getJSONArray("carros");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject objCarro = array.getJSONObject(i);
                        Rota rota = new Rota();
//                        carro.nome = objCarro.getString("nome");
//                        carro.ano = objCarro.getInt("ano");
//                        carro.marca = objCarro.getString("marca");
//                        carro.preco = (float) objCarro.getDouble("preco");
                        rotas.add(rota);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return rotas;
        }

        @Override
        protected void onPostExecute(List<Rota> rotas) {
            lView.setAdapter(new ArrayAdapter<Rota>(LogadoActivity.this, android.R.layout.simple_list_item_1,
                    rotas));
            dialog.dismiss();
        }
    }
}