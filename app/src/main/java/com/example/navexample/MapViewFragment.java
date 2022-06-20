package com.example.navexample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView mapView;
    LatLng myPosition;
    LocationTag locationTag;
    Button goBackButton;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taglocationmap, container, false);

        goBackButton = view.findViewById(R.id.tagLocationMap_goBack);
        String userRef = this.getArguments().getString("UserRef");

        goBackButton.setOnClickListener(v -> {
            String[] userIdArr = {userRef};
            MainActivity.loadFragment(new MyProfileFragment(), new MapViewFragment(),userIdArr);
        });
        // Gets the MapView from the XML layout and creates it
        locationTag = new LocationTag(this.getArguments().getDouble("Lat"),this.getArguments().getDouble("Long"),this.getArguments().getString("Name"));

        mapView = view.findViewById(R.id.tagLocationMap_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        SetMapOnLocation(googleMap);
    }

    @SuppressLint("MissingPermission")
    private void SetMapOnLocation(GoogleMap googleMap) {
        double latitude = locationTag.getLatitude();
        double longitude = locationTag.getLongitude();
        myPosition = new LatLng(latitude, longitude);
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.addMarker(new MarkerOptions().position(myPosition));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 10));

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
}
