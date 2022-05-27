package com.project.oneco;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 모든 파일에서 접근 가능한 변수 저장
public class OnEcoApplication extends Application {

    // 오늘 날짜 가져오기
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat ymd = new SimpleDateFormat("yyyy.MM.dd (E)", Locale.KOREA);
    String todayDate = ymd.format(date);

    // 오늘 시간 가져오기
    SimpleDateFormat hm = new SimpleDateFormat("aa hh:mm", Locale.KOREA);
    String real_tempTime = hm.format(date);

    public String active_activity;

    public int number = 0;
    public int count = 0;

    // 물 사용 종류
    public String waterType;

    // 데이터 정의(오늘 사용한 물 사용량, 전일 대비 절약한 물 사용량)
    public int inputTodayWater = 0;
    public int savedWater = 0;

    public int timerSec = 0;
    public int RtimerSec = 0;

    public float Wpower = 0f;
    public float usedW = 0f;      // 물 사용량 = Wpower * usedWT
    public float usedWT = 0f;      // 물 사용 시간
    public float noUsedWT = 0f;    // 물 미사용 시간

    public String Wtap = "null";

}
