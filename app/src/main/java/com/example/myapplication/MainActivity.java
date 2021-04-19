package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import androidx.core.content.ContextCompat;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //create variables 
    //for edittext and 2 buttons
    private Button selectButton,uploadButton,submitButton,deleteButton;
    private TextInputEditText editText;
    private TextInputLayout textInputLayout;
    private LinearLayout linearLayout1;
    private TextInputLayout textfield;
    private TextView textView,contentView;
    private ListView listView;
    private Button buttonView;
    private ImageView imageView;
    private String imagePath;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    ArrayList<ImgItem> image_list ;
//    private ImageView imageView;




    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> keylist = new ArrayList<String>();
//    FirebaseRecyclerAdapter<Post,BlogViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLayoutManager;
    //upload image

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
        contentView = linearLayout1.findViewById(R.id.article);
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
                if (imageView.getDrawable() == null || editText.getText().length() ==0 || contentView.getText().length()==0) {
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

            imageRef = storageref.child("images/" + editText.getText().toString());
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);

//            ImgItem it = new ImgItem(imagePath,Integer.parseInt(String.valueOf(imageRef.getDownloadUrl())));
//            image_list.add(it);
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
            post.setArticle(contentView.getText().toString().trim());
            databaseReference.push().setValue(post);
            imageView.setImageDrawable(null);
            editText.setText(null);
            contentView.setText(null);

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
//                        openGallery();
                        Intent i = new Intent(MainActivity.this,ImgActivity.class);
                        i.putExtra("imgitem",image_list);
                        startActivity(i);
                        break;
                    case 1:
//                        Toast.makeText(MainActivity.this,"click on camera",Toast.LENGTH_LONG).show();
                        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                        {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        }
                        else
                        {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                        break;
                }

            }

        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

}