package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class WaterPower extends AppCompatActivity {

    private OnEcoApplication application;

    // 사용자가 누르는 버튼에 따른 수도꼭지, 수압 처리
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.Wbath:
                    application.Wtap = "Wbath";
                    break;
                case R.id.Wsink:
                    application.Wtap = "Wsink";
                    break;
                case R.id.WshowerHead:
                    application.Wtap = "WshowerHead";
                    break;

                case R.id.Wstrenth:
                    application.Wpower = 140;
                    break;
                case R.id.Wmiddle:
                    application.Wpower = 80;
                    break;
                case R.id.Wweakness:
                    application.Wpower = 35;
                    break;
            }
            Log.d("jay", "Wpower: " + application.Wpower);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_power);


        application = (OnEcoApplication) getApplication();

        LinearLayout waterTab = findViewById(R.id.waterTap);
        LinearLayout waterPower = findViewById(R.id.waterPower);

        ImageButton btn_backToWriteWater = findViewById(R.id.btn_backToWriteWater);
        TextView Txt_waterTap = findViewById(R.id.Txt_waterTap);

        Button Wbath = findViewById(R.id.Wbath);
        Button Wsink = findViewById(R.id.Wsink);
        Button WshowerHead = findViewById(R.id.WshowerHead);

        Button next_choice = findViewById(R.id.next_choice);

        Button Wstrenth = findViewById(R.id.Wstrenth);
        Button Wmiddle = findViewById(R.id.Wmiddle);
        Button Wweakness = findViewById(R.id.Wweakness);

        Button before_choice = findViewById(R.id.before_choice);
        Button choice = findViewById(R.id.ok_choice);

        Wbath.setOnClickListener(listener);
        Wsink.setOnClickListener(listener);
        WshowerHead.setOnClickListener(listener);

        Wstrenth.setOnClickListener(listener);
        Wmiddle.setOnClickListener(listener);
        Wweakness.setOnClickListener(listener);

        // 상단 이전 버튼 누르면
        btn_backToWriteWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(application.active_activity.equals("MainHome")){
                    Intent intent = new Intent(getApplicationContext(), MainHome.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), WriteWater.class);
                    startActivity(intent);
                }
                setInit();
                application.active_activity = "";
            }
        });

        // 다음 버튼 누르면
        next_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(application.Wtap != ""){
                    waterTab.setVisibility(View.GONE);
                    waterPower.setVisibility(View.VISIBLE);
                    Txt_waterTap.setText("사용할 물의 세기를 선택해주세요");
                } else{
                    Toast.makeText(getApplicationContext(), "사용할 수도꼭지를 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 하단의 이전 버튼 누르면
        before_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterTab.setVisibility(View.VISIBLE);
                waterPower.setVisibility(View.GONE);
                Txt_waterTap.setText("사용할 수도꼭지를 선택해주세요");

                application.Wtap = "";
                application.Wpower = 0f;
            }
        });

        // 확인 버튼 누르면 액티비티(팝업) 닫기
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(application.Wpower != 0f){
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "사용할 수압을 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }   // end of onCreate


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    // 백버튼 막은 이유 : 백버튼 누르면 수도꼭지랑 수압 선택 안 한 상태로 스탑워치 화면이 뜨게 됨
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(application.active_activity.equals("MainHome")){
            Intent intent = new Intent(getApplicationContext(), MainHome.class);
            startActivity(intent);
        } else if (application.active_activity.equals("WriteWater")){
            Intent intent = new Intent(getApplicationContext(), WriteWater.class);
            startActivity(intent);
        }
        setInit();
        application.active_activity = "";
    }

    private void setInit(){
        application.waterType = "";
        application.Wtap = "";
        application.Wpower = 0f;
    }
}   // end of class