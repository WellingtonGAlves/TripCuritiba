package com.example.wellclbo.tripcuritiba;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Diego on 12/03/2017.
 */

public class Util {


    /**
     *Lê um arquivo da pasta raw (Resources) e converte o mesmo em String.
     *@param inputStream Stream do arquivo local no aplicativo
     *@return O arquivo convertido em String.
     */
    public static String rawToJson(InputStream inputStream) {
        InputStream localStream = inputStream;
        String jsonString = "";
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(localStream, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
            jsonString = writer.toString();
            writer.close();
            reader.close();
            localStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    /**
     *Lê um arquivo da web via HTTP e converte o mesmo em String.
     *@param inputStream Stream do arquivo local no aplicativo
     *@return O arquivo convertido em String.
     */
    public static String webToString(InputStream inputStream) {
        InputStream localStream = inputStream;
        String localString = "";
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(localStream, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
            localString = writer.toString();
            writer.close();
            reader.close();
            localStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localString;
    }

    public static Pessoa JSONtoPessoa(String jsonString){
        try{
            Pessoa pessoa = new Pessoa();

            JSONObject mainObj = new JSONObject((jsonString));
            pessoa.setId(Integer.parseInt(mainObj.getString("Id")));
            pessoa.setNome(mainObj.getString("Nome"));
            pessoa.setEmail(mainObj.getString("Email"));
            pessoa.setAceito(Boolean.parseBoolean(mainObj.getString("Aceito")));
            pessoa.setDataCadastro(new Date(Date.parse(mainObj.getString("DataCadastro"))));
            return pessoa;
        }catch(Exception e){
            Log.e("ERROR","Error",e);
            return null;
        }
    }
    public static Pessoa JsonParaObjetoPessoa(String json) throws JSONException {
        Pessoa pessoa = new Pessoa();
        JSONObject jsonObjPessoa = new JSONObject(json);
        pessoa.setAceito(jsonObjPessoa.getBoolean("Aceito"));
        String dateStr = jsonObjPessoa.getString("DataCadastro").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dataCadastro = new Date();
        try{
            dataCadastro = sdf.parse(dateStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        pessoa.setDataCadastro(dataCadastro);
        pessoa.setEmail(jsonObjPessoa.getString("Email"));
        pessoa.setId(jsonObjPessoa.getInt("Id"));
        pessoa.setNome(jsonObjPessoa.getString("Nome"));
        //pegar rotas
        List<Rota> lRotas = new ArrayList<Rota>();
        Rota rota ;

        JSONArray jArrRota = jsonObjPessoa.getJSONArray("Rotas");
        for(int i = 0; i <jArrRota.length (); i ++) {
            rota = new Rota();
            JSONObject objRotas = jArrRota.getJSONObject (i);
            rota.setNome(objRotas.getString("Nome"));
            rota.setId(objRotas.getInt("Id"));

            String dateStrRota = objRotas.getString("Data").toString();
            SimpleDateFormat sdfRota = new SimpleDateFormat("dd-MM-yyyy");
            Date dataRota = new Date();
            try{
                dataRota = sdfRota.parse(dateStrRota);
            }catch (Exception e){
                e.printStackTrace();
            }

            rota.setData(dataRota);
            rota.setPessoaId(objRotas.getInt("PessoaId"));
            rota.setStatus(objRotas.getBoolean("Status"));

            //pega os pontos turisticos
            List<PontoTuristico> lPontoTuristicos = new ArrayList<PontoTuristico>();
            PontoTuristico pontoTuristico = new PontoTuristico();

            JSONArray jArrPonto = objRotas.getJSONArray("PontoTuristicos");
            for(int x = 0; x <jArrPonto.length (); x ++){
                pontoTuristico = new PontoTuristico();
                JSONObject objPonto = jArrPonto.getJSONObject (x);
                pontoTuristico.setId(objPonto.getInt("Id"));
                pontoTuristico.setNome_ponto(objPonto.getString("Nome_ponto"));
                lPontoTuristicos.add(pontoTuristico);
            }

            rota.setlPontoTuristicos(lPontoTuristicos);
            lRotas.add(rota);

        }
        pessoa.setRotas(lRotas);
        return pessoa;
    }


}
