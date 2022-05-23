package com.project.oneco;

import android.app.Application;

public class OnEcoApplication extends Application {
    // 모든 파일에서 접근 가능한 변수 저장

    public int number = 0;
    public int count = 0;

    // 손씻기, 샤워 등등을 누를 때마다 저장할 변수 생성(Data type: String)
    // 물 사용 종류
    private String waterType;

    // 데이터 정의(오늘 사용한 물 사용량, 전일 대비 절약한 물 사용량)
    private String todayWater;
    private String removedWater;

    // todo: OnEcoApplication에 물 사용 유형(샤워, 설거지, 기타 등등...)을 저장 ??
}
