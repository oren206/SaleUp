package com.saleup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter extends BaseAdapter {

    Context c;
    Item[] images;

    public Adapter(Context c, Item[] images){
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
            convertView = inflater.inflate(R.layout.fragment_home_item, null);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.imageView2);
        TextView text = (TextView) convertView.findViewById(R.id.textView5);

        if(images[position].Image != null) {
            byte[] bitmapdata = Base64.decode(images[position].Image, 0);
            Bitmap bmp = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            img.setImageBitmap(bmp);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        text.setText(images[position].EndDate);

        return convertView;
    }
}
