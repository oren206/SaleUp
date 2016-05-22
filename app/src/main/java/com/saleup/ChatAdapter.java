package com.saleup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {

    Context c;
    ChatMessage[] images;

    public ChatAdapter(Context c, ChatMessage[] images){
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
            convertView = inflater.inflate(R.layout.fragment_chat_item, null);
        }

        TextView textUserName = (TextView) convertView.findViewById(R.id.txt_chat_username);
        TextView textMessage = (TextView) convertView.findViewById(R.id.txt_chat_message);

        textUserName.setText(images[position].UserName + ": ");
        textMessage.setText(images[position].Message);

        return convertView;
    }
}