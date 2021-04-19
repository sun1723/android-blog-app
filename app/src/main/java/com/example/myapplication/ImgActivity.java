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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class ImgActivity extends AppCompatActivity {
    private RecyclerView rvImg;
    private TextView tv_img_msg;
    private Button btn;
    private static final int PICK_IMAGE = 1;
    private String imagePath;
    private ImageView imageView;
    ArrayList<ImgItem> img_list = new ArrayList<ImgItem>();
//    ArrayList<Integer> url_list;
    FirebaseStorage storage;

    StorageReference storageref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference().child("images");
        setContentView(R.layout.img_library);
        rvImg = findViewById(R.id.rv_img);
        btn = findViewById(R.id.add_button);
        Intent i = getIntent();
        img_list = i.<ImgItem>getParcelableArrayListExtra("imgitem");
        if(img_list == null)
        {
            Toast.makeText(this, "0", Toast.LENGTH_LONG).show();
        }

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getApplicationContext());
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
//        ImgItem it = new ImgItem("this is", Integer.parseInt(imagePath));
//        img_list.add(it);
        RecycleViewImgAdapter adapter = new RecycleViewImgAdapter(img_list);
//        rvImg.setLayoutManager(new GridLayoutManager(this,3));
        rvImg.setLayoutManager(linearLayoutManager);
        rvImg.setAdapter(adapter);



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

            imagePath = file.getLastPathSegment().toString();
//            Toast.makeText(this,picturePath,Toast.LENGTH_LONG).show();
            ImgItem it = new ImgItem("title",selectedImage.hashCode());
            if(img_list == null){
                Toast.makeText(this, "0", Toast.LENGTH_LONG).show();
                img_list = new ArrayList<ImgItem>();
            }

            img_list.add(it);
            cursor.close();
            //                imageView.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage)));

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}