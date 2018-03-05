package com.example.abanoub.emailsystemdemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class TrashActivity extends AppCompatActivity {

    DrawerLayout myDrawerlayout;
    ActionBarDrawerToggle myToggle;
    NavigationView navigationView;

    ListView listView;
    MessageAdapter adapter;
    LinearLayout emptyLinear;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);


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
        databaseReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("Trash");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NewEmail> emails_list = Utilities.getAllEmails(dataSnapshot);
                fillListView(emails_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = item.getTitle().toString();
                myDrawerlayout.closeDrawer(Gravity.LEFT);

                switch (title) {
                    case "Inbox":
                        startActivity(new Intent(TrashActivity.this, MainActivity.class));

                        break;
                    case "Sent":
                        startActivity(new Intent(TrashActivity.this, SentActivity.class));

                        break;
                    case "Favorites":
                        startActivity(new Intent(TrashActivity.this, FavoritesActivity.class));

                        break;
                    case "Trash":

                        break;
                    case "Sign out":
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(TrashActivity.this, SignInActivity.class));
                        break;
                    case "Profile":
                        startActivity(new Intent(TrashActivity.this, ProfileActivity.class));
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewEmail email = (NewEmail) adapter.getItem(position);
                if (email==null)
                    return;
                Intent intent = new Intent(TrashActivity.this, DetailedActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Showing google speech input dialog
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        getString(R.string.speech_prompt));
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        composeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrashActivity.this, ComposeEmailActivity.class));
            }
        });
    }

    private void fillListView(ArrayList<NewEmail> emails_list) {
        if (emails_list.size() == 0)
            emptyLinear.setVisibility(View.VISIBLE);
        else
            emptyLinear.setVisibility(View.GONE);

        adapter = new MessageAdapter(this, emails_list);
        listView.setAdapter(adapter);
    }


    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (result.get(0).equals("compose email") || result.get(0).equals("compose an email")
                            || result.get(0).equals("compose new email") || result.get(0).equals("compose mail")
                            || result.get(0).equals("compose new mail") || result.get(0).equals("write email")
                            || result.get(0).equals("write an email") || result.get(0).equals("write new email")
                            || result.get(0).equals("write mail") || result.get(0).equals("write new mail")
                            || result.get(0).contains("compose") || result.get(0).contains("write")
                            || result.get(0).contains("new mail"))
                        startActivity(new Intent(TrashActivity.this, ComposeEmailActivity.class));

                    else if (result.get(0).equals("sign out") || result.get(0).equals("log out")
                            || result.get(0).contains("sign out")) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(TrashActivity.this, SignInActivity.class));

                    } else if (result.get(0).equals("profile") || result.get(0).equals("open profile")
                            || result.get(0).equals("open my profile")|| result.get(0).equals("show me my profile")
                            || result.get(0).equals("show profile") || result.get(0).contains("profile"))
                        startActivity(new Intent(TrashActivity.this, ProfileActivity.class));

                    else if (result.get(0).equals("sent") || result.get(0).equals("open sent")
                            || result.get(0).equals("open sent emails")|| result.get(0).equals("open sent page")
                            || result.get(0).equals("open sent mails") || result.get(0).equals("show me sent emails")
                            || result.get(0).equals("show me sent mails") || result.get(0).contains("sent"))
                        startActivity(new Intent(TrashActivity.this, SentActivity.class));

                    else if (result.get(0).equals("favorites") || result.get(0).equals("open favorites")
                            || result.get(0).equals("open my favorites") || result.get(0).equals("open favorite emails")
                            || result.get(0).equals("open favorites page") || result.get(0).equals("open favorite mails")
                            || result.get(0).equals("show me favorite emails")|| result.get(0).equals("show me favorite mails")
                            || result.get(0).contains("favorite")|| result.get(0).contains("favorites")
                            || result.get(0).contains("favourite")|| result.get(0).contains("favourites"))
                        startActivity(new Intent(TrashActivity.this, FavoritesActivity.class));

                    else if (result.get(0).equals("inbox") || result.get(0).equals("open inbox")
                            || result.get(0).equals("open my inbox") || result.get(0).equals("open received emails")
                            || result.get(0).equals("open inbox page") || result.get(0).equals("open received mails")
                            || result.get(0).contains("show me inbox")|| result.get(0).contains("show me my inbox")
                            || result.get(0).equals("show me received emails")|| result.get(0).equals("show me received mails")
                            || result.get(0).contains("inbox")|| result.get(0).contains("received"))
                        startActivity(new Intent(TrashActivity.this, MainActivity.class));


                    else if (result.get(0).equals("exit") || result.get(0).equals("exit application")
                            || result.get(0).equals("exit from application")|| result.get(0).equals("back")
                            || result.get(0).equals("go back"))
                        moveTaskToBack(true); //exit app
                    else
                        Toast.makeText(this, "Not recognized", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true); //exit app
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