package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.oneco.tensorflow.ClassifierActivity;
import com.project.oneco.test.GetDialogTest;

public class MyPage extends AppCompatActivity {

    private OnEcoApplication application;

    private long backpressedTime = 0;

    TextView temp_level;
    TextView Txt_tempPoint;
    ImageView Img_temp_level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        application = (OnEcoApplication) getApplication();

        temp_level = findViewById(R.id.temp_level);
        Txt_tempPoint = findViewById(R.id.Txt_tempPoint);
        Img_temp_level = findViewById(R.id.Img_temp_level);

        onBackPressed();

        // 검색화면으로 넘어가기
        ImageButton Btn_search = findViewById(R.id.btn_search);
        Btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        // 쓰레기 스캔
        Button Btn_scan_trash = findViewById(R.id.Btn_scan_trash);
        Btn_scan_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);
            }
        });

        // 물 사용량 측정
        Button Btn_measure_water_use = findViewById(R.id.Btn_measure_water_use);
        Btn_measure_water_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterStopWatch.class);
                startActivity(intent);
            }
        });

        Txt_tempPoint.setText("눈송이님의 햇살은 " + application.getPoint() + "밝기 입니다.");

        if (application.getPoint() < 30){
            Img_temp_level.setImageResource(R.drawable.level1);
            temp_level.setText("레벨1");
        } else if (30 <= application.getPoint() & application.getPoint() < 500){
            Img_temp_level.setImageResource(R.drawable.level2);
            temp_level.setText("레벨2");
        } else if (500 <= application.getPoint() & application.getPoint() < 1500){
            Img_temp_level.setImageResource(R.drawable.level3);
            temp_level.setText("레벨3");
        } else if (1500 <= application.getPoint() & application.getPoint() < 2000){
            Img_temp_level.setImageResource(R.drawable.level4);
            temp_level.setText("레벨4");
        } else if (2000 <= application.getPoint()){
            Img_temp_level.setImageResource(R.drawable.level5);
            temp_level.setText("레벨5");
        }


    }   /**end of onCreate**/

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }

    }

}   /**end of class**/