package com.alex.vr_party_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alex.vr_party_app.chat.ChatActivity;
import com.alex.vr_party_app.databinding.ActivityMainBinding;
import com.alex.vr_party_app.fragments.ChatFragment;
import com.alex.vr_party_app.fragments.ClubsFragment;
import com.alex.vr_party_app.fragments.GalleryFragment;
import com.alex.vr_party_app.fragments.MainFragment;
import com.alex.vr_party_app.reg_log.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));

    ActivityMainBinding binding;

    Fragment activeFragment, homeFragment, clubsFragment, chatFragment, galleryFragment;
    private HashMap<String, String> linksTwitchDJs = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);

        initFragments();


//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> data = new HashMap<>();
//
//        String date = "21";
//        //String month = "June";
//        String name = "DDVR";
//        ArrayList<String> listDJNames = new ArrayList<>(Arrays.asList("ShootBang", "SwaMusic","Synthakat","Chrysaora"));
//        ArrayList<Double> listDJHours = new ArrayList<>(Arrays.asList(1.5D, 2.0D, 2.0D, 2.0D));
//        String start_hours = "00";
//        String start_minutes = "00";
//        data.put("Date", date);
//        data.put("Start_hours", start_hours);
//        data.put("Start_minutes", start_minutes);
//        data.put("Community", name);
//        data.put("DJs_name", listDJNames);
//        data.put("DJs_hours", listDJHours);
//        db.collection("VR_PARTY").document("Wednesday").collection("Parties").document(name).set(data);


    }


    private void initFragments() {
        homeFragment = new MainFragment();
        clubsFragment = new ClubsFragment();
        galleryFragment = new GalleryFragment();
        chatFragment = new ChatFragment();
        activeFragment = homeFragment;
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.fragment_container, galleryFragment, "4").
                hide(galleryFragment).
                commit();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.fragment_container, chatFragment, "3").
                hide(chatFragment).
                commit();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.fragment_container, clubsFragment, "2").
                hide(clubsFragment).
                commit();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.fragment_container, homeFragment, "1").
                commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
            closeKeyboard();
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().
                            beginTransaction().
                            hide(activeFragment).
                            show(homeFragment).
                            commit();
                    activeFragment = homeFragment;
                    return true;
                case R.id.gallery:
                    getSupportFragmentManager().
                            beginTransaction().
                            hide(activeFragment).
                            show(galleryFragment).
                            commit();
                    activeFragment = galleryFragment;
                    return true;
                case R.id.chat:
                    getSupportFragmentManager().
                            beginTransaction().
                            hide(activeFragment).
                            show(chatFragment).
                            commit();
                    activeFragment = chatFragment;
                    return true;
                case R.id.clubs:
                    getSupportFragmentManager().
                            beginTransaction().
                            hide(activeFragment).
                            show(clubsFragment).
                            commit();
                    activeFragment = clubsFragment;
                    return true;
            }
            return false;
        }
    };

    private void closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();
        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void createDialogInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogInfo);
        builder.setTitle(getString(R.string.information_main_title));
        String information = getString(R.string.main_information);
        builder.setMessage(information);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

