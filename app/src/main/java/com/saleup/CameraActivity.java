package com.saleup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap = null;
    byte[] byteArray = {1,2,3};
    static boolean firstTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Button _submitButton = (Button) findViewById(R.id.btn_camera_submit);

        _submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submitItem();
            }
        });

        final TextView _txtEndDateValue = (TextView) findViewById(R.id.txtEndDateValue);
        SeekBar _seekBar = (SeekBar) findViewById(R.id.seekBar);

        _seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                _txtEndDateValue.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                //_txtEndDateValue.setText(progress);
                //t1.setTextSize(progress);
                //Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();

            }
        });

        ArrayList<Double> o = (ArrayList<Double>)Cache.GetInstance().Get(CameraActivity.this, "byteArray");

        byteArray = Utils.toPremitiveByteArray(o);
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView _photoView = (ImageView) findViewById(R.id.img_preview);
        _photoView.setImageBitmap(bmp);

    }

    public void submitItem(){

        TextView _descriptionText = (TextView) findViewById(R.id.txt_desc);
        final String description = _descriptionText.getText().toString();

        TextView _locationText = (TextView) findViewById(R.id.txt_camera_locationt);
        final String location = _locationText.getText().toString();

        SeekBar _seekBar = (SeekBar) findViewById(R.id.seekBar);
        int x = _seekBar.getProgress();
        Date today = new Date();
        final Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * x));

        final ProgressDialog progressDialog = new ProgressDialog(CameraActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending...");
        progressDialog.show();

        new Thread(new MyThread(
                new OnRunMe(){public Result run(){
                    try  {

                        String token = "";
                        String user = (String) Cache.GetInstance().Get(CameraActivity.this, "UserData");
                        try {
                            JSONTokener tokener = new JSONTokener(user);
                            JSONObject finalResult = new JSONObject(tokener);

                            token = finalResult.getString("Token");


                        }
                        catch (JSONException ex){

                        }

                        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                        HttpPost post = new HttpPost("http://saleup.azurewebsites.net/api/Item/SubmitItem");

                        post.addHeader("Content-type", "application/x-www-form-urlencoded");
                        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                        urlParameters.add(new BasicNameValuePair("Token", token));
                        urlParameters.add(new BasicNameValuePair("Description", description));
                        urlParameters.add(new BasicNameValuePair("LocationId", location));
                        urlParameters.add(new BasicNameValuePair("EndDate", tomorrow.toString()));
                        String imageString = "";
                        //byteArray = (byte[])Cache.GetInstance().Get(CameraActivity.this, "byteArray");
                        if(byteArray != null) {
                            //imageString = Base64.encodeToString(byteArray, 0);
                            imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            urlParameters.add(new BasicNameValuePair("Image", imageString));
                        }

                        byte[] bitmapdata = Base64.decode(imageString, 0);
                        Bitmap bmp = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

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
                    CameraActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject data = (JSONObject) result.data;

                                if(data.getInt("ResultNumber") == 1){

                                    Intent k = new Intent(CameraActivity.this, BaseActivity.class);
                                    startActivity(k);
                                    finish();
                                }
                                else{
                                    Toast.makeText(CameraActivity.this, "Error! try again",
                                            Toast.LENGTH_LONG).show();

                                    Intent k = new Intent(CameraActivity.this, LoginActivity.class);
                                    startActivity(k);
                                    finish();
                                }

                            }catch (JSONException ex){
                                Toast.makeText(CameraActivity.this, "Unknown error occurred!",
                                        Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

                }},
                new OnCallback(){public void callback(Result result){
                    CameraActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            progressDialog.dismiss();
                        }
                    });

                }}
        )).start();
    }

    

}
