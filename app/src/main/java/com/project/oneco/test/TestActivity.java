package com.project.oneco.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.oneco.R;

import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {
    private TextView tvSec;
    private TimerTask second;
    private int timerSec = 0;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvSec = findViewById(R.id.tv_sec);
        Button btnNext = findViewById(R.id.btn_next);
        Button btnStart = findViewById(R.id.btn_start);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TestActivity2.class);
                startActivity(intent);
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testStart();
            }
        });
    }

    public void testStart() {
        timerSec = 0;
        second = new TimerTask() {
            @Override
            public void run() {
                Log.i("Test", "Timer start");
                updateUi();
                timerSec++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }

    private void updateUi() {
        Runnable updater = new Runnable() {
            public void run() {
                tvSec.setText(timerSec + "초");
            }
        };
        handler.post(updater);
    }
}
