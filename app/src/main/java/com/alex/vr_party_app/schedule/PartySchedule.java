
package com.alex.vr_party_app.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ActivityPartyScheduleBinding;
import com.alex.vr_party_app.settings.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TreeSet;

public class PartySchedule extends AppCompatActivity {
    private String day;
    TreeSet<PartyClass> partyClassTreeSet = new TreeSet<>();
    private int[] offset;

    //for highlight current table row
    ArrayList<DateHourMinute> DHM = new ArrayList<>();

    //for check if there is 30 min
    private Boolean isEven = true;

    ArrayList<Integer> listCalendars = new ArrayList<>();

    private HashMap<String, String> linksTwitchDJs = new HashMap<>();

    private static SharedPreferences settings;
    private static final String APP_PREFERENCES = "mysettings";

    Toolbar toolbar;

    ActivityPartyScheduleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartyScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        day = intent.getStringExtra("day");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(day);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        makeLinksTwitchDJs();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("VR_PARTY").document(day).collection("Parties")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                binding.progress.setVisibility(View.GONE);
                                binding.textNoPartiesAnnouncence.setVisibility(View.VISIBLE);
//                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);
//                                TextView tv = (TextView) tr.getChildAt(0);
//                                tv.setText(getResources().getString(R.string.NoPartiesAnnounced));
//                                tv.setTextColor(getResources().getColor(R.color.white));
//                                party_schedule_table.addView(tr);
//                                party_schedule_table.setColumnCollapsed(0, false);
                            } else {
                                ArrayList<PartyClass> parties = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getData().size() == 0) {
                                        continue;
                                    }
                                    String date = document.getData().get("Date").toString();
                                    String time_hours = document.getData().get("Start_hours").toString();
                                    String time_minutes = document.getData().get("Start_minutes").toString();

                                    //make list dates of party
                                    listCalendars.add(Integer.valueOf(date));

                                    String name = document.getData().get("Community").toString();
                                    ArrayList<String> listDJNames = (ArrayList<String>) document.getData().get("DJs_name");
                                    ArrayList<Double> listDJHours = (ArrayList<Double>) document.getData().get("DJs_hours");

                                    //for convert from Long(numbers in database) to Double
