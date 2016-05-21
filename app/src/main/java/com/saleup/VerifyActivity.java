package com.saleup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VerifyActivity extends AppCompatActivity {
    private static final String TAG = "VerifyActivity";

    @InjectView(R.id.txt_code) EditText _codeText;
    @InjectView(R.id.btn_verify) Button _verifyButton;
    @InjectView(R.id.lbl_verify_desc) TextView _lblDesc;
    @InjectView(R.id.btnResend) Button _resendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        ButterKnife.inject(this);

        _verifyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });

        _resendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resendCode();
            }
        });

        String phone = (String)Cache.GetInstance().Get(VerifyActivity.this, "PhoneNumber");
        _lblDesc.setText(_lblDesc.getText().toString() + " " + phone);

    }

    public void resendCode(){
        Log.d(TAG, "resendCode");

        final ProgressDialog progressDialog = new ProgressDialog(VerifyActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending...");
        progressDialog.show();

        final String phone = (String)Cache.GetInstance().Get(VerifyActivity.this, "PhoneNumber");



        new Thread(new MyThread(
                new OnRunMe(){public Result run(){
                    try  {

                        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                        HttpPost post = new HttpPost("http://saleup.azurewebsites.net/api/User/ResendCode");

                        post.addHeader("Content-type", "application/x-www-form-urlencoded");
                        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                        //urlParameters.add(new BasicNameValuePair("DeviceId", "abcd"));
                        urlParameters.add(new BasicNameValuePair("PhoneNumber", phone));
                        //urlParameters.add(new BasicNameValuePair("Code", "0"));

                        post.setEntity(new UrlEncodedFormEntity(urlParameters));


                        HttpResponse responseGet = httpClient.execute(post);

                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseGet.getEntity().getContent(), "UTF-8"));
                        String json = reader.readLine();

                        JSONTokener tokener = new JSONTokener(json);
                        JSONObject finalResult = new JSONObject(tokener);

                        httpClient.close();

                        Result result = new Result();
                        result.status = true;
                        result.data = finalResult;
                        return  result;

                    } catch (IOException e) {
                        Result result = new Result();
                        result.status = false;
                        return  result;
                    }
                    catch (JSONException e) {
                        Result result = new Result();
                        result.status = false;
                        return  result;
                    }
                }},
                new OnCallback(){public void callback(final Result result){
                    VerifyActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                            try {
                                JSONObject data = (JSONObject) result.data;

                                if(data.getInt("ResultNumber") == 1){

                                }
                                else{

                                }

                            }catch (JSONException ex){

                            }


                        }
                    });

                }},
                new OnCallback(){public void callback(Result result){
                    VerifyActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });

                }}
        )).start();
    }

    public void verifyCode() {
        Log.d(TAG, "verifyCode");

        _verifyButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(VerifyActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String phone = (String)Cache.GetInstance().Get(VerifyActivity.this, "PhoneNumber");
        final String code = _codeText.getText().toString();

        new Thread(new MyThread(
                new OnRunMe(){public Result run(){
                    try  {

                        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                        HttpPost post = new HttpPost("http://saleup.azurewebsites.net/api/User/ConfirmAuthentication");

                        post.addHeader("Content-type", "application/x-www-form-urlencoded");
                        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                        urlParameters.add(new BasicNameValuePair("DeviceId", "abcd"));
                        urlParameters.add(new BasicNameValuePair("PhoneNumber", phone));
                        urlParameters.add(new BasicNameValuePair("Code", code));

                        post.setEntity(new UrlEncodedFormEntity(urlParameters));


                        HttpResponse responseGet = httpClient.execute(post);

                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseGet.getEntity().getContent(), "UTF-8"));
                        String json = reader.readLine();

                        JSONTokener tokener = new JSONTokener(json);
                        JSONObject finalResult = new JSONObject(tokener);

                        httpClient.close();

                        Result result = new Result();
                        result.status = true;
                        result.data = finalResult;
                        return  result;

                    } catch (IOException e) {
                        Result result = new Result();
                        result.status = false;
                        return  result;
                    }
                    catch (JSONException e) {
                        Result result = new Result();
                        result.status = false;
                        return  result;
                    }
                }},
                new OnCallback(){public void callback(final Result result){
                    VerifyActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject data = (JSONObject) result.data;

                                if(data.getInt("ResultNumber") == 1){
                                    Cache.GetInstance().Set(VerifyActivity.this, "UserData", data.getString("Data"));

                                    Intent k = new Intent(VerifyActivity.this, BaseActivity.class);
                                    startActivity(k);
                                }
                                else{

                                }

                            }catch (JSONException ex){

                            }
                        }
                    });

                }},
                new OnCallback(){public void callback(Result result){
                    VerifyActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });

                }}
        )).start();

    }
}
