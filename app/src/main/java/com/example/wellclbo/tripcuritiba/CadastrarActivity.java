package com.example.wellclbo.tripcuritiba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CadastrarActivity extends AppCompatActivity {

    private Pessoa pessoa;
    private String json;
    private TextView lblEmail;
    private EditText edtNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        lblEmail = (TextView)findViewById(R.id.lblEmail);
        edtNome = (EditText)findViewById(R.id.edtNome) ;

        Intent intent = getIntent();

        Bundle params = intent.getExtras();
        Pessoa paramsPessoa = new Pessoa();
        if (params != null) {
            paramsPessoa = (Pessoa) params.getSerializable("pessoa");
            pessoa = paramsPessoa;
            lblEmail.setText(pessoa.getEmail().toString());
        }
    }

    public  void salvarUsuario(View v) {
        pessoa.setNome(edtNome.getText().toString());
        new CadastrarActivity.CreateuserFromApiLogin().execute(pessoa);
    }

    private class CreateuserFromApiLogin extends AsyncTask<Pessoa, Void, String> {

        private ProgressDialog dialog;
        private boolean isConnected = false;
        private int serverResponseCode;
        private String serverResponseMessage;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(CadastrarActivity.this, "Aviso", "Aguarde, buscando dados");
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
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://tripcuritiba.azurewebsites.net/api/login?email="+params[0].getEmail().toString()+"&nome="+pessoa.getNome().toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());

                String result = Util.ConvertPessoaToJSON(params[0]);
                outputStream.writeBytes(result);

                serverResponseCode = urlConnection.getResponseCode();
//                /
                serverResponseMessage = Util.webToString(urlConnection.getInputStream());

                outputStream.flush();
                outputStream.close();

                result = serverResponseMessage;
                if(serverResponseCode == 200){
                    result = serverResponseMessage;

                }else{
                    result = null;
                }
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


            json = result;
            try {
                pessoa = Util.JsonParaObjetoPessoa(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(pessoa!=null ){
                Intent intent = new Intent(CadastrarActivity.this, LoginActivity.class);
                Bundle params = new Bundle();
                Toast.makeText(CadastrarActivity.this,"Cadastro realizado com Sucesso!" , Toast.LENGTH_LONG).show();
                LoginActivity.bool = true;

                startActivity(intent);

            }else
            {
                Toast.makeText(CadastrarActivity.this,"Desculpe, não foi possivel cadastrar!Tente novamente!" , Toast.LENGTH_LONG).show();
                //existeNaBasedeDados = false;
                Intent intent = new Intent(CadastrarActivity.this, CadastrarActivity.class);
                Bundle params = new Bundle();
                params.putSerializable("pessoa", pessoa);
                intent.putExtras(params);
                startActivity(intent);
            }


        }
    }
}