//                                    ArrayList<Double> listDJHours = new ArrayList<>();
//                                    for (int i = 0; i < listDJHourstemp.size(); i++) {
//                                        String s = listDJHourstemp.get(i) + "";
//                                        listDJHours.add(Double.parseDouble(s));
//                                    }

                                    PartyClass temp = new PartyClass(Integer.valueOf(date), Integer.valueOf(time_hours), Integer.valueOf(time_minutes), name, listDJNames, listDJHours);
                                    parties.add(temp);
                                }
                                binding.progress.setVisibility(View.GONE);
                                for (int i = 0; i < parties.size(); i++) {
                                    if (parties.get(i).getEven() == false) {
                                        isEven = false;
                                        break;
                                    }
                                }
                                Log.d("AlexDebug", "isEven: " + isEven);
                                makeTable(parties);
                            }
                        } else {
                            Log.e("AlexDebug", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void makeLinksTwitchDJs() {
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
        linksTwitchDJs.put("curly_taters", "https://www.twitch.tv/curly_taters");
        linksTwitchDJs.put("BLVNK", "https://www.twitch.tv/konceded_ttv");
        linksTwitchDJs.put("Badlands", "https://www.twitch.tv/badlands1");
        linksTwitchDJs.put("Diamonddoge", "https://www.twitch.tv/diamonddoge2142");
        linksTwitchDJs.put("Arashi", "https://www.twitch.tv/arashi76");
        linksTwitchDJs.put("Ghaladeon", "https://www.twitch.tv/ghaladeon");
        linksTwitchDJs.put("Zoommair", "https://www.twitch.tv/zoommair");
        linksTwitchDJs.put("gravekeepr", "https://www.twitch.tv/gravekeepr");
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
        linksTwitchDJs.put("salakasto", "https://www.twitch.tv/salakasto");
        linksTwitchDJs.put("Noireuwu", "https://www.twitch.tv/noiremusic");
        linksTwitchDJs.put("dnDaan", "https://www.twitch.tv/dndaan");
        linksTwitchDJs.put("Nyaoire", "https://twitch.tv/noiremusic");
        linksTwitchDJs.put("Renagaderr", "https://www.twitch.tv/renagaderr");
        linksTwitchDJs.put("RisaNRG", "https://www.twitch.tv/risa_nrg");
        linksTwitchDJs.put("Sennomen", "https://www.twitch.tv/sennomen");
        linksTwitchDJs.put("Ashelia", "https://www.twitch.tv/asheliavr");
        linksTwitchDJs.put("ShootBang", "https://www.twitch.tv/shootbanger");

        //Blindmuted
        linksTwitchDJs.put("Blindmuted :p", "https://www.twitch.tv/sladeax");
    }

    private void getOffset() {
        int arrayDateTime[][] = new int[partyClassTreeSet.size()][3];
        Iterator<PartyClass> itDateTime = partyClassTreeSet.iterator();
        for (int i = 0; i < partyClassTreeSet.size(); i++) {
            PartyClass temp = itDateTime.next();
            arrayDateTime[i][0] = temp.getDate();
            arrayDateTime[i][1] = temp.getTime_hours();
            arrayDateTime[i][2] = temp.getTime_minutes();
        }
        Log.d("AlexDebug", "arrayDateTime: " + Arrays.deepToString(arrayDateTime));
        offset = new int[partyClassTreeSet.size()];
        offset[0] = 0;
        for (int i = 1; i < offset.length; ++i) {
            if (arrayDateTime[0][0] != arrayDateTime[i][0]) {
                offset[i] = 0;
                if (isEven) {
                    offset[i] = 24 - arrayDateTime[0][1] + arrayDateTime[i][1];
                } else {
                    offset[i] = (24 - arrayDateTime[0][1] + arrayDateTime[i][1]) * 2;
                }
                if (arrayDateTime[0][2] == arrayDateTime[i][2]) {
                } else {
                    if (arrayDateTime[0][2] == 30) {
                        offset[i] -= 1;
                    } else {
                        offset[i] += 1;
                    }
                }
            } else {
                if (isEven) {
                    offset[i] = arrayDateTime[i][1] - arrayDateTime[0][1];
                } else {
                    offset[i] = (arrayDateTime[i][1] - arrayDateTime[0][1]) * 2;
                    if (arrayDateTime[i][2] != arrayDateTime[0][2]) {
                        if (arrayDateTime[i][2] == 30) {
                            offset[i] += 1;
                        } else {
                            offset[i] -= 1;
                        }
                    }
                }
            }
        }
        Log.d("AlexDebug", "ofset: " + Arrays.toString(offset));
    }

    /**
     * @param startParty_hours
     * @param startParty_minutes
     * @return String[][] like [[22:00, 22:30, 23:00], [16:00, 16:30, 17:00], [01:00, 01:30, 02:00]]'
     * [0][] - utc
     * [1][] - cst
     * [2][] - local
     */
    private String[][] getTimes(int startParty_hours, int startParty_minutes, int lenghtOfParty) {

        //find start of utc time for table
        Calendar calendar_utc = new GregorianCalendar();
        calendar_utc.set(Calendar.HOUR_OF_DAY, startParty_hours);
        calendar_utc.set(Calendar.MINUTE, startParty_minutes);

        //find start of cst time for table
        Calendar calendar_cst = new GregorianCalendar();
        calendar_cst.set(Calendar.HOUR_OF_DAY, startParty_hours - 5);
        calendar_cst.set(Calendar.MINUTE, startParty_minutes);

        //find start of local time for table
        Calendar calendarCurrent = new GregorianCalendar();
        Log.d("AlexDebug", "calendarCurrent.getTime(): " + calendarCurrent.getTime());

        //SimpleDateFormat dateFormatUTC = new SimpleDateFormat("HH");
        //dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        //int different = Integer.parseInt(dateFormatCurrent.format(calendarCurrent.getTime())) - Integer.parseInt(dateFormatUTC.format(calendarCurrent.getTime()));
        //Log.d("AlexDebug", "different: " + different);

        int differentTIME = TimeZone.getDefault().getRawOffset() / (1000 * 60 * 60);
        Log.d("AlexDebug", "differentTest: " + differentTIME);

        calendarCurrent.set(Calendar.DATE, partyClassTreeSet.first().getDate());
        Log.d("AlexDebug", "calendarCurrent.getTime(): " + calendarCurrent.getTime());
        calendarCurrent.set(Calendar.HOUR_OF_DAY, startParty_hours + differentTIME);
        Log.d("AlexDebug", "calendarCurrent.getTime(): " + calendarCurrent.getTime());
        calendarCurrent.set(Calendar.MINUTE, startParty_minutes);


//        Log.d("AlexDebug", "partyClassTreeSet.first().getDate(): " + partyClassTreeSet.first().getDate());
//        Log.d("AlexDebug", "startParty_hours + different: " + (startParty_hours + different));
//        Log.d("AlexDebug", "startParty_minutes: " + startParty_minutes);


        SimpleDateFormat dateFormatCurrent = new SimpleDateFormat("HH");
        SimpleDateFormat dateFormatDD = new SimpleDateFormat("dd");
        SimpleDateFormat dateFormatMM = new SimpleDateFormat("mm");

        // dateFormat "HH:mm" for fill listTimes
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String[][] listTimes = new String[3][lenghtOfParty];
        for (int i = 0; i < lenghtOfParty; i++) {
            if (isEven) {
                DHM.add(new DateHourMinute(
                        Integer.parseInt(dateFormatDD.format(calendarCurrent.getTime())),
                        Integer.parseInt(dateFormatCurrent.format(calendarCurrent.getTime())),
                        Integer.parseInt(dateFormatMM.format(calendarCurrent.getTime())))
                );
                listTimes[0][i] = dateFormat.format(calendar_utc.getTime());
                calendar_utc.add(Calendar.MINUTE, 60);
                listTimes[1][i] = dateFormat.format(calendar_cst.getTime());
                calendar_cst.add(Calendar.MINUTE, 60);
                listTimes[2][i] = dateFormat.format(calendarCurrent.getTime());
                calendarCurrent.add(Calendar.MINUTE, 60);
            } else {
                DHM.add(new DateHourMinute(
                        Integer.parseInt(dateFormatDD.format(calendarCurrent.getTime())),
                        Integer.parseInt(dateFormatCurrent.format(calendarCurrent.getTime())),
                        Integer.parseInt(dateFormatMM.format(calendarCurrent.getTime())))
                );
                listTimes[0][i] = dateFormat.format(calendar_utc.getTime());
                calendar_utc.add(Calendar.MINUTE, 30);
                listTimes[1][i] = dateFormat.format(calendar_cst.getTime());
                calendar_cst.add(Calendar.MINUTE, 30);
                listTimes[2][i] = dateFormat.format(calendarCurrent.getTime());
                calendarCurrent.add(Calendar.MINUTE, 30);
            }
        }
        //Log.d("AlexDebug", "listTimes: " + Arrays.deepToString(listTimes));
        Log.d("AlexDebug", "DHM: " + DHM.toString());
        return listTimes;
    }


    /**
     * @param lenghtOfParty
     * @return ArrayList<ArrayList < String>> like [[DJs name, DJs name, -,], [-, DJs name, DJs name]]
     */
    private ArrayList<ArrayList<String>> getDJs(int lenghtOfParty) {
        ArrayList<ArrayList<String>> djsList = new ArrayList<>();
        Iterator<PartyClass> itFinalList = partyClassTreeSet.iterator();
        ArrayList<String> temp;
        for (int i = 0; i < partyClassTreeSet.size(); i++) {
            if (isEven) {
                temp = itFinalList.next().getFinalList_even();
            } else {
                temp = itFinalList.next().getFinalList_odd();
            }
            //Log.d("AlexDebug", "temp: " + temp.toString());

            ArrayList<String> forAdd = new ArrayList<>();
            for (int j = 0, k = 0; j < lenghtOfParty; j++) {
                if (j >= offset[i] && j < (temp.size() + offset[i])) {
                    forAdd.add(temp.get(k));
                    k++;
                } else {
                    forAdd.add("-");
                }
            }
            djsList.add(forAdd);
        }
        Log.d("AlexDebug", "djsArr: " + djsList.toString());
        return djsList;
    }

    private void makeTable(ArrayList<PartyClass> p) {

        ArrayList<PartyClass> parties = p;
        for (int i = 0; i < parties.size(); i++) {
            partyClassTreeSet.add(parties.get(i));
        }

        addRowTitle();

        getOffset();

        //lenghtOfParties
        TreeSet<Double> maxHoursParty = new TreeSet<>();
        Iterator<PartyClass> itMaxParty = partyClassTreeSet.iterator();
        for (int i = 0; i < partyClassTreeSet.size(); i++) {
            if (isEven) {
                maxHoursParty.add(itMaxParty.next().getPartyHours_even());
            } else {
                maxHoursParty.add(itMaxParty.next().getPartyHours_odd());
            }
        }

        Double lenghtOfParty = offset[offset.length - 1] + maxHoursParty.last();
        Log.d("AlexDebug", "lenghtOfParty: " + lenghtOfParty);

        String[][] times = getTimes(partyClassTreeSet.first().getTime_hours(), partyClassTreeSet.first().getTime_minutes(), lenghtOfParty.intValue());
        ArrayList<ArrayList<String>> DJs = getDJs(lenghtOfParty.intValue());

        int extraRowsForDelete=0;
        for (int j = (int) (lenghtOfParty - 1); j >= 0; j--) {
            int counter = 0;
            for (int i = 0; i < DJs.size(); i++) {
                Log.d("AlexDebug", "DJs.get(i).get(j)" + DJs.get(i).get(j));
                if (DJs.get(i).get(j).equals("-")) {
                    counter++;
                }
            }
            if (counter == DJs.size()) {
                Log.d("AlexDebug", "empty found");
                extraRowsForDelete++;
            } else {
                break;
            }
        }
        lenghtOfParty -= (double) extraRowsForDelete;

        //make table
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Calendar currentCalendar = new GregorianCalendar();
        for (int j = 0; j < lenghtOfParty; j++) {
            TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);
            tr.setSelected(true);
            TextView tv = null;
            //set times
            for (int i = 0; i < 3; i++) {
                tv = (TextView) tr.getChildAt(i);
                tv.setText(times[i][j]);
                tv.setTextColor(getResources().getColor(R.color.white));
            }

            //set DJs
            for (int i = 3, k = 0; i < 3 + partyClassTreeSet.size(); i++, k++) {
                tv = (TextView) tr.getChildAt(i);
                //set twitch link to DJ
                final String name = DJs.get(k).get(j);
                tv.setText(name);
                tv.setTextColor(getResources().getColor(R.color.white));

                if (linksTwitchDJs.get(name) != null) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linksTwitchDJs.get(name)));
                            startActivity(browserIntent);
                        }
                    });
                }
            }

            //highlight row if time is current
            if (currentCalendar.get(Calendar.DATE) == DHM.get(j).getDate()) {
                Log.d("AlexDebug", "Date is same: " + DHM.get(j).getDate());
                if (isEven) {
                    if (currentCalendar.get(Calendar.HOUR_OF_DAY) == DHM.get(j).getHour()) {
                        tr.setBackgroundColor(getResources().getColor(R.color.menuTextColor));
                        Log.d("AlexDebug", "Hour is same: " + DHM.get(j).getHour());
                    }
                } else {
                    if (currentCalendar.get(Calendar.HOUR_OF_DAY) == DHM.get(j).getHour()) {
                        Log.d("AlexDebug", "Hour is same: " + DHM.get(j).getHour());
                        if (DHM.get(j).getMinute() == 0 && currentCalendar.get(Calendar.MINUTE) < 30) {
                            tr.setBackgroundColor(getResources().getColor(R.color.menuTextColor));
                        }
                        if (DHM.get(j).getMinute() == 30 && currentCalendar.get(Calendar.MINUTE) >= 30) {
                            tr.setBackgroundColor(getResources().getColor(R.color.menuTextColor));
                        }
                    }
                }
            }

            binding.partyScheduleTable.addView(tr);
        }

    }

    public void addRowTitle() {
        String[] times = {"UTC", "CST", "LOCAL"};
        String[] forTitle = new String[partyClassTreeSet.size()];
        Iterator<PartyClass> itForTitle = partyClassTreeSet.iterator();
        for (int i = 0; i < partyClassTreeSet.size(); i++) {
            forTitle[i] = itForTitle.next().getName();
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);
        tr.setSelected(true);
        //full times name
        for (int i = 0; i < 3; i++) {
            TextView tv = (TextView) tr.getChildAt(i);
            tv.setText(times[i]);
            tv.setTextColor(getResources().getColor(R.color.white));
        }

        //custom settings of table
        if (settings.getBoolean(SettingsActivity.APP_PREFERENCES_CHECK_UTC, true)) {
            binding.partyScheduleTable.setColumnCollapsed(0, false);
        }
        if (settings.getBoolean(SettingsActivity.APP_PREFERENCES_CHECK_CST, true)) {
            binding.partyScheduleTable.setColumnCollapsed(1, false);
        }
        if (settings.getBoolean(SettingsActivity.APP_PREFERENCES_CHECK_LOCALE, true)) {
            binding.partyScheduleTable.setColumnCollapsed(2, false);
        }

        //fill parties name
        for (int i = 0, j = 3; i < forTitle.length; i++, j++) {
            TextView tv = (TextView) tr.getChildAt(j);
            tv.setText(forTitle[i]);
            binding.partyScheduleTable.setColumnCollapsed(j, false);
            tv.setTextColor(getResources().getColor(R.color.white));
        }
        binding.partyScheduleTable.addView(tr);
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
