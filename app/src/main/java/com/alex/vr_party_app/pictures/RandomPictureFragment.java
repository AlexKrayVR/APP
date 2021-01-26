package com.alex.vr_party_app.pictures;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.gallery.ImageAdapter;
import com.alex.vr_party_app.gallery.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class RandomPictureFragment extends Fragment {

    private RecyclerView recycler_view_random_picture;
    private ProgressBar progress, progress_upload_image;
    private FloatingActionButton add_picture;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    CardView choose, cancel, upload;
    LinearLayout upload_image_layout;
    ImageView image;

    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Upload> mUploads;
    private StorageReference mStorageRef;

    public RandomPictureFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mUploads == null) {
            mUploads = new ArrayList<>();
            mAdapter = new ImageAdapter(getContext(), mUploads);
            recycler_view_random_picture.setAdapter(mAdapter);
            getRandomPictures();
        }else {
            recycler_view_random_picture.setAdapter(mAdapter);
            progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_picture, container, false);
        recycler_view_random_picture = view.findViewById(R.id.recycler_view_group_picture);
        progress = view.findViewById(R.id.progress);
        progress_upload_image = view.findViewById(R.id.progress_upload_image);
        choose = view.findViewById(R.id.choose);
        cancel = view.findViewById(R.id.cancel);
        upload = view.findViewById(R.id.upload);
        image = view.findViewById(R.id.image);
        add_picture = view.findViewById(R.id.addPictureHolder);
        upload_image_layout = view.findViewById(R.id.upload_image_layout);
        recycler_view_random_picture.setLayoutManager(new StaggeredGridLayoutManager(3, 1));

        mStorageRef = FirebaseStorage.getInstance().getReference("random_upload_pictures");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("random_upload_pictures");

        add_picture.setOnClickListener(v -> {
            openFileChooser();
            //add_picture.startAnimation(animation_to_hide);
        });

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                add_picture.startAnimation(animation_to_show);
//
//            }
//        });

//        choose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFileChooser();
//            }
//        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d("AlexDebug", "imageUri: " + imageUri.toString());
            //createDialogUploadPicture();
            uploadFile();
            //Picasso.with(getContext()).load(imageUri).resize(0, 800).centerInside().into(image);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void getRandomPictures() {
        Log.d("AlexDebug", "getRandomPictures called");

//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUploads.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Upload upload = postSnapshot.getValue(Upload.class);
//                    mUploads.add(upload);
//                }
//                mAdapter = new ImageAdapter(getContext(), mUploads);
//                recycler_view_group_picture.setAdapter(mAdapter);
//                recycler_view_group_picture.smoothScrollToPosition(mUploads.size());
//                progress.setVisibility(View.INVISIBLE);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("AlexDebug", "databaseError: " + databaseError.getMessage());
//                progress.setVisibility(View.INVISIBLE);
//            }
//        });

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Upload upload = dataSnapshot.getValue(Upload.class);
                mUploads.add(upload);
                mAdapter.notifyDataSetChanged();
                recycler_view_random_picture.smoothScrollToPosition(mUploads.size());
                progress.setVisibility(View.INVISIBLE);
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

    private void createDialogUploadPicture() {
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.dialog_upload_picture, null);

        final CardView choose = dialog.findViewById(R.id.choose);
        final ProgressBar progress = dialog.findViewById(R.id.progress);
        final CardView cancel = dialog.findViewById(R.id.cancel);
        final ImageView image = dialog.findViewById(R.id.image);
        final CardView upload = dialog.findViewById(R.id.upload);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialog);
        final AlertDialog alert = builder.create();
        Picasso.get().load(imageUri).resizeDimen(R.dimen.image_size_width, R.dimen.image_size_height).centerInside().into(image);
        choose.setOnClickListener(v -> {
            alert.cancel();
            openFileChooser();
        });
        cancel.setOnClickListener(v -> alert.cancel());

        upload.setOnClickListener(v -> {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                if (imageUri != null) {
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setProgress(0);
                                }
                            }, 100);
                            //progressBar.setProgress(0);
                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            //Upload upload1 = new Upload("sdf", downloadUrl.toString());
                            String uploadId = mDatabaseReference.push().getKey();
                            //mDatabaseReference.child(uploadId).setValue(upload1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress_time = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progress.setProgress((int) progress_time);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    //Upload upload = new Upload("default", downloadUrl.toString());
                    String uploadId = mDatabaseReference.push().getKey();
                    mDatabaseReference.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
