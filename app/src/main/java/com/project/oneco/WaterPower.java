package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
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


        // 다음 버튼 누르면
        next_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(application.Wtap != "null"){
                    waterTab.setVisibility(View.GONE);
                    waterPower.setVisibility(View.VISIBLE);
                } else{
                    Toast.makeText(getApplicationContext(), "사용할 수도꼭지를 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 이전 버튼 누르면
        before_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterTab.setVisibility(View.VISIBLE);
                waterPower.setVisibility(View.GONE);

                application.Wtap = "null";
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


//        //데이터 가져오기
//        Intent intent = getIntent();
//        String data = intent.getStringExtra("data");
//        //txtText.setText(data);

    }   // end of onCreate


//    // 확인 버튼 클릭
//    public void choice(View v){
//        //데이터 전달하기
//        Intent intent = new Intent();
//        intent.putExtra("result", "Close Popup");
//        setResult(RESULT_OK, intent);
//
//        //액티비티(팝업) 닫기
//        finish();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}   // end of class