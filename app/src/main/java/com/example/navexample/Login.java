package com.example.navexample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText _eMail, _password;
    Button _logIn;
    Button _signUp;
    TextView _createButton;
    ProgressBar progressBar;
    FirebaseAuth _auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _eMail = findViewById(R.id.login_email);
        _password = findViewById(R.id.login_password);

        _auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_progressBar);
        _logIn = findViewById(R.id.login_logIn);
        _signUp = findViewById(R.id.login_signUp);



        if(_auth.getCurrentUser() != null)
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //get reference
            DatabaseReference ref = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Users").child(user.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final UserHelperClass profile = dataSnapshot.getValue(UserHelperClass.class);
                    if(profile != null)
                    {
                        if(profile.teamRef == null)
                        {
                            startActivity(new Intent(getApplicationContext(), JoinOrCreateTeam.class));
                        }else{
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }else{
                                Toast.makeText(Login.this, "Somethings gone wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        _logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = _eMail.getText().toString().trim();
                String password = _password.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    _eMail.setError("E-mail is required.");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    _password.setError("Password is required.");
                    return;
                }
                if(password.length() < 6)
                {
                    _password.setError("Your password must be longer than 6 characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                _auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            //get firebase user
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            //get reference
                            DatabaseReference ref = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("Users").child(user.getUid());

                            //grab info
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final UserHelperClass profile = dataSnapshot.getValue(UserHelperClass.class);
                                    if(profile.teamRef == null)
                                    {
                                        startActivity(new Intent(getApplicationContext(), JoinOrCreateTeam.class));
                                    }else{
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }else{
                            Toast.makeText(Login.this, "Something went wrong " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void signUp(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }
}