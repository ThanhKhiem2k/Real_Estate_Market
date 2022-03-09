package com.example.real_estate_market.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.real_estate_market.R;

import java.util.ArrayList;
public class GalleryAdapter extends BaseAdapter {
    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    ArrayList<Uri> mArrayUri;
    public GalleryAdapter(Context ctx, ArrayList<Uri> mArrayUri) {
        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
    }

    public int getCount() {
        return mArrayUri.size();
    }

    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View itemView = inflater.inflate(R.layout.gv_item, parent, false);

        ivGallery = itemView.findViewById(R.id.ivGallery);

        ivGallery.setImageURI(mArrayUri.get(position));

        return itemView;
    }
}
