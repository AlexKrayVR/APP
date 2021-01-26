package com.alex.vr_party_app.user;

public class User {

    private String mId;
    private String mUserName;
    private String mImageURL;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public User() {
    }

    public User(String id, String userName, String imageURL) {
        mId = id;
        mUserName = userName;
        mImageURL = imageURL;
    }
}
