package com.example.navexample;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

public class SubmitAMoodService {

    public SubmitAMoodService() {
    }

    public String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public int GetWeekOfYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DayOfWeek GetCurrentDayOfWeek() {
        LocalDate localDate = LocalDate.now();
        return localDate.getDayOfWeek();
    }


}
