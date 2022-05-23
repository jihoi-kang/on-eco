package com.project.oneco;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.oneco.test.TestActivity;

public class Statistic extends AppCompatActivity {
    final int DIALOG_DATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo: UI 화면 만들기(activity_statistic.xml), 그래프 제외(다음주 수업때 알려드릴게요~)
        setContentView(R.layout.activity_statistic);

//        // 모든 class에서 참조할 수 있는 변수 선언
//        OnEcoApplication application = (OnEcoApplication) getApplication();
//        int number = application.number;
//        int count = application.count;
//
//        Log.d("jay", "Statistic number: " + number);
//        Log.d("jay", "Statistic count: " + count);

        Button Btn_graph_trash = findViewById(R.id.Btn_graph_trash);
        Button Btn_graph_water = findViewById(R.id.Btn_graph_water);

        ImageView trashTypeColor_View = findViewById(R.id.trashTypeColor_View);
        ImageView waterTypeColor_View = findViewById(R.id.waterTypeColor_View);

        TextView Text_pickDate = findViewById(R.id.Text_pickDate);

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

        // 마이페이지로 넘어가기
        ImageButton goto_mypage = findViewById(R.id.goto_mypage);
        goto_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent);
            }
        });


        Btn_graph_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashTypeColor_View.setVisibility(View.VISIBLE);
                waterTypeColor_View.setVisibility(View.GONE);
            }
        });

        Btn_graph_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashTypeColor_View.setVisibility(View.GONE);
                waterTypeColor_View.setVisibility(View.VISIBLE);
            }
        });

        Text_pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_DATE);
            }
        });
    }   // end of onCreate

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DIALOG_DATE:
                DatePickerDialog dpd = new DatePickerDialog(Statistic.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        TextView Text_pickDate = findViewById(R.id.Text_pickDate);
                        Text_pickDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
//                        Toast.makeText(getApplicationContext(),
//                                year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일 을 선택했습니다",
//                                Toast.LENGTH_SHORT).show();
                    }
                },
                2022, 05, 18);
                return dpd;
        }
        return super.onCreateDialog(id);
    }
}   // end of class