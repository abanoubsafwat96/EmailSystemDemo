package com.example.abanoub.voicebasedemailsystem;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgetPasswordActivity extends Activity {

    NewUser newUser;
    EditText username, secretAnswer, newPassword, confirmPassword;
    TextView secretQuestion;
    Button getSecretQuestion_btn, update_btn;

    FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference personalDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        username = (EditText) findViewById(R.id.username);
        secretQuestion = findViewById(R.id.secretQuestion);
        secretAnswer = findViewById(R.id.secretAnswerED);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        getSecretQuestion_btn = findViewById(R.id.getSecretQA);
        update_btn = (Button) findViewById(R.id.update_btn);

        firebaseUser = Utilities.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
//        personalDataReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("PersonalData");

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(newPassword.getText()) == false
                        && TextUtils.isEmpty(confirmPassword.getText()) == false) {
                    update_btn.setEnabled(true);

                } else {
                    update_btn.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(username.getText()) == false
                        && TextUtils.isEmpty(confirmPassword.getText()) == false) {
                    update_btn.setEnabled(true);

                } else {
                    update_btn.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(newPassword.getText()) == false
                        && TextUtils.isEmpty(username.getText()) == false) {
                    update_btn.setEnabled(true);

                } else {
                    update_btn.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(ForgetPasswordActivity.this)) {
                    if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(newPassword.getText())
                            || TextUtils.isEmpty(confirmPassword.getText()))
                        Toast.makeText(ForgetPasswordActivity.this, R.string.fields_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    else {
                        if (newUser != null) {
                            if (username.getText().toString().equals(newUser.password)) {
                                if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                                    firebaseUser.updatePassword(newPassword.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // updating password success
                                                        newUser.password = newPassword.getText().toString();
                                                        personalDataReference.child(newUser.pushID).setValue(newUser);
                                                        Toast.makeText(ForgetPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        // If updating fails, display a message to the user.
                                                        Toast.makeText(ForgetPasswordActivity.this, "Password updating failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else
                                    Toast.makeText(ForgetPasswordActivity.this, R.string.passwords_donot_match, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(ForgetPasswordActivity.this, "Old password is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else
                    Toast.makeText(ForgetPasswordActivity.this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
