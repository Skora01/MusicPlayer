package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {
    public String[] items;
    public LayoutInflater mInflater;
    public ItemAdapter(Context c,String[] items)
    {
        this.items = items;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.list_items,null);
        TextView txtSongName = (TextView) v.findViewById(R.id.txtsongname);
        String songName = items[position];
        txtSongName.setText(songName);
        return v;
    }
}
