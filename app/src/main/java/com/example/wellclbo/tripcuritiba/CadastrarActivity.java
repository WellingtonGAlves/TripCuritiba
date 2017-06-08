package com.example.wellclbo.tripcuritiba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CadastrarActivity extends AppCompatActivity {

    private Pessoa pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);



        Intent intent = getIntent();

        Bundle params = intent.getExtras();
        Pessoa paramsPessoa = new Pessoa();
        if (params != null) {
            paramsPessoa = (Pessoa) params.getSerializable("pessoa");
            pessoa = paramsPessoa;
        }
    }

    public void salvarUsuario(View view) {
        new CadastrarActivity.CreateuserFromApiLogin().execute(pessoa);
    }

    private class CreateuserFromApiLogin extends AsyncTask<Pessoa, Void, String> {

        private ProgressDialog dialog;
        boolean isConnected = false;
        int serverResponseCode;
        String serverResponseMessage;

        @Override
        protected void onPreExecute() {

            //dialog = ProgressDialog.show(LoginActivity.this, "Aviso", "Aguarde, estamos cadastrando");
            ConnectivityManager cm =
                    (ConnectivityManager)CadastrarActivity.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if(!isConnected) {
                dialog.dismiss();

                Toast.makeText(CadastrarActivity.this, "Verifique a conexão com a internet...", Toast.LENGTH_LONG).show();
            }
        }

        protected String doInBackground(Pessoa... params) {
            if (LoginActivity.existeNaBasedeDados == false) {

                params[0].setNome("Me deixa");

                HttpURLConnection urlConnection = null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date dataCadastro = new Date();
//                String dataStr = "";
//                try {
//                    dataCadastro = sdf.parse(dataStr);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                params[0].setDataCadastro(dataCadastro);
                Gson gson = new Gson();
                String json = gson.toJson(params[0]);


                try {
                    URL url = new URL("http://tripcuritiba.azurewebsites.net/api/login");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-type", "application/json; charset=UTF-8");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);


                    DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                    outputStream.writeBytes(String.valueOf(json.toString().getBytes("UTF-8")));
                    //outputStream.close();
                    String result = Util.ConvertPessoaToJSON(params[0]);

                    serverResponseCode = urlConnection.getResponseCode();
                    serverResponseMessage = Util.webToString(urlConnection.getInputStream());

                    outputStream.flush();
                    outputStream.close();

                    result = serverResponseMessage;

                    return result;

                } catch (Exception e) {
                    Log.e("Error", "Error ", e);
                    InputStream erro = urlConnection.getErrorStream();
                    String da = String.valueOf(erro);

                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            //dialog.dismiss();
            super.onPostExecute(result);
            if(result!=null) {
                Toast.makeText(CadastrarActivity.this, "Cadastrado realizado com Sucesso", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(CadastrarActivity.this, "Não foi possivel cadastrar", Toast.LENGTH_LONG).show();

            }
        }
    }
}
