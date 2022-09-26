package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;



@SuppressWarnings("deprecation") //이거 있어야 합니다.

public class MainActivity extends TabActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabHost tabHost = getTabHost(); //탭 호스트 객체 생성

// 탭스팩 선언하고, 탭의 내부 명칭, 탭에 출력될 글 작성
        TabHost.TabSpec spec;
        Intent intent; //객체


//탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, MainHome.class);
        spec = tabHost.newTabSpec("Main"); // 객체를 생성
        spec.setIndicator("메인화면"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


//탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, WriteTrash.class);
        spec = tabHost.newTabSpec("Trash"); // 객체를 생성
        spec.setIndicator("쓰레기 배출량 기록"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


//탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, WriteWater.class);
        spec = tabHost.newTabSpec("Water"); // 객체를 생성
        spec.setIndicator("물 사용량 기록"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, Statistic.class);
        spec = tabHost.newTabSpec("Statistic"); // 객체를 생성
        spec.setIndicator("통계"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


//탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, MyPage.class);
        spec = tabHost.newTabSpec("Mypage"); // 객체를 생성
        spec.setIndicator("마이페이지"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(0); //먼저 열릴 탭을 선택!

    }

}