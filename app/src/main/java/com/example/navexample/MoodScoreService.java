package com.example.navexample;

import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MoodScoreService {

    public MoodScoreService() {
    }

    public MoodEnum getAverageMoodScore(List<MoodScore> thisWeeksMoodScores) {
        List<Integer> moodValues = new ArrayList<>();
        MoodEnum averageMood = null;
        for (MoodScore moodScore: thisWeeksMoodScores) {
            int moodValue = 0;
            switch (moodScore.MoodScore)
            {
                case VerySad:
                    moodValue = 1;
                    break;
                case Sad:
                    moodValue = 2;
                    break;
                case Neutral:
                    moodValue = 3;
                    break;
                case Happy:
                    moodValue = 4;
                    break;
                case VeryHappy:
                    moodValue = 5;
                    break;
            }
            moodValues.add(moodValue);
        }
        int averageMoodScore = calculateAverage(moodValues);
        if(averageMoodScore == 1)
            averageMood = MoodEnum.VerySad;
        else if(averageMoodScore == 2)
            averageMood = MoodEnum.Sad;
        else if(averageMoodScore == 3)
            averageMood = MoodEnum.Neutral;
        else if(averageMoodScore == 4)
            averageMood = MoodEnum.Happy;
        else if(averageMoodScore == 5)
            averageMood = MoodEnum.VeryHappy;
        return averageMood;
    }

    public static int calculateAverage(List<Integer> moodValues) {
        Integer sum = 0;
        if(!moodValues.isEmpty()) {
            for (Integer value : moodValues) {
                sum += value;
            }
            return (int) (sum.doubleValue() / moodValues.size());
        }
        return sum;
    }

    public int SetThisWeeksAverageMoodImage(MoodEnum thisWeeksAverageMood) {
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
        else
            return R.drawable.crossmark;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MoodEnum GetAverageMoodForLocation(String locationName, List<MoodScore> moodScoreLocationTags) {

        List<MoodScore> moodScores = moodScoreLocationTags.stream().filter(l -> l.LocationTag.locationName.equals(locationName)).collect(Collectors.toList());
        return getAverageMoodScore(moodScores);
    }

    public List<MoodScore> MoodEnumToScoreTransformer(List<MoodEnum> teamMoodEnums) {
        List<MoodScore> moodScoreList = new ArrayList<>();
        for (MoodEnum moodEnum: teamMoodEnums) {
            MoodScore moodScore = new MoodScore();
            moodScore.MoodScore = moodEnum;
            moodScoreList.add(moodScore);
        }
        return moodScoreList;
    }
}
