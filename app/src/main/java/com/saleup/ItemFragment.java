package com.saleup;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {


    public ItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        String jsonMyObject = getArguments().getString("myObject");

        Item myObject = new Gson().fromJson(jsonMyObject, Item.class);

        TextView text = (TextView) view.findViewById(R.id.txtDesc_item);
        text.setText(myObject.Description);

        if(myObject.Image != null) {
            byte[] bitmapdata = Base64.decode(myObject.Image, 0);
            Bitmap bmp = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            ImageView imageView = (ImageView) view.findViewById(R.id.img_item);
            imageView.setImageBitmap(bmp);
        }

        return view;
    }

}
