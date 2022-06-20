package com.example.navexample;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MoodScoreServiceTest {

    MoodScoreService moodScoreService;
    @Before
    public void setUp() throws Exception {
        moodScoreService = new MoodScoreService();
    }

    @Test
    public void givenAListOfThisWeeksMoodScoresThenCalculateAverageMoodEnum() {
        //Given
        List<MoodScore> thisWeeksMoodScores = new ArrayList<>();
        MoodScore moodScore1 = new MoodScore();
        moodScore1.setMoodScore(MoodEnum.VerySad);
        MoodScore moodScore2 = new MoodScore();
        moodScore2.setMoodScore(MoodEnum.VeryHappy);
        MoodScore moodScore3 = new MoodScore();
        moodScore3.setMoodScore(MoodEnum.Neutral);
        thisWeeksMoodScores.add(moodScore1);
        thisWeeksMoodScores.add(moodScore2);
        thisWeeksMoodScores.add(moodScore3);

        MoodEnum expectedResult = MoodEnum.Neutral;

        //When
        MoodEnum result = moodScoreService.getAverageMoodScore(thisWeeksMoodScores);

        //Then
        assertEquals(expectedResult,result);

    }

    @Test
    public void givenAListOfMoodValuesThenCalculateAverageMoodScore(){
        //Given
        List<Integer> moodValues = new ArrayList<Integer>();
        moodValues.add(3);
        moodValues.add(5);
        moodValues.add(1);

        int expectedResult = 3;

        //When
        int expectedAverage = moodScoreService.calculateAverage(moodValues);

        //Then
        assertNotEquals(0,expectedAverage);
        assertEquals(expectedResult,expectedAverage);
    }


    @Test
    public void givenThisWeeksAverageMoodReturnCorrectImage() {
        //Given
        MoodEnum thisWeeksAverageMood = MoodEnum.VeryHappy;
        int expectedResult = R.drawable.very_happy;

        //When
        int result = moodScoreService.SetThisWeeksAverageMoodImage(thisWeeksAverageMood);

        //Then
        assertEquals(expectedResult,result);
    }

    @Test
    public void givenNullWeeksAverageMoodReturnCrossMarks() {
        //Given
        int expectedResult = R.drawable.crossmark;

        //When
        int result = moodScoreService.SetThisWeeksAverageMoodImage(null);

        //Then
        assertEquals(expectedResult,result);
    }

    @Test
    public void getAverageMoodForLocation() {
    }
}