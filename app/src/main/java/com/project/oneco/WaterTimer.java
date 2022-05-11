package com.project.oneco;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class WaterTimer extends AppCompatActivity {

    private int start_already = 0;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지

    FrameLayout setting;    // 셋팅 화면
    FrameLayout timer;      // 타이머 화면

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_timer);

        // todo: SoundMeter 이용하여 기능 구현(TestActivity 참고)
        // todo: timer 구현(62초 -> 00:01:02)

        setting = findViewById(R.id.setting);
        timer = findViewById(R.id.timer);

        // 측정 시작 버튼 누르면 기록 & 타이머 프레임 레이아웃 보이기
        Button Btn_start_TimerW = findViewById(R.id.Btn_start_TimerW);
        Btn_start_TimerW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_already = 1;

                setting.setVisibility(View.GONE);    // 설정 사라짐
                timer.setVisibility(View.VISIBLE);     // 타이머 생김
            }
        });

        // 상황에 따른 이전 버튼 동작 선택
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_already == 1) {
                    // 종료하시겠습니까? 팝업창을 띄움
                    AlertDialog.Builder dlg_sure_out = new AlertDialog.Builder(WaterTimer.this);
                    dlg_sure_out.setMessage("취소하시겠습니까?");
                    dlg_sure_out.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Toast.makeText(WaterTimer.this, "계속합니다.", Toast.LENGTH_SHORT).show();
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
        ImageButton Wstatistic = findViewById(R.id.Wstatistic);
        Wstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });
    }
}