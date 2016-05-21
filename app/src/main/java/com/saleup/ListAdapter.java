package com.saleup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

    Context c;
    Offer[] images;

    public ListAdapter(Context c, Offer[] images){
        this.c = c;
        this.images = images;

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_offer_item, null);
        }

        TextView textPrice = (TextView) convertView.findViewById(R.id.txt_offer_price);
        TextView textLocation = (TextView) convertView.findViewById(R.id.txt_offer_location);
        TextView textDate = (TextView) convertView.findViewById(R.id.txt_offer_date);

        textPrice.setText(Integer.toString(images[position].Price));
        textLocation.setText(Integer.toString(images[position].LocationId));
        //textDate.setText(images[position].EndDate);

        return convertView;
    }
}