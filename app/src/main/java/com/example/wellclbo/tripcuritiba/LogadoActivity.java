package com.example.wellclbo.tripcuritiba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LogadoActivity extends Activity implements AdapterView.OnItemClickListener {

    private Pessoa pessoa;
    private ListView listRotas;
    private String json;

    private ArrayList<Rota> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado);
        //Pega a referencia do ListView
        listRotas = (ListView) findViewById(R.id.listClubes);
        //Define o Listener quando alguem clicar no item.
        //listRotas.setOnItemClickListener((AdapterView.OnItemClickListener) this);



        Intent intent = getIntent();

        Bundle params = intent.getExtras();

        if (params != null) {
            String paramsString = params.getString("json");
            json = paramsString;
        }
        carregaPessoa(json);
    }

    private void carregaPessoa(String json) {
        try {
            pessoa = Util.JsonParaObjetoPessoa(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(pessoa==null){
            //nao carrega rotas
        }else{
            ArrayList<Rota> rotaArrayList =(ArrayList<Rota>) pessoa.getRotas();



            ArrayAdapter<Rota> clubeAdapter = new RotaAdapter(LogadoActivity.this,R.layout.rota_item,rotaArrayList);
            ListView listaClube = (ListView) findViewById(R.id.listClubes);
            listaClube.setAdapter(clubeAdapter);
        }
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        //Pega o item que foi selecionado.
        //Rota item = adapterListView.(arg2);
        //Demostração
        //Toast.makeText(this, "Você Clicou em: " + item.getNome(), Toast.LENGTH_LONG).show();
    }

    public void mostrarRota(View v){
        Intent intent = new Intent(LogadoActivity.this, ApresentacaoDaRota.class);
        Bundle params = new Bundle();


        TextView rotaSelecionada = (TextView)findViewById(R.id.textNome);

        Pessoa pessoa = new Pessoa();
        try {
            pessoa = Util.JsonParaObjetoPessoa(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.putString("rotaSelecionada", rotaSelecionada.getText().toString());
        params.putSerializable("pessoa",pessoa);
        intent.putExtras(params);
        startActivity(intent);
    }

}