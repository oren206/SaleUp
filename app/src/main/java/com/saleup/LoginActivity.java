package com.saleup;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_phone) EditText _phoneText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });




/*
        ServerCall.initSomeProcess(new Runnable() {
            @Override
            public void run() {
                int i = 0;
            }
        });*/
    }

    public void login() {
        Log.d(TAG, "Login");


        final String phone = _phoneText.getText().toString();

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

                    HttpPost post = new HttpPost("http://saleup.azurewebsites.net/api/User/BeginRegistration");
                    /*
                    get.setHeader("Accept", "application/json");
                    //get.setHeader("X-Api-Key", apiKey);
                    StringEntity params =new StringEntity("details={\"name\":\"myname\"} ");
                    get.addHeader("content-type", "application/x-www-form-urlencoded");
                    get.setEntity(params);
                    //get.setHeader("X-Api-Key", apiKey);
                    */

                    //post.setHeader("User-Agent", USER_AGENT);



                    
                    post.addHeader("Content-type", "application/x-www-form-urlencoded");
                    List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                    urlParameters.add(new BasicNameValuePair("PhoneNumber", phone));

                    post.setEntity(new UrlEncodedFormEntity(urlParameters));

                    HttpResponse responseGet = httpClient.execute(post);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseGet.getEntity().getContent(), "UTF-8"));
                    String json = reader.readLine();
                    int i = 0;
                } catch (IOException e) {

                    // handle

                }
            }
        });

        thread.start();






/*
        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String phone = _phoneText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

                */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _phoneText.getText().toString();

        if (email.isEmpty() ) {
            _phoneText.setError("enter a valid email address");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        return valid;
    }
}