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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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

        Button _photoButton = (Button) findViewById(R.id.button);

        _photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        Button _submitButton = (Button) findViewById(R.id.btn_camera_submit);

        _submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submitItem();
            }
        });

    }

    public void submitItem(){

        TextView _descriptionText = (TextView) findViewById(R.id.txt_desc);
        final String description = _descriptionText.getText().toString();

        TextView _locationText = (TextView) findViewById(R.id.txt_camera_locationt);
        final String location = _locationText.getText().toString();

        TextView _endDateText = (TextView) findViewById(R.id.txt_camera_endDate);
        final String endDate = _endDateText.getText().toString();

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
                        urlParameters.add(new BasicNameValuePair("EndDate", endDate));
                        String imageString = "";
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
                                    Intent k = new Intent(CameraActivity.this, LoginActivity.class);
                                    startActivity(k);
                                    finish();
                                }

                            }catch (JSONException ex){

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

    public void takePhoto(){
        firstTaken = true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), REQUEST_IMAGE_CAPTURE);
                */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView _photoView = (ImageView) findViewById(R.id.img_preview);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            _photoView.setImageBitmap(imageBitmap);

            /*
            int width = imageBitmap.getWidth();
            int height = imageBitmap.getHeight();

            int size = imageBitmap.getRowBytes() * imageBitmap.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            imageBitmap.copyPixelsToBuffer(byteBuffer);
            byteArray = byteBuffer.array();
            */

            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
            byteArray = byteArrayOS.toByteArray();
        }
    }

}
