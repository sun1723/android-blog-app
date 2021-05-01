package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

public class ImgItem implements Serializable {
    private String title;
    private byte[] img_byte;
    private Context context;
    private StorageReference ref;
    private String img_url;


    public ImgItem()
    {

    }



    public void setTitle(String title )
    {
        this.title = title;
    }
//    public void setImage(byte[] bytes)
//    {
//        img_byte =bytes;
//    }
    public void setImage(String img_url)
    {
        this.img_url = img_url;
    }
    public void setRef(StorageReference  ref)
    {
        this.ref = ref;
    }


    public String getTitle() {
        return this.title;
    }

    public String getImageUrl()
    {
        return this.img_url;
    }

    public StorageReference getRef() {return this.ref;}


    public static ArrayList<ImgItem> createImgList() {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference  storageref = storage.getReference().child("images/");
//        final long ONE_MEGABYTE = 1024 * 1024*10;
//        ArrayList<ImgItem> img_list = new ArrayList<ImgItem>();
//        storageref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//            @Override
//            public void onSuccess(ListResult listResult) {
//
//                for (StorageReference item : listResult.getItems()) {
//
//                    item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                        @Override
//                        public void onSuccess(byte[] bytes) {
////                            Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
//
//                            ImgItem it = new ImgItem();
//                            it.setTitle("1");
//                            it.setImage(bytes);
//                            img_list.add(img_list.size(), it);
////                            Toast.makeText(this, "the length is: "+String.valueOf(img_list.size()), Toast.LENGTH_LONG).show();
//                            Log.d("out",String.valueOf(img_list.size()));
//
//                        }
//
//                    });
//                }
//
//            }
//        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference  storageref = storage.getReference().child("images/");
        final long ONE_MEGABYTE = 1024 * 1024*10;
        ArrayList<ImgItem> img_list = new ArrayList<ImgItem>();
        storageref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>()  {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference item : listResult.getItems()) {

                    String path = item.getPath();
//                            Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();

                    ImgItem it = new ImgItem();
                    it.setTitle("1");
                    it.setImage(path);
                    img_list.add(it);
//                            Toast.makeText(this, "the length is: "+String.valueOf(img_list.size()), Toast.LENGTH_LONG).show();
                    Log.d("out", String.valueOf(img_list.size()));


                }

            }
        });
        return img_list;
    }


}
