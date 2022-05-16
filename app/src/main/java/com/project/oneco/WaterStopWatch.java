package com.project.oneco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.LinearLayout;

public class WaterStopWatch extends AppCompatActivity {

    private int start_already = 0;
    private int stop_already = 0;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지

    LinearLayout setting;    // 셋팅 화면
    LinearLayout timer;      // 타이머 화면


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_stop_watch);


        // todo: SoundMeter 이용하여 기능 구현(TestActivity 참고)
        // todo: timer 구현(62초 -> 00:01:02)

        setting = findViewById(R.id.setting);
        timer = findViewById(R.id.timer);

        // 측정 시작 버튼 누르면 기록 & 타이머 프레임 레이아웃 보이기
        Button Btn_start_ST = findViewById(R.id.Btn_start_ST);
        Btn_start_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_already = 1;

                setting.setVisibility(View.GONE);    // 설정 사라짐
                timer.setVisibility(View.VISIBLE);     // 타이머 생김
            }
        });

        // 중지 버튼 눌렀을 때
        Button Btn_stop_ST = findViewById(R.id.Btn_stop_ST);
        Btn_stop_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop_already = 1;
            }
        });

        // 상황에 따른 이전 버튼 동작 선택
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_already == 1 | stop_already == 1) {
                    // 종료하시겠습니까? 팝업창을 띄움
                    AlertDialog.Builder dlg_sure_out = new AlertDialog.Builder(WaterStopWatch.this);
                    dlg_sure_out.setMessage("취소하시겠습니까?");
                    dlg_sure_out.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Toast.makeText(WaterStopWatch.this, "계속합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlg_sure_out.setNegativeButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    });
                    dlg_sure_out.show();
                } else {
                    onBackPressed();
                }
            }
        });


        // 물 사용 통계 화면 넘어가기
        // todo: 통계화면 넘어갈 때 물 vs 쓰레기 중 먼저 보여주는 그래프가 다르도록 구현
        ImageButton Wstatistic = findViewById(R.id.Wstatistic);
        Wstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });

        // WaterAfterStati(물 사용 측정 후 Text 통계 화면)로 넘어가기
        Button btn_finish_ST = findViewById(R.id.Btn_finish_ST);
        btn_finish_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });
    }
}