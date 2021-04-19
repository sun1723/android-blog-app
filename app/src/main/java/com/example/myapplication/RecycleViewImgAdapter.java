package com.example.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewImgAdapter extends RecyclerView.Adapter<RecycleViewImgAdapter.ViewHolder> {
    private ArrayList<ImgItem> imgItem ;

    public RecycleViewImgAdapter(ArrayList<ImgItem> imgItem)
    {
        this.imgItem = imgItem;
    }
    @NonNull
    @Override
    public RecycleViewImgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_item,parent,false);
        return new RecycleViewImgAdapter.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewImgAdapter.ViewHolder holder, int position) {
        holder.txtViewTitle.setText(imgItem.get(position).getTitle());
//        holder.settext("this is a text");
//        holder.imgView.setImageResource(imgItem.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return (imgItem == null) ? 0 : imgItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;
        public ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtViewTitle = (TextView)itemView.findViewById(R.id.text);
            imgView = (ImageView)itemView.findViewById(R.id.img);
        }
        public void settext(String txt)
        {
            TextView t = (TextView)itemView.findViewById(R.id.text);
            t.setText(txt);
        }
    }
}
