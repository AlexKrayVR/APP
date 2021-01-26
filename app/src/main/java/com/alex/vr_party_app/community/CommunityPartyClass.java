package com.alex.vr_party_app.community;

import java.util.ArrayList;

public class CommunityPartyClass {

    String name;
    //String description;
    String link;
    ArrayList<Integer> listOfPictures;
    int icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<Integer> getListOfPictures() {
        return listOfPictures;
    }

    public void setListOfPictures(ArrayList<Integer> listOfPictures) {
        this.listOfPictures = listOfPictures;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public CommunityPartyClass(String name, String link, ArrayList<Integer> listOfPictures, int icon) {
        this.name = name;
        this.link = link;
        this.listOfPictures = listOfPictures;
        this.icon = icon;
    }
}
