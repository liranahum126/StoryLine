package com.storyline;

public class InterActiveFriend {
    String userId;
    boolean isMyTurn;


    public InterActiveFriend(String userId, boolean isMyTurn) {
        this.userId = userId;
        this.isMyTurn = isMyTurn;
    }

    public InterActiveFriend() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }
}
