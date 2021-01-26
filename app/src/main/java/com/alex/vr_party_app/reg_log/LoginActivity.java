package com.alex.vr_party_app.reg_log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.vr_party_app.MainActivity;
import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private ActivityLoginBinding binding;

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.login.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.loginTitle));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.forgotPassword.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * validate email addresses
     * */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void login(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("AlexDebug", "Authenticate the user: success");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Log.e("AlexDebug", "Authenticate the user: failed", task.getException());
                    binding.progressBarLogin.setVisibility(View.GONE);
                    //Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String userEmail = binding.mail.getText().toString().trim();
                String userPassword = binding.pass.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail)) {
                    binding.mail.setError(getText(R.string.emailIsRequired));
                    return;
                }
                if (TextUtils.isEmpty(userPassword)) {
                    binding.pass.setError(getText(R.string.passwordIsRequired));
                    return;
                }
                if (!validateEmail(userEmail)) {
                    binding.mail.setError(getText(R.string.emailIsWrong));
                    return;
                }
                if (userPassword.length() < 6) {
                    binding.pass.setError(getText(R.string.passwordMoreThen6Characters));
                    return;
                }
                binding.progressBarLogin.setVisibility(View.VISIBLE);
                login(userEmail, userPassword);
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, RecoveryPasswordActivity.class));
                break;
        }
    }



}
