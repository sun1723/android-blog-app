package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    private LinearLayout linearLayout2;
    Button addbutton;
    private ImageView imageView;
    DatabaseReference databaseReference;
    FirebaseStorage storage;

    StorageReference storageref;

    Query query1;

    FirebaseRecyclerAdapter<Post, MainActivity2.BlogViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout2 = findViewById(R.id.linear_layout2);
        addbutton = findViewById(R.id.add_button);
        recyclerView = linearLayout2.findViewById(R.id.recycler1);
        storage =FirebaseStorage.getInstance();
        storageref = storage.getReference();

        setSupportActionBar(findViewById(R.id.appbar));
        addbutton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        query1 = FirebaseDatabase.getInstance().getReference("Post");
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(query1,Post.class).build();
//        Log.d("Options","data: "+ options);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, MainActivity2.BlogViewHolder>(options)
        {
            @NonNull
            @Override
            public MainActivity2.BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post,parent,false);
                return new MainActivity2.BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainActivity2.BlogViewHolder holder, int position, @NonNull Post postp) {
                holder.settext(postp.getEdittext());
                try {
                    holder.setimage(postp.getEdittext());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                View.OnClickListener onclicklistener = new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v) {
                        //debug:
//                                        Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
                       int id = v.getId();
                       if(id == R.id.delete_button) {
                           getRef(position).removeValue();
                           firebaseRecyclerAdapter.notifyItemRemoved(position);
                       }
                       else if(id == R.id.readmore_btn){
                           Intent i = new Intent(getApplicationContext(), PostDetail.class);

                           i.putExtra("title",postp.getEdittext());
                           i.putExtra("post_key",getRef(position).getKey());
                           startActivity(i);
                       }
                    }
                };
                holder.dele_button.setOnClickListener(onclicklistener);
                holder.readmore_btn.setOnClickListener(onclicklistener);
            }
        };

        firebaseRecyclerAdapter.startListening();
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        int i_d = v.getId();
        if(i_d == R.id.add_button)
        {
            Intent i = new Intent(this, MainActivity.class);

            startActivity(i);
        }
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder  {
        private static final long MEGABYTE = 1024 * 1024 * 5 ;
        Button dele_button;
        Button readmore_btn;
        View mView;
        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            dele_button = (Button)mView.findViewById(R.id.delete_button);
            readmore_btn= (Button)mView.findViewById(R.id.readmore_btn);
        }

        public void settext (String text)
        {
            TextView etext = (TextView)mView.findViewById(R.id.text);
            etext.setText(text);
        }
        public void setimage (String imagepath) throws FileNotFoundException {
//            Toast.makeText(MainActivity.this,"path: "+imagepath,Toast.LENGTH_LONG).show();
            Uri selectedImage = Uri.parse(imagepath);
//            Toast.makeText(MainActivity.this,"uri: "+ selectedImage,Toast.LENGTH_LONG).show();
            StorageReference imagevRef = storageref.child("images/" + imagepath);
            ImageView image = (ImageView)mView.findViewById(R.id.image_View);
            imagevRef.getBytes(MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity2.this,"download image failed",Toast.LENGTH_LONG).show();
                }
            });
//            Picasso.with(MainActivity.this).load(String.valueOf(imagevRef.getDownloadUrl())).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(image);
//            image.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage)));
//            Picasso.get().load( imagepath).into(image);
//            image.setImageURI(Uri.fromFile(new File(imagepath)));
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            imageLoader.displayImage(selectedImage.toString(),image);
        }
    }
}