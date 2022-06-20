package com.example.navexample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MyProfileFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    UserHelperClass userProfile;
    
    TextView displayName;
    TextView weekNumber;
    int weekSearchingInt;
    MoodEnum thisWeeksAverageMood;
    ImageView thisWeeksAverageMoodImage;

    ImageButton leftArrow;
    ImageButton rightArrow;

    Button logOut;

    List<Integer> taggedLocationImages = new ArrayList<>();
    ListView taggedLocationListView;
    List<String> locationTagNames = new ArrayList<>();
    List<UserReferenceClass> userIdList = new ArrayList<>();
    List<MoodScore> moodScoreLocationTags = new ArrayList<>();
    List<MoodScore> thisWeeksMoodScores = new ArrayList<MoodScore>();

    MoodScoreService moodScoreService = new MoodScoreService();

    final DatabaseReference usersRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);

        displayName = view.findViewById(R.id.myProfile_displayName);;
        thisWeeksAverageMoodImage = view.findViewById(R.id.myProfile_averageMoodImage);
        weekNumber = view.findViewById(R.id.myProfile_weekNumber);
        leftArrow = view.findViewById(R.id.myProfile_weekNumberLeftArrow);
        rightArrow = view.findViewById(R.id.myProfile_weekNumberRightArrow);
        taggedLocationListView = view.findViewById(R.id.myProfile_taggedLocationsList);
        logOut = view.findViewById(R.id.myProfile_logOut);
        weekSearchingInt = Integer.parseInt(GetWeekOfYear());

        userId = this.getArguments().getString("Info");

        if(!userId.equals(user.getUid()))
            logOut.setVisibility(View.GONE);

        GetUserProfile();

        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(view.getContext(), Login.class));
        });

        leftArrow.setOnClickListener(v -> {
            if(weekSearchingInt >= 2)
            {
                weekSearchingInt--;
                GetUserProfile();
            }
        });

        rightArrow.setOnClickListener(v -> {
            if(weekSearchingInt <= 51)
            {
                weekSearchingInt++;
                GetUserProfile();
            }
        });

        return view;
    }



    private void GetUserProfile() {
        thisWeeksMoodScores.clear();
        usersRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Assign user profile.
                userProfile = dataSnapshot.child(userId).getValue(UserHelperClass.class);
                //Get this weeks moods
                for (DataSnapshot ds: dataSnapshot.child(userId).child("MoodScores").child(String.valueOf(weekSearchingInt)).getChildren()) {
                    MoodScore moodScore = ds.getValue(MoodScore.class);
                    thisWeeksMoodScores.add(moodScore);
                }
                //Get average mood from this weeks moods
                thisWeeksAverageMood  = moodScoreService.getAverageMoodScore(thisWeeksMoodScores);
                //Set image
                thisWeeksAverageMoodImage.setImageResource(moodScoreService.SetThisWeeksAverageMoodImage(thisWeeksAverageMood));
                //Set display name
                setDisplayNameAndWeekNumber(userProfile.displayName);
                //Get list of locations and location names
                userIdList.add(new UserReferenceClass(userId,userProfile.displayName));
                locationTagNames.clear();
                taggedLocationImages.clear();
                GetAndSetTaggedLocationMoods(moodScoreLocationTags, thisWeeksMoodScores);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void GetAndSetTaggedLocationMoods(List<MoodScore> moodScoreLocationTags, List<MoodScore> thisWeeksMoodScores) {
        moodScoreLocationTags.clear();
        for (MoodScore moodScore: thisWeeksMoodScores) {
            moodScoreLocationTags.add(moodScore);
            if(moodScore.LocationTag != null)
            {
                if(!locationTagNames.contains(moodScore.LocationTag.locationName))
                {
                    locationTagNames.add(moodScore.LocationTag.locationName);
                }
            }
        }
        moodScoreLocationTags.removeIf(l -> l.LocationTag == null);
        List<LocationTag> locationTagList = new ArrayList<>();
        for (MoodScore distinctLocationtTags: moodScoreLocationTags) {
            MoodEnum locationEnum = moodScoreService.GetAverageMoodForLocation(distinctLocationtTags.LocationTag.locationName,moodScoreLocationTags);
            taggedLocationImages.add(MyTeamFragment.GetImage(locationEnum));
            locationTagList.add(distinctLocationtTags.LocationTag);
        }
        if(!locationTagNames.isEmpty())
        {
            setTaggedLocationsListView(userIdList,locationTagNames,locationTagList);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDisplayNameAndWeekNumber(String text){
        displayName.setText(text + "'s Profile");
        weekNumber.setText("Week Number: " + weekSearchingInt);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String GetWeekOfYear() {
        Calendar cal = Calendar.getInstance();
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        return String.valueOf(weekOfYear);
    }

    private void setTaggedLocationsListView(List<UserReferenceClass> userReferenceClassList, List<String> locationTagNames, List<LocationTag> locationTag) {
        if(this.getContext() != null)
        {
            List<UserReferenceClass> users = new ArrayList<>();

            for (UserReferenceClass user: userReferenceClassList) {
                users.add(user);
            }
            ProgramAdapter programAdapter = new ProgramAdapter(this.getContext(), users, locationTagNames,taggedLocationImages,locationTag,true);
            taggedLocationListView.setAdapter(programAdapter);
        }
    }
}
