package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //create variables 
    //for edittext and 2 buttons
    private Button selectButton,uploadButton,submitButton,deleteButton;
    private TextInputEditText editText;
    private TextInputLayout textInputLayout;
    private LinearLayout linearLayout1;
    private TextInputLayout textfield;
    private TextView textView;
    private ListView listView;
    private Button buttonView;
    private ImageView imageView;
    private String imagePath;
//    private ImageView imageView;




    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> keylist = new ArrayList<String>();
//    FirebaseRecyclerAdapter<Post,BlogViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLayoutManager;
    //upload image
    private static final int PICK_IMAGE = 1;
//    private static int count ;
    //variable for firebase database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageref;
    StorageReference imageRef;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);


        //initializing edittext and buttons
        linearLayout1 = findViewById(R.id.linear_layout1);

        textfield = findViewById(R.id.textField);
        editText =linearLayout1.findViewById(R.id.edit_Text);
        selectButton = linearLayout1.findViewById(R.id.select_button);
        uploadButton = linearLayout1.findViewById(R.id.upload_button);
        submitButton = linearLayout1.findViewById(R.id.submit_button);

        imageView = linearLayout1.findViewById(R.id.image_view);

        //action bar
        setSupportActionBar(findViewById(R.id.edit_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Post Page");

        //create Post object
        post = new Post();

        //create intent object
        Intent intent = new Intent();

        //get instance of the firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage =FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        storageref = storage.getReference();
//        imageRef = storageref.child("/images");
        linearLayout1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean res = true;
                if (imageView.getDrawable() == null || editText.getText().length() ==0) {
                    submitButton.setEnabled(false);
                } else {
                    submitButton.setEnabled(true);
                }
                return res;
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().length() > 0)
                    textfield.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        selectButton.setOnClickListener(MainActivity.this);
        uploadButton.setOnClickListener(MainActivity.this);
        submitButton.setOnClickListener(MainActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(this,MainActivity2.class);
                NavUtils.navigateUpTo(this,i);
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

    }

    @Override
    public void onClick(View view) {
        int i_d = view.getId();

        if(i_d == R.id.select_button){
//            openGallery();
            openDialog();
        }
        else if (i_d == R.id.upload_button)
        {

            imageRef = storageref.child("images/" + imagePath);
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,"FAILED UPLOADING IMAGE",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(MainActivity.this,"upload successfully",Toast.LENGTH_LONG).show();
//                    DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("image");
//                    
//                    HashMap<String,String> hashMap = new HashMap<>();
//                    hashMap.put("imageurl",String.valueOf(o.))
                }
            });

        }
        else if( i_d == R.id.submit_button) {
            //DEBUG:
            //Toast.makeText(this, "submit_button is clicked", Toast.LENGTH_SHORT).show();
            post.setEdittext(editText.getText().toString().trim());
            post.setImagePath(imagePath);
            databaseReference.push().setValue(post);
            imageView.setImageDrawable(null);
            editText.setText(null);

        }


    }

    private void openDialog() {
        String[] items ={"photo galary","camera"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Alert");
        alertDialog.setItems(items,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //Toast.makeText(MainActivity.this,"click on photo galary",Toast.LENGTH_LONG).show();
                        openGallery();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this,"click on camera",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();


    }

    private void openGallery() {
//        Bundle options = new Bundle();
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }
    @Override
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
            cursor.close();
            try {
                imageView.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}