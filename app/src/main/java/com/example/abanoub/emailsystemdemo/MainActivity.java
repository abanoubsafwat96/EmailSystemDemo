package com.example.abanoub.emailsystemdemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    DrawerLayout myDrawerlayout;
    ActionBarDrawerToggle myToggle;
    ListView listView;
    MessageAdapter adapter;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myToggle = new ActionBarDrawerToggle(this, myDrawerlayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        myDrawerlayout.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.EmailsListView);
        adapter = new MessageAdapter(getApplicationContext());
        listView.setAdapter(adapter);

        FloatingActionButton mic = (FloatingActionButton) findViewById(R.id.mic);
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

        FloatingActionButton composeEmail = (FloatingActionButton) findViewById(R.id.composeEmail);
        composeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ComposeEmailActivity.class));
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (myToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                return true;
            case R.id.profile_menu:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true); //exit app
    }
}
