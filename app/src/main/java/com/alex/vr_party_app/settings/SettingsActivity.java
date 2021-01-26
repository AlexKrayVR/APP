package com.alex.vr_party_app.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivitySettingsBinding;

import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    androidx.appcompat.widget.Toolbar toolbar;

    private static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CHECK_UTC = "CHECK_UTC";
    public static final String APP_PREFERENCES_CHECK_CST = "CHECK_CST";
    public static final String APP_PREFERENCES_CHECK_LOCALE = "CHECK_LOCALE";
    private static SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.Settings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (!settings.contains(APP_PREFERENCES_CHECK_UTC)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(APP_PREFERENCES_CHECK_UTC, true);
            editor.putBoolean(APP_PREFERENCES_CHECK_CST, true);
            editor.putBoolean(APP_PREFERENCES_CHECK_LOCALE, true);
            editor.apply();
            binding.utc.setChecked(true);
            binding.cst.setChecked(true);
            binding.local.setChecked(true);
        } else {
            binding.utc.setChecked(settings.getBoolean(APP_PREFERENCES_CHECK_UTC, true));
            binding.cst.setChecked(settings.getBoolean(APP_PREFERENCES_CHECK_CST, true));
            binding.local.setChecked(settings.getBoolean(APP_PREFERENCES_CHECK_LOCALE, true));
        }


        binding.utc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (binding.utc.isChecked()) {
                settings.edit().putBoolean(APP_PREFERENCES_CHECK_UTC, true).apply();
            } else {
                settings.edit().putBoolean(APP_PREFERENCES_CHECK_UTC, false).apply();
            }
        });

        binding.cst.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (binding.cst.isChecked()) {
                settings.edit().putBoolean(APP_PREFERENCES_CHECK_CST, true).apply();
            } else {
                settings.edit().putBoolean(APP_PREFERENCES_CHECK_CST, false).apply();
            }
        });

        binding.local.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (binding.local.isChecked()) {
                settings.edit().putBoolean(APP_PREFERENCES_CHECK_LOCALE, true).apply();
            } else {
                settings.edit().putBoolean(APP_PREFERENCES_CHECK_LOCALE, false).apply();
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