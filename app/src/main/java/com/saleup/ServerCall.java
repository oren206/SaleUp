package com.saleup;

import android.os.Handler;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerCall {
    public static void initSomeProcess(Runnable callbackProcessOnFailed) {
        final Runnable runOnFailed = callbackProcessOnFailed;

        //Do something...

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

                    HttpPost get = new HttpPost("http://saleup.azurewebsites.net/api/User/GetValues");
                    get.setHeader("Accept", "application/json");
                    //get.setHeader("X-Api-Key", apiKey);
                    StringEntity params =new StringEntity("details={\"name\":\"myname\"} ");
                    get.addHeader("content-type", "application/x-www-form-urlencoded");
                    get.setEntity(params);
                    HttpResponse responseGet = httpClient.execute(get);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseGet.getEntity().getContent(), "UTF-8"));
                    String json = reader.readLine();
                    int i = 0;

                    /*if (runOnFailed!=null) {
                        Handler h = new Handler();
                        h.post(runOnFailed);
                    }*/

                } catch (IOException e) {

                    // handle

                }
            }
        });

        thread.start();




    }
}
