package com.example.wellclbo.tripcuritiba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

import static java.lang.System.in;

public class ApresentacaoDaRota extends Activity {

    private Pessoa pessoa;
    private ListView listPontos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao_da_rota);
        listPontos = (ListView) findViewById(R.id.listPontos);


        pessoa = new Pessoa();

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        ArrayList<RotaComPonto> rotaComPontoArrayList = new ArrayList<RotaComPonto>();
        if (params != null) {

            String rotaSelecionada = params.getString("rotaSelecionada");
            pessoa = (Pessoa) params.getSerializable("pessoa");
            for(Rota element : pessoa.getRotas()) {
                if(element.getNome().equals(rotaSelecionada)){

                    rotaComPontoArrayList =(ArrayList<RotaComPonto>) element.getlRotaComPontos();

                }

            }
            ArrayList<PontoTuristico> pontoTuristicoArrayList = new ArrayList<PontoTuristico>();
            for (RotaComPonto rotaComPonto: rotaComPontoArrayList) {
                pontoTuristicoArrayList.add(rotaComPonto.getPontoTuristico());
            }


            ListView listaPonto = (ListView) findViewById(R.id.listPontos);
            ArrayAdapter<PontoTuristico> pontoTuristicoAdapter = new PontoTuristicoAdapter(ApresentacaoDaRota.this,R.layout.ponto_item,pontoTuristicoArrayList);
            listaPonto.setAdapter(pontoTuristicoAdapter);
        }


    }





}
