package com.example.navexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class JoinOrCreateTeam extends AppCompatActivity {

    Button _joinTeam;
    Button _createTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_or_create_team);

        _joinTeam = findViewById(R.id.joinOrCreateTeam_joinTeam);
        _createTeam = findViewById(R.id.joinOrCreateTeam_createTeam);

        _joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JoinATeam.class));
            }
        });

        _createTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateATeam.class));
            }
        });
    }
}
