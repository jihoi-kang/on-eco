package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


    }   // end of onCreate

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }

    }

}   // end of class