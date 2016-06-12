package com.saleup;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    TextView _txtFirstName = null;
    TextView _txtLastName = null;
    TextView _txtEmail = null;
    TextView _txtLocation = null;
    Spinner dropdown = null;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        dropdown = (Spinner)view.findViewById(R.id.spinner1_profile_location);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                Utils.Cities);
        dropdown.setAdapter(adapter);

        Button _updateButton = (Button) view.findViewById(R.id.btn_update_my_profile);
        _updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                update();
            }
        });

        _txtFirstName = (TextView) view.findViewById(R.id.txt_firstname_my_profile);
        _txtLastName = (TextView) view.findViewById(R.id.txt_lastname_my_profile);
        _txtEmail = (TextView) view.findViewById(R.id.txt_email_my_profile);
        //_txtLocation = (TextView) view.findViewById(R.id.txt_location_my_profile);

        String user = (String) Cache.GetInstance().Get(getActivity(), "UserData");
        try {
            JSONTokener tokener = new JSONTokener(user);
            JSONObject finalResult = new JSONObject(tokener);

            _txtFirstName.setText(finalResult.getString("FirstName"));
            _txtLastName.setText(finalResult.getString("LastName"));
            _txtEmail.setText(finalResult.getString("Email"));
            int x = Integer.parseInt(finalResult.getString("LocationId"));
            //_txtLocation.setText();
            dropdown.setSelection(x);

        } catch (JSONException ex){

        }



        return view;
    }

    public void update(){
        final String firstName = _txtFirstName.getText().toString();
        final String lastName = _txtLastName.getText().toString();
        final String email = _txtEmail.getText().toString();
        final String location = Integer.toString(dropdown.getSelectedItemPosition()); //_txtLocation.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Collecting data...");
        progressDialog.show();

        new Thread(new MyThread(
                new OnRunMe(){public Result run(){
                    try  {

                        String token = "";
                        String user = (String) Cache.GetInstance().Get(getActivity(), "UserData");
                        try {
                            JSONTokener tokener = new JSONTokener(user);
                            JSONObject finalResult = new JSONObject(tokener);

                            token = finalResult.getString("Token");


                        }
                        catch (JSONException ex){

                        }

                        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                        HttpPost post = new HttpPost("http://saleup.azurewebsites.net/api/User/Update");

                        post.addHeader("Content-type", "application/x-www-form-urlencoded");
                        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                        urlParameters.add(new BasicNameValuePair("Token", token));
                        urlParameters.add(new BasicNameValuePair("FirstName", firstName));
                        urlParameters.add(new BasicNameValuePair("LastName", lastName));
                        urlParameters.add(new BasicNameValuePair("Email", email));
                        urlParameters.add(new BasicNameValuePair("LocationId", location));

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
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject data = (JSONObject) result.data;

                                if(data.getInt("ResultNumber") == 1){
                                    Cache.GetInstance().Set(getActivity(), "UserData", data.getString("Data"));
                                }
                                else{
                                    Toast.makeText(getActivity(), "Error! try again!",
                                            Toast.LENGTH_LONG).show();
                                }

                            }catch (JSONException ex){
                                Toast.makeText(getActivity(), "Unknown error occurred!",
                                        Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

                }},
                new OnCallback(){public void callback(Result result){
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            progressDialog.dismiss();
                        }
                    });

                }}
        )).start();
    }

}
