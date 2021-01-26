package com.alex.vr_party_app.editor.dj;

import android.os.Parcel;
import android.os.Parcelable;

public class DJ implements Parcelable {
    String link;
    String nameDJ;
    String hours;

    public DJ(String link, String nameDJ, String hours) {
        this.link = link;
        this.nameDJ = nameDJ;
        this.hours = hours;
    }

    protected DJ(Parcel in) {
        link = in.readString();
        nameDJ = in.readString();
        hours = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(link);
        dest.writeString(nameDJ);
        dest.writeString(hours);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DJ> CREATOR = new Creator<DJ>() {
        @Override
        public DJ createFromParcel(Parcel in) {
            return new DJ(in);
        }

        @Override
        public DJ[] newArray(int size) {
            return new DJ[size];
        }
    };

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public DJ() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNameDJ() {
        return nameDJ;
    }

    public void setNameDJ(String nameDJ) {
        this.nameDJ = nameDJ;
    }



}
