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


    public static ArrayList<NewEmail> getAllEmails(DataSnapshot dataSnapshot) {

        ArrayList<NewEmail> list = getAllEmailsHelper((Map<String, Object>) dataSnapshot.getValue());
        return list;
    }

    private static ArrayList<NewEmail> getAllEmailsHelper(Map<String, Object> dataSnapShot) {

        ArrayList<NewEmail> list = new ArrayList<>();

        //iterate through each email, ignoring their UID
        if (dataSnapShot != null) {
            for (Map.Entry<String, Object> entry : dataSnapShot.entrySet()) {

                //Get email map
                Map singleEmail = (Map) entry.getValue();
                NewEmail emailObj = new NewEmail();
                emailObj.sender = (String) singleEmail.get("sender");
                emailObj.receiver = (String) singleEmail.get("receiver");
                emailObj.title = (String) singleEmail.get("title");
                emailObj.body = (String) singleEmail.get("body");
                emailObj.date = (String) singleEmail.get("date");
                list.add(emailObj);
            }
        }
        return list;
    }


    public static ArrayList<UserEmail> getAllUsersEmails(DataSnapshot dataSnapshot) {

        ArrayList<UserEmail> list = getAllUsersHelper((Map<String, Object>) dataSnapshot.getValue());
        return list;
    }

    private static ArrayList<UserEmail> getAllUsersHelper(Map<String, Object> dataSnapShot) {

        ArrayList<UserEmail> list = new ArrayList<>();

        //iterate through each user, ignoring their UID
        if (dataSnapShot != null) {
            for (Map.Entry<String, Object> entry : dataSnapShot.entrySet()) {

                //Get user map
                Map singleEmail = (Map) entry.getValue();
                UserEmail userEmailObj = new UserEmail();
                userEmailObj.email = (String) singleEmail.get("email");
                userEmailObj.pushID = (String) singleEmail.get("pushID");
                list.add(userEmailObj);
            }
        }
        return list;
    }


    public static ArrayList<NewUser> getPersonalData(DataSnapshot dataSnapshot) {

        ArrayList<NewUser> list = getPersonalDataHelper((Map<String, Object>) dataSnapshot.getValue());
        return list;
    }

    private static ArrayList<NewUser> getPersonalDataHelper(Map<String, Object> dataSnapShot) {

        ArrayList<NewUser> list = new ArrayList<>();

        if (dataSnapShot != null) {
            for (Map.Entry<String, Object> entry : dataSnapShot.entrySet()) {

                //Get user map
                Map singleUser = (Map) entry.getValue();
                NewUser userObj = new NewUser();
                userObj.fullname = (String) singleUser.get("fullname");
                userObj.email = (String) singleUser.get("email");
                userObj.password = (String) singleUser.get("password");
                userObj.birthdate = (String) singleUser.get("birthdate");
                userObj.gender = (String) singleUser.get("gender");
                userObj.phoneNumber = (String) singleUser.get("phoneNumber");
                userObj.secretQuestion = (String) singleUser.get("secretQuestion");
                userObj.secretAnswer = (String) singleUser.get("secretAnswer");
                userObj.country = (String) singleUser.get("country");
                userObj.pushID = (String) singleUser.get("pushID");
                list.add(userObj);
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
