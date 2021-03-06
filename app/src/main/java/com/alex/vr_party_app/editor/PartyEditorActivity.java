package com.alex.vr_party_app.editor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityPartyEditorBinding;
import com.alex.vr_party_app.editor.dj.DJ;
import com.alex.vr_party_app.editor.dj.DJsActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PartyEditorActivity extends AppCompatActivity {

    ArrayList<String> listDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));

    ActivityPartyEditorBinding binding;
    Toolbar toolbar;

    int startPartyHour = 0, startPartyMinute = 0;
    String countDjsHours = "00", countDjsMinutes = "00";
    Double djTime = 0.0;

    TimePickerCustom timePickerDialog;
    TimePickerCustomDate timePickerDialogDate;

    Resources system;

    String partyDay = "Monday";
    String partyDate = "";
    String partyCommunity = "DDVR";
    String DJ = "";
    public static final int DJ_REQUEST_CODE = 5;

    ArrayList<DJ> djsFullList = new ArrayList<>();

    DJsAdapterList djsAdapter;

    DJ chosenDJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartyEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        system = Resources.getSystem();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.partyEditor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding();

        getAllDjs();
        fillRecycler();

        setAdminSettings();
    }

    private void binding() {
        bindingSpinnerDay();
        bindingTimePickers();
        bindingAddDj();
        binding.deleteAll.setOnClickListener(v -> getDocumentForDelete());
        binding.searchDJ.setOnClickListener(v -> {
            Intent intent = new Intent(PartyEditorActivity.this, DJsActivity.class);
            intent.putParcelableArrayListExtra("djs", djsFullList);
            startActivityForResult(intent, DJ_REQUEST_CODE);
        });
        binding.apply.setOnClickListener(v -> sendToServer());
    }

    private void setAdminSettings() {
        Editor admin = getIntent().getParcelableExtra("admin");
        if (admin != null) {
            Log.d("AlexDebug", "admin: " + admin.toString());
            if (admin.getId().equals("NckZzlHsYAcKmJhPeSakULZvRbZ2")) {
                binding.deleteAll.setVisibility(View.VISIBLE);
            }
            if (admin.getEditor().equals("all")) {
                binding.spinnerChoosePartyCommunity.setVisibility(View.VISIBLE);
                bindingSpinnerPartyCommunity();
            } else {
                partyCommunity = admin.getEditor();
                binding.communityName.setText(partyCommunity);
            }
        }
    }

    private void bindingSpinnerPartyCommunity() {
        final String[] choosePartyCommunity = getResources().getStringArray(R.array.partyCommunity);
        ArrayAdapter<String> adapterPartyCommunity = new ArrayAdapter<>(this,
                R.layout.spinner_dropdown_custom, R.id.holder, choosePartyCommunity);
        binding.spinnerChoosePartyCommunity.setAdapter(adapterPartyCommunity);
        binding.spinnerChoosePartyCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("AlexDebug", "choosePartyCommunity: " + choosePartyCommunity[i]);
                partyCommunity = choosePartyCommunity[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void sendToServer() {
        if (binding.timeHolder.getText().toString().isEmpty()) {
            Toast.makeText(this, getText(R.string.partyEditorChooseTime), Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.dateHolder.getText().toString().isEmpty()) {
            Toast.makeText(this, getText(R.string.partyEditorChooseDate), Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(binding.dateHolder.getText().toString()) > 31) {
            Toast.makeText(this, getText(R.string.partyEditorChooseCorrectDate), Toast.LENGTH_SHORT).show();
            return;
        }
        if (djsAdapter.getDjsList().size() == 0) {
            Toast.makeText(this, getText(R.string.partyEditorAddAtLeastOneDJ), Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        //partyDate = String.valueOf(dateChosen);

        String date = binding.dateHolder.getText().toString().trim();
        //String date = partyDate;
        //String month = "June";
        String name = partyCommunity;
        String start_hours = String.valueOf(startPartyHour);
        String start_minutes = String.valueOf(startPartyMinute);

        ArrayList<String> listDJNames = new ArrayList<>();
        ArrayList<Double> listDJHours = new ArrayList<>();

        for (DJ dj : djsAdapter.getDjsList()) {
            listDJNames.add(dj.getNameDJ());
            int tempInt = (int) Double.parseDouble(dj.getHours());
            double tempDouble = Double.parseDouble(dj.getHours());
            if ((double) tempInt == tempDouble) {
                listDJHours.add(Double.parseDouble(dj.getHours()));
            } else {
                listDJHours.add(Double.parseDouble(dj.getHours()) + 0.2);
            }
        }

        data.put("Date", date);
        data.put("Start_hours", start_hours);
        data.put("Start_minutes", start_minutes);
        data.put("Community", name);
        data.put("DJs_name", listDJNames);
        data.put("DJs_hours", listDJHours);

        Log.d("AlexDebug", "data: " + data.toString());

        binding.timeHolder.setText("");
        binding.dateHolder.setText("");
        djsAdapter.clearDjsList();

        db.collection("VR_PARTY").document(partyDay).collection("Parties").document(name).set(data);
    }

    private void bindingAddDj() {
        binding.addDJ.setOnClickListener(v -> {
            if (binding.nameDJ.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, getText(R.string.partyEditorFullNameOfDJ), Toast.LENGTH_SHORT).show();
            } else if (binding.djHours.getText().toString().isEmpty()) {
                Toast.makeText(this, getText(R.string.partyEditorFullHoursOfDJ), Toast.LENGTH_SHORT).show();
            } else {
                if (chosenDJ == null) {
                    chosenDJ = new DJ("",
                            binding.nameDJ.getText().toString().trim(),
                            binding.djHours.getText().toString());
                } else {
                    chosenDJ.setHours(binding.djHours.getText().toString());
                }
                djsAdapter.addDj(chosenDJ);
                binding.djHours.setText("");
                binding.nameDJ.setText("");
                chosenDJ = null;
            }
        });
    }

    private void fillRecycler() {
        djsAdapter = new DJsAdapterList(this);
        binding.recyclerDJs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerDJs.setAdapter(djsAdapter);
    }


    private void getAllDjs() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DJs");
        reference.addChildEventListener(new ChildEventListener() {
            //вызывается когда происходит добавление нового сообщения в базу
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DJ dj = dataSnapshot.getValue(DJ.class);
                if (dj != null) {
                    //Log.d("AlexDebug", "name: " + dj.getNameDJ());
                    //Log.d("AlexDebug", "link: " + dj.getLink());
                    djsFullList.add(dj);
                } else {
                    Log.d("AlexDebug", "Method getAllDjs() - dj is null");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == DJ_REQUEST_CODE) {
            chosenDJ = data.getParcelableExtra("DJ");
            if (chosenDJ != null) {
                Log.d("AlexDebug", "DJ: " + chosenDJ.getNameDJ());
                binding.nameDJ.setText(chosenDJ.getNameDJ());
            }
        }
    }

    private void bindingTimePickers() {
        binding.djHours.setOnClickListener((v) -> {
            timePickerDialog = new TimePickerCustom(
                    PartyEditorActivity.this,
                    //TimePickerDialog.THEME_HOLO_LIGHT,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                            countDjsHours = String.valueOf(hours);
                            countDjsMinutes = String.valueOf(minutes);
                            if (minutes == 60 || minutes == 0) {
                                countDjsMinutes = "00";
                            }

                            Log.d("AlexDebug", "countDjsHours: " + countDjsHours);
                            Log.d("AlexDebug", "countDjsMinutes: " + countDjsMinutes);
                            if (!countDjsMinutes.equals("00") || !countDjsHours.equals("0")) {
                                binding.djHours.setText(String.format("%s.%s", countDjsHours, countDjsMinutes));
                                //djTime += countDjsHours;
                                //djTime += (countDjsMinutes == 0 ? 0 : 0.5);
                                //Log.d("AlexDebug", "djTime: " + djTime);
                            }

                        }
                    },
                    0,
                    0,
                    true
            );
            timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getText(R.string.partyEditorOk), timePickerDialog);
            timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getText(R.string.partyEditorCancel), timePickerDialog);
            timePickerDialog.show();
        });


//        binding.dateHolder.setOnClickListener((v) -> {
//            timePickerDialogDate = new TimePickerCustomDate(
//                    PartyEditorActivity.this,
//                    //TimePickerDialog.THEME_HOLO_LIGHT,
//                    (TimePicker timePicker, int dateChosen, int dateZero) -> {
//                        partyDate = String.valueOf(dateChosen);
//                        Log.d("AlexDebug", "date: " + partyDate);
//                        binding.dateHolder.setText(String.format("%02d", dateChosen));
//                    },
//                    0,
//                    0,
//                    true
//            );
//
//            timePickerDialogDate.setButton(DialogInterface.BUTTON_POSITIVE, getText(R.string.partyEditorOk), timePickerDialogDate);
//            timePickerDialogDate.setButton(DialogInterface.BUTTON_NEGATIVE, getText(R.string.partyEditorCancel), timePickerDialogDate);
//            timePickerDialogDate.show();
//        });


        binding.timeHolder.setOnClickListener((v) -> {
            timePickerDialog = new TimePickerCustom(
                    PartyEditorActivity.this,
                    //TimePickerDialog.THEME_HOLO_LIGHT,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                            startPartyHour = hours;
                            startPartyMinute = minutes;
                            if (startPartyMinute == 60) {
                                startPartyMinute = 0;
                            }
                            Log.d("AlexDebug", "hour1: " + startPartyHour);
                            Log.d("AlexDebug", "minute1: " + startPartyMinute);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(0, 0, 0, startPartyHour, startPartyMinute);
                            binding.timeHolder.setText(DateFormat.format("HH:mm", calendar));
                        }
                    },
                    0,
                    0,
                    true
            );
            timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getText(R.string.partyEditorOk), timePickerDialog);
            timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getText(R.string.partyEditorCancel), timePickerDialog);
            timePickerDialog.show();
        });
    }

    private void bindingSpinnerDay() {

        final String[] chooseDay = getResources().getStringArray(R.array.daysOfWeek);
        ArrayAdapter<String> adapterDays = new ArrayAdapter<String>(this,
                R.layout.spinner_dropdown_custom, R.id.holder, chooseDay);
        binding.spinnerChooseDay.setAdapter(adapterDays);
        binding.spinnerChooseDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("AlexDebug", "chooseDay: " + chooseDay[i]);
                partyDay = chooseDay[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

    //search all documents in the FirebaseFirestore for delete
    private void getDocumentForDelete() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (int i = 0; i < listDays.size(); i++) {
            final int finalI = i;
            db.collection("VR_PARTY").document(listDays.get(i)).collection("Parties")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                if (task.getResult().size() == 0) {
                                    Log.d("AlexDebug", "empty");
                                } else {
                                    ArrayList<String> partyList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String name = (String) document.getData().get("Community");
                                        partyList.add(name);
                                    }
                                    deletePartyWeek(partyList, listDays.get(finalI));
                                }
                            } else {
                                Log.d("AlexDebug", "task.getResult() == null");
                            }
                        } else {
                            Log.d("AlexDebug", "task is not successful()");
                        }
                    });
        }
    }

    //delete a document by day in the FirebaseFirestore
    //method is calling by getDocumentForDelete()
    private void deletePartyWeek(ArrayList<String> partyList, String day) {
        for (int i = 0; i < partyList.size(); i++) {
            DocumentReference ref = FirebaseFirestore.getInstance().collection("VR_PARTY").document(day).collection("Parties").document(partyList.get(i));
            Log.d("AlexDebug", "partyList: " + partyList.toString() + "\nday: " + day);
            ref.delete();
        }
        Toast.makeText(this, "All parties have been deleted", Toast.LENGTH_LONG).show();
    }


//    @SuppressLint("LongLogTag")
//    private void set_numberpicker_text_colour(NumberPicker number_picker) {
//        final int count = number_picker.getChildCount();
//        final int color = getResources().getColor(R.color.white);
//
//        for (int i = 0; i < count; i++) {
//            View child = number_picker.getChildAt(i);
//
//            try {
//                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
//                wheelpaint_field.setAccessible(true);
//
//                ((Paint) wheelpaint_field.get(number_picker)).setColor(color);
//                ((EditText) child).setTextColor(color);
//                number_picker.invalidate();
//            } catch (NoSuchFieldException e) {
//                Log.w("setNumberPickerTextColor", e);
//            } catch (IllegalAccessException e) {
//                Log.w("setNumberPickerTextColor", e);
//            } catch (IllegalArgumentException e) {
//                Log.w("setNumberPickerTextColor", e);
//            }
//        }
//    }

}