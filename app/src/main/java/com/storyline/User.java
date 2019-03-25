package com.storyline;

public class User {
    String userId;
    String userEmail;
    String name;
    String photoUrl;


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
}
