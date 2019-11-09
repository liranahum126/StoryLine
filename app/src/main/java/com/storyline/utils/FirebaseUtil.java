package com.storyline.utils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.storyline.InterActiveFriend;

public class FirebaseUtil {

    public static void getUserFromFriendInterActiveFriend(FirebaseUser mFirebaseUser,String friendUserId,
                                                           ValueEventListener valueEventListener){
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("users").child(friendUserId).
                child("interActiveFriendList").
                child(mFirebaseUser.getUid()).addValueEventListener(valueEventListener);
    }

    public static void updateMyTurn(FirebaseUser mFirebaseUser,String friendUserId,
                                    String lastword,int currentCount,String fullStory){
        final DatabaseReference updateTurnsReferance = getUsersTableDataReference();
        //update my turn
        InterActiveFriend interActiveFriendMyTurn = new InterActiveFriend(friendUserId,
                InterActiveFriend.END_GAME, lastword,currentCount+1);
        interActiveFriendMyTurn.setFullStory(fullStory.toString());
        updateTurnsReferance.child(mFirebaseUser.getUid()).child("interActiveFriendList").
                child(friendUserId).setValue(interActiveFriendMyTurn);

    }

    public static void updateFriendTurn(FirebaseUser mFirebaseUser,String friendUserId,
                                        String lastword,int currentCount,String fullStory){
        final DatabaseReference updateTurnsReferance = getUsersTableDataReference();
        InterActiveFriend interActiveFriendFriendTurn =
                new InterActiveFriend(mFirebaseUser.getUid(), InterActiveFriend.FRIEND_TURN,
                        lastword, currentCount+1);
        interActiveFriendFriendTurn.setFullStory(fullStory);
        updateTurnsReferance.child(friendUserId).child("interActiveFriendList").
                child(mFirebaseUser.getUid()).setValue(interActiveFriendFriendTurn);
    }

    public static DatabaseReference getUsersTableDataReference(){
        return FirebaseDatabase.getInstance().getReference().child("users");
    }
}
