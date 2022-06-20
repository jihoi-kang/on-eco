package com.project.oneco.test;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.project.oneco.Statistic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

    public MyXAxisValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value) {
        return getDisplayDay(value);
    }

    private String getDisplayDay(float value) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE", Locale.KOREA);
        Date date = new Date(Statistic.selectedDate.getTime() - 1000 * 60 * 60 * 24 * (6 - (int) value));
        String dayName = simpledateformat.format(date);

        return dayName;
    }

}