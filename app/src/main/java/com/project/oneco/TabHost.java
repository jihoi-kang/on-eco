package com.project.oneco;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;


@SuppressWarnings("deprecation") //이거 있어야 합니다.

public class TabHost extends TabActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host);


        android.widget.TabHost tabHost = getTabHost(); //탭 호스트 객체 생성

        // 탭스팩 선언하고, 탭의 내부 명칭, 탭에 출력될 글 작성
        android.widget.TabHost.TabSpec spec;
        Intent intent; //객체



        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, WriteTrash.class);
        spec = tabHost.newTabSpec("Trash"); // 객체를 생성
        spec.setIndicator("TRASH", this.getResources().getDrawable(R.drawable.write_trash)); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, WriteWater.class);
        spec = tabHost.newTabSpec("Water"); // 객체를 생성
        spec.setIndicator("WATER", this.getResources().getDrawable(R.drawable.write_water)); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, Statistic.class);
        spec = tabHost.newTabSpec("Statistic"); // 객체를 생성
        spec.setIndicator("STATISTIC", this.getResources().getDrawable(R.drawable.statistic)); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, MyPage.class);
        spec = tabHost.newTabSpec("Mypage"); // 객체를 생성
        spec.setIndicator("MYPAGE", this.getResources().getDrawable(R.drawable.mypage)); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(0); //먼저 열릴 탭을 선택!

    }

}