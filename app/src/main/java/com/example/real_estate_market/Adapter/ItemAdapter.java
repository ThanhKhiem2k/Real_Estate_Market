package com.example.real_estate_market.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.real_estate_market.Class.Class.DataPost;
import com.example.real_estate_market.R;

import java.text.DecimalFormat;
import java.util.List;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<DataPost> mdataPost;

    public ItemAdapter(Context context, int layout, List<DataPost> mdataPost) {
        this.context = context;
        this.layout = layout;
        this.mdataPost = mdataPost;
    }

    @Override
    public int getCount() {
        return mdataPost.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView Name_Project,PriceItem,PhoneNumber, Address;
        ImageView MainImg_Item;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            //Anh xa
            holder = new ViewHolder();
            holder.Name_Project = convertView.findViewById(R.id.textViewNameProject);
            holder.PriceItem = convertView.findViewById(R.id.textViewPriceItem);
            holder.PhoneNumber = convertView.findViewById(R.id.textViewPNumberItem);
            holder.MainImg_Item = convertView.findViewById(R.id.imageViewMain);
            holder.Address = convertView.findViewById(R.id.textViewAddress);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Gán giá trị
        DataPost DP = mdataPost.get(position);
        holder.Name_Project.setText(DP.getNameProject());
        double price = Double.parseDouble(DP.getPriceItem());
        DecimalFormat f = new DecimalFormat("##.00");
        if(price>=1000000000){
            price = price/1000000000;
            holder.PriceItem.setText( f.format(price) + " tỷ VNĐ");
        }else if(price>=1000000){
            price = price/1000000;
            holder.PriceItem.setText( f.format(price) + " triệu VNĐ");
        }else {
            holder.PriceItem.setText(price + " VNĐ");
        }

        holder.PhoneNumber.setText("Liên hệ: " + DP.getPNumberItem());
        holder.Address.setText("Địa chỉ: "+DP.getAddress());
        Glide.with(convertView).load(DP.getImageMain()).into(holder.MainImg_Item);
        return convertView;
    }

}
