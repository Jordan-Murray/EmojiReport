package com.example.navexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class JoinATeam extends AppCompatActivity {

    Button _joinTeam;
    Button _goBack;
    EditText _teamPassword;
    String userId;
    UserHelperClass userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_a_team);

        _teamPassword = findViewById(R.id.joinATeam_teamPassword);
        _joinTeam = findViewById(R.id.joinATeam_joinTeam);
        _goBack = findViewById(R.id.joinATeam_goBack);

        _goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JoinOrCreateTeam.class));
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        final DatabaseReference ref = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Teams");

        final FirebaseDatabase db = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/");

        _joinTeam.setOnClickListener(v -> {
            // Read from the database

            final String password = _teamPassword.getText().toString().trim();
            final DatabaseReference usersRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
            final DatabaseReference teamRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Teams");

            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Assign user profile.
                    userProfile = dataSnapshot.child(userId).getValue(UserHelperClass.class);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
            //build child
            teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Add teamRef to user.
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        String teamPass = (String) map.get("_Password");
                        if(teamPass != null)
                        {
                            if(teamPass.equals(password))
                            {
                                String teamUID = ds.getKey();
                                usersRef.child(userId).child("teamRef").setValue(teamUID);
                                teamRef.child(teamUID).child("Members").child(userId).setValue(new UserReferenceClass(userId,userProfile.displayName));
                                Toast.makeText(JoinATeam.this, "Joined team!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
        });
    }
}
