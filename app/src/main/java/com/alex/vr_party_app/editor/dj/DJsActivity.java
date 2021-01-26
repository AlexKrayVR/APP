package com.alex.vr_party_app.editor.dj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityDJsBinding;
import com.alex.vr_party_app.editor.dj.DJ;
import com.alex.vr_party_app.editor.dj.DJsAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DJsActivity extends AppCompatActivity {

    ActivityDJsBinding binding;
    DJsAdapter djsAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDJsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(binding.toolbar.getRoot().getId());
        toolbar.setTitle("DJs");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillRecycler(getIntent().getParcelableArrayListExtra("djs"));
    }

    private void fillRecycler(ArrayList<DJ> djs) {

        djsAdapter = new DJsAdapter(this, djs);
        djsAdapter.setListener(dj -> {
            Log.d("AlexDebug", "djName: " + dj.nameDJ);
            Intent intent = new Intent();
            intent.putExtra("DJ", dj);
            setResult(RESULT_OK, intent);
            finish();
        });

        binding.recyclerDJs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerDJs.setAdapter(djsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                djsAdapter.getFilter().filter(newText);
                binding.recyclerDJs.scrollToPosition(0);

                Log.d("AlexDebug", "newText: " + newText);

                //countryAdapter.setQuery(newText.toLowerCase().trim());
                //countryAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
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