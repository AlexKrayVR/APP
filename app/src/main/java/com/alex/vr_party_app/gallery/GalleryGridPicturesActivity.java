package com.alex.vr_party_app.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityGalleryGridPicturesBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryGridPicturesActivity extends AppCompatActivity {

    private ActivityGalleryGridPicturesBinding binding;
    private DatabaseReference databaseReference;
    private ArrayList<Upload> uploadsPicture = new ArrayList<>();
    private StorageReference storageRef;
    String path;
    String title;
    private ImageStaggeredGridAdapter adapterPictures;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGalleryGridPicturesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        path = getIntent().getStringExtra("path");
        title = getIntent().getStringExtra("title");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapterPictures = new ImageStaggeredGridAdapter(this, uploadsPicture);

        binding.recyclerGallery.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
        binding.recyclerGallery.setAdapter(adapterPictures);
        storageRef = FirebaseStorage.getInstance().getReference(path);
        databaseReference = FirebaseDatabase.getInstance().getReference(path);

        binding.addPictureHolder.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddPictureActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("title", title);
            startActivity(intent);
        });


        getPictures();


    }

    private void getPictures() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Upload upload = dataSnapshot.getValue(Upload.class);
                uploadsPicture.add(upload);
                Log.d("AlexDebug", upload.getImageUrl());
                adapterPictures.notifyDataSetChanged();
                //binding.recyclerRandomPicture.smoothScrollToPosition(uploadsRandomPicture.size());
                binding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}