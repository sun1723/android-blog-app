package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostDetail extends AppCompatActivity {
    FirebaseStorage storage;
    String key;
    StorageReference storageref;
    TextView textview;
    ImageView imgView;
    TextView articleView;

    private static final long MEGABYTE = 1024 * 1024 * 5 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        storage =FirebaseStorage.getInstance();
        storageref = storage.getReference();
        Intent i = getIntent();
        key = i.getStringExtra("post_key");

        textview = findViewById(R.id.title);
        imgView = findViewById(R.id.image);
        articleView = findViewById(R.id.article);

        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Post").child(key);
        postRef.addValueEventListener((new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post p = snapshot.getValue(Post.class);
                StorageReference imagevRef = storageref.child("images/"+ p.getEdittext().toString().trim());
                imagevRef.getBytes(MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imgView.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostDetail.this,"download image failed",Toast.LENGTH_LONG).show();
                    }
                });
                textview.setText(p.getEdittext());
                articleView.setText(p.getArticle());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));


    }
}