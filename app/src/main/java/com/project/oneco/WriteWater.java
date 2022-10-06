package com.project.oneco;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.WaterUsage;
import com.project.oneco.tensorflow.CameraActivity;
import com.project.oneco.tensorflow.ClassifierActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WriteWater extends AppCompatActivity {

    private int touchCount1, touchCount2, touchCount3, touchCount4, touchCount5, touchCount6, touchCount7, touchCount8, touchCount9 = 0;
    androidx.appcompat.widget.AppCompatButton Btn_toothBrush;
    androidx.appcompat.widget.AppCompatButton Btn_handWash;
    androidx.appcompat.widget.AppCompatButton Btn_faceWash;
    androidx.appcompat.widget.AppCompatButton Btn_shower;
    androidx.appcompat.widget.AppCompatButton Btn_dishWash;
    androidx.appcompat.widget.AppCompatButton Btn_etc_water;

    EditText ET_UserInputWater;
    Button Btn_add;
    Button Btn_sub;

    LinearLayout LO_water_PT;
    androidx.appcompat.widget.AppCompatButton Wbath;
    androidx.appcompat.widget.AppCompatButton Wsink;
    androidx.appcompat.widget.AppCompatButton WshowerHead;
    androidx.appcompat.widget.AppCompatButton Wstrenth;
    androidx.appcompat.widget.AppCompatButton Wmiddle;
    androidx.appcompat.widget.AppCompatButton Wweakness;

    LinearLayout Layout_play;    // 플레이 버튼 화면
    LinearLayout Layout_pauseStop;      // 정지, 종료 버튼 화면

    TextView Txt_today_date;
    TextView Txt_today_water_input;
    TextView Txt_saved_water;

    private ArrayList<Float> dbList = new ArrayList<>();
    private ArrayList<Float> dbUsedList = new ArrayList<>();
    private ArrayList<Float> dbNoUsedList = new ArrayList<>();

    private TextView Txt_countup;// 타이머 현황

    private ImageButton Btn_pause_ST;
    private ImageButton Btn_stop_ST;

    private Timer countUpTimer;
    private TimerTask second;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지
    private boolean start_already;  // 일시정지하면 처음은 아니지만 이미 시작한 상태

    private OnEcoApplication application;   // Activity간의 데이터 공유를 위한 application 가져오기1
    private PreferenceManager preferenceManager;
    private Gson gson;
    private final Handler handler = new Handler();
    private SoundMeter soundMeter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_water);

        Btn_toothBrush = findViewById(R.id.Btn_toothBrush);
        Btn_handWash = findViewById(R.id.Btn_handWash);
        Btn_faceWash = findViewById(R.id.Btn_faceWash);
        Btn_shower = findViewById(R.id.Btn_shower);
        Btn_dishWash = findViewById(R.id.Btn_dishWash);
        Btn_etc_water = findViewById(R.id.Btn_etc_water);


        ET_UserInputWater = findViewById(R.id.UserInputWater);
        Btn_add = findViewById(R.id.Btn_add);
        Btn_sub = findViewById(R.id.Btn_sub);

        LO_water_PT = findViewById(R.id.water_PT);
        Wbath = findViewById(R.id.Wbath);
        Wsink = findViewById(R.id.Wsink);
        WshowerHead = findViewById(R.id.WshowerHead);
        Wstrenth = findViewById(R.id.Wstrenth);
        Wmiddle = findViewById(R.id.Wmiddle);
        Wweakness = findViewById(R.id.Wweakness);

        Wbath.setOnClickListener(listener);
        Wsink.setOnClickListener(listener);
        WshowerHead.setOnClickListener(listener);
        Wstrenth.setOnClickListener(listener);
        Wmiddle.setOnClickListener(listener);
        Wweakness.setOnClickListener(listener);

        Layout_play = findViewById(R.id.Layout_play);
        Layout_pauseStop = findViewById(R.id.Layout_pauseStop);
        Txt_countup = findViewById(R.id.spended_AllT);
        Btn_pause_ST = findViewById(R.id.Btn_pause_ST);
        Btn_stop_ST = findViewById(R.id.Btn_stop_ST);

        Txt_today_water_input = findViewById(R.id.TXT_today_water);
        Txt_saved_water = findViewById(R.id.TXT_saved_water);

        application = (OnEcoApplication) getApplication();  // Activity간의 데이터 공유를 위한 application 가져오기2
        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();
        soundMeter = new SoundMeter();


        checkPermission();
        checkPermission_camera();
        Layout_pauseStop.setVisibility(View.GONE);
        Wbath.setSelected(true);
        Wmiddle.setSelected(true);
        Btn_etc_water.setSelected(true);

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

        // 샤워 타이머 게임 화면으로 넘어가기
        Button Btn_bef_WTimer_Game = findViewById(R.id.Btn_bef_WTimer_Game);
        Btn_bef_WTimer_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.waterType = "shower";
                application.active_activity = "WriteWater";
                Intent intent = new Intent(getApplicationContext(), WaterStopGame.class);
                startActivity(intent);
            }
        });



        // Button을 눌렀을 때 waterType에 물 사용 유형 저장
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.Btn_toothBrush:
                        application.waterType = "tooth";
                        Btn_toothBrush.setSelected(true);
                        Btn_handWash.setSelected(false);
                        Btn_faceWash.setSelected(false);
                        Btn_shower.setSelected(false);
                        Btn_dishWash.setSelected(false);
                        Btn_etc_water.setSelected(false);
                        touchCount1++;
                        ListVisible();
                        break;

                    case R.id.Btn_handWash:
                        application.waterType = "hand";
                        Btn_toothBrush.setSelected(false);
                        Btn_handWash.setSelected(true);
                        Btn_faceWash.setSelected(false);
                        Btn_shower.setSelected(false);
                        Btn_dishWash.setSelected(false);
                        Btn_etc_water.setSelected(false);
                        touchCount2++;
                        ListVisible();
                        break;

                    case R.id.Btn_faceWash:
                        application.waterType = "face";
                        Btn_toothBrush.setSelected(false);
                        Btn_handWash.setSelected(false);
                        Btn_faceWash.setSelected(true);
                        Btn_shower.setSelected(false);
                        Btn_dishWash.setSelected(false);
                        Btn_etc_water.setSelected(false);
                        touchCount3++;
                        ListVisible();
                        break;

                    case R.id.Btn_shower:
                        application.waterType = "shower";
                        Btn_toothBrush.setSelected(false);
                        Btn_handWash.setSelected(false);
                        Btn_faceWash.setSelected(false);
                        Btn_shower.setSelected(true);
                        Btn_dishWash.setSelected(false);
                        Btn_etc_water.setSelected(false);
                        touchCount4++;
                        ListVisible();
                        break;

                    case R.id.Btn_dishWash:
                        application.waterType = "dish";
                        Btn_toothBrush.setSelected(false);
                        Btn_handWash.setSelected(false);
                        Btn_faceWash.setSelected(false);
                        Btn_shower.setSelected(false);
                        Btn_dishWash.setSelected(true);
                        Btn_etc_water.setSelected(false);
                        touchCount5++;
                        ListVisible();
                        break;

                    case R.id.Btn_etc_water:
                        application.waterType = "etc_water";
                        Btn_toothBrush.setSelected(false);
                        Btn_handWash.setSelected(false);
                        Btn_faceWash.setSelected(false);
                        Btn_shower.setSelected(false);
                        Btn_dishWash.setSelected(false);
                        Btn_etc_water.setSelected(true);
                        touchCount6++;
                        ListVisible();
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


        // + 버튼 눌렀을 때
        Btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (application.waterType == "") {
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
                        // gson이용하여 가져와 data를 String으로 변환 후 객체에 저장.
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
                    Txt_today_water_input.setText(waterTotal + "ml");

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

        // - 버튼 눌렀을 때
        Btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (application.waterType.equals("")) {
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
                        // gson이용하여 가져와 data를 String으로 변환 후 객체에 저장.
                        waterUsage = gson.fromJson(waterUsageStr, WaterUsage.class);
                    }

                    // todo:오늘 날짜로 데이트 피커 날짜 세팅
                    Txt_today_date.setText(application.todayDate);

                    // 물 전체 사용량(ml) 구하기
                    float waterTotal = waterUsage.getTooth() + waterUsage.getHand()
                            + waterUsage.getFace() + waterUsage.getShower()
                            + waterUsage.getDish() + waterUsage.getEtcWater();

                    if (waterTotal - inputWater > 0) {
                        // 물 타입에 따라 사용자가 직접 입력한 값이 들어감
                        if (application.waterType.equals("tooth")) {
                            float tooth = waterUsage.getTooth();
                            waterUsage.setTooth(tooth - inputWater);
                        } else if (application.waterType.equals("hand")) {
                            float hand = waterUsage.getHand();
                            waterUsage.setHand(hand - inputWater);
                        } else if (application.waterType.equals("face")) {
                            float face = waterUsage.getFace();
                            waterUsage.setFace(face - inputWater);
                        } else if (application.waterType.equals("shower")) {
                            float shower = waterUsage.getShower();
                            waterUsage.setShower(shower - inputWater);
                        } else if (application.waterType.equals("dish")) {
                            float dish = waterUsage.getDish();
                            waterUsage.setDish(dish - inputWater);
                        } else if (application.waterType.equals("etc_water")) {
                            float etc_water = waterUsage.getEtcWater();
                            waterUsage.setEtcWater(etc_water - inputWater);
                        }

//                        // 물 전체 사용량(ml) 구하기
//                        float waterTotal = waterUsage.getTooth() + waterUsage.getHand()
//                                + waterUsage.getFace() + waterUsage.getShower()
//                                + waterUsage.getDish() + waterUsage.getEtcWater();
                        waterUsage.setWaterTotal(waterTotal);
                        Txt_today_water_input.setText(waterTotal + "ml");

                        // localStorage에 저장
                        String updatedWaterUsage = gson.toJson(waterUsage);
                        preferenceManager.putString(key + "-water-usage", updatedWaterUsage);

                        Log.d("jay", "key: " + key);
                        Log.d("jay", "waterUsageStr: " + waterUsageStr);
                        Log.d("jay", "waterTotal: " + waterTotal);
                    }

                    // 전일 대비 절약한 물의 양.
                    setPreSavedWater(waterTotal);
                }
            }
        });


        // 측정 시작 버튼 눌렀을 때
        ImageButton Btn_bef_WTimer = findViewById(R.id.Btn_bef_WTimer);
        Btn_bef_WTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // Toast message : 물 사용 유형 먼저 선택하세요
                if (application.waterType == "") {
                    Toast.makeText(getApplicationContext(), "사용할 물의 유형을 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                } else { // 타이머 시작
                    application.active_activity = "WriteWater";
                    firstState = true;
                    start_already = true;
                    Layout_play.setVisibility(View.GONE);    // 플레이 버튼 사라짐
                    Layout_pauseStop.setVisibility(View.VISIBLE);     // 타이머 생김
                    startStop();
                }
            }
        });

        // 종료버튼을 누르면
        Btn_stop_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // second와 soundMeter를 중지시킨다.
                second.cancel();
                soundMeter.stop();
                timerRunning = false;

                // 지금까지 모아둔 데시벨 값을 파싱한다.
                float total = 0f;
                for (int i = 0; i < dbList.size(); i++) {
                    total = total + dbList.get(i);
                }

                // 평균 데시벨
                float average = total / dbList.size();

                for (int i = 0; i < dbList.size(); i++) {
                    if (dbList.get(i) > average) {
                        // 물을 사용하는 중이라는 뜻
                        dbUsedList.add(dbList.get(i));
                    } else {
                        // 물을 사용하지 않고 있다는 뜻
                        dbNoUsedList.add(dbList.get(i));
                    }
                }

                application.usedWT = dbUsedList.size();
                application.noUsedWT = dbNoUsedList.size();

                application.usedW = application.usedWT * application.Wpower;


                // 물 사용 통계 화면 넘어가기
                Intent intent = new Intent(getApplicationContext(), WaterAfterStati.class);
                startActivity(intent);
            }
        });

        // 일시 정지
        Btn_pause_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
                start_already = true;
                firstState = false;
            }
        });
    }

    // 타이머 상태에 따른 시작 & 정지
    private void startStop() {
        if (timerRunning) {   // 시작이면 정지
            stopTimer();
            Btn_pause_ST.setImageResource(R.drawable.replay);
        } else {
            startTimer();   // 정지면 시작
            Btn_pause_ST.setImageResource(R.drawable.pause);
        }
    }

    // 타이머 구현
    private void startTimer() {
        // 처음이면 00:00으로 UI 변경
        if (firstState) {
            Txt_countup.setText("00:00");

            // 초기화 작업
            application.timerSec = 0;
            application.RtimerSec = 0;
        }

        // 데시벨을 얻어오기 위해 sound meter 시작
        if (soundMeter == null) {
            soundMeter = new SoundMeter();
        }
        soundMeter.start();

        // 초 세면서 데시벨 값 저장하기
        second = new TimerTask() {  // TimerTask는 안드로이드 클래스
            @Override
            public void run() {

                // soundMeter getAmplitude() 작업 수행
                if (soundMeter == null) {
                    return;
                }
                double amplitude = soundMeter.getAmplitude();
                float db = 20 * (float) (Math.log10(amplitude));
                Log.d("jay", "db: " + db);

                // 데이터를 전역변수의 dbList로 저장한다.
                dbList.add(db);

                application.timerSec += 1000;
                application.RtimerSec++;
                setSec();
            }
        };

        Timer CountUpTimer = new Timer();  // Timer는 안드로이드 클래스
        CountUpTimer.schedule(second, 0, 1000);

        //Btn_pause_ST.setText("일시정지");
        timerRunning = true;
        firstState = false;
    }

    // Time up UI 변경 메서드
    private void setSec() {
        Runnable updater = new Runnable() {
            public void run() {
                // 여기서부터는 main(UI) thread를 활용한다.
                Txt_countup.setText(getUITime(application.timerSec));
            }
        };
        handler.post(updater);
    }

    // 타이머 정지
    private void stopTimer() {
        second.cancel();
        timerRunning = false;
        soundMeter.stop();
        //Btn_pause_ST.setText("계속");
    }

    // 시간 업데이트
    private String getUITime(long time) {
        //int one_day = 1000 * 60 * 60 * 24;
        int one_hour = 1000 * 60 * 60;
        int one_min = 1000 * 60;
        int one_sec = 1000;

        //int hours = (int) time % one_day / one_hour;
        int minutes = (int) time % one_hour / one_min;
        int seconds = (int) time % one_hour % one_min / one_sec;

        String timeLeftText = "";

        // 분이 10보다 작으면 0이 붙는다
        //if (hours < 10) timeLeftText += "0";
        //timeLeftText += hours + ":";

        // 분이 10보다 작으면 0이 붙는다
        if (minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + ":";

        // 초가 10보다 작으면 0이 붙는다
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        return timeLeftText;

    } // end of Oncreate




    // 백버튼을 누르면
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        application.active_activity = "";
        application.statisticType = "";
        application.waterType = "";

        if (start_already) {
            stopTimer();

            // 취소하시겠습니까? 팝업창을 띄움
            AlertDialog.Builder dlg_sure_out = new AlertDialog.Builder(WriteWater.this);
            dlg_sure_out.setMessage("취소하시겠습니까?");
            dlg_sure_out.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    // firstState = false;
                    // todo: 1초 텀 없이 바로 올라가는 문제 해결 필요
                    Toast.makeText(WriteWater.this, "계속합니다.", Toast.LENGTH_SHORT).show();
                }
            });
            dlg_sure_out.setNegativeButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Layout_play.setVisibility(View.VISIBLE); // 설정 생김
                    Layout_pauseStop.setVisibility(View.GONE);        // 타이머 사라짐

                    firstState = true;
                    start_already = false;
                    // updateTimer();

                    // 초기화
                    Txt_countup.setText("00:00");
                    application.timerSec = 0;
                    application.RtimerSec = 0;
                    soundMeter = null;
                    dbList = new ArrayList<>();
                    dbUsedList = new ArrayList<>();
                    dbNoUsedList = new ArrayList<>();
                    // 수도꼭지, 수압 초기화
                    application.Wtap = "";
                    application.Wpower = 0f;

                    setSec();
                }
            });
            dlg_sure_out.show();
        } else {
            onBackPressed();

            // 수도꼭지, 수압 초기화
            application.Wtap = "";
            application.Wpower = 0f;
        }
    }


    // Button을 눌렀을 때 레이아웃 보이고 안보이기
    private void ListVisible() {
        if (LO_water_PT.getVisibility() == LO_water_PT.GONE) {
            LO_water_PT.setVisibility(View.VISIBLE);
        } else {
            if (touchCount1 == 2 || touchCount2 == 2 || touchCount3 == 2 || touchCount4 == 2 || touchCount5 == 2 || touchCount6 == 2 || touchCount7 == 2 || touchCount8 == 2 || touchCount9 == 2) {
                LO_water_PT.setVisibility(View.GONE);
                touchCount1 = 0;
                touchCount2 = 0;
                touchCount3 = 0;
                touchCount4 = 0;
                touchCount5 = 0;
                touchCount6 = 0;
                touchCount7 = 0;
                touchCount8 = 0;
                touchCount9 = 0;
                Btn_toothBrush.setSelected(false);
                Btn_handWash.setSelected(false);
                Btn_faceWash.setSelected(false);
                Btn_shower.setSelected(false);
                Btn_dishWash.setSelected(false);
                Btn_etc_water.setSelected(true);
                application.waterType = "etc_water";
            }
        }
    }


    // 사용자가 누르는 버튼에 따른 수도꼭지, 수압 처리
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.Wbath:
                    application.Wtap = "Wbath";
                    Wbath.setSelected(true);
                    Wsink.setSelected(false);
                    WshowerHead.setSelected(false);
                    break;
                case R.id.Wsink:
                    application.Wtap = "Wsink";
                    Wbath.setSelected(false);
                    Wsink.setSelected(true);
                    WshowerHead.setSelected(false);
                    break;
                case R.id.WshowerHead:
                    application.Wtap = "WshowerHead";
                    Wbath.setSelected(false);
                    Wsink.setSelected(false);
                    WshowerHead.setSelected(true);
                    break;

                case R.id.Wstrenth:
                    application.Wpower = 140;
                    Wstrenth.setSelected(true);
                    Wmiddle.setSelected(false);
                    Wweakness.setSelected(false);
                    break;
                case R.id.Wmiddle:
                    application.Wpower = 80;
                    Wstrenth.setSelected(false);
                    Wmiddle.setSelected(true);
                    Wweakness.setSelected(false);
                    break;
                case R.id.Wweakness:
                    application.Wpower = 35;
                    Wstrenth.setSelected(false);
                    Wmiddle.setSelected(false);
                    Wweakness.setSelected(true);
                    break;
            }
            Log.d("jay", "Wpower: " + application.Wpower);
        }
    };

    private void setInit() {
        application.waterType = "Wmiddle";
        application.Wtap = "Wbath";
        application.Wpower = 80f;
    }


    // 오늘 사용한 전체 물의 양과 비교하여 UI도 변경해주는
    public void setPreSavedWater(float todayTotal) {
        // 전일 사용한 전체 양 구하기
        float preTotal = 0f;
        // 전일 대비 절약한 물의 양.
        // 전날 데이터가 null이 아닐 때까지 데이터 불러오기
        for (int i = 1; i < 10; i++) {
            // 어제 물 전체 사용량(waterTotal) 불러오기
            Date dDate = new Date();
            dDate = new Date(dDate.getTime() + (1000 * 60 * 60 * 24 * -i));
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
        Txt_saved_water.setText(savedWater + "ml");
    }


    /**
     * CAMERA 권한이 있는지 확인합니다.
     */
    private void checkPermission_camera() {
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(WriteWater.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(WriteWater.this, "Camera Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    /**
     * 데시벨을 측정하기 위해선 RECORD_AUDIO라는 권한을 사용자로부터 받아야 합니다.
     * RECORD_AUDIO 권한이 있는지 확인합니다.
     */
    private void checkPermission() {
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(WriteWater.this, "Audio Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(WriteWater.this, "Audio Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check();
    }


} // end of class