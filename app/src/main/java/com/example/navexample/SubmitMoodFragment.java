package com.example.navexample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SubmitMoodFragment extends Fragment implements OnMapReadyCallback {

    TextView date;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    Button submitButton;
    EditText _locationTagTitle;

    private GoogleMap map;
    private MapView mapView;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    LatLng myPosition;
    Location myLocation;

    int[] buttonIDs = new int[]{R.id.submitAMood_verySad, R.id.submitAMood_sad, R.id.submitAMood_neutral, R.id.submitAMood_happy, R.id.submitAMood_veryHappy};

    MoodEnum selectedMoodEnum;
    private static int verySad = 0;
    private static int sad = 1;
    private static int neutral = 2;
    private static int happy = 3;
    private static int veryHappy = 4;

    DayOfWeek dayOfWeek;
    int weekOfYear;
    boolean allowEntryToday;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    UserHelperClass userProfile;

    SubmitAMoodService submitAMoodService = new SubmitAMoodService();

    final FirebaseDatabase db = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/");
    final DatabaseReference usersRef = FirebaseDatabase.getInstance("https://emoji-report-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submitmood, container, false);
        requestLocationPermission();

        date = view.findViewById(R.id.submitAMood_date);
        radioGroup = view.findViewById(R.id.submitAMood_radioButtonGroup);
        submitButton = view.findViewById(R.id.submitAMood_submit);
        _locationTagTitle = view.findViewById(R.id.submitAMood_locationTag);

        verySad = view.findViewById(R.id.submitAMood_verySad).getId();
        sad = view.findViewById(R.id.submitAMood_sad).getId();
        neutral = view.findViewById(R.id.submitAMood_neutral).getId();
        happy = view.findViewById(R.id.submitAMood_happy).getId();
        veryHappy = view.findViewById(R.id.submitAMood_veryHappy).getId();

        // Gets the MapView from the XML layout and creates it
        mapView = view.findViewById(R.id.submitAMood_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        dayOfWeek = submitAMoodService.GetCurrentDayOfWeek();
        weekOfYear = submitAMoodService.GetWeekOfYear();

        Calendar cal = Calendar.getInstance();
        String dayNumberSuffix = submitAMoodService.getDayNumberSuffix(cal.get(Calendar.DAY_OF_MONTH));
        String currentDate = new SimpleDateFormat("EEEE d'" + dayNumberSuffix + "' MMMM yyy", Locale.getDefault()).format(new Date());

        if(userProfile == null)
            GetUser();

        date.setText(currentDate);
        setHighlightForSelectedButton();
        submitButtonOnClick();

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLastLocationNewMethod(googleMap);
    }

    @SuppressLint("MissingPermission")
    private void SetMapOnLocation(GoogleMap googleMap) {
        if(myLocation !=null)
        {
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            myPosition = new LatLng(latitude, longitude);
            map = googleMap;
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.addMarker(new MarkerOptions().position(myPosition));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 10));
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocationNewMethod(GoogleMap googleMap){
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            myLocation = location;
                            SetMapOnLocation(googleMap);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(getContext(), perms)) {
            //Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setHighlightForSelectedButton() {
        // get selected radio button from radioGroup
        radioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            // find the radiobutton by returned id
            ResetButtonHighlight();
            selectedRadioButton = (RadioButton) getView().findViewById(checkedId);
            Drawable background = selectedRadioButton.getBackground();
            background.setTint(getResources().getColor(R.color.colorButtonHighlight));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ResetButtonHighlight() {
        for (int i = 0; i < buttonIDs.length; i++)
        {
            RadioButton resetBackground = (RadioButton) getView().findViewById(buttonIDs[i]);
            Drawable background = resetBackground.getBackground();
            background.setTint(getResources().getColor(R.color.colorTextWhite));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void submitButtonOnClick() {
        submitButton.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if(selectedId == verySad)
                selectedMoodEnum = MoodEnum.VerySad;
            else if (selectedId == sad)
                selectedMoodEnum = MoodEnum.Sad;
            else if (selectedId == neutral)
                selectedMoodEnum = MoodEnum.Neutral;
            else if (selectedId == happy)
                selectedMoodEnum = MoodEnum.Happy;
            else if (selectedId == veryHappy)
                selectedMoodEnum = MoodEnum.VeryHappy;
            else{
                Toast.makeText(getContext(),
                        "Please select a mood", Toast.LENGTH_SHORT).show();
            }

            String locationTagTitle = _locationTagTitle.getText().toString().trim();

            if(TextUtils.isEmpty(locationTagTitle))
            {
                _locationTagTitle.setError("Location Tag is required.");
                return;
            }

            LocationTag locationTag = new LocationTag(myLocation.getLatitude(),myLocation.getLongitude(),locationTagTitle);

            MoodScore todaysMoodScore = new MoodScore(selectedMoodEnum,dayOfWeek,locationTag);

            if(allowEntryToday && (todaysMoodScore.MoodScore != null && todaysMoodScore.LocationTag != null))
            {
                usersRef.child(userId).child("MoodScores").child(String.valueOf(weekOfYear)).child(String.valueOf(dayOfWeek)).setValue(todaysMoodScore);
                Toast.makeText(getContext(), "Mood Score for " + todaysMoodScore.Day + " - " + todaysMoodScore.MoodScore + " has been submitted", Toast.LENGTH_SHORT).show();
                allowEntryToday = false;
                ResetButtonHighlight();
            }
            else if(!allowEntryToday)
                Toast.makeText(getContext(), "You have already entered a mood score for today", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Please fill out all elements to submit today's mood score", Toast.LENGTH_SHORT).show();
        });
    }

    private void GetUser() {
        userId = user.getUid();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Assign user profile.
                userProfile = dataSnapshot.child(userId).getValue(UserHelperClass.class);
                if(!dataSnapshot.child(userId).child("MoodScores").hasChild(String.valueOf(weekOfYear)))
                {
                    allowEntryToday = true;
                }else{
                    if(!dataSnapshot.child(userId).child("MoodScores").child(String.valueOf(weekOfYear)).hasChild(String.valueOf(dayOfWeek)))
                        allowEntryToday = true;
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }
}
