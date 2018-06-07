package com.example.abanoub.voicebasedemailsystem;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abanoub.voicebasedemailsystem.Shaking.MyService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;

public class SignInWithVoiceActivity extends Activity {

    TextView GotoSignUp;
    FirebaseAuth firebaseAuth;

    public static boolean isServiceRunning = false;

    //Handler work every x time
    Handler handler = new Handler();
    int delay = 3000; //1 second=1000 milisecond
    Runnable runnable;

    //UI View
    EditText usernameEdit, passwordEdit;
    Button signin_btn;
    String usernameString = null, passwordString = null;
    Boolean isUsername = false, isPassword = false;

    static String usernameSpeech = "Please enter your username";
    static String passSpeech = "Please enter your password";
    static String passError = "Password must be at least six character, Please enter your password again";
    static String loginFailedError = "Username or password is incorrect, please enter them again";
    static String loginSuccess = "Login successfully and you are in the inbox page";

    //Text to speech API
    TextToSpeech txtToSpeech;

    //speech to text API
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        startService(new Intent(this, MyService.class));

        firebaseAuth = FirebaseAuth.getInstance();

        usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        signin_btn = (Button) findViewById(R.id.signin_btn);
        GotoSignUp = (TextView) findViewById(R.id.signup_link);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("response", "Login");
                txtToSpeech.speak(passSpeech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        GotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInWithVoiceActivity.this, SignUp1Activity.class);
                startActivity(intent);
            }
        });

        txtToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    txtToSpeech.setLanguage(Locale.UK);
                }
            }
        });

//        usernameEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(password.getText())==false) {
//                    signin_btn.setEnabled(true);
//                } else {
//                    signin_btn.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//
//        passwordEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(username.getText())==false) {
//                    signin_btn.setEnabled(true);
//
//                } else {
//                    signin_btn.setEnabled(false);
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(SignInWithVoiceActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start handler as activity become visible
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                getDataUsingVoice();
                runnable = this;
            }
        }, delay);
    }

    private void getDataUsingVoice() {
        if (usernameString == null) {
            txtToSpeech.speak(usernameSpeech, TextToSpeech.QUEUE_FLUSH, null);
            //make app wait for 2 seconds then resume executing
            try {
                Thread.sleep(1900);
            } catch (InterruptedException ex) {
                Log.d("exception", ex.toString());
            }
            promptSpeechInput();
            isUsername = true;
            isPassword = false;
        } else if (passwordString == null) {
            txtToSpeech.speak(passSpeech, TextToSpeech.QUEUE_FLUSH, null);
            while (txtToSpeech.isSpeaking()) {}
            promptSpeechInput();
            isPassword = true;
            isUsername = false;
        } else {
            if (passwordString.length() < 6) {
                passwordString = null;
                txtToSpeech.speak(passError, TextToSpeech.QUEUE_FLUSH, null);
                while (txtToSpeech.isSpeaking()) { }
                promptSpeechInput();
                isPassword = true;
                isUsername = false;
            } else
                login();
        }
    }

    private void login() {
        if (Utilities.isNetworkAvailable(SignInWithVoiceActivity.this)) {
            if (usernameString.endsWith("@vmail.com")) {}
            else
                usernameString = usernameString + "@vmail.com";

            firebaseAuth.signInWithEmailAndPassword(usernameString, passwordString)
                    .addOnCompleteListener(SignInWithVoiceActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("response", "Succcccccccccccccccccccccessssssss");
                                txtToSpeech.speak(loginSuccess, TextToSpeech.QUEUE_FLUSH, null);
                                Intent intent = new Intent(SignInWithVoiceActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                txtToSpeech.speak(loginFailedError, TextToSpeech.QUEUE_FLUSH, null);
                                setDataToNull();
                                handler.postDelayed(runnable, delay);
                                Log.d("response", "Faileeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeed");
                            }
                        }
                    });
        } else {
            txtToSpeech.speak(getResources().getString(R.string.check_internet_connection), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void setDataToNull() {
        usernameEdit.setText("");
        passwordEdit.setText("");
        passwordString = null;
        usernameString = null;
        isUsername = false;
        isPassword = false;
    }

//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(true); //exit app
//    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible
    }

    @Override
    protected void onStop() {
        super.onStop();
        //called to stop handler if user login successfully
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //called to stop handler if user login successfully
        handler.removeCallbacksAndMessages(null);

        handler = null;
        runnable = null;
        txtToSpeech = null;
    }

    //speech to text
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (isUsername) {
                        //remove all space between characters
                        usernameString = result.get(0).replaceAll("\\s", "");
                        Log.i("username we get ", usernameString);
                        usernameEdit.setText(usernameString);
                    }

                    if (isPassword) {
                        //remove all space between characters
                        passwordString = result.get(0).replaceAll("\\s+", "");
                        Log.i("password we get ", result.get(0));
                        passwordEdit.setText(passwordString);
                    }
                }
                break;
            }
        }
    }
}