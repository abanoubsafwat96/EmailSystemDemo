package com.example.abanoub.emailsystemdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView fullName,received,sent,favorites,email,phoneNumber,country,birthdate;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference pesonalDataReference,inboxReference,sentReference,favoritesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseDatabase=FirebaseDatabase.getInstance();
        pesonalDataReference=firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("PersonalData");
        inboxReference=firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("Inbox");
        sentReference=firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("Sent");
        favoritesReference=firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("Favorites");

        profile_image= (CircleImageView) findViewById(R.id.profile_image);
        fullName= (TextView) findViewById(R.id.fullName);
        received= (TextView) findViewById(R.id.received);
        sent= (TextView) findViewById(R.id.sent);
        favorites= (TextView) findViewById(R.id.favorites);
        email= (TextView) findViewById(R.id.email);
        phoneNumber= (TextView) findViewById(R.id.phoneNumber);
        country= (TextView) findViewById(R.id.country);
        birthdate= (TextView) findViewById(R.id.birthdate);

        pesonalDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NewUser user=Utilities.getPersonalData(dataSnapshot);
                setPersonalData(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        inboxReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NewEmail> emails=Utilities.getAllEmails(dataSnapshot);
                received.setText(emails.size()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NewEmail> emails=Utilities.getAllEmails(dataSnapshot);
                sent.setText(emails.size()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        favoritesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NewEmail> emails=Utilities.getAllEmails(dataSnapshot);
                favorites.setText(emails.size()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setPersonalData(NewUser user) {

//        profile_image.setImageResource();
        fullName.setText(user.fullname);
//        received.setText();
//        sent.setText();
//        favorites.setText();
        email.setText(user.email);
        phoneNumber.setText(user.phoneNumber);
        country.setText(user.country);
        birthdate.setText(user.birthdate);
    }
}
