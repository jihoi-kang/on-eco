package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.oneco.test.GetDialogTest;

public class MyPage extends AppCompatActivity {
    private OnEcoApplication application;
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

        Txt_tempPoint.setText("눈송이님의 햇살은 " + application.getPoint() + "밝기 입니다.");

        // 이전 버튼
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 홈 화면으로 넘어가기
        TextView title_ONECO = findViewById(R.id.title_ONECO);
        title_ONECO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainHome.class);
                startActivity(intent);
            }
        });

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


    }   // end of onCreate

}   // end of class