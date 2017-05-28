package com.example.wellclbo.tripcuritiba;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Diego on 12/03/2017.
 */

public class Util {


    public   static List<Bitmap> listaBitmapImages = new ArrayList<Bitmap>();

    public static void setListaBitmapImages() {

    }

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

        if(json == null){
            return null;

        }else {

            Pessoa pessoa = new Pessoa();
            JSONObject jsonObjPessoa = new JSONObject(json);
            pessoa.setAceito(jsonObjPessoa.getBoolean("Aceito"));
            String dateStr = jsonObjPessoa.getString("DataCadastro").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dataCadastro = new Date();
            try {
                dataCadastro = sdf.parse(dateStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pessoa.setDataCadastro(dataCadastro);
            pessoa.setEmail(jsonObjPessoa.getString("Email"));
            pessoa.setId(jsonObjPessoa.getInt("Id"));
            pessoa.setNome(jsonObjPessoa.getString("Nome"));
            //pegar rotas
            List<Rota> lRotas = new ArrayList<Rota>();
            Rota rota;

            JSONArray jArrRota = jsonObjPessoa.getJSONArray("Rotas");
            for (int i = 0; i < jArrRota.length(); i++) {
                rota = new Rota();
                JSONObject objRotas = jArrRota.getJSONObject(i);
                rota.setNome(objRotas.getString("Nome"));
                rota.setId(objRotas.getInt("Id"));

                String dateStrRota = objRotas.getString("Data").toString();
                SimpleDateFormat sdfRota = new SimpleDateFormat("dd-MM-yyyy");
                Date dataRota = new Date();
                try {
                    dataRota = sdfRota.parse(dateStrRota);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                rota.setData(dataRota);
                rota.setPessoaId(objRotas.getInt("PessoaId"));
                rota.setStatus(objRotas.getBoolean("Status"));

                //pega as Rotas Com Pontos
                List<RotaComPonto> lRotaComPontos = new ArrayList<RotaComPonto>();
                RotaComPonto rotaComPonto = new RotaComPonto();

                JSONArray jArrRotaComPonto = objRotas.getJSONArray("RotaComPontos");
                for (int x = 0; x < jArrRotaComPonto.length(); x++) {
                    rotaComPonto = new RotaComPonto();
                    JSONObject objRotaComPonto = jArrRotaComPonto.getJSONObject(x);
                    rotaComPonto.setId(objRotaComPonto.getInt("Id"));
                    rotaComPonto.setPontoTuristicoId(objRotaComPonto.getInt("PontoTuristicoId"));
                    rotaComPonto.setRotaId(objRotaComPonto.getInt("RotaId"));
                    int a = objRotaComPonto.getString("PontoTuristico").length();
                    JSONObject pontoObjeto = new JSONObject(objRotaComPonto.getString("PontoTuristico"));

                    JSONObject pontoJson = new JSONObject();
//                for(int y = 0; y <jArrPontoTuristico.length (); y ++) {
//
//                    pontoJson = jArrPontoTuristico.getJSONObject(y);
//                }
                    PontoTuristico pontoTuristico = new PontoTuristico();
                    pontoTuristico = Util.jsonTOPontoTuristico(pontoObjeto);
                    rotaComPonto.setPontoTuristico(pontoTuristico);
                    lRotaComPontos.add(rotaComPonto);
                }
                rota.setlRotaComPontos(lRotaComPontos);
                lRotas.add(rota);

            }
            pessoa.setRotas(lRotas);
            return pessoa;
        }
    }

    private static PontoTuristico jsonTOPontoTuristico(JSONObject pontoObjeto) {
        PontoTuristico pontoTuristico = new PontoTuristico();

//        try {
//            pontoObjeto = pontoObjeto.getJSONObject("PontoTuristico");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        try {
            if(pontoObjeto.getString("Id")!=null){
                pontoTuristico.setId(pontoObjeto.getInt("Id"));
            }

            if(pontoObjeto.getString("Imagem_ponto")!=null){
                pontoTuristico.setImagem_ponto(pontoObjeto.getString("Imagem_ponto"));
            }
            if(pontoObjeto.getString("Nome_ponto")!=null){
                pontoTuristico.setNome_ponto(pontoObjeto.getString("Nome_ponto"));
            }
            if(pontoObjeto.getString("Cont1")!=null){
                pontoTuristico.setCont1(pontoObjeto.getString("Cont1"));
            }
            if(pontoObjeto.getString("Cont2")!=null){
                pontoTuristico.setCont2(pontoObjeto.getString("Cont2"));
            }
            if(pontoObjeto.getString("Cont3")!=null){
                pontoTuristico.setCont3(pontoObjeto.getString("Cont3"));
            }
            if(pontoObjeto.getString("Cont4")!=null){
                pontoTuristico.setCont4(pontoObjeto.getString("Cont4"));
            }
            if(pontoObjeto.getString("Dica_ponto")!=null){
                pontoTuristico.setDica_ponto(pontoObjeto.getString("Dica_ponto"));
            }
            if(pontoObjeto.getString("Horarios_ponto")!=null){
                pontoTuristico.setHorarios_ponto(pontoObjeto.getString("Horarios_ponto"));
            }
            if(pontoObjeto.getString("Imagem_carrossel")!=null){
                pontoTuristico.setImagem_carrossel(pontoObjeto.getString("Imagem_carrossel"));
            }
            if(pontoObjeto.getString("Imagem_historia")!=null){
                pontoTuristico.setImagem_historia(pontoObjeto.getString("Imagem_historia"));
            }
            if(pontoObjeto.getString("Imagem_ponto")!=null){
                pontoTuristico.setImagem_ponto(pontoObjeto.getString("Imagem_ponto"));
            }
            if(pontoObjeto.getString("Informacoes_ponto")!=null){
                pontoTuristico.setInformacoes_ponto(pontoObjeto.getString("Informacoes_ponto"));
            }
            if(pontoObjeto.getString("Ordem")!=null){
                pontoTuristico.setOrdem(pontoObjeto.getInt("Ordem"));
            }
            if(pontoObjeto.getString("Selecionado")!=null){
                pontoTuristico.setSelecionado(pontoObjeto.getBoolean("Selecionado"));
            }
            if(pontoObjeto.getString("Sub1")!=null){
                pontoTuristico.setSub1(pontoObjeto.getString("Sub1"));
            }
            if(pontoObjeto.getString("Sub2")!=null){
                pontoTuristico.setSub2(pontoObjeto.getString("Sub2"));
            }
            if(pontoObjeto.getString("Sub3")!=null){
                pontoTuristico.setSub3(pontoObjeto.getString("Sub3"));
            }
            if(pontoObjeto.getString("Sub4")!=null){
                pontoTuristico.setSub4(pontoObjeto.getString("Sub4"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  pontoTuristico;
    }


    public static String ConvertPessoaToJSON(Pessoa pessoa) {
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("email",pessoa.getEmail());
            return mainObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap loadImage (URL url) throws IOException {


        InputStream inputStream;
        Bitmap myBitMap;

        inputStream = url.openStream();
        myBitMap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        return myBitMap;

    }
    //Carrega imagens para o app
    public static class DownloadImageAsync extends AsyncTask<URL,Integer,Bitmap> {
        ProgressDialog progress;
        @Override
        protected Bitmap doInBackground(URL... params) {
            Bitmap myBitmap = null;
            try{
                myBitmap = Util.loadImage(params[0]);
            }catch(IOException e){

            }
            return myBitmap;
        }
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            if(bitmap != null){
                listaBitmapImages.add(bitmap);
            }
        }
    }

}
