package com.project.oneco;

import android.app.Application;

public class OnEcoApplication extends Application {
    // 모든 파일에서 접근 가능한 변수 저장

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
    public float Wpower = 80f;
    public float usedW = 0f;      // 물 사용량 = Wpower * usedWT
    public float usedWT = 0f;      // 물 사용 시간
    public float noUsedWT = 0f;    // 물 미사용 시간

    // todo: OnEcoApplication에 물 사용 유형(샤워, 설거지, 기타 등등...)을 저장 ??
}
