package com.example.abanoub.emailsystemdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Abanoub on 2017-12-03.
 */

public class Utilities {

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getCurrentUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null)
            return null;

        return user;
    }

    public static String getModifiedCurrentEmail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null)
            return null;
        String currentUserEmail = user.getEmail().replace(".", "_");

        return currentUserEmail;
    }

    public static ArrayList<Email> getAllEmails(DataSnapshot dataSnapshot) {

        ArrayList<Email> list = getAllEmailsHelper((Map<String, Object>) dataSnapshot.getValue());
        return list;
    }

    private static ArrayList<Email> getAllEmailsHelper(Map<String, Object> dataSnapShot) {

        ArrayList<Email> list = new ArrayList<>();

        //iterate through each note, ignoring their UID
        if (dataSnapShot != null) {
            for (Map.Entry<String, Object> entry : dataSnapShot.entrySet()) {

                //Get note map
                Map singleEmail = (Map) entry.getValue();
                Email EmailObj = new Email();
//                noteObj.title = (String) singleEmail.get("title");
//                noteObj.note = (String) singleEmail.get("note");
//                noteObj.pushId = (String) singleEmail.get("pushId");
                list.add(EmailObj);
            }
        }
        return list;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
