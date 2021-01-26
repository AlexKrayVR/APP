package com.alex.vr_party_app.reg_log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.vr_party_app.MainActivity;
import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityLoginBinding;
import com.alex.vr_party_app.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, email, password, phone;

    private FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    DatabaseReference mDatabaseReference;

    String userId;
    String codeSent;
    PhoneAuthProvider.ForceResendingToken token;


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private ActivityRegisterBinding binding;
    androidx.appcompat.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);

        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();


        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                //String userPhone = phone.getText().toString().trim();
                String username = userName.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    email.setError("Email is Required.");
                    return;
                }

//                if (TextUtils.isEmpty(userPhone)) {
//                    phone.setError("Phone is Required.");
//                    phone.requestFocus();
//                    return;
//                }
//
//                if (userPhone.length() < 10) {
//                    phone.setError("Enter a valid phone.");
//                    phone.requestFocus();
//                    return;
//                }

                if (TextUtils.isEmpty(username)) {
                    userName.setError("UserName is Required.");
                    userName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is Required.");
                    password.requestFocus();
                    return;
                }

                if (!validateEmail(mail)) {
                    email.setError("Email is wrong.");
                    return;
                }

                if (pass.length() < 6) {
                    password.setError("Password must be more then 6 characters");
                    password.requestFocus();
                    return;
                }
                binding.progress.setVisibility(View.VISIBLE);
                Log.d("AlexDebug", "startverify");

                //sendVerificationCode(userPhone);
                //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, phone.getText().toString());
                //signInWithPhoneAuthCredential(credential);

                register(mail, pass);
            }
        });


    }

    private void sendVerificationCode(String userPhone) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(userPhone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.d("AlexDebug", "onVerificationFailed: ", e.fillInStackTrace());
                                binding.progress.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, getText(R.string.error_register) + e.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                               // super.onCodeSent(s, forceResendingToken);
                                codeSent = s;
                                token = forceResendingToken;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AlexDebug", "signInWithPhoneAuthCredential:success");
                            //FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(RegisterActivity.this, getText(R.string.signUpSuccess), Toast.LENGTH_SHORT).show();

                            userId = mAuth.getCurrentUser().getUid();

                            mDatabaseReference = FirebaseDatabase.getInstance().getReference("UsersStore").child(userId);
                            HashMap<String, String> hashMap = new HashMap<>();

                            hashMap.put("id", userId);
                            hashMap.put("imageURL", "default");
                            hashMap.put("userName", userName.getText().toString());

                            Log.d("AlexDebug", "id " + userId);
                            Log.d("AlexDebug", "userName " + userName.getText().toString());

                            mDatabaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        binding.progress.setVisibility(View.GONE);
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });




                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d("AlexDebug", "signInWithCredential:failure", task.getException());
                            binding.progress.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, getText(R.string.error_register) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(RegisterActivity.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
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


    //register user if pass and email correct
    private void register(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //FirebaseUser fUser = mAuth.getCurrentUser();
                    Log.d("AlexDebug", "createUserWithEmail:success");
                    Toast.makeText(RegisterActivity.this, getText(R.string.signUpSuccess), Toast.LENGTH_SHORT).show();

                    userId = mAuth.getCurrentUser().getUid();
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("id", userId);
                    hashMap.put("imageURL", "default");
                    hashMap.put("userName", userName.getText().toString());

                    Log.d("AlexDebug", "id " + userId);
                    Log.d("AlexDebug", "userName " + userName.getText().toString());

                    mDatabaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    Log.d("AlexDebug", "createUserWithEmail:failure", task.getException());
                    binding.progress.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, getText(R.string.error_register) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * validate email addresses
     *
     * @param emailStr - email
     * @return true if validate
     */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
