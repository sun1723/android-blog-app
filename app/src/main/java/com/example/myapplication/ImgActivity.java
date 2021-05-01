package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class ImgActivity extends AppCompatActivity {
    private RecyclerView rvImg;
    private TextView tv_img_msg;
    private Button btn;
    private static final int PICK_IMAGE = 1;
    private String imagePath;
    private ImageView imageView;
     ArrayList<ImgItem> img_list ;
    RecycleViewImgAdapter adapter;

    int i =0;
//    ArrayList<Integer> url_list;
    FirebaseStorage storage;

    StorageReference storageref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();

        storageref = storage.getReference().child("images/");
        setContentView(R.layout.img_library);
        rvImg = findViewById(R.id.rv_img);
        btn = findViewById(R.id.add_button);


        final long ONE_MEGABYTE = 1024 * 1024*10;
        final  int i =0;
//        Toast.makeText(this, storageref, Toast.LENGTH_LONG).show();
//        Intent intent = (ArrayList<ImgItem>)getIntent();
        Bundle bd = getIntent().getBundleExtra("bundle");
        img_list = (ArrayList<ImgItem>)bd.getSerializable("imgitem");

        adapter = new RecycleViewImgAdapter(img_list);
        adapter.updateAdapter(img_list);
        if(img_list == null)
        {
            Toast.makeText(this, "0", Toast.LENGTH_LONG).show();
        }else
        {
            Toast.makeText(this, String.valueOf(img_list.size()), Toast.LENGTH_LONG).show();
        }
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getApplicationContext());
        rvImg.setLayoutManager(linearLayoutManager);
//        rvImg.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        rvImg.swapAdapter(adapter,true);



        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
//        ImgItem it = new ImgItem("this is", 1);
//        img_list.add(it);






    }
    private void openGallery() {
//        Bundle options = new Bundle();
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Uri file = Uri.fromFile(new File(picturePath));

            imagePath = file.getLastPathSegment();
//            Toast.makeText(this,picturePath,Toast.LENGTH_LONG).show();
            UploadTask uploadTask = storage.getReference().child("images/" + imagePath).putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"upload failed",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageMetadata data = taskSnapshot.getMetadata();
                }
            });
            cursor.close();
            //                imageView.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage)));

        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}