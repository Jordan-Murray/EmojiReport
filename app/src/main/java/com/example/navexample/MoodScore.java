package com.example.navexample;

import android.location.Location;

import java.time.DayOfWeek;

public class MoodScore {

    DayOfWeek Day;
    MoodEnum MoodScore;

    public com.example.navexample.LocationTag getLocationTag() {
        return LocationTag;
    }

    public void setLocationTag(com.example.navexample.LocationTag locationTag) {
        LocationTag = locationTag;
    }

    LocationTag LocationTag;

    public MoodScore() {
    }

    public MoodScore(MoodEnum moodScore, DayOfWeek day, LocationTag locationTag) {
        MoodScore = moodScore;
        Day = day;
        LocationTag = locationTag;
    }

    public MoodEnum getMoodScore() {
        return MoodScore;
    }

    public void setMoodScore(MoodEnum moodScore) {
        MoodScore = moodScore;
    }

    public DayOfWeek getDay() {
        return Day;
    }

    public void setDay(DayOfWeek day) {
        this.Day = day;
    }


}
