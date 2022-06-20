package com.example.navexample;

import java.util.List;

public class UserHelperClass {
    String displayName;
    String email;
    String phoneNumber;
    String password;
    String teamRef;
    List<WeeklyMoodScores> weeklyMoodScores;

    public UserHelperClass() {
    }

    public UserHelperClass(String displayName, String email, String phoneNumber,String password) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public List<WeeklyMoodScores> getWeeklyMoodScoresList() {
        return weeklyMoodScores;
    }

    public void setWeeklyMoodScoresList(List<WeeklyMoodScores> weeklyMoodScoresList) {
        this.weeklyMoodScores = weeklyMoodScoresList;
    }

    public String getTeamRef() { return teamRef; }

    public void setTeamRef(String teamRef) { this.teamRef = teamRef; }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
