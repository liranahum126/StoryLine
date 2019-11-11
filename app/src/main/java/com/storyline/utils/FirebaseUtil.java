package com.storyline.utils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.storyline.FriendlyMessage;
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
                                    String lastword,int currentCount,String fullStory
            ,int interActiveFriendStatus,boolean isLastTurn){
        final DatabaseReference updateTurnsReferance = getUsersTableDataReference();
        //update my turn
        InterActiveFriend interActiveFriendMyTurn = new InterActiveFriend(friendUserId,
                interActiveFriendStatus, lastword,currentCount+1);
        if(isLastTurn) {
            interActiveFriendMyTurn.setFullStory(fullStory.toString());
        }
        updateTurnsReferance.child(mFirebaseUser.getUid()).child("interActiveFriendList").
                child(friendUserId).setValue(interActiveFriendMyTurn);

    }

    public static void updateFriendTurn(FirebaseUser mFirebaseUser,String friendUserId,
                                        String lastword,int currentCount,String fullStory,boolean isLastTurn){
        final DatabaseReference updateTurnsReferance = getUsersTableDataReference();
        InterActiveFriend interActiveFriendFriendTurn =
                new InterActiveFriend(mFirebaseUser.getUid(), InterActiveFriend.FRIEND_TURN,
                        lastword, currentCount+1);
        if(isLastTurn) {
            interActiveFriendFriendTurn.setFullStory(fullStory);
        }
        updateTurnsReferance.child(friendUserId).child("interActiveFriendList").
                child(mFirebaseUser.getUid()).setValue(interActiveFriendFriendTurn);
    }

    public static DatabaseReference getUsersTableDataReference(){
        return getBseDataReference().child("users");
    }

   public static DatabaseReference getBseDataReference(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public static void pushMessageToChatTable(String tableName, FriendlyMessage value){
        FirebaseDatabase.getInstance().getReference().child(tableName)
                .push().setValue(value);
    }
}
