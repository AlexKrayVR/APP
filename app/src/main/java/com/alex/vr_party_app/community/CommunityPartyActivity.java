package com.alex.vr_party_app.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityCommunityPartyBinding;

import java.util.ArrayList;

public class CommunityPartyActivity extends AppCompatActivity {

    ActivityCommunityPartyBinding binding;
    Toolbar toolbar;
    private ArrayList<CommunityPartyClass> listCommunity = new ArrayList<>();
    CommunityPartyAdapter communityPartyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityPartyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.communities));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //linearLayoutManager.setStackFromEnd(true);
        binding.recyclerCommunity.setLayoutManager(new LinearLayoutManager(this));

        //fill list of Community data from CommunityConstants.class
        for (int i = 0; i < CommunityConstants.mListNames.size(); i++) {
            CommunityPartyClass temp = new CommunityPartyClass(
                    CommunityConstants.mListNames.get(i),
                    CommunityConstants.mListLinks.get(i),
                    CommunityConstants.mListAllPicture.get(i),
                    CommunityConstants.mListIcon.get(i));
            listCommunity.add(temp);
        }
        communityPartyAdapter = new CommunityPartyAdapter(this, listCommunity);
        binding.recyclerCommunity.setAdapter(communityPartyAdapter);
//Picasso.with(activity).load(url).transform(new CircleTransform()).into(imageView);
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