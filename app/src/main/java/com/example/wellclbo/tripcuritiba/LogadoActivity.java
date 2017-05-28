package com.example.wellclbo.tripcuritiba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.wellclbo.tripcuritiba.PontoTuristicoAdapter.arrayString;

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
        String caminhoImagem;
        int i = -1;
        int cont = -1;
        for (Rota rota:pessoa.getRotas()) {
            i= i+1;
            cont = cont + 1;
            caminhoImagem = rota.getlRotaComPontos().get(i).getPontoTuristico().getImagem_historia();
            URL url = null;
            try {
                url = new URL("https://tripcuritiba.azurewebsites.net/Uploads/thumbgaleria/"+caminhoImagem.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            arrayString.add(Integer.parseInt(String.valueOf(cont)),caminhoImagem);

            new PontoTuristicoAdapter.DownloadImageAsync().execute(url);
        }

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