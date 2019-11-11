package com.storyline.ui.chat;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.storyline.FriendlyMessage;
import com.storyline.utils.FirebaseUtil;

public class ChatFragmentHelper {

    public static String getLastWordFromText(String text){
        return text.trim().substring(text.lastIndexOf(" ") + 1);
    }

    public static void pushMessageToChatTable(String message, String mPhotoUrl, int currentCount,
                                              FirebaseUser mFirebaseUser,String openingSentance,
                                              String friendUserId,String tableName){
        FriendlyMessage friendlyMessage = new
                FriendlyMessage(message.trim(),
                "mUsername",
                mPhotoUrl,
                null /* no image */,currentCount);
        friendlyMessage.setId(mFirebaseUser.getUid());

        if (!TextUtils.isEmpty(openingSentance)) {
            FriendlyMessage friendlyMessageOpening = new
                    FriendlyMessage(openingSentance,
                    "mUsername",
                    mPhotoUrl,
                    null /* no image */,
                    currentCount);
            friendlyMessageOpening.setId(friendUserId);
            FirebaseUtil.pushMessageToChatTable(tableName,friendlyMessageOpening);

        }

        FirebaseUtil.pushMessageToChatTable(tableName,friendlyMessage);


    }
}
