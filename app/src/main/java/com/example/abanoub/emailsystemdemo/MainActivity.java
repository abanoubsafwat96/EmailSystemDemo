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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

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
        databaseReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("Inbox");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emails_list = Utilities.getAllEmails(dataSnapshot);
                fillListView();
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
                        break;
                    case "Sent":
                        startActivity(new Intent(MainActivity.this, SentActivity.class));

                        break;
                    case "Favorites":
                        startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
                        break;
                    case "Trash":
                        startActivity(new Intent(MainActivity.this, TrashActivity.class));
                        break;
                    case "Sign out":
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        break;
                    case "Profile":
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
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
                startActivity(new Intent(MainActivity.this, DetailedActivity.class));

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
                startActivity(new Intent(MainActivity.this, ComposeEmailActivity.class));
            }
        });
    }

    private void fillListView() {
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

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result.get(0).equals("compose email") || result.get(0).equals("write an email"))
                        startActivity(new Intent(MainActivity.this, ComposeEmailActivity.class));
                    else if (result.get(0).equals("sign out") || result.get(0).equals("log out")) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    } else
                        Toast.makeText(this, "we didn't set it yet", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            //t1.shutdown();
        }
        super.onPause();
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
