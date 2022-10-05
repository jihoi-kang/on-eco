package com.project.oneco;

import android.app.Application;

import com.project.oneco.data.PreferenceManager;

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
    public String waterType = "";

    // 쓰레기 사용 종류
    public String trashType;

    public int timerSec = 0;
    public int RtimerSec = 0;

    public String Wtap = "";
    public float Wpower = 0f;

    public float usedW = 0f;      // 물 사용량 = Wpower * usedWT
    public float usedWT = 0f;      // 물 사용 시간
    public float noUsedWT = 0f;    // 물 미사용 시간

    public String statisticType = ""; // water-usage | trash-usage

    public boolean min3 = false;
    public boolean min5 = false;
    public boolean min7 = false;
    public boolean min10 = false;
    public boolean min15 = false;

    private PreferenceManager preferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();

        preferenceManager = PreferenceManager.getInstance(this);
    }

    public void setPoint(int point) {
        // 기존의 포인트를 가져와서 파라미터 포인트와 더한다.
        int prePoint = preferenceManager.getInt("point", 0);
        // 그리고 다시 저장
        preferenceManager.putInt("point", prePoint + point);
    }

    public int getPoint() {
        // 기존의 포인트를 가져온다
        int prePoint = preferenceManager.getInt("point", 0);
        return prePoint;
    }

}
