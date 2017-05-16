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

public class LogadoActivity extends Activity {


    private ListView listRotas;
    private String json;
    private Pessoa pessoa;
    private ArrayList<Rota> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado);
        //Pega a referencia do ListView
        listRotas = (ListView) findViewById(R.id.listRotas);
        //Define o Listener quando alguem clicar no item.
        //listRotas.setOnItemClickListener((AdapterView.OnItemClickListener) this);



        Intent intent = getIntent();

        Bundle params = intent.getExtras();
        Pessoa paramsPessoa = new Pessoa();
        if (params != null) {
            paramsPessoa = (Pessoa) params.getSerializable("pessoa");
            pessoa = paramsPessoa;
        }
        carregaPessoa(paramsPessoa);
    }

    private void carregaPessoa(Pessoa paramsPessoa) {

        if(paramsPessoa==null){
            //nao carrega rotas
        }else{
            final ArrayList<Rota> rotaArrayList =(ArrayList<Rota>) paramsPessoa.getRotas();
            ArrayAdapter<Rota> rotaAdapter = new RotaAdapter(LogadoActivity.this,R.layout.rota_item,rotaArrayList);
            ListView listaRota = (ListView) findViewById(R.id.listRotas);
            listaRota.setAdapter(rotaAdapter);
            listaRota.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(LogadoActivity.this, ApresentacaoDaRota.class);
                    Bundle params = new Bundle();
                    //Pega o item que foi selecionado.
                    Rota rota = rotaArrayList.get(position);
                    //Demostração
                    //Toast.makeText(LogadoActivity.this, "Você Clicou em: " + rota.getNome(), Toast.LENGTH_LONG).show();
                    params.putSerializable("rota",rota);
                    intent.putExtras(params);
                    startActivity(intent);
                }
            });
        }
    }


//    public void mostrarRota(View v){
//        Intent intent = new Intent(LogadoActivity.this, ApresentacaoDaRota.class);
//        Bundle params = new Bundle();
//
//
//        TextView rotaSelecionada = (TextView)findViewById(R.id.textNome);
//
//
//        params.putString("rotaSelecionada", rotaSelecionada.getText().toString());
//        params.putSerializable("pessoa",pessoa);
//        intent.putExtras(params);
//        startActivity(intent);
//    }

}