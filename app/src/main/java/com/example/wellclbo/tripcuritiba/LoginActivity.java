package com.example.wellclbo.tripcuritiba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {

    private EditText editEmail;
    private Button btnLogin;
    private String buscaEmail;
    private Boolean pgLogado = false;
    private Pessoa pessoa;
    private String json;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail = (EditText)findViewById(R.id.ediEmail);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        buscaEmail = editEmail.getText().toString();




    }
    public void onClick(View v)
    {
        pessoa = new Pessoa();
        buscaEmail = editEmail.getText().toString();
        pessoa.setEmail(buscaEmail);
        //new LoginActivity.DownloadFromApiLogin().execute();
        validaAcesso(v);
    }

    private void validaAcesso(View v) {
        new LoginActivity.DownloadFromApiLogin().execute(pessoa);

    }

    private class DownloadFromApiLogin extends AsyncTask<Pessoa, Void, String> {

        private ProgressDialog dialog;
        boolean isConnected = false;
        int serverResponseCode;
        String serverResponseMessage;

        @Override
         protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginActivity.this, "Aviso", "Aguarde, buscando dados");
            ConnectivityManager cm =
                    (ConnectivityManager)LoginActivity.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if(!isConnected) {
                Toast.makeText(LoginActivity.this, "Verifique a conexão com a internet...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
//        protected String doInBackground(Void... params) {
//            HttpURLConnection urlConnection = null;
//            try {
//                URL url;
//
//                    url = new URL("http://tripcuritiba.azurewebsites.net/api/login?email="+buscaEmail.toString());
//
//
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.setReadTimeout(20000);
//                urlConnection.connect();
//
//                String result = Util.webToString(urlConnection.getInputStream());
//                //pessoa = Util.JsonParaObjetoPessoa(result);
//                json = result;
//                return result;
//            } catch (Exception e) {
//                Log.e("Error", "Error ", e);
//                return null;
//            } finally{
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//            }
//        }

        protected String doInBackground(Pessoa... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://tripcuritiba.azurewebsites.net/api/login?email="+params[0].getEmail().toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());

                String result = Util.ConvertPessoaToJSON(params[0]);
                outputStream.writeBytes(result);

                serverResponseCode = urlConnection.getResponseCode();
                serverResponseMessage = Util.webToString(urlConnection.getInputStream());

                outputStream.flush();
                outputStream.close();

                    result = serverResponseMessage;

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
        protected void onPostExecute(String result) {
            dialog.dismiss();
            super.onPostExecute(result);
            if(result!=null){
                Intent intent = new Intent(LoginActivity.this, LogadoActivity.class);
                Bundle params = new Bundle();

                Pessoa paramsPessoa = new Pessoa();
                json = result;
                try {
                 paramsPessoa = Util.JsonParaObjetoPessoa(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                params.putSerializable("pessoa", paramsPessoa);
                intent.putExtras(params);
                startActivity(intent);

            }else{
                pgLogado = false;
            }

        }
    }
}
