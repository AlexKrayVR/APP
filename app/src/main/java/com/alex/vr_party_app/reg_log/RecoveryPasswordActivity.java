package com.alex.vr_party_app.reg_log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityRecoveryPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RecoveryPasswordActivity extends AppCompatActivity {
    private ActivityRecoveryPasswordBinding binding;
    private FirebaseAuth auth;

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecoveryPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.PasswordRecovery));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.recover.setOnClickListener(v -> {
            String email = binding.mail.getText().toString();

            if (TextUtils.isEmpty(email)) {
                binding.mail.setError(getText(R.string.emailIsRequired));
                return;
            }

            if (!LoginActivity.validateEmail(email)) {
                binding.mail.setError(getText(R.string.emailIsWrong));
                return;
            }

            auth.sendPasswordResetEmail(email).addOnSuccessListener(aVoid -> {
                Log.d("AlexDebug", "Success change password");
                binding.resultOfPasswordChanged.setText(getText(R.string.CheckYourEmailForPasswordChanged));
                //Toast.makeText(RecoveryPasswordActivity.this, getText(R.string.CheckYourEmailForPasswordChanged), Toast.LENGTH_LONG).show();
                StartHandler();
            }).addOnFailureListener(e -> {
                Log.d("AlexDebug", "Error change password" + e.getMessage().toString());
                binding.resultOfPasswordChanged.setText(getText(R.string.ErrorChangePassword));
                StartHandler();
                //Toast.makeText(RecoveryPasswordActivity.this, getText(R.string.ErrorChangePassword) + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        });
    }

    private void StartHandler(){
        new Handler(Looper.getMainLooper()) {{
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.resultOfPasswordChanged.setText("");
                }
            }, 2000);
        }};
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