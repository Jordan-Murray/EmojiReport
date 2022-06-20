package com.example.navexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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
import java.util.Random;
import java.util.UUID;

public class CreateATeam extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    UserHelperClass userProfile;

    ListView listView;
    Button _saveChanges;
    Button _goBack;
    EditText _teamName;
    TextView teamPassword;

    String teamPasswordString;

    final FirebaseDatabase db = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/");
    final DatabaseReference usersRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_a_team);

        userId = user.getUid();

        _teamName = findViewById(R.id.createATeam_teamName);
        teamPassword = findViewById(R.id.createATeam_teamPassword);
        _saveChanges = findViewById(R.id.createATeam_saveChanges);
        _goBack = findViewById(R.id.createATeam_goBack);

        teamPasswordString = GeneratePassword();
        teamPassword.setText("Team Password: " + teamPasswordString);


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Assign user profile.
                userProfile = dataSnapshot.child(userId).getValue(UserHelperClass.class);
                setMembersListView(new ArrayList<UserReferenceClass>() {{add( new UserReferenceClass(userId,userProfile.displayName));}});
                if(userProfile.teamRef != null)
                    SetMembersListViewForTeamFromProfile(userProfile.teamRef);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        _goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JoinOrCreateTeam.class));
            }
        });

        _saveChanges.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                final String TeamName = _teamName.getText().toString().trim();
                final DatabaseReference teamDbRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Teams");

                if(TextUtils.isEmpty(TeamName))
                {
                    _teamName.setError("Team Name is required.");
                    return;
                }
                
                TeamHelperClass team = new TeamHelperClass();
                team.setTeamName(TeamName);
                //team.setMembers(new ArrayList<UserReferenceClass>() {{add( new UserReferenceClass(userId,userProfile.displayName));}});
                setTeamDisplayName(team._TeamName);
                team.setTeamUID(UUID.randomUUID().toString());
                team.set_Password(teamPasswordString);
                teamDbRef.child(team._TeamUID).setValue(team);
                teamDbRef.child(team._TeamUID).child("Members").child(userId).setValue(new UserReferenceClass(userId,userProfile.displayName));
                usersRef.child(userId).child("teamRef").setValue(team._TeamUID);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String GeneratePassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    private void SetMembersListViewForTeamFromProfile(final String teamRef) {
        final DatabaseReference teamDbRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Teams").child(teamRef);

        teamDbRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Assign user profile.
                List<UserReferenceClass> userReferenceClassList = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.child("Members").getChildren()) {
                    UserReferenceClass user = ds.getValue(UserReferenceClass.class);
                    userReferenceClassList.add(user);
                }
                TeamHelperClass teamHelperClass = new TeamHelperClass();
                teamHelperClass.setMembers(userReferenceClassList);
                teamHelperClass._TeamName = dataSnapshot.child("TeamName").getValue(String.class);
                setTeamDisplayName(teamHelperClass._TeamName);
                setMembersListView(teamHelperClass.getMembers());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTeamDisplayName(String text){
        TextView textView = (TextView) findViewById(R.id.createATeam_teamName);
        textView.setAutoSizeTextTypeUniformWithConfiguration(
                1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
        textView.setText(text);
    }


    private void setMembersListView(List<UserReferenceClass> userReferenceClassList) {
        List<String> userDisplayNames = new ArrayList<>();
        for (UserReferenceClass user: userReferenceClassList) {
            userDisplayNames.add(user.displayName);
        }
        listView = (ListView) findViewById(R.id.createATeam_teamMembers);
        List<Integer> memberMoodImages = new ArrayList<Integer>();
        memberMoodImages.add(R.drawable.crossmark);
        ProgramAdapter programAdapter = new ProgramAdapter(getApplicationContext(), null, userDisplayNames,memberMoodImages,null,false);
        listView.setAdapter(programAdapter);
    }

}
