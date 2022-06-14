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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WriteWater extends AppCompatActivity{

    private OnEcoApplication application;

    EditText ET_UserInputWater;
    TextView TXT_today_water_input;
    TextView TXT_saved_water;

    Button Btn_add;

    private PreferenceManager preferenceManager;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_water);


        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();

        Button Btn_toothBrush = findViewById(R.id.Btn_toothBrush);
        Button Btn_handWash = findViewById(R.id.Btn_handWash);
        Button Btn_faceWash = findViewById(R.id.Btn_faceWash);
        Button Btn_shower = findViewById(R.id.Btn_shower);
        Button Btn_dishWash = findViewById(R.id.Btn_dishWash);
        Button Btn_etc_water = findViewById(R.id.Btn_etc_water);

        ET_UserInputWater = findViewById(R.id.UserInputWater);
        Btn_add = findViewById(R.id.Btn_add);
        TXT_today_water_input = findViewById(R.id.TXT_today_water_input);
        TXT_saved_water = findViewById(R.id.TXT_saved_water);

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
                }
            }
        });

        // 샤워 타이머 게임 화면으로 넘어가기
        Button Btn_bef_WTimer_Game = findViewById(R.id.Btn_bef_WTimer_Game);
        Btn_bef_WTimer_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.waterType = "shower";
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
                        application.waterType = "tooth";
                        break;

                    case R.id.Btn_handWash:
                        application.waterType = "hand";
                        break;

                    case R.id.Btn_faceWash:
                        application.waterType = "face";
                        break;

                    case R.id.Btn_shower:
                        application.waterType = "shower";
                        break;

                    case R.id.Btn_dishWash:
                        application.waterType = "dish";
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
                    // 사용자 입력값인 editText의 값을 변환 후, 변수 inputWater에 저장
                    int inputWater = Integer.parseInt(ET_UserInputWater.getText().toString());

                    // SharedPreference 저장 과정
                    // 오늘의 날짜를 구한 후 key값으로 등록 - 220531
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
                    String key = simpledateformat.format(calendar.getTime());
                    String waterUsageStr = preferenceManager.getString(key + "-water-usage", "");   // 태그 붙이고 변수에 저장

                    // data 만들기 - WaterUseage 클래스 객체 생성
                    WaterUsage waterUsage;

                    if (waterUsageStr.equals("")) { // 프리퍼런스에 저장된 값이 없다면
                        waterUsage = new WaterUsage();
                    } else {    // 프리퍼런스에 저장된 값이 있다면 데이터 모델링
                        // gson이용하여 가져와 data를 String으로 변환 후 객체에 저장. todo: WaterUsage 클래스?
                        waterUsage = gson.fromJson(waterUsageStr, WaterUsage.class);
                    }

                    // 물 타입에 따라 사용자가 직접 입력한 값이 들어감
                    if (application.waterType.equals("tooth")) {
                        float tooth = waterUsage.getTooth();
                        waterUsage.setTooth(tooth + inputWater);
                    } else if (application.waterType.equals("hand")) {
                        float hand = waterUsage.getHand();
                        waterUsage.setHand(hand + inputWater);
                    } else if (application.waterType.equals("face")) {
                        float face = waterUsage.getFace();
                        waterUsage.setFace(face + inputWater);
                    } else if (application.waterType.equals("shower")) {
                        float shower = waterUsage.getShower();
                        waterUsage.setShower(shower + inputWater);
                    } else if (application.waterType.equals("dish")) {
                        float dish = waterUsage.getDish();
                        waterUsage.setDish(dish + inputWater);
                    } else if (application.waterType.equals("etc_water")) {
                        float etc_water = waterUsage.getEtcWater();
                        waterUsage.setEtcWater(etc_water + inputWater);
                    }

                    // 물 전체 사용량(ml) 구하기
                    float waterTotal = waterUsage.getTooth() + waterUsage.getHand()
                            + waterUsage.getFace() + waterUsage.getShower()
                            + waterUsage.getDish() + waterUsage.getEtcWater();
                    waterUsage.setWaterTotal(waterTotal);
                    TXT_today_water_input.setText(waterTotal + "ml");

                    // localStorage에 저장
                    String updatedWaterUsage = gson.toJson(waterUsage);
                    preferenceManager.putString(key + "-water-usage", updatedWaterUsage);

                    Log.d("jay", "key: " + key);
                    Log.d("jay", "waterUsageStr: " + waterUsageStr);
                    Log.d("jay", "waterTotal: " + waterTotal);


                    // 전일 대비 절약한 물의 양.
                    setPreSavedWater(waterTotal);
                }
            }
        });


        /* <저장할 데이터>
        * todayDate : 오늘의 날짜
        * real_tempTime  : 현재 시간
        * waterType : 물 사용 유형
        * usedWater : 사용한 물의 양 (측정값)
        * UserInputWater : 오늘 사용한 물의 양 (사용자 입력값)
        * savedWater     : 전일 대비 절약한 물의 양 */

        // 저장되어 있는 water 값 반영
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String key = simpledateformat.format(calendar.getTime()); // 220530

        String waterUsageStr = preferenceManager.getString(key + "-water-usage", "");
        WaterUsage todayWaterUsage;
        if (waterUsageStr.equals("")) {
            todayWaterUsage = new WaterUsage();
        } else {
            todayWaterUsage = gson.fromJson(waterUsageStr, WaterUsage.class);
        }

        // 물 전체 사용량(ml) 구하기
        float waterTotal = todayWaterUsage.getWaterTotal();
        TXT_today_water_input.setText(waterTotal + "ml");


        // 전일 대비 절약한 물의 양 반영
        // 전일 물 전체 사용량(waterTotal) 불러오기
        setPreSavedWater(waterTotal);
    } // end of onClick


    // 백버튼 막은 이유는 물 측정하는 화면으로 넘어갔다가 다시 왔을 때,
    // 백버튼을 누르면 홈화면으로 돌아가지 않고 물측정화면으로 넘어가기 때문
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    // 오늘 사용한 전체 물의 양과 비교하여 UI도 변경해주는
    public void setPreSavedWater(float todayTotal) {
        // 전일 사용한 전체 양 구하기
        float preTotal = 0f;
        // 전일 대비 절약한 물의 양.
        // 전날 데이터가 null이 아닐 때까지 데이터 불러오기
        for(int i = 1; i<10; i++){
            // 어제 물 전체 사용량(waterTotal) 불러오기
            Date dDate = new Date();
            dDate = new Date(dDate.getTime()+(1000*60*60*24*-i));
            SimpleDateFormat dSdf = new SimpleDateFormat("yyMMdd", Locale.KOREA);
            String key_yesterday = dSdf.format(dDate.getTime());
            Log.d("jay", "key_yesterday: " + key_yesterday);

            String yesterday_waterUsageStr = preferenceManager.getString(key_yesterday + "-water-usage", "");
            WaterUsage yesterday_waterUsage;

            Log.d("jay", "yesterday_waterUsageStr : " + yesterday_waterUsageStr);

            // 만약 어제 데이터가 없으면 그 전날 데이터 데이터 불러오기
            if (yesterday_waterUsageStr.equals("")) {
                continue;
            } else {
                yesterday_waterUsage = gson.fromJson(yesterday_waterUsageStr, WaterUsage.class);
            }

            preTotal = yesterday_waterUsage.getWaterTotal();

            if (preTotal > 0) {
                break;
            }

            Log.d("jay", "key_yesterday: " + key_yesterday);
            Log.d("jay", "yesterday_waterUsageStr: " + yesterday_waterUsageStr);
        }

        // 오늘과 전일 비교
        float savedWater = todayTotal - preTotal;

        // UI 변경
        TXT_saved_water.setText(savedWater + "ml");
    }


} // end of class