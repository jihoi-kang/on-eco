package com.project.oneco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.project.oneco.test.TestActivity;

public class MainHome extends AppCompatActivity {

    // todo: layout 공통 => 필요한 곳에 ScrollView 달아주기


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        onBackPressed();

        // Activity간의 데이터 공유를 위한 application 가져오기
        OnEcoApplication application = (OnEcoApplication) getApplication();

        ImageButton goto_mypage = findViewById(R.id.goto_mypage);
        goto_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent);
            }
        });

        Button goto_write_trash = findViewById(R.id.goto_write_trash);
        goto_write_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteTrash.class);
                startActivity(intent);
            }
        });

        Button goto_write_water = findViewById(R.id.goto_write_water);
        goto_write_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                application.active_activity = "mainHome";

                Intent intent = new Intent(getApplicationContext(), WriteWater.class);
                startActivity(intent);
            }
        });

        Button goto_statistic = findViewById(R.id.goto_statistic);
        goto_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                application.active_activity = "mainHome";

                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });

        Button goto_shower_game = findViewById(R.id.goto_shower_game);
        goto_shower_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterStopGame.class);
                startActivity(intent);
            }
        });
    }   // end of onCreate

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}   // end of class