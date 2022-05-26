package com.project.oneco;

import android.app.Application;

// 모든 파일에서 접근 가능한 변수 저장
public class OnEcoApplication extends Application {

    public String active_activity;

    public int number = 0;
    public int count = 0;

    // 손씻기, 샤워 등등을 누를 때마다 저장할 변수 생성(Data type: String)
    // 물 사용 종류
    public String waterType;

    // 데이터 정의(오늘 사용한 물 사용량, 전일 대비 절약한 물 사용량)
    public String todayWater;
    public String removedWater;

    public int timerSec = 0;
    public int RtimerSec = 0;

    public float Wpower = 0f;
    public float usedW = 0f;      // 물 사용량 = Wpower * usedWT
    public float usedWT = 0f;      // 물 사용 시간
    public float noUsedWT = 0f;    // 물 미사용 시간

    public String Wtap = "null";

}
