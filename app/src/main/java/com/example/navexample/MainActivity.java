package com.example.navexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    static Fragment savedFragment = new Fragment();

    Fragment myTeamFragment;
    Fragment submitAMoodFragment;
    Fragment myProfileFragment;

    static FragmentManager fragmentManager;
    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        fragmentManager = getSupportFragmentManager();

        myTeamFragment = new MyTeamFragment();
        submitAMoodFragment = new SubmitMoodFragment();
        myProfileFragment = new MyProfileFragment();

        if (savedInstanceState != null)
        {
            savedFragment = fragmentManager.getFragment(savedInstanceState, "Frag");
        }
        else
            loadFragment(myTeamFragment,null);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        fragmentManager.putFragment(outState, "SavedFragment", savedFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment hideFragment = selectedFragment;

                    switch (item.getItemId())
                    {
                        case R.id.van_myTeam:
                            selectedFragment = myTeamFragment;
                            break;
                        case R.id.van_submitMood:
                            selectedFragment = submitAMoodFragment;
                            break;
                        case R.id.van_profile:
                            selectedFragment = myProfileFragment;
                            String[] userIdArr = {user.getUid()};
                            return(loadFragment(selectedFragment,hideFragment,userIdArr));
                    }

                    return loadFragment(selectedFragment,hideFragment);
                }
            };

    public static boolean loadFragment(Fragment selectedFragment, Fragment hideFragment) {
        if (selectedFragment != null)
        {
            if(hideFragment !=null)
            {
                fragmentManager.beginTransaction().hide(hideFragment).commit();
            }
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            fragmentManager.beginTransaction().show(selectedFragment).commit();

            savedFragment = selectedFragment;
            return true;
        }
        return false;
    }

    public static boolean loadFragment(Fragment selectedFragment, Fragment hideFragment, String[] info) {
        Bundle bundle = new Bundle();
        if(info.length == 1)
            bundle.putString("Info",info[0]);
        else{
            bundle.putDouble("Lat", Double.parseDouble(info[0]));
            bundle.putDouble("Long", Double.parseDouble(info[1]));
            bundle.putString("Name",info[2]);
            bundle.putString("UserRef",info[3]);
        }
        if (selectedFragment != null)
        {
            if(hideFragment !=null)
            {
                fragmentManager.beginTransaction().hide(hideFragment).commit();
            }
            selectedFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            fragmentManager.beginTransaction().show(selectedFragment).commit();

            savedFragment = selectedFragment;
            return true;
        }
        return false;
    }
}