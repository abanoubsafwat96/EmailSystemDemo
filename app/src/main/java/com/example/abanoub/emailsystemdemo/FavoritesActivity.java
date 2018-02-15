package com.example.abanoub.emailsystemdemo;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    DrawerLayout myDrawerlayout;
    ActionBarDrawerToggle myToggle;
    NavigationView navigationView;

    ListView listView;
    MessageAdapter adapter;
    LinearLayout emptyLinear;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextToSpeech textToSpeech;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<NewEmail> emails_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        myDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myToggle = new ActionBarDrawerToggle(this, myDrawerlayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        myDrawerlayout.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyLinear = (LinearLayout) findViewById(R.id.emptyLinear);
        listView = (ListView) findViewById(R.id.EmailsListView);
        FloatingActionButton mic = (FloatingActionButton) findViewById(R.id.mic);
        FloatingActionButton composeEmail = (FloatingActionButton) findViewById(R.id.composeEmail);
        navigationView = (NavigationView) findViewById(R.id.navDrawer);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("Favorites");


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = item.getTitle().toString();
                myDrawerlayout.closeDrawer(Gravity.LEFT);

                switch (title) {
                    case "Inbox":
                        startActivity(new Intent(FavoritesActivity.this, MainActivity.class));

                        break;
                    case "Sent":
                        startActivity(new Intent(FavoritesActivity.this, SentActivity.class));

                        break;
                    case "Favorites":

                        break;
                    case "Trash":
                        startActivity(new Intent(FavoritesActivity.this, TrashActivity.class));
                        break;
                    case "Sign out":
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(FavoritesActivity.this, SignInActivity.class));
                        break;
                    case "Profile":
                        startActivity(new Intent(FavoritesActivity.this, ProfileActivity.class));
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        fillListView();
    }

    private void fillListView() {
        if (emails_list.size() == 0)
            emptyLinear.setVisibility(View.VISIBLE);
        else
            emptyLinear.setVisibility(View.GONE);

        adapter = new MessageAdapter(this, emails_list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (myToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

}
