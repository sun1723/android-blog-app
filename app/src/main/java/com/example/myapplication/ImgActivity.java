package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class ImgActivity extends AppCompatActivity {
    private RecyclerView rv_img;
    private TextView tv_img_msg;
    ArrayList<String> imglist = null;
    FirebaseStorage storage;

    StorageReference storageref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();

        setContentView(R.layout.img_library);
        rv_img = findViewById(R.id.rv_img);
        setImageList();
        loadUI();
    }
    public void loadUI(){
        List<ImgFile> list = new ArrayList<>();
        ImgFile img = new ImgFile();
        img.setFileName("第一张");
        img.setFileSrc("");
        list.add(img);
        img = new ImgFile();
        img.setFileName("第二张");
        img.setFileSrc("");
        list.add(img);
        img = new ImgFile();
        img.setFileName("第三张");
        img.setFileSrc("");
        list.add(img);
        if(list != null && list.size() > 0){
            GridLayoutManager layoutManager = new GridLayoutManager(ImgActivity.this,3);
            rv_img.setLayoutManager(layoutManager);
            imgAdapter adapter = new imgAdapter(list);
            rv_img.setAdapter(adapter);
        }else{
            tv_img_msg.setVisibility(View.VISIBLE);
        }
    }
    class imgAdapter extends RecyclerView.Adapter<imgAdapter.ImageViewHolder>
    {
        private List<ImgFile> list = null;
        public imgAdapter(List<ImgFile> list){ this.list = list; }


        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ImgActivity.this).inflate(R.layout.img_item, parent, false);
            return new ImageViewHolder(v);
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{
            private static final long MEGABYTE = 1024 * 1024 * 5 ;
            View mView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                mView = itemView;
            }
            public void settext(String text){
                TextView t = (TextView)mView.findViewById(R.id.text);
                t.setText(text);
            }

            public void setimage (String imageString) throws FileNotFoundException {
                Uri selectedImg = Uri.parse(imageString);
                StorageReference imageRef = storageref.child( imageString);
                ImageView img = (ImageView)mView.findViewById(R.id.img);

                imageRef.getBytes(MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        img.setImageBitmap(bitmap);
                    }
                });
            }
        }

        @SuppressLint("NewApi")

        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            ImgFile img = list.get(position);
            img.setFileName("PIC");
            try {
                holder.setimage(imglist.get(position));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View ve) {
                    Toast.makeText(ImgActivity.this,"on click a image", LENGTH_LONG).show();
//                    ve.getContext().startActivity(
//                            new Intent(ve.getContext(),OtherActivity.class),
//
//                            ActivityOptions.makeSceneTransitionAnimation((Activity) ve.getContext(),
//                                    ve,"sharedView").toBundle()
//                    );
                }
            });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    public void setImageList() {
        StorageReference imgref = storageref.child("images/");
        Task<ListResult> tasks = imgref.listAll();
        tasks.addOnFailureListener(new OnFailureListener(){

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImgActivity.this,e.toString(), LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult result) {
                for(StorageReference fileRef : result.getItems()) {
                    // TODO: Download the file using its reference (fileRef)
                    imglist.add(fileRef.getName());
                }
            }
        });



    }


    class ImgFile {
        private String fileName;
        private String fileSrc;

        public ImgFile(){}

        public String getFileName() { return fileName; }

        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getFileSrc() { return fileSrc; }

        public void setFileSrc(String fileSrc) { this.fileSrc = fileSrc; }
    }

}