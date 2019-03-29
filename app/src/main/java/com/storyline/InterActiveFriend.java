package com.storyline;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class InterActiveFriend {
    String userId;
    String lastWord;

    public static final int START_GAME = 0;
    public static final int MY_TURN = 1;
    public static final int FRIEND_TURN = 2;
    public static final int END_GAME = 3;

    @IntDef({START_GAME, MY_TURN, FRIEND_TURN, END_GAME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TurnStatus{};

    @TurnStatus int currentGameStatus = START_GAME;


    public InterActiveFriend(String userId, @TurnStatus int currentGameStatus, String lastWord) {
        this.userId = userId;
        this.currentGameStatus = currentGameStatus;
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

    public String getLastWord() {
        return lastWord;
    }

    public void setLastWord(String lastWord) {
        this.lastWord = lastWord;
    }

    public int getCurrentGameStatus() {
        return currentGameStatus;
    }

    public void setCurrentGameStatus(int currentGameStatus) {
        this.currentGameStatus = currentGameStatus;
    }
}
