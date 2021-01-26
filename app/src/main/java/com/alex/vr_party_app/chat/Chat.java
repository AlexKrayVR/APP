package com.alex.vr_party_app.chat;

public class Chat {

    private String username;
    private String message;
    private String userid;
    private String imageURL;
    private String date;

    public Chat(String username, String message, String userid, String imageURL,String date) {
        this.username = username;
        this.message = message;
        this.userid = userid;
        this.imageURL = imageURL;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Chat() {
    }
}
