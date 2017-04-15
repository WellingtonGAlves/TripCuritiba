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
    private ImageView imageView;
    private Boolean pgLogado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail = (EditText)findViewById(R.id.ediEmail);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        imageView = (ImageView)findViewById(R.id.imageView);
        buscaEmail = editEmail.getText().toString();
        imageView.setEnabled(true);



    }
    public void onClick(View v)
    {
        buscaEmail = editEmail.getText().toString();
        new LoginActivity.DownloadFromApiLogin().execute();
        if(pgLogado==true){
            Intent intent = new Intent(v.getContext(), LogadoActivity.class);
            Bundle params = new Bundle();

            String email = buscaEmail;
            params.putString("email", email);
            intent.putExtras(params);
            startActivity(intent);


        }
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
                urlConnection.connect();

                String result = Util.webToString(urlConnection.getInputStream());

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
            if(result==null) {
                pgLogado = false;
            }else{
                pgLogado = true;
            }

        }
    }
}
