package com.alex.vr_party_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.community.CommunityConstants;
import com.alex.vr_party_app.community.CommunityPartyAdapter;
import com.alex.vr_party_app.community.CommunityPartyClass;
import com.alex.vr_party_app.databinding.FragmentClubsBinding;
import com.alex.vr_party_app.databinding.FragmentGalleryBinding;

import java.util.ArrayList;

public class ClubsFragment extends Fragment {

    FragmentClubsBinding binding;
    private ArrayList<CommunityPartyClass> listCommunity = new ArrayList<>();
    CommunityPartyAdapter communityPartyAdapter;
    public ClubsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClubsBinding.inflate(inflater, container, false);
        binding.recyclerCommunity.setLayoutManager(new LinearLayoutManager(getContext()));
        //fill list of Community data from CommunityConstants.class
        for (int i = 0; i < CommunityConstants.mListNames.size(); i++) {
            CommunityPartyClass temp = new CommunityPartyClass(
                    CommunityConstants.mListNames.get(i),
                    CommunityConstants.mListLinks.get(i),
                    CommunityConstants.mListAllPicture.get(i),
                    CommunityConstants.mListIcon.get(i));
            listCommunity.add(temp);
        }
        communityPartyAdapter = new CommunityPartyAdapter(getContext(), listCommunity);
        binding.recyclerCommunity.setAdapter(communityPartyAdapter);

        return binding.getRoot();
    }
}