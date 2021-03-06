package com.alex.vr_party_app.chat;

public class Chat {

    private String userName;
    private String message;
    private String userID;
    private String imageURL;
    private String date;

    public Chat(String userName, String message, String userID, String imageURL, String date) {
        this.userName = userName;
        this.message = message;
        this.userID = userID;
        this.imageURL = imageURL;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    @Override
    public String toString() {
        return "Chat{" +
                "userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", userID='" + userID + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
