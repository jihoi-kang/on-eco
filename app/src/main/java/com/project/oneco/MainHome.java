package com.project.oneco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.TrashUsage;
import com.project.oneco.data.WaterUsage;
import com.project.oneco.test.TestActivity;

public class MainHome extends AppCompatActivity {

    // todo: layout 공통 =>        application = (OnEcoApplication) getApplication(); 필요한 곳에 ScrollView 달아주기


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        // Activity간의 데이터 공유를 위한 application 가져오기
        OnEcoApplication application = (OnEcoApplication) getApplication();

//        // 임의로 값 추가
        PreferenceManager preferenceManager = PreferenceManager.getInstance(this);
        TrashUsage trashUsage = new TrashUsage();
        trashUsage.setEmpty_bottle(10f);
        trashUsage.setCan(20f);
        trashUsage.setPlastic_bag(20f);
        trashUsage.setPaper(50f);
        trashUsage.setPlastic(20f);
        trashUsage.setTrashEtc(30f);
        trashUsage.setDisposable_spoon(20f);
        trashUsage.setDisposable_cup(10f);
        trashUsage.setTissue(20f);
        trashUsage.setTrashTotal(200f);
        preferenceManager.putString("220710-trash-usage", new Gson().toJson(trashUsage));

        //        // 임의로 값 추가2
        trashUsage.setEmpty_bottle(20f);
        trashUsage.setCan(30f);
        trashUsage.setPlastic_bag(30f);
        trashUsage.setPaper(40f);
        trashUsage.setPlastic(10f);
        trashUsage.setTrashEtc(40f);
        trashUsage.setDisposable_spoon(20f);
        trashUsage.setDisposable_cup(10f);
        trashUsage.setTissue(20f);
        trashUsage.setTrashTotal(200f);
        preferenceManager.putString("220617-trash-usage", new Gson().toJson(trashUsage));

        // 포인트 값 임의 조정
        //application.setPoint(-1500);

        onBackPressed();

        application.statisticType = "trash-usage";

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
                application.statisticType = "water-usage";
                application.active_activity = "WriteWater";

                Intent intent = new Intent(getApplicationContext(), WriteWater.class);
                startActivity(intent);
            }
        });

        Button goto_statistic = findViewById(R.id.goto_statistic);
        goto_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.active_activity = "MainHome";
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });

        Button goto_shower_game = findViewById(R.id.goto_shower_game);
        goto_shower_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.active_activity = "MainHome";
                application.statisticType = "water-usage";
                application.waterType = "shower";
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