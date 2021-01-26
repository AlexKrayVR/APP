package com.alex.vr_party_app.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityAddPictureBinding;
import com.alex.vr_party_app.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.GregorianCalendar;

public class AddPictureActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    //private Target mTarget;

    private Uri imageUri;

    private DatabaseReference databaseRef;
    private DatabaseReference userDatabaseReference;

    private StorageReference storageRef;

    //prevents from many clicks on the download button
    private StorageTask uploadTask;

    private ActivityAddPictureBinding binding;

    FirebaseUser firebaseUser;

    String path;
    String title;
    String userName = "";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        path = getIntent().getStringExtra("path");
        title = getIntent().getStringExtra("title");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.UploadTo) + title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.getUserName();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        storageRef = FirebaseStorage.getInstance().getReference(path);
        databaseRef = FirebaseDatabase.getInstance().getReference(path);

//        mTarget = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                if (bitmap == null) {
//                    Log.d("AlexDebug", "Null");
//                } else {
//                    Log.d("AlexDebug", "Worked");
//                }
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                Log.d("AlexDebug", "failed");
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                Log.d("AlexDebug", "Prepare: ");
//            }
//        };


        binding.addPictureHolder.setOnClickListener(v -> openFileChooser());

        binding.uploadPicture.setOnClickListener((View.OnClickListener) v -> {
            //until the picture is uploaded you cannot upload a new one
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(AddPictureActivity.this, getText(R.string.uploadInProgress), Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }
        });


//        Map<String, Object> read = new HashMap<>();
//        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("VRPARTY").document("Monday");
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    Log.d("Alex", "ALL data: " + documentSnapshot.getData());
//
//
//                    Map<String, Object> temp = documentSnapshot.getData();
//                    for (String key : temp.keySet()) {
//                        Log.d("Alex", "key: "+ key);
//                        listParties.add(new String(key));
//                    }
//                }else {
//                    Log.d("Alex", "doesnt exist");
//                }
//                Log.d("Alex", "ALL: " + listParties.toString());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("Alex", e.toString());
//            }
//        });
//
//


//        Map<String, Object> day=new HashMap<>();
//        day.put("Date","23");
//        day.put("Time","23");
//        db.collection("Thuesday").document("Parties").collection("MVP").document("Data").set(day).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(MainActivity.this, "Value added!", Toast.LENGTH_SHORT).show();
//            }
//        });

//        Map<String, Object> status=new HashMap<>();
//        status.put("Status",true);
//        db.collection("Thuesday").document("Parties").set(status).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(MainActivity.this, "Value added!", Toast.LENGTH_SHORT).show();
//            }
//        });

//        DocumentReference reference=FirebaseFirestore.getInstance().collection("Thuesday").document("Parties");
//        reference.update("Status", false);


//        Map<String, Object> day=new HashMap<>();
//        ArrayList<String> names=new ArrayList<>();
//        ArrayList<Double> hours=new ArrayList<>();
//        names.add("DeFreeze");
//        names.add("Tongkii");
//        hours.add(2.5);
//        hours.add(1.5);
//        day.put("Date","13");
//        day.put("Time","20:30");
//        day.put("DJsName",names);
//        day.put("DJsHours",hours);
//
//        db.collection("Wednesday").document("Parties").collection("Just Party").document("Data").set(day).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(MainActivity.this, "Value added!", Toast.LENGTH_SHORT).show();
//            }
//        });


//        Map<String, Object> read2 = new HashMap<>();
//        final ArrayList<String> listParties2 = new ArrayList<>();
//        DocumentReference documentReference2 = FirebaseFirestore.getInstance().collection("Wednesday").document("NamesParties");
//        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc.exists()) {
//                        Log.d("Alex", doc.getData().toString());
//                        Map<String, Object> temp = doc.getData();
//                        for (String key : temp.keySet()) {
//                            Log.d("Alex", key);
//                            listParties2.add(key);
//                        }
//                    } else {
//                        Log.d("Alex", "No data");
//                    }
//                    Log.d("Alex", listParties2.toString());
//
//                }
//            }
//        });
//        Log.d("Alex", "ALL: "+listParties2.toString());


//        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Wednesday").document("Parties").collection("Just Party").document("Data");
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc.exists()) {
//                        Log.d("Alex", doc.getData().toString());
//                        Map<String, Object> temp = doc.getData();
//                        for (String key : temp.keySet()) {
//                            Log.d("Alex", key);
//                            listParties.add(key);
//                        }
//                    } else {
//                        Log.d("Alex", "No data");
//                    }
//                    Log.d("Alex", listParties.toString());
//
//                }
//            }
//        });

    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.addPictureHolder);
            //img.setImageURI(imageUri); //without picasso
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void uploadFile() {
        if (imageUri != null) {
            binding.progressUpload.setVisibility(View.VISIBLE);

            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.progress.setProgress(0);
                        }
                    }, 100);

                    Toast.makeText(AddPictureActivity.this, getText(R.string.uploadSuccessful), Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    Upload upload = new Upload(userName,
                            firebaseUser.getUid(),
                            downloadUrl.toString(),
                            GregorianCalendar.getInstance().getTime().toString());
                    String uploadId = databaseRef.push().getKey();
                    databaseRef.child(uploadId).setValue(upload);
                    binding.progressUpload.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPictureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressUpload.setVisibility(View.GONE);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    binding.progress.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, getText(R.string.noFileSelected), Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
}
