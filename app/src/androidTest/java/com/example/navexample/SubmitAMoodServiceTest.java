package com.example.navexample;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class SubmitAMoodServiceTest {

    SubmitAMoodService submitAMoodService;

    @Before
    public void setUp() throws Exception {
        submitAMoodService = new SubmitAMoodService();
    }

    @Test
    public void given1stDayWhenGetSuffixIsCalledThenCorrectSuffixIsReturned() {
        //Given
        int day = 1;

        String expectedSuffix = "st";
        //When
        String resultSuffix = submitAMoodService.getDayNumberSuffix(day);

        //Then
        assertNotEquals(0,resultSuffix.length());
        assertEquals(expectedSuffix,resultSuffix);
    }

    @Test
    public void given2ndDayWhenGetSuffixIsCalledThenCorrectSuffixIsReturned() {
        //Given
        int day = 2;

        String expectedSuffix = "nd";
        //When
        String resultSuffix = submitAMoodService.getDayNumberSuffix(day);

        //Then
        assertNotEquals(0,resultSuffix.length());
        assertEquals(expectedSuffix,resultSuffix);
    }

    @Test
    public void given3rdDayWhenGetSuffixIsCalledThenCorrectSuffixIsReturned() {
        //Given
        int day = 3;

        String expectedSuffix = "rd";
        //When
        String resultSuffix = submitAMoodService.getDayNumberSuffix(day);

        //Then
        assertNotEquals(0,resultSuffix.length());
        assertEquals(expectedSuffix,resultSuffix);
    }

    @Test
    public void given4thDayWhenGetSuffixIsCalledThenCorrectSuffixIsReturned() {
        //Given
        int day = 4;

        String expectedSuffix = "th";
        //When
        String resultSuffix = submitAMoodService.getDayNumberSuffix(day);

        //Then
        assertNotEquals(0,resultSuffix.length());
        assertEquals(expectedSuffix,resultSuffix);
    }
}