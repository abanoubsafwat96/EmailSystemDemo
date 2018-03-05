package com.example.abanoub.emailsystemdemo;

import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.abanoub.emailsystemdemo.R.id.editText;
import static com.example.abanoub.emailsystemdemo.R.id.email;

public class DetailedActivity extends AppCompatActivity {

    TextView title,sender,date,body;
    ImageView star_btn;
    CircleImageView profile_image;
    LinearLayout replay,forward;
    TextToSpeech textToSpeech;
    NewEmail clicked_email;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        clicked_email=getIntent().getParcelableExtra("email");
        String child=getIntent().getStringExtra("child");

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child(child);

        title= (TextView) findViewById(R.id.title);
        sender= (TextView) findViewById(R.id.sender);
        date= (TextView) findViewById(R.id.date);
        body= (TextView) findViewById(R.id.body);
        star_btn= (ImageView) findViewById(R.id.star);
        profile_image= (CircleImageView) findViewById(R.id.profile_image);
        replay= (LinearLayout) findViewById(R.id.replayLinear);
        forward= (LinearLayout) findViewById(R.id.forwardLinear);
        FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);

        title.setText(clicked_email.title);
        sender.setText(clicked_email.sender);
        date.setText(clicked_email.date);
        body.setText(clicked_email.body);
//        profile_image.setImageResource();

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = body.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
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
        inflater.inflate(R.menu.detailed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_menu:

                //delete emai;
                databaseReference.child(clicked_email.pushID).setValue(null);
                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
