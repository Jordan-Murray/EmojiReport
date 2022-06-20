package com.example.navexample;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LocationTag{
    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public LocationTag(Double latitude, Double longitude, String locationName) {
        Latitude = latitude;
        Longitude = longitude;
        this.locationName = locationName;
    }

    Double Latitude;
    Double Longitude;
    String locationName;

    public LocationTag() {
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}
