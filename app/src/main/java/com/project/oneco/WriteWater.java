package com.project.oneco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.WaterUsage;
import com.project.oneco.test.TestActivity;

public class WriteWater extends AppCompatActivity{

    private OnEcoApplication application;

    EditText UserInputWater;
    TextView TXT_today_water_input;

    Button Btn_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_water);


        Button Btn_toothBrush = findViewById(R.id.Btn_toothBrush);
        Button Btn_handWash = findViewById(R.id.Btn_handWash);
        Button Btn_faceWash = findViewById(R.id.Btn_faceWash);
        Button Btn_shower = findViewById(R.id.Btn_shower);
        Button Btn_dishWash = findViewById(R.id.Btn_dishWash);
        Button Btn_etc_water = findViewById(R.id.Btn_etc_water);

        UserInputWater = findViewById(R.id.UserInputWater);
        Btn_add = findViewById(R.id.Btn_add);

        // Activity간의 데이터 공유를 위한 application 가져오기
        application = (OnEcoApplication) getApplication();

        // 이전 버튼
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainHome.class);
                startActivity(intent);
                application.active_activity = null;
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

        // 통계 화면으로 넘어가기
        ImageButton Wstatistic = findViewById(R.id.Wstatistic);
        Wstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);

                application.active_activity = "statistic";
            }
        });

        // 물 사용량 측정 버튼 눌렀을 때
        Button Btn_bef_WTimer = findViewById(R.id.Btn_bef_WTimer);
        Btn_bef_WTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // Toast message : 물 사용 유형 먼저 선택하세요
                if (application.waterType == null){
                    Toast.makeText(getApplicationContext(), "사용할 물의 유형을 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                } else{ // 물 사용량 측정 화면으로 넘어가기
                    Intent intent = new Intent(getApplicationContext(), WaterStopWatch.class);
                    startActivity(intent);

                    application.waterType = null;
                }
            }
        });

        // 샤워 타이머 게임 화면으로 넘어가기
        Button Btn_bef_WTimer_Game = findViewById(R.id.Btn_bef_WTimer_Game);
        Btn_bef_WTimer_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterStopGame.class);
                startActivity(intent);
            }
        });

        // Button을 눌렀을 때 waterType에 물 사용 유형 저장
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                    case R.id.Btn_toothBrush:
                        application.waterType = "toothBrush";
                        break;

                    case R.id.Btn_handWash:
                        application.waterType = "handWash";
                        break;

                    case R.id.Btn_faceWash:
                        application.waterType = "faceWash";
                        break;

                    case R.id.Btn_shower:
                        application.waterType = "shower";
                        break;

                    case R.id.Btn_dishWash:
                        application.waterType = "dishWash";
                        break;

                    case R.id.Btn_etc_water:
                        application.waterType = "etc_water";
                        break;
                }
            }
        };

        Btn_toothBrush.setOnClickListener(onClickListener);
        Btn_handWash.setOnClickListener(onClickListener);
        Btn_faceWash.setOnClickListener(onClickListener);
        Btn_shower.setOnClickListener(onClickListener);
        Btn_dishWash.setOnClickListener(onClickListener);
        Btn_etc_water.setOnClickListener(onClickListener);

        // 추가 버튼 눌렀을 때
        Btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (application.waterType == null){
                    Toast.makeText(getApplicationContext(), "사용한 물의 유형을 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // todo:오류남.. ㅠㅜ
                    // editText의 값을 변환 후 변수 inputTodayWater에 넣음
                    application.inputTodayWater = Integer.parseInt(UserInputWater.getText().toString());
                    TXT_today_water_input.setText(UserInputWater.getText().toString() + "ml");
                }
            }
        });


        // SharedPreference 데이터 저장하기
        PreferenceManager manager = PreferenceManager.getInstance(WriteWater.this);
        Gson gson = new Gson();

        /* <저장할 데이터>
        * todayDate : 오늘의 날짜
        * real_tempTime  : 현재 시간
        * waterType : 물 사용 유형
        * usedWater : 사용한 물의 양 (측정값)
        * UserInputWater : 오늘 사용한 물의 양 (사용자 입력값)
        * savedWater     : 전일 대비 절약한 물의 양 */

        // data 만들기
        WaterUsage usage = new WaterUsage();

        // todo: 측정값 or 사용자가 직접 입력한 값이 들어가도록
        usage.setTooth(3f);
        usage.setHand(7f);
        usage.setFace(3f);
        usage.setDish(3f);
        usage.setShower(7f);
        usage.setEtc(3f);

        // data를 String화 시키기
        String json = gson.toJson(usage);

        // 변환한 String 값을 SharedPreference에 저장
        manager.putString("0508", json);

        // 데이터 꺼내오기
        String data = manager.getString("0508", "");

        // String을 데이터 모델로 변경
        WaterUsage waterUsage = gson.fromJson(data, WaterUsage.class);
        Log.d("jay", "dish: " + waterUsage.getTooth());
        Log.d("jay", "hand: " + waterUsage.getHand());
        Log.d("jay", "dish: " + waterUsage.getFace());
        Log.d("jay", "hand: " + waterUsage.getShower());
        Log.d("jay", "dish: " + waterUsage.getDish());
        Log.d("jay", "hand: " + waterUsage.getEtc());

    } // end of onClick

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


} // end of class