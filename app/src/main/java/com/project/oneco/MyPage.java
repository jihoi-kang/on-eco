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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

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


//        // todo: remove 임시로 이미지 누르면 다이얼로그 테스트로 넘어가기
//        ImageView imageView = findViewById(R.id.imageView);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(view.getContext(), GetDialogTest.class));
//            }
//        });


    }   // end of onCreate

}   // end of class