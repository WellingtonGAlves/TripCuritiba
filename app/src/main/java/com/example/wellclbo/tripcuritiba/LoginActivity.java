package com.example.wellclbo.tripcuritiba;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.ImageView;

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
        //new LoginActivity.DownloadFromApiLogin().execute();
        validaAcesso(v);
    }

    private void validaAcesso(View v) {
        new LoginActivity.DownloadFromApiLogin().execute();

    }

    private class DownloadFromApiLogin extends AsyncTask<Void, Void, String> {

        private ProgressDialog dialog;

        @Override
         protected void onPreExecute() {
                        dialog = ProgressDialog.show(LoginActivity.this, "Aviso", "Aguarde, buscando dados");
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url;

                    url = new URL("http://tripcuritiba.azurewebsites.net/api/login?email="+buscaEmail.toString());



                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(20000);
                urlConnection.connect();

                String result = Util.webToString(urlConnection.getInputStream());
                //pessoa = Util.JsonParaObjetoPessoa(result);
                json = result;
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
            if(result!=null){
                Intent intent = new Intent(LoginActivity.this, LogadoActivity.class);
                Bundle params = new Bundle();


                params.putString("json", json);
                intent.putExtras(params);
                startActivity(intent);


            }else{
                pgLogado = false;
            }

        }
    }
}
