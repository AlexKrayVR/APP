package com.alex.vr_party_app.gallery;

public class Upload {

    private String userName;
    private String userID;
    private String imageUrl;
    private String date;

    public Upload(String userName, String userID, String imageUrl, String date) {
        this.userName = userName;
        this.userID = userID;
        this.imageUrl = imageUrl;
        this.date = date;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Upload() {
    }
}
