package com.example.navexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText _displayName, _eMail, _password, _confirmPassword, _phoneNumber;
    Button _signUp;
    FirebaseAuth _auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _displayName = findViewById(R.id.register_displayName);
        _phoneNumber = findViewById(R.id.register_phoneNumber);
        _eMail = findViewById(R.id.register_email);
        _password = findViewById(R.id.register_password);
        _confirmPassword = findViewById(R.id.register_confirmPassword);
        _signUp = findViewById(R.id.register_signUp);

        _auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.register_progressBar);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/");
        final DatabaseReference reference = database.getReference("Users");

        if(_auth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        _signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String displayName = _displayName.getText().toString().trim();
                final String email = _eMail.getText().toString().trim();
                final String phoneNumber = _phoneNumber.getText().toString().trim();
                final String password = _password.getText().toString().trim();
                String confirmPassword = _confirmPassword.getText().toString().trim();

                if(TextUtils.isEmpty(displayName))
                {
                    _displayName.setError("Display Name is required.");
                    return;
                }
                if(TextUtils.isEmpty(email))
                {
                    _eMail.setError("E-mail is required.");
                    return;
                }
                if(TextUtils.isEmpty(phoneNumber))
                {
                    _eMail.setError("E-mail is required.");
                    return;
                }
                if(phoneNumber.length() != 11)
                {
                    _eMail.setError("Please enter a valid phone number");
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
                if(TextUtils.isEmpty(confirmPassword))
                {
                    _confirmPassword.setError("Please re-enter your password to confirm.");
                    return;
                }
                if(!password.equals(confirmPassword))
                {
                    _confirmPassword.setError("Your passwords did not match.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                UserHelperClass userHelperClass = new UserHelperClass(displayName,email,phoneNumber,password);


                _auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            //get firebase user
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            //get reference
                            DatabaseReference ref = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
                            //build child
                            ref.child(user.getUid()).setValue(new UserHelperClass(displayName,email,phoneNumber,password));
                            startActivity(new Intent(getApplicationContext(), JoinOrCreateTeam.class));
                        }else{
                            Toast.makeText(Register.this, "An error has occurred " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}