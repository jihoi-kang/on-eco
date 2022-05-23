package com.project.oneco.test;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

    public MyXAxisValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value) {
        return getDisplayDay(value);
    }

    /**
     * 일: 0
     * 월: 1
     * 화: 2
     * 수: 3
     * 목: 4
     * 금: 5
     * 토: 6
     */
    private String getDisplayDay(float value) {
        if (value == 0) {
            return "일";
        } else if (value == 1) {
            return "월";
        } else if (value == 2) {
            return "화";
        } else if (value == 3) {
            return "수";
        } else if (value == 4) {
            return "목";
        } else if (value == 5) {
            return "금";
        } else if (value == 6) {
            return "토";
        } else {
            return "Unknown";
        }
    }

}