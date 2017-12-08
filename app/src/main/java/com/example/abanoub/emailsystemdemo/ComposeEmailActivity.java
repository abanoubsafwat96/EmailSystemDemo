package com.example.abanoub.emailsystemdemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class ComposeEmailActivity extends AppCompatActivity {

    EditText senderED;
    EditText receiverED;
    EditText titleED;
    EditText bodyED;

    FloatingActionButton speak_btn;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        senderED = (EditText) findViewById(R.id.senderED);
        receiverED = (EditText) findViewById(R.id.receiverED);
        titleED = (EditText) findViewById(R.id.titleED);
        bodyED = (EditText) findViewById(R.id.bodyED);

        speak_btn = (FloatingActionButton) findViewById(R.id.fab);
        speak_btn.setOnClickListener(new View.OnClickListener() {
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
                    bodyED.append(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_email_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_menu:

                if (TextUtils.isEmpty(senderED.getText()) || TextUtils.isEmpty(receiverED.getText())
                        || TextUtils.isEmpty(bodyED.getText()))
                    Toast.makeText(ComposeEmailActivity.this, R.string.fill_all_data, Toast.LENGTH_SHORT).show();
                else {

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
