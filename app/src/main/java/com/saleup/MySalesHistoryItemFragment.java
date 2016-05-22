package com.saleup;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

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
public class MySalesHistoryItemFragment extends Fragment {

    Item myObject = null;

    public MySalesHistoryItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_my_sales_history_item, container, false);

        String jsonMyObject = getArguments().getString("myObject");

        myObject = new Gson().fromJson(jsonMyObject, Item.class);

        TextView text = (TextView) view.findViewById(R.id.txtDesc_my_sales_history);
        text.setText(myObject.Description);

        if(myObject.Image != null) {
            byte[] bitmapdata = Base64.decode(myObject.Image, 0);
            Bitmap bmp = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            ImageView imageView = (ImageView) view.findViewById(R.id.img_my_sales_history);
            imageView.setImageBitmap(bmp);
        }

        Button _btnChat = (Button) view.findViewById(R.id.btn_my_sales_history_chat);
        _btnChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToChat();
            }
        });

        return view;
    }

    public void goToChat(){
        ChatFragment fragment = new ChatFragment();
        Bundle b = new Bundle();
        b.putString("myObject", new Gson().toJson(myObject));
        fragment.setArguments(b);

        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
