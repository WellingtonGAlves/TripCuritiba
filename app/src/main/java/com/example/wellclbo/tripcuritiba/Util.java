package com.example.wellclbo.tripcuritiba;

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
import java.sql.Date;
import java.util.ArrayList;
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

}
