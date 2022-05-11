package com.project.oneco;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class Statistic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo: UI 화면 만들기(activity_statistic.xml), 그래프 제외(다음주 수업때 알려드릴게요~)
        setContentView(R.layout.activity_statistic);

        OnEcoApplication application = (OnEcoApplication) getApplication();
        int number = application.number;
        int count = application.count;

        Log.d("jay", "Statistic number: " + number);
        Log.d("jay", "Statistic count: " + count);
    }
}