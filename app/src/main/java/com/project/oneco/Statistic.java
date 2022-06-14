package com.project.oneco;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.WaterUsage;
import com.project.oneco.test.MyXAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Statistic extends AppCompatActivity {
    final int DIALOG_DATE = 1;
    OnEcoApplication application;

    BarChart bcChart;
    TextView Txt_pickDate;
    TextView Txt_aDayAllWater;
    TextView Txt_aDayTooth;
    TextView Txt_aDayHand;
    TextView Txt_aDayFace;
    TextView Txt_aDayShower;
    TextView Txt_aDayDish;
    TextView Txt_aDayEtc;

    private PreferenceManager preferenceManager;
    private Gson gson;

    private ArrayList<WaterUsage> waterUsageList;

    String picked_date_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);


        Button Btn_graph_trash = findViewById(R.id.Btn_graph_trash);
        Button Btn_graph_water = findViewById(R.id.Btn_graph_water);

        ImageView trashTypeColor_View = findViewById(R.id.trashTypeColor_View);
        ImageView waterTypeColor_View = findViewById(R.id.waterTypeColor_View);

        Txt_pickDate = findViewById(R.id.Txt_pickDate);
        Txt_aDayAllWater = findViewById(R.id.Txt_aDayAllWater);
        Txt_aDayTooth = findViewById(R.id.Txt_aDayTooth);
        Txt_aDayHand = findViewById(R.id.Txt_aDayHand);
        Txt_aDayFace = findViewById(R.id.Txt_aDayFace);
        Txt_aDayShower = findViewById(R.id.Txt_aDayShower);
        Txt_aDayDish = findViewById(R.id.Txt_aDayDish);
        Txt_aDayEtc = findViewById(R.id.Txt_aDayEtc);

        waterUsageList = new ArrayList<>();

        // 모든 class에서 참조할 수 있는 변수 선언
        application = (OnEcoApplication) getApplication();

        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();

        bcChart = findViewById(R.id.bc_chart);


        // 이전 버튼
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (application.active_activity) {
                    case "mainHome":
                        Intent intent = new Intent(getApplicationContext(), MainHome.class);
                        startActivity(intent);
                        application.active_activity = null;
                        break;
                    case "waterAfterStati":
                        Intent intent2 = new Intent(getApplicationContext(), WriteWater.class);
                        startActivity(intent2);
                        break;
                    default: onBackPressed();
                        break;
                }
            }
        });

        // 홈 화면으로 넘어가기
        TextView title_ONECO = findViewById(R.id.title_ONECO);
        title_ONECO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainHome.class);
                startActivity(intent);
            }
        });

        // 마이페이지로 넘어가기
        ImageButton goto_mypage = findViewById(R.id.goto_mypage);
        goto_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent);
            }
        });


        Btn_graph_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashTypeColor_View.setVisibility(View.VISIBLE);
                waterTypeColor_View.setVisibility(View.GONE);
            }
        });

        Btn_graph_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashTypeColor_View.setVisibility(View.GONE);
                waterTypeColor_View.setVisibility(View.VISIBLE);
            }
        });

        // <-- chart 그리기 - (1) setting

        bcChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        bcChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        bcChart.setPinchZoom(false);

        bcChart.setDrawGridBackground(false);
        bcChart.setDrawBarShadow(false);

        bcChart.setDrawValueAboveBar(false);
        bcChart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = bcChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        bcChart.getAxisRight().setEnabled(false);

        XAxis xLabels = bcChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setValueFormatter(new MyXAxisValueFormatter());

        Legend l = bcChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        // chart 그리기 - (1) setting -->

        Txt_pickDate.setText(application.todayDate);

        Txt_pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_DATE);
            }
        });

        // 오늘 포함 일주일치 사용한 물의 양 각각 불러오기
        waterData_week();

        // 오늘 날짜 데이터 값 불러서 UI 표시하기
        setTodayWaterUsage();

    }   // end of onCreate


    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DIALOG_DATE:
                DatePickerDialog dpd = new DatePickerDialog(Statistic.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                        monthOfYear = monthOfYear + 1;

                        String new_monthOfYear = "";

                        if (monthOfYear < 10){
                            new_monthOfYear = "0" + monthOfYear;
                        }

                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE", Locale.KOREA);
                        String dayName = simpledateformat.format(calendar.getTime());
                        Log.d("jay", "dayName"+ dayName);

                        Txt_pickDate.setText(year + "." + new_monthOfYear + "." + dayOfMonth + " (" + dayName + ")");

                        String pick_date = year + "" + new_monthOfYear + "" + dayOfMonth ; //20220614
                        picked_date_key = pick_date.substring(2,8);
                        Log.d("jay", "pick_date : " + pick_date);
                        Log.d("jay", "pick_date_key : " + picked_date_key);

                        // todo: 선택한 날짜의 저장된 값 가져와서 찍어주기
                        String picked_date_waterUsageStr = preferenceManager.getString(picked_date_key + "-water-usage", "");

                        Log.d("jay", "picked_date_waterUsageStr : " + picked_date_waterUsageStr);

                        if (picked_date_waterUsageStr.equals("")) {
                            // todo : ???
                        } else { // 데이터 꺼내오기
                            // String을 데이터 모델로 변경
                            WaterUsage picked_waterUsage = gson.fromJson(picked_date_waterUsageStr, WaterUsage.class);
                            Txt_aDayAllWater.setText("총 물 사용량 : " + picked_waterUsage.getWaterTotal() + " ml");
                            Txt_aDayTooth.setText("양치 : " + picked_waterUsage.getTooth() + " ml");
                            Txt_aDayHand.setText("손 씻기 : " + picked_waterUsage.getHand() + " ml");
                            Txt_aDayFace.setText("세수 : " + picked_waterUsage.getFace() + " ml");
                            Txt_aDayShower.setText("샤워 : " + picked_waterUsage.getShower() + " ml");
                            Txt_aDayDish.setText("설거지 : " + picked_waterUsage.getDish() + " ml");
                            Txt_aDayEtc.setText("기타 : " + picked_waterUsage.getEtcWater() + " ml");

                            Log.d("jay", "waterTotal: " + picked_waterUsage.getWaterTotal());
                            Log.d("jay", "tooth: " + picked_waterUsage.getTooth());
                            Log.d("jay", "hand: " + picked_waterUsage.getHand());
                            Log.d("jay", "face: " + picked_waterUsage.getFace());
                            Log.d("jay", "shower: " + picked_waterUsage.getShower());
                            Log.d("jay", "dish: " + picked_waterUsage.getDish());
                            Log.d("jay", "etc: " + picked_waterUsage.getEtcWater());
                        }
                    }
                },
                2022, 05, 18);
                return dpd;
        }
        return super.onCreateDialog(id);
    }

    // 오늘 포함 일주일치 사용한 물의 양 각각 불러오기
    public void waterData_week() {
        float preTotal = 0f;
        for(int i = 0; i<7; i++){
            Date dDate = new Date();
            dDate = new Date(dDate.getTime()+(1000*60*60*24*-i));
            SimpleDateFormat dSdf = new SimpleDateFormat("yyMMdd", Locale.KOREA);
            String key_yesterday = dSdf.format(dDate.getTime());

            String yesterday_waterUsageStr = preferenceManager.getString(key_yesterday + "-water-usage", "");
            Log.d("jay", "yesterday_waterUsageStr" + yesterday_waterUsageStr);

            if (yesterday_waterUsageStr.equals("")) {
                waterUsageList.add(new WaterUsage());
            } else {
                WaterUsage yesterday_waterUsage = gson.fromJson(yesterday_waterUsageStr, WaterUsage.class);
                waterUsageList.add(yesterday_waterUsage);
            }
        }


        // <-- chart 그리기 - (2) check values

        for (int i = 0; i < waterUsageList.size(); i++) {
            WaterUsage waterUsage = waterUsageList.get(i);
            Log.d("jay", "item" +i+ ": " + waterUsage.getWaterTotal());
        }
        // chart 그리기 - (2) check values -->


        // <-- chart 그리기 - (3) draw
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < waterUsageList.size(); i++) {
            WaterUsage waterUsage = waterUsageList.get(i);
            float val1 = waterUsage.getTooth();
            float val2 = waterUsage.getHand();
            float val3 = waterUsage.getFace();
            float val4 = waterUsage.getShower();
            float val5 = waterUsage.getDish();
            float val6 = waterUsage.getEtcWater();

            values.add(new BarEntry(
                    i,
                    new float[]{val1, val2, val3},
                    getResources().getDrawable(R.drawable.ic_launcher_foreground)));
        }

        BarDataSet set1;

        set1 = new BarDataSet(values, "물 사용량");
        set1.setDrawIcons(false);
        set1.setColors(getColors());
        set1.setStackLabels(new String[]{"양치", "손 씻기", "세수"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextColor(Color.WHITE);

        bcChart.setData(data);

        bcChart.setFitBars(true);
        bcChart.invalidate();
    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }
    // chart 그리기 - (3) draw -->


    public void setTodayWaterUsage() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String key = simpledateformat.format(calendar.getTime());
        String waterUsageStr = preferenceManager.getString(key + "-water-usage", "");   // 태그 붙이고 변수에 저장

        WaterUsage waterUsage;

        if (waterUsageStr.equals("")) {
            waterUsage = new WaterUsage();
        } else {
            waterUsage = gson.fromJson(waterUsageStr, WaterUsage.class);

            Txt_aDayAllWater.setText("총 물 사용량 : " + waterUsage.getWaterTotal() + " ml");
            Txt_aDayTooth.setText("양치 : " + waterUsage.getTooth() + " ml");
            Txt_aDayHand.setText("손 씻기 : " + waterUsage.getHand() + " ml");
            Txt_aDayFace.setText("세수 : " + waterUsage.getFace() + " ml");
            Txt_aDayShower.setText("샤워 : " + waterUsage.getShower() + " ml");
            Txt_aDayDish.setText("설거지 : " + waterUsage.getDish() + " ml");
            Txt_aDayEtc.setText("기타 : " + waterUsage.getEtcWater() + " ml");
        }
    }

}   // end of class