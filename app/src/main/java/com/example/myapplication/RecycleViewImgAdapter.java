package com.example.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecycleViewImgAdapter extends RecyclerView.Adapter<RecycleViewImgAdapter.ViewHolder> {
    private ArrayList<ImgItem> imgItem = new ArrayList<ImgItem>();
    private Context context;

    public RecycleViewImgAdapter( ArrayList<ImgItem> imgItem)
    {
//        this.context = context;
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
//        imgItem = ImgItem.createImgList();
        Log.d("it has",String.valueOf(this.imgItem.size()));
        holder.txtViewTitle.setText(this.imgItem.get(position).getTitle());
        holder.settext("this is a text");
//        holder.imgView.setImageResource(imgItem.get(position).getImageUrl());
//        byte[] bytes = imgItem.get(position).getImageUrl();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
////        Toast.makeText( "hey", Toast.LENGTH_LONG).show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageref = storage.getReference().child(this.imgItem.get(position).getImageUrl());
        final long ONE_MEGABYTE = 1024 * 1024*5;
//        ArrayList<ImgItem> img_list = new ArrayList<ImgItem>();
//        storageref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//            @Override
//            public void onSuccess(ListResult listResult) {
//
//                for (StorageReference item : listResult.getItems()) {

                    storageref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
//                            Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();

//                            ImgItem it = new ImgItem();
//                            it.setTitle("1");
//                            it.setImage(bytes);
//                            img_list.add(img_list.size(), it);
//                            Toast.makeText(this, "the length is: "+String.valueOf(img_list.size()), Toast.LENGTH_LONG).show();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            holder.imgView.setImageBitmap(bitmap);
//                            Log.d("out",String.valueOf(img_list.size()));
                        }
                    });
//                }
//
//            }
//        });
//        holder.imgView.setImageBitmap(bitmap);


    }

    @Override
    public int getItemCount() {
        return (imgItem == null) ? 0 : imgItem.size();
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, ImgItem data) {
        imgItem.add(position, data);
        notifyItemInserted(position);
    }

    public void updateAdapter(ArrayList<ImgItem> imglist){
        this.imgItem = imglist;
        notifyDataSetChanged();
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
