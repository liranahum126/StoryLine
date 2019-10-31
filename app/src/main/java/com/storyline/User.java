package com.storyline;

import java.util.HashMap;
import java.util.List;

public class User {
    public String userId;
    public String userEmail;
    public String name;
    public String photoUrl;
    public HashMap<String,InterActiveFriend> interActiveFriendList;


    public User() {
    }

    public User(String userId, String userEmail, String name, String photoUrl) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public HashMap<String,InterActiveFriend> getInterActiveFriendList() {
        return interActiveFriendList;
    }

    public void setInterActiveFriendList(HashMap<String,InterActiveFriend> interActiveFriendList) {
        this.interActiveFriendList = interActiveFriendList;
    }
}
