package com.example.wellclbo.tripcuritiba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {

    private EditText editEmail;
    private Button btnLogin;
    private String buscaEmail;
    private Boolean pgLogado = false;
    private Pessoa pessoa;
    private String json;
    private ImageView imageView;
    private Toolbar toolbar;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login2);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Login", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    if (email != null) {
                                        Toast.makeText(Login.this, email.toString(), Toast.LENGTH_LONG).show();
                                        Pessoa pessoa = new Pessoa();
                                        pessoa.setEmail(email);
                                        new Login.DownloadFromApiLogin().execute(pessoa);
                                    } else {
                                        Toast.makeText(Login.this, "Email esta nuloooo...", Toast.LENGTH_LONG).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //String birthday = object.getString("birthday"); // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
                Log.v("Login", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("Login", exception.getCause().toString());
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            redirect(accessToken.getUserId());
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void redirect(String ID) {
        Intent intent = new Intent(Login.this, LogadoActivity.class);
        intent.putExtra("FB_ID", ID);
        startActivity(intent);
    }

    public void onClick(View v) {
        pessoa = new Pessoa();
        buscaEmail = editEmail.getText().toString();
        pessoa.setEmail(buscaEmail);
        //new Login.DownloadFromApiLogin.execute();
        validaAcesso(v);
    }

    private void validaAcesso(View v) {
        new Login.DownloadFromApiLogin().execute(pessoa);

    }


    private class DownloadFromApiLogin extends AsyncTask<Pessoa, Void, String> {

        private ProgressDialog dialog;
        boolean isConnected = false;
        int serverResponseCode;
        String serverResponseMessage;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Login.this, "Aviso", "Aguarde, buscando dados");
            ConnectivityManager cm =
                    (ConnectivityManager) Login.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (!isConnected) {
                dialog.dismiss();

                Toast.makeText(Login.this, "Verifique a conexão com a internet...", Toast.LENGTH_LONG).show();
            }
        }

        protected String doInBackground(Pessoa... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://tripcuritiba.azurewebsites.net/api/login?email=" + params[0].getEmail().toString());
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
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            super.onPostExecute(result);

            Pessoa paramsPessoa = new Pessoa();
            json = result;
            try {
                paramsPessoa = Util.JsonParaObjetoPessoa(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (paramsPessoa != null && paramsPessoa.getRotas().size() > 0) {
                Intent intent = new Intent(Login.this, LogadoActivity.class);
                Bundle params = new Bundle();


                params.putSerializable("pessoa", paramsPessoa);
                intent.putExtras(params);
                startActivity(intent);

            } else if (paramsPessoa != null && paramsPessoa.getRotas().size() == 0) {

                Toast.makeText(Login.this, "Você não possue rotas salvas!", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(Login.this, "Email não encontrado na base de dados!", Toast.LENGTH_LONG).show();
            }

        }
    }
}



