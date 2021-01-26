package com.alex.vr_party_app.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityGalleryBinding;
import com.alex.vr_party_app.support.ItemOffsetDecoration;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    private ActivityGalleryBinding binding;
    androidx.appcompat.widget.Toolbar toolbar;

    private DatabaseReference databaseRandomReference, databaseGroupReference, databaseUserReference;

    private ArrayList<Upload> uploadsGroupPicture = new ArrayList<>(),
            uploadsRandomPicture = new ArrayList<>(),
            uploadsUserPicture = new ArrayList<>();

    private StorageReference storageRef;

    private ImageAdapter adapterGroupPicture, adapterRandomPicture, adapterUserPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.Gallery));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.randomGallery.setOnClickListener(v->{
            Intent intent = new Intent(this, GalleryGridPicturesActivity.class);
            intent.putExtra("path","random_upload_pictures");
            intent.putExtra("title","Random Photos");
            startActivity(intent);
        });

        binding.usersGallery.setOnClickListener(v->{
            Intent intent = new Intent(this, GalleryGridPicturesActivity.class);
            intent.putExtra("path","user_upload_pictures");
            intent.putExtra("title","Users Photos");
            startActivity(intent);
        });

        binding.groupGallery.setOnClickListener(v->{
            Intent intent = new Intent(this, GalleryGridPicturesActivity.class);
            intent.putExtra("path","group_upload_pictures");
            intent.putExtra("title","Group Photos");
            startActivity(intent);
        });




        adapterGroupPicture = new ImageAdapter(this, uploadsGroupPicture);
        adapterRandomPicture = new ImageAdapter(this, uploadsRandomPicture);
        adapterUserPicture = new ImageAdapter(this, uploadsUserPicture);

        binding.recyclerGroupPicture.addItemDecoration(new ItemOffsetDecoration((int) getResources().getDimension(R.dimen.photos_adapter_offset_10dp)));
        binding.recyclerRandomPicture.addItemDecoration(new ItemOffsetDecoration((int) getResources().getDimension(R.dimen.photos_adapter_offset_10dp)));
        binding.recyclerUserPicture.addItemDecoration(new ItemOffsetDecoration((int) getResources().getDimension(R.dimen.photos_adapter_offset_10dp)));

        binding.recyclerGroupPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.recyclerRandomPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.recyclerUserPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        databaseUserReference = FirebaseDatabase.getInstance().getReference("user_upload_pictures");
        databaseRandomReference = FirebaseDatabase.getInstance().getReference("random_upload_pictures");
        databaseGroupReference = FirebaseDatabase.getInstance().getReference("group_upload_pictures");

        binding.recyclerGroupPicture.setAdapter(adapterGroupPicture);
        binding.recyclerRandomPicture.setAdapter(adapterRandomPicture);
        binding.recyclerUserPicture.setAdapter(adapterUserPicture);


        databaseRandomReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Upload upload = dataSnapshot.getValue(Upload.class);
                uploadsRandomPicture.add(upload);
                Log.d("AlexDebug", upload.getImageUrl());
                adapterRandomPicture.notifyDataSetChanged();
                //binding.recyclerRandomPicture.smoothScrollToPosition(uploadsRandomPicture.size());
                binding.progressRandomPhotos.setVisibility(View.GONE);
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

        databaseUserReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Upload upload = dataSnapshot.getValue(Upload.class);
                uploadsUserPicture.add(upload);
                Log.d("AlexDebug", upload.getImageUrl());
                adapterUserPicture.notifyDataSetChanged();
                //binding.recyclerRandomPicture.smoothScrollToPosition(uploadsRandomPicture.size());
                binding.progressUsersPhotos.setVisibility(View.GONE);
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

        databaseGroupReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Upload upload = dataSnapshot.getValue(Upload.class);
                uploadsGroupPicture.add(upload);
                Log.d("AlexDebug", upload.getImageUrl());
                adapterGroupPicture.notifyDataSetChanged();
                //binding.recyclerRandomPicture.smoothScrollToPosition(uploadsRandomPicture.size());
                binding.progressGroupPhotos.setVisibility(View.GONE);
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