//    private void createDialogSettingsTable() {
//        final LayoutInflater inflater = this.getLayoutInflater();
//        final View dialog = inflater.inflate(R.layout.dialog_settings_table, null);
//        final CheckBox UTC = dialog.findViewById(R.id.utc);
//        UTC.setChecked(timesTableSettings[0]);
//        final CheckBox CST = dialog.findViewById(R.id.cst);
//        CST.setChecked(timesTableSettings[1]);
//        final CheckBox LOCAL = dialog.findViewById(R.id.local);
//        LOCAL.setChecked(timesTableSettings[2]);
//
//        UTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (UTC.isChecked()) {
//                    timesTableSettings[0] = true;
//                    Log.d("AlexKray", "UTC is Checked");
//                    Log.d("AlexKray", Arrays.toString(timesTableSettings));
//
//                } else {
//                    timesTableSettings[0] = false;
//                    Log.d("AlexKray", "UTC is not Checked");
//                    Log.d("AlexKray", Arrays.toString(timesTableSettings));
//
//                }
//            }
//        });
//
//        CST.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (CST.isChecked()) {
//                    timesTableSettings[1] = true;
//                    Log.d("AlexKray", "CST is Checked");
//                    Log.d("AlexKray", Arrays.toString(timesTableSettings));
//                } else {
//                    timesTableSettings[1] = false;
//                    Log.d("AlexKray", "CST is not Checked");
//                    Log.d("AlexKray", Arrays.toString(timesTableSettings));
//                }
//            }
//        });
//
//        LOCAL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (LOCAL.isChecked()) {
//                    timesTableSettings[2] = true;
//                    Log.d("AlexKray", "LOCAL is Checked");
//                    Log.d("AlexKray", Arrays.toString(timesTableSettings));
//                } else {
//                    timesTableSettings[2] = false;
//                    Log.d("AlexKray", "LOCAL is not Checked");
//                    Log.d("AlexKray", Arrays.toString(timesTableSettings));
//                }
//            }
//        });
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogInfo);
//        builder.setView(dialog);
//        builder.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//        builder.show();
//    }

    private void createDialogVerify() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verify your email!");
        //String information = getString(R.string.information);
        //builder.setMessage(information);
        builder.setCancelable(false);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
        builder.show();
    }

    private void makeLinksTwitchDJs() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        linksTwitchDJs.put("Darkfade", "https://www.twitch.tv/djdarkfade");
        linksTwitchDJs.put("Deadlydoener", "https://www.twitch.tv/deadlydoener");
        linksTwitchDJs.put("Tongkii", "https://www.twitch.tv/tongkii");
        linksTwitchDJs.put("SmileznWigglez", "https://www.twitch.tv/smileznwigglez");
        linksTwitchDJs.put("G-Dub", "https://www.twitch.tv/gdub__");
        linksTwitchDJs.put("Buckenbooz", "https://www.twitch.tv/buckenbooz");
        linksTwitchDJs.put("Jigsaw", "https://www.twitch.tv/jigsawthedj");
        linksTwitchDJs.put("Krazy", "https://www.twitch.tv/djkrazy18");
        linksTwitchDJs.put("Zephin", "https://www.twitch.tv/dj_zephin");
        linksTwitchDJs.put("DeViousDecoy", "https://www.twitch.tv/deviousdecoy");
        linksTwitchDJs.put("Classwork", "https://www.twitch.tv/xclasswork");
        linksTwitchDJs.put("MarioPlaysKeys", "https://www.twitch.tv/marioplayskeys");
        linksTwitchDJs.put("FloydianSound", "https://www.twitch.tv/floydiansound");
        linksTwitchDJs.put("MissStabby", "https://www.twitch.tv/missstabby");
        linksTwitchDJs.put("Oxiden", "https://www.twitch.tv/oxi_den");
        linksTwitchDJs.put("Alease", "https://www.twitch.tv/alease");
        linksTwitchDJs.put("Dvize", "https://www.twitch.tv/dvize1");
        linksTwitchDJs.put("DeFreeze", "https://www.twitch.tv/thedefreeze");
        linksTwitchDJs.put("S3cret Ag3nt", "https://www.twitch.tv/s3cretag3nt");
        linksTwitchDJs.put("SladeAx", "https://www.twitch.tv/sladeax");
        linksTwitchDJs.put("TZAR_POTATO", "https://www.twitch.tv/tzar_potato");
        linksTwitchDJs.put("Jus_Hak", "https://www.twitch.tv/jus_hak");
        linksTwitchDJs.put("Sarri", "https://www.twitch.tv/sarrixo");
        linksTwitchDJs.put("SenpaiWubz", "https://www.twitch.tv/senpaiwubz");
        linksTwitchDJs.put("Maximemoring", "https://www.twitch.tv/maximemoring");
        linksTwitchDJs.put("Pachmaster", "https://www.twitch.tv/pachmaster");
        linksTwitchDJs.put("DarkGamer120", "https://www.twitch.tv/darkgamer120");
        linksTwitchDJs.put("Xlawguardian", "https://www.twitch.tv/xlawguardian");
        linksTwitchDJs.put("Bock", "https://www.twitch.tv/bockmusic");
        linksTwitchDJs.put("Funkyshark", "https://www.twitch.tv/funkyshark");
        linksTwitchDJs.put("FetusGrinder", "https://www.twitch.tv/djfetusgrinder");
        linksTwitchDJs.put("Digital_House_Party", "https://www.twitch.tv/digital_house_party");
        linksTwitchDJs.put("Lumi", "https://www.twitch.tv/itslumivr");
        linksTwitchDJs.put("Paulmanton", "https://www.twitch.tv/mrpaulmanton");
        linksTwitchDJs.put("Chrysaora", "https://www.twitch.tv/djchrysaora");
        linksTwitchDJs.put("Kitsune", "https://www.twitch.tv/kitsune_91");
        linksTwitchDJs.put("Turner", "https://www.twitch.tv/dj_turner");
        linksTwitchDJs.put("Indent1", "https://www.twitch.tv/indent1");
        linksTwitchDJs.put("Ufgkirk", "https://www.twitch.tv/ufgkirk");
        linksTwitchDJs.put("Clio Ryder", "https://www.twitch.tv/clioryder");
        linksTwitchDJs.put("Sinister Being", "https://www.twitch.tv/sinister_being");
        linksTwitchDJs.put("KAT_PW", "https://www.twitch.tv/kat_pw");
        linksTwitchDJs.put("Konceded", "https://www.twitch.tv/konceded_ttv");
        linksTwitchDJs.put("MrFalconMusic", "https://www.twitch.tv/misterfalconmusic");
        linksTwitchDJs.put("Frosty_shark", "https://www.twitch.tv/frosty_shark");
        linksTwitchDJs.put("LongwellArt", "https://www.twitch.tv/longwellart");
        linksTwitchDJs.put("SHYLYNX", "https://www.twitch.tv/iamshylynx");
        linksTwitchDJs.put("Walleee", "https://www.twitch.tv/dj_walleee");
        linksTwitchDJs.put("GeneralTsoLit", "https://www.twitch.tv/generaltsolit");
        linksTwitchDJs.put("Screwwella", "https://www.twitch.tv/DJ_screwwella");
        linksTwitchDJs.put("A.M.plex", "https://www.twitch.tv/amplex");
        linksTwitchDJs.put("TommyGG", "https://www.twitch.tv/protommygg");
        linksTwitchDJs.put("Bleach", "https://www.twitch.tv/bleach1879");
        linksTwitchDJs.put("Nox", "https://www.twitch.tv/agrifolia_official");
        linksTwitchDJs.put("Ru", "https://www.twitch.tv/ru______");
        linksTwitchDJs.put("Cassie", "https://www.twitch.tv/cmdrcassie");
        linksTwitchDJs.put("CasshernLive", "https://www.twitch.tv/casshernlive");
        linksTwitchDJs.put("Ellie", "https://twitch.tv/ellie_the_fox");
        linksTwitchDJs.put("d4rkcide", "https://www.twitch.tv/d4rkcide");
        linksTwitchDJs.put("Beep0110", "https://www.twitch.tv/beep0110");
        linksTwitchDJs.put("S A B E R", "https://www.twitch.tv/afaceofmercy");
        linksTwitchDJs.put("Curly_taters", "https://www.twitch.tv/curly_taters");
        linksTwitchDJs.put("BLVNK", "https://www.twitch.tv/konceded_ttv");
        linksTwitchDJs.put("Badlands", "https://www.twitch.tv/badlands1");
        linksTwitchDJs.put("Diamonddoge", "https://www.twitch.tv/diamonddoge2142");
        linksTwitchDJs.put("Arashi", "https://www.twitch.tv/arashi76");
        linksTwitchDJs.put("Ghaladeon", "https://www.twitch.tv/ghaladeon");
        linksTwitchDJs.put("Zoommair", "https://www.twitch.tv/zoommair");
        linksTwitchDJs.put("Gravekeepr", "https://www.twitch.tv/gravekeepr");
        linksTwitchDJs.put("Inc0", "https://www.twitch.tv/inconsistent");
        linksTwitchDJs.put("0b4K3", "https://www.twitch.tv/obakelab");
        linksTwitchDJs.put("Casshern", "https://www.twitch.tv/casshernlive");
        linksTwitchDJs.put("Bennibean", "https://www.twitch.tv/bennibean");
        linksTwitchDJs.put("ViperStar", "https://www.twitch.tv/djviperstar");
        linksTwitchDJs.put("Colonel Cthulu", "https://www.twitch.tv/colonel_cthulu");
        linksTwitchDJs.put("Myntt", "https://www.twitch.tv/mynttiefresh");
        linksTwitchDJs.put("Invadgaming", "https://www.twitch.tv/the_invad");
        linksTwitchDJs.put("Pearsoup", "https://www.twitch.tv/pearsoup");
        linksTwitchDJs.put("Rolledturtle420", "https://www.twitch.tv/rolledturtle420");
        linksTwitchDJs.put("OctaneMusic", "https://www.twitch.tv/octanemusic");
        linksTwitchDJs.put("Deerden", "https://www.twitch.tv/deerboytv");
        linksTwitchDJs.put("SwaMusic", "https://twitch.tv/SwaMusic");
        linksTwitchDJs.put("Atryxun", "https://www.twitch.tv/atryxun_");
        linksTwitchDJs.put("jmorrissette1", "https://www.twitch.tv/jmorrissette1");
        linksTwitchDJs.put("Magical_Sea", "https://www.twitch.tv/magical__sea");
        linksTwitchDJs.put("DJObseqV2", "https://www.twitch.tv/djobseqv2");
        linksTwitchDJs.put("Salakasto", "https://www.twitch.tv/salakasto");
        linksTwitchDJs.put("Noireuwu", "https://www.twitch.tv/noiremusic");
        linksTwitchDJs.put("dnDaan", "https://www.twitch.tv/dndaan");
        linksTwitchDJs.put("Nyaoire", "https://twitch.tv/noiremusic");
        linksTwitchDJs.put("Renagaderr", "https://www.twitch.tv/renagaderr");
        linksTwitchDJs.put("RisaNRG", "https://www.twitch.tv/risa_nrg");
        linksTwitchDJs.put("Sennomen", "https://www.twitch.tv/sennomen");
        linksTwitchDJs.put("Ashelia", "https://www.twitch.tv/asheliavr");
        linksTwitchDJs.put("ShootBang", "https://www.twitch.tv/shootbanger");
        linksTwitchDJs.put("Synthakat", "https://www.twitch.tv/synthakat");
        linksTwitchDJs.put("Kuroneko", "https://www.twitch.tv/dj__kuroneko");
        linksTwitchDJs.put("EpicOfficer", "https://twitch.tv/epicofficer");
        linksTwitchDJs.put("Melonslices", "https://www.twitch.tv/melonslicesvr");
        linksTwitchDJs.put("Melody", "https://www.twitch.tv/melody_iidx");
        linksTwitchDJs.put("Spontaneity", "https://www.twitch.tv/spontaneousity");
        linksTwitchDJs.put("Annias", "https://www.twitch.tv/annias");
        linksTwitchDJs.put("Hypersonic1 ", "https://www.twitch.tv/hypersonic10");

        //Blindmuted
        linksTwitchDJs.put("Blindmuted :p", "https://www.twitch.tv/sladeax");

        for (Map.Entry<String, String> entry : linksTwitchDJs.entrySet()) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("nameDJ", entry.getKey());
            hashMap.put("link", entry.getValue());
            reference.child("DJs").push().setValue(hashMap);
        }
    }

}
