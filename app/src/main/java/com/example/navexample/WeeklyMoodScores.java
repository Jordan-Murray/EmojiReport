package com.example.navexample;

import java.util.List;

public class WeeklyMoodScores {

    List<MoodScore> MoodScores;

    public WeeklyMoodScores() {
    }

    public void addMoodToList(MoodScore moodScore)
    {
        if(MoodScores.size() <= 6)
            MoodScores.add(moodScore);
    }

    public WeeklyMoodScores(List<MoodScore> moodScoreList) {
        this.MoodScores = moodScoreList;
    }

    public List<MoodScore> getMoodScores() {
        return MoodScores;
    }

    public void setMoodScores(List<MoodScore> moodScores) {
        this.MoodScores = moodScores;
    }
}
