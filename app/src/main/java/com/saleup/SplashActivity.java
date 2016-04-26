package com.saleup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new MyThread(
                new OnRunMe(){public Result run(){
                    try  {

                        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                        HttpPost post = new HttpPost("http://saleup.azurewebsites.net/api/User/CheckAuthentication");

                        post.addHeader("Content-type", "application/x-www-form-urlencoded");
                        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                        urlParameters.add(new BasicNameValuePair("DeviceId", "abcd"));

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
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject data = (JSONObject) result.data;

                                if(data.getInt("ResultNumber") == 1){
                                    Cache.GetInstance().Set(SplashActivity.this, "UserData", data.getString("Data"));

                                    Intent k = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(k);
                                    finish();
                                }
                                else{
                                    Intent k = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(k);
                                    finish();
                                }

                            }catch (JSONException ex){

                            }
                        }
                    });

                }},
                new OnCallback(){public void callback(Result result){
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });

                }}
        )).start();

    }
}
