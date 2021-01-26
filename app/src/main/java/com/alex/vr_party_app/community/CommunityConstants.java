package com.alex.vr_party_app.community;

import com.alex.vr_party_app.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CommunityConstants {

    static public ArrayList<String> mListNames = new ArrayList<>(Arrays.asList(
            "Dance Dance VR",
            "Miami Vice Parties",
            "Interconnection",
            "Rizumu",
            "Just Party",
            //"Elysium",
            //"VRCPC",
            "Loli Squad",
            "Groovr Events",
            //"RaveOn",
            "Addiction Club"
    ));

    static public ArrayList<Integer> mListIcon = new ArrayList<>(Arrays.asList(
            R.drawable.ddvr_new,
            R.drawable.mvp,
            R.drawable.interconnection,
            R.drawable.rizumu,
            R.drawable.just_party,
            //R.drawable.elysium,
            //R.drawable.vrcpc,
            R.drawable.loli_squad,
            //R.drawable.raveon,
            R.drawable.groovr,
            R.drawable.addiction
    ));

    //Elysium and MVP don't have links
    static public ArrayList<String> mListLinks = new ArrayList<>(Arrays.asList(
            "https://discord.gg/BNMhtbwM",
            "https://discord.gg/mvp",
            "https://discord.gg/g6zyYQqr",
            "https://discord.gg/g4tYdP5",
            "https://discord.gg/MZQtCtZm",
            //"doesnt have link",
            //"https://discord.gg/mVU2rp",
            "https://discord.gg/5nqrQTHM",
            //"https://discord.gg/Z6hbdv",
            //"https://discord.gg/tYFrsN",
            "https://discord.gg/groovr",
            ""
    ));

    static public ArrayList<Integer> mListRizumuPicture = new ArrayList<>(Arrays.asList(
            R.drawable.mini_rizumu1,
            R.drawable.mini_rizumu2,
            R.drawable.mini_rizumu3,
            R.drawable.mini_rizumu4
    ));

    static public ArrayList<Integer> mListAddictionPicture = new ArrayList<>(Arrays.asList(
            R.drawable.mini_addiction1,
            R.drawable.mini_addiction2,
            R.drawable.mini_addiction4,
            R.drawable.mini_addiction3
    ));

    static public ArrayList<Integer> mListMVPPicture = new ArrayList<>(Arrays.asList(
            R.drawable.mini_mvp1,
            R.drawable.mini_mvp3,
            R.drawable.mini_mvp2,
            R.drawable.mini_mvp4
    ));

    static public ArrayList<Integer> mListDDVRPicture = new ArrayList<>(Arrays.asList(
            R.drawable.mini_ddvr1,
            R.drawable.mini_ddvr2,
            R.drawable.mini_ddvr3,
            R.drawable.mini_ddvr4
    ));

    static public ArrayList<Integer> mListGroovrPicture = new ArrayList<>(Arrays.asList(
            R.drawable.mini_groovr1,
            R.drawable.mini_groovr2,
            R.drawable.mini_groovr3,
            R.drawable.mini_groovr4
    ));

    static public ArrayList<Integer> mListJPPicture = new ArrayList<>(Arrays.asList(
            R.drawable.mini_jp4,
            R.drawable.mini_jp1,
            R.drawable.mini_jp2,
            R.drawable.mini_jp3
    ));

    static public ArrayList<Integer> mListLoliSquadPicture = new ArrayList<>(Arrays.asList(
            R.drawable.loli_squad1,
            R.drawable.loli_squad2,
            R.drawable.loli_squad3,
            R.drawable.loli_squad4
    ));

    static public ArrayList<Integer> listInterconnectionPicture = new ArrayList<>(Arrays.asList(
            R.drawable.inter1,
            R.drawable.inter2,
            R.drawable.inter3,
            R.drawable.inter4
    ));


    static public ArrayList<ArrayList<Integer>> mListAllPicture = new ArrayList<>(Arrays.asList(
            mListDDVRPicture,
            mListMVPPicture,
            listInterconnectionPicture,
            mListRizumuPicture,
            mListJPPicture,
            mListLoliSquadPicture,
            mListGroovrPicture,
            mListAddictionPicture
    ));


}
