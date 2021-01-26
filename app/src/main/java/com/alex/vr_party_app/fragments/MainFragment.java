package com.alex.vr_party_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.vr_party_app.editor.Editor;
import com.alex.vr_party_app.editor.PartyEditorActivity;
import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.FragmentMainBinding;
import com.alex.vr_party_app.info.InfoActivity;
import com.alex.vr_party_app.schedule.PartySchedule;
import com.alex.vr_party_app.settings.SettingsActivity;
import com.alex.vr_party_app.support.ScreenDimensions;
import com.alex.vr_party_app.user.ProfileActivity;
import com.alex.vr_party_app.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MainFragment extends Fragment implements View.OnClickListener {

    private FragmentMainBinding binding;

    //add views in array for easy filling
    ArrayList<TextView> listTextView;
    ArrayList<ConstraintLayout> listConstraintLayout;
    ArrayList<ProgressBar> listProgressBar;
    ArrayList<LinearLayout> listLinearLayoutIcons;

    //Toolbar toolbar;
    HashMap<String, Integer> mapPartyIcons = new HashMap<>();

    //for intent
    public static String DAY = "day";
    ArrayList<String> listDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    Editor admin;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null || user.getImageURL().equals("default")) {
                    //Picasso.get().load(R.drawable.candy).resize(200, 200).centerCrop().into(binding.profileImage);
                } else {
                    Picasso.get().load(user.getImageURL()).resize(200, 200).centerCrop().into(binding.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkUserAdmin();

        fillMapPartyIcons();
        fillListConstraintLayout();
        fillListProgressBar();
        fillListLinearLayoutIcons();

        setDaysOfWeek();

        //setPartyIcon();

    }

    private void checkUserAdmin() {
        Log.d("AlexDebug", "Current user id: " + firebaseUser.getUid());

        FirebaseDatabase.getInstance().getReference("admins").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Editor editor = dataSnapshot.getValue(Editor.class);
                Log.d("AlexDebug", "getId: " + editor.getId());
                Log.d("AlexDebug", "getEditor: " + editor.getEditor());
                if (editor.getId().equals(firebaseUser.getUid())) {
                    binding.editor.setVisibility(View.VISIBLE);
                    admin = editor;
                }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        binding.info.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), InfoActivity.class));
        });

        binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SettingsActivity.class));
        });

        binding.profileImage.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProfileActivity.class));
        });

        //editor
        binding.editor.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PartyEditorActivity.class);
            intent.putExtra("admin", admin);
            startActivity(intent);
        });

        return binding.getRoot();
    }


    private void fillListLinearLayoutIcons() {
        listLinearLayoutIcons = new ArrayList<>(Arrays.asList(
                binding.mondayImageLayout,
                binding.tuesdayImageLayout,
                binding.wednesdayImageLayout,
                binding.thursdayImageLayout,
                binding.fridayImageLayout,
                binding.saturdayImageLayout,
                binding.sundayImageLayout));
    }

    private void fillListProgressBar() {
        listProgressBar = new ArrayList<>(Arrays.asList(
                binding.progressMonday,
                binding.progressTuesday,
                binding.progressWednesday,
                binding.progressThursday,
                binding.progressFriday,
                binding.progressSaturday,
                binding.progressSunday));
    }

    private void fillListConstraintLayout() {
        listConstraintLayout = new ArrayList<>(Arrays.asList(
                binding.mondayLayout,
                binding.tuesdayLayout,
                binding.wednesdayLayout,
                binding.thursdayLayout,
                binding.fridayLayout,
                binding.saturdayLayout,
                binding.sundayLayout));
        for (int i = 0; i < 7; i++) {
            listConstraintLayout.get(i).setOnClickListener(this);
        }
    }

    private void fillMapPartyIcons() {
        mapPartyIcons.put("DDVR", R.drawable.ddvr_new);
        mapPartyIcons.put("Addiction", R.drawable.addiction);
        mapPartyIcons.put("Elysium", R.drawable.elysium);
        mapPartyIcons.put("MVP", R.drawable.mvp);
        mapPartyIcons.put("Rizumu", R.drawable.rizumu);
        mapPartyIcons.put("Just Party", R.drawable.just_party);
        mapPartyIcons.put("Groovr Events", R.drawable.groovr);
        mapPartyIcons.put("Loli Squad", R.drawable.loli_squad);
        mapPartyIcons.put("Unknown", R.drawable.question);
        //mapPartyIcons.put("DDVRizumu ", R.drawable.question);
    }

    private void setDaysOfWeek() {
        listTextView = new ArrayList<>(Arrays.asList(
                binding.mondayDay,
                binding.tuesdayDay,
                binding.wednesdayDay,
                binding.thursdayDay,
                binding.fridayDay,
                binding.saturdayDay,
                binding.sundayDay));

        String[] dayOfWeeks = new String[]{
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday),
                getString(R.string.saturday),
                getString(R.string.sunday)
        };
        Calendar printedDate = GregorianCalendar.getInstance();
        Log.d("AlexDebug", "printedDate:" + printedDate.get(Calendar.DAY_OF_WEEK));
        Log.d("AlexDebug", "printedMonth" + printedDate.get(Calendar.DAY_OF_MONTH));
        if (printedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            printedDate.set(Calendar.DAY_OF_MONTH, printedDate.get(Calendar.DAY_OF_MONTH) - 6);
        } else {
            printedDate.set(Calendar.DAY_OF_MONTH, printedDate.get(Calendar.DAY_OF_MONTH) - printedDate.get(Calendar.DAY_OF_WEEK) + 2);
        }
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Calendar currentDate = GregorianCalendar.getInstance();
        for (int i = 0; i < 7; i++) {
            if (currentDate.get(Calendar.DAY_OF_MONTH) == printedDate.get(Calendar.DAY_OF_MONTH)) {
                listTextView.get(i).setTextColor(getResources().getColor(R.color.menuTextColor));
            }
            Log.d("AlexDebug", "DAY_OF_MONTH:" + i + " " + printedDate.get(Calendar.DAY_OF_MONTH));
            Log.d("AlexDebug", "DAY_OF_WEEK:" + i + " " + printedDate.get(Calendar.DAY_OF_WEEK));
            listTextView.get(i).setText(dayOfWeeks[i] + " " + df.format(printedDate.getTime()));
            printedDate.set(Calendar.HOUR, 24);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setPartyIcon();
    }

    private void setPartyIcon() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (int i = 0; i < 7; i++) {
            final int finalI = i;
            db.collection("VR_PARTY").document(listDays.get(i)).collection("Parties")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() == 0) {
                                    listProgressBar.get(finalI).setVisibility(View.GONE);
                                    // listAnnounce.get(finalI).setVisibility(View.VISIBLE);
                                } else {
                                    ArrayList<String> partyList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String name = document.getData().get("Community").toString();
                                        partyList.add(name);
                                    }
                                    setLogosInCards(partyList, finalI);
                                }
                            } else {
                                Log.e("AlexDebug", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

    private void setLogosInCards(ArrayList<String> partyList, int position) {
        listLinearLayoutIcons.get(position).removeAllViews();

        for (int j = 0; j < partyList.size(); j++) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ScreenDimensions screenDimension = new ScreenDimensions(getActivity());
            int left = (int) (8 * screenDimension.getScreenDensity());
            lp.setMargins(left, 0, 0, 0);
            imageView.setLayoutParams(lp);
            imageView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            imageView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            listProgressBar.get(position).setVisibility(View.GONE);
            imageView.setAdjustViewBounds(true);
            if (mapPartyIcons.get(partyList.get(j)) == null) {
                imageView.setImageResource(mapPartyIcons.get("Unknown"));
            } else {
                imageView.setImageResource(mapPartyIcons.get(partyList.get(j)));
            }
            listLinearLayoutIcons.get(position).addView(imageView);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), PartySchedule.class);
        switch (v.getId()) {
            case R.id.mondayLayout:
                intent.putExtra(DAY, listDays.get(0));
                startActivity(intent);
                break;
            case R.id.tuesdayLayout:
                intent.putExtra(DAY, listDays.get(1));
                startActivity(intent);
                break;
            case R.id.wednesdayLayout:
                intent.putExtra(DAY, listDays.get(2));
                startActivity(intent);
                break;
            case R.id.thursdayLayout:
                intent.putExtra(DAY, listDays.get(3));
                startActivity(intent);
                break;
            case R.id.fridayLayout:
                intent.putExtra(DAY, listDays.get(4));
                startActivity(intent);
                break;
            case R.id.saturdayLayout:
                intent.putExtra(DAY, listDays.get(5));
                startActivity(intent);
                break;
            case R.id.sundayLayout:
                intent.putExtra(DAY, listDays.get(6));
                startActivity(intent);
                break;
        }
    }
}