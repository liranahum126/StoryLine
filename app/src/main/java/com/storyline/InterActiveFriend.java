package com.storyline;

public class InterActiveFriend {
    String userId;
    boolean isMyTurn;
    String lastWord;


    public InterActiveFriend(String userId, boolean isMyTurn, String lastWord) {
        this.userId = userId;
        this.isMyTurn = isMyTurn;
        this.lastWord = lastWord;
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

    public String getLastWord() {
        return lastWord;
    }

    public void setLastWord(String lastWord) {
        this.lastWord = lastWord;
    }
}
