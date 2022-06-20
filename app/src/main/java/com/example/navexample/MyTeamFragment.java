package com.example.navexample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyTeamFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    UserHelperClass userProfile;

    TeamHelperClass currentTeam;
    TextView teamName;
    TextView teamPassword;
    ImageView teamAverageMoodScoreImage;
    MoodEnum teamAverageMoodScore;

    ListView listView;
    TextView currentWeek;
    int weekSearchingInt;

    ImageButton leftArrow;
    ImageButton rightArrow;
    Button leaveTeam;

    MoodScoreService moodScoreService = new MoodScoreService();

    List<Integer> memberMoodImages = new ArrayList<>();

    final DatabaseReference usersRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
    final DatabaseReference teamDbRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Teams");


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myteam, container, false);

        currentWeek = view.findViewById(R.id.myTeam_weekNumber);
        listView = view.findViewById(R.id.myTeam_teamMembers);
        teamName =view.findViewById(R.id.myTeam_teamName);
        teamAverageMoodScoreImage = view.findViewById(R.id.myTeam_averageMoodImage);
        leftArrow = view.findViewById(R.id.myTeam_weekNumberLeftArrow);
        rightArrow = view.findViewById(R.id.myTeam_weekNumberRightArrow);
        leaveTeam = view.findViewById(R.id.myTeam_leaveTeam);
        teamPassword = view.findViewById(R.id.myTeam_teamPassword);

        weekSearchingInt = Integer.parseInt(MyProfileFragment.GetWeekOfYear());

        if(userProfile == null || currentTeam == null || currentTeam._Members.size() == 0)
            GetUserProfile();
        else{
            setTeamDisplayNameAndDate(currentTeam._TeamName, weekSearchingInt);
            setMembersListView(currentTeam.getMembers());
            teamAverageMoodScoreImage.setImageResource(moodScoreService.SetThisWeeksAverageMoodImage(teamAverageMoodScore));
        }

        leaveTeam.setOnClickListener(v -> {

            teamDbRef.child(userProfile.teamRef).child("Members").child(userId).removeValue();
            usersRef.child(user.getUid()).child("teamRef").setValue(null);
            startActivity(new Intent(getActivity(), JoinOrCreateTeam.class));
        });

        leftArrow.setOnClickListener(v -> {
            if(weekSearchingInt >= 2)
            {
                weekSearchingInt--;
                GetTeamFromProfile(userProfile.teamRef);
            }
        });

        rightArrow.setOnClickListener(v -> {
            if(weekSearchingInt <= 51)
            {
                weekSearchingInt++;
                GetTeamFromProfile(userProfile.teamRef);
            }
        });

        return view;
    }

    private void GetUserProfile() {
        userId = user.getUid();

        usersRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Assign user profile.
                userProfile = dataSnapshot.child(userId).getValue(UserHelperClass.class);
                if(userProfile.teamRef != null)
                    GetTeamFromProfile(userProfile.teamRef);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public static int GetImage(MoodEnum thisWeeksAverageMood) {
        if(thisWeeksAverageMood == MoodEnum.VerySad)
            return R.drawable.very_sad;
        else if(thisWeeksAverageMood == MoodEnum.Sad)
            return R.drawable.sad;
        else if(thisWeeksAverageMood == MoodEnum.Neutral)
            return R.drawable.neutral;
        else if(thisWeeksAverageMood == MoodEnum.Happy)
            return R.drawable.happy;
        else if(thisWeeksAverageMood == MoodEnum.VeryHappy)
            return R.drawable.very_happy;
        return 0;
    }

    void GetTeamFromProfile(final String teamRef) {
        List<MoodEnum> teamMoodEnums = new ArrayList<>();
        memberMoodImages.clear();
        teamDbRef.child(teamRef).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Assign user profile.
                    currentTeam = new TeamHelperClass();
                    List<UserReferenceClass> userReferenceClassList = new ArrayList<>();
                    String childPath;
                    if(dataSnapshot.hasChild("members"))
                        childPath = "members";
                    else
                        childPath = "Members";
                    for (DataSnapshot ds: dataSnapshot.child(childPath).getChildren()) {
                        UserReferenceClass user = ds.getValue(UserReferenceClass.class);
                        userReferenceClassList.add(user);
                    }
                    currentTeam._Members = userReferenceClassList;
                    currentTeam._TeamName = dataSnapshot.child("teamName").getValue(String.class);
                    currentTeam._Password = dataSnapshot.child("_Password").getValue(String.class);
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(int i = 0; i < currentTeam._Members.size(); i++)
                            {
                                MoodEnum thisWeeksAverageMood;
                                List<MoodScore> thisWeeksMoodScores = new ArrayList<MoodScore>();
                                for (DataSnapshot ds: dataSnapshot.child(currentTeam._Members.get(i).UserRef).child("MoodScores").child(String.valueOf(weekSearchingInt)).getChildren()) {
                                    MoodScore moodScore = ds.getValue(MoodScore.class);
                                    thisWeeksMoodScores.add(moodScore);
                                }
                                if(!thisWeeksMoodScores.isEmpty())
                                {
                                    //Get average mood from this weeks moods
                                    thisWeeksAverageMood  = moodScoreService.getAverageMoodScore(thisWeeksMoodScores);
                                    //Set image
                                    memberMoodImages.add(GetImage(thisWeeksAverageMood));
                                    //Add to team scores to calculate average
                                    teamMoodEnums.add(thisWeeksAverageMood);
                                }else{
                                    memberMoodImages.add(R.drawable.crossmark);
                                }
                            }
                            setTeamDisplayNameAndDate(currentTeam._TeamName, weekSearchingInt);
                            setMembersListView(currentTeam._Members);
                            teamAverageMoodScore = moodScoreService.getAverageMoodScore(moodScoreService.MoodEnumToScoreTransformer(teamMoodEnums));
                            teamAverageMoodScoreImage.setImageResource(moodScoreService.SetThisWeeksAverageMoodImage(teamAverageMoodScore));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                //}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }



    private void GetUserProfile(String _userId) {
        List<MoodScore> thisWeeksMoodScores = new ArrayList<MoodScore>();
        usersRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Assign user profile.
                userProfile = dataSnapshot.child(userId).getValue(UserHelperClass.class);
                //Get this weeks moods

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTeamDisplayNameAndDate(String text, int weekNo){
        currentWeek.setText("Week Number: " + weekNo);
        teamName.setText(text);
        teamPassword.setText("Team Password: " + currentTeam._Password);
    }

    private void setMembersListView(List<UserReferenceClass> userReferenceClassList) {
        if(this.getContext() != null)
        {
            List<UserReferenceClass> users = new ArrayList<>();
            List<String> userDisplayNames = new ArrayList<>();

            for (UserReferenceClass user: userReferenceClassList) {
                users.add(user);
                userDisplayNames.add(user.displayName);
            }
            ProgramAdapter programAdapter = new ProgramAdapter(this.getContext(), users, userDisplayNames,memberMoodImages,null,false);
            listView.setAdapter(programAdapter);
        }
    }
}
