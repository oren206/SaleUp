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
import android.widget.ProgressBar;
import android.widget.TextView;

public class Adapter extends BaseAdapter {

    // our ViewHolder.
    // caches our TextView
    static class ViewHolderItem {
        TextView textViewItem;
        ImageView imageViewItem;
    }

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
        final ViewHolderItem viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_home_item, null);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.textView5);
            viewHolder.imageViewItem = (ImageView) convertView.findViewById(R.id.imageView2);

            //store the holder with the view.
            convertView.setTag(viewHolder);

        }
        else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }


        if(images[position].Image != null) {
            byte[] bitmapdata = Base64.decode(images[position].Image, 0);
            Bitmap bmp = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            viewHolder.imageViewItem.setImageBitmap(bmp);
            viewHolder.imageViewItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        viewHolder.textViewItem.setText(images[position].EndDate);

        return convertView;
    }
}
