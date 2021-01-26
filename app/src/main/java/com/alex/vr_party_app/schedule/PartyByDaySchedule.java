package com.alex.vr_party_app.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityPartyByDayScheduleBinding;
import com.alex.vr_party_app.support.ScreenDimensions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class PartyByDaySchedule extends AppCompatActivity implements View.OnClickListener {
    ActivityPartyByDayScheduleBinding binding;

    //add views in array for easy filling
    ArrayList<TextView> listTextView;
    ArrayList<ConstraintLayout> listConstraintLayout;
    ArrayList<ProgressBar> listProgressBar;
    ArrayList<LinearLayout> listLinearLayoutIcons;

    //to set space between party icons
    ScreenDimensions screenDimension = new ScreenDimensions(this);

    //Toolbar toolbar;
    HashMap<String, Integer> mapPartyIcons = new HashMap<>();

    //for intent
    public static String DAY = "day";
    ArrayList<String> listDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartyByDayScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.schedule));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillMapPartyIcons();
        fillListConstraintLayout();
        fillListProgressBar();
        fillListLinearLayoutIcons();

        setDaysOfWeek();

        setPartyIcon();


    }

    private void fillListLinearLayoutIcons() {
        listLinearLayoutIcons = new ArrayList<>(Arrays.asList(
                binding.mondayImageLayout,
                binding.thursdayImageLayout,
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
        mapPartyIcons.put("Interconnection", R.drawable.interconnection);
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
        for (int j = 0; j < partyList.size(); j++) {
            ImageView imageView = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PartyByDaySchedule.this, PartySchedule.class);
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