package com.project.oneco.test;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.oneco.R;
import com.project.oneco.SoundMeter;

public class TestActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        SoundMeter soundMeter = new SoundMeter();
    }
}
