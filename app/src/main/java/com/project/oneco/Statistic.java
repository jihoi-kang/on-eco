package com.project.oneco;

import static android.view.View.VISIBLE;

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
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.TrashUsage;
import com.project.oneco.data.WaterUsage;
import com.project.oneco.test.MyXAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Statistic extends AppCompatActivity {

    final int DIALOG_DATE = 1;
    OnEcoApplication application;

    private PreferenceManager preferenceManager;
    private Gson gson;

    private ArrayList<TrashUsage> trashUsageList;
    private ArrayList<WaterUsage> waterUsageList;
    private ArrayList<String> dayTotalList;

    String picked_date_key = "";

    // 차트에서 요일 변경해줌
    public static Date selectedDate;

    BarChart bcChart;
    TextView Txt_pickDate;
    TextView Txt_item_all;
    TextView Txt_item1;
    TextView Txt_item2;
    TextView Txt_item3;
    TextView Txt_item4;
    TextView Txt_item5;
    TextView Txt_item6;

    TextView day1;
    TextView day2;
    TextView day3;
    TextView day4;
    TextView day5;
    TextView day6;
    TextView day7;

    ImageView trashTypeColor_View;
    ImageView waterTypeColor_View;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        // 모든 class에서 참조할 수 있는 변수 선언
        application = (OnEcoApplication) getApplication();
        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();

        selectedDate = new Date();
        waterUsageList = new ArrayList<>();
        trashUsageList = new ArrayList<>();
        dayTotalList = new ArrayList<>();

        trashTypeColor_View = findViewById(R.id.trashTypeColor_View);
        waterTypeColor_View = findViewById(R.id.waterTypeColor_View);

        setupUi();
    } // end of onCreate


    private void setupUi() {
        Button Btn_graph_trash = findViewById(R.id.Btn_graph_trash);
        Button Btn_graph_water = findViewById(R.id.Btn_graph_water);

        Txt_pickDate = findViewById(R.id.Txt_pickDate);
        Txt_item_all = findViewById(R.id.Txt_item6);
        Txt_item1 = findViewById(R.id.Txt_item2);
        Txt_item2 = findViewById(R.id.Txt_item5);
        Txt_item3 = findViewById(R.id.Txt_item1);
        Txt_item4 = findViewById(R.id.Txt_item_all);
        Txt_item5 = findViewById(R.id.Txt_item3);
        Txt_item6 = findViewById(R.id.Txt_item4);
        bcChart = findViewById(R.id.bc_chart);

        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        day6 = findViewById(R.id.day6);
        day7 = findViewById(R.id.day7);


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
        bcChart.getAxisLeft().setEnabled(false);

        XAxis xLabels = bcChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setValueFormatter(new MyXAxisValueFormatter());

        Legend l = bcChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(0f);
        l.setFormToTextSpace(0f);
        l.setXEntrySpace(0f);
        // chart 그리기 - (1) setting -->

        // 오늘 날짜로 세팅하기
        setupUiUsage();

        // 이전 버튼
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (application.active_activity) {
                    case "mainHome":
                        Intent intent = new Intent(getApplicationContext(), MainHome.class);
                        startActivity(intent);
                        application.active_activity = "";
                        break;
                    case "waterAfterStati":
                        Intent intent2 = new Intent(getApplicationContext(), WriteWater.class);
                        startActivity(intent2);
                        break;
                    default:
                        onBackPressed();
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

        // 쓰레기 배출량 버튼
        Btn_graph_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupTrashUsage();
                setupUiUsage();
            }
        });

        // 물 사용량 버튼
        Btn_graph_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupWaterUsage();
                setupUiUsage();
            }
        });

        // 넘어오는 경로에 따른 화면 변환 (쓰레기 or 물)
        if (application.statisticType.equals("water-usage")) {
            setupWaterUsage();
            setupUiUsage();
        } else if (application.statisticType.equals("trash-usage")) {
            setupTrashUsage();
            setupUiUsage();
        }
        application.statisticType = "";

    } // end of setUpUi()


    private void setupTrashUsage() {
        // 리스트 초기화
        trashUsageList.clear();
        dayTotalList.clear();

        trashTypeColor_View.setVisibility(VISIBLE);
        waterTypeColor_View.setVisibility(View.GONE);

        // 1주일치 데이터 가져오기
        for (int i = 0; i < 7; i++) {

            // 오늘을 기준으로 -> 하단의 데이트피커에서 날짜를 선택하지 않았을 때 (picked_date_key값이 없을 때)
            if(picked_date_key.equals("")){
                setupUiUsage();

                Date dDate = new Date();
                dDate = new Date(dDate.getTime() + (1000 * 60 * 60 * 24 * -i));
                SimpleDateFormat dSdf = new SimpleDateFormat("yyMMdd", Locale.KOREA);
                String key_yesterday = dSdf.format(dDate.getTime());

                String yesterday_trashUsageStr = preferenceManager.getString(key_yesterday + "-trash-usage", "");
                Log.d("jay", "week_trashUsageStr" + yesterday_trashUsageStr);

                if (yesterday_trashUsageStr.equals("")) {
                    trashUsageList.add(new TrashUsage());
                } else {
                    TrashUsage yesterday_trashUsage = gson.fromJson(yesterday_trashUsageStr, TrashUsage.class);
                    trashUsageList.add(yesterday_trashUsage);
                }
            }

            // 특정 날짜를 기준으로 -> 데이트피커에서 날짜를 선택했을 때
            else {
                String key_week = Integer.toString(Integer.parseInt(picked_date_key) - i) ; // 220621, 220620 ...

                String week_trashUsageStr = preferenceManager.getString(key_week + "-trash-usage", "");
                Log.d("jay", "week_trashUsageStr" + week_trashUsageStr);

                if (week_trashUsageStr.equals("")) {
                    trashUsageList.add(new TrashUsage());
                } else {
                    TrashUsage week_trashUsage = gson.fromJson(week_trashUsageStr, TrashUsage.class);
                    trashUsageList.add(week_trashUsage);
                }
            }
        }

        // 값 만들기
        ArrayList<BarEntry> values = new ArrayList<>();

        Collections.reverse(trashUsageList);
        for (int i = 0; i < trashUsageList.size(); i++) {
            TrashUsage trashUsage = trashUsageList.get(i);
            float val1 = trashUsage.getPaper();
            float val2 = trashUsage.getPlastic();
            float val3 = trashUsage.getPlastic_bag();
            float val4 = trashUsage.getCan();
            float val5 = trashUsage.getEmpty_bottle();
            float val6 = trashUsage.getTrashEtc();

            values.add(new BarEntry(
                    i,
                    new float[]{val1, val2, val3, val4, val5, val6},
                    getResources().getDrawable(R.drawable.ic_launcher_foreground)));

            dayTotalList.add(Float.toString(
                    trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag()
                            + trashUsage.getCan() + trashUsage.getEmpty_bottle() + trashUsage.getTrashEtc()));
            Log.d("hun","dayTotal_trash : " + dayTotalList.get(i));
        }

        // ui
        setChart(values, "쓰레기 배출량");
        day1.setText(dayTotalList.get(0) + " g");
        day2.setText(dayTotalList.get(1) + " g");
        day3.setText(dayTotalList.get(2) + " g");
        day4.setText(dayTotalList.get(3) + " g");
        day5.setText(dayTotalList.get(4) + " g");
        day6.setText(dayTotalList.get(5) + " g");
        day7.setText(dayTotalList.get(6) + " g");
    }

    private void setupWaterUsage() {
        waterUsageList.clear();
        dayTotalList.clear();

        trashTypeColor_View.setVisibility(View.GONE);
        waterTypeColor_View.setVisibility(VISIBLE);
        application.statisticType = "";

        // 1주일치 데이터 가져오기
        for (int i = 0; i < 7; i++) {
            if (picked_date_key.equals("")) {
                setupUiUsage();

                Date dDate = new Date();
                dDate = new Date(dDate.getTime() + (1000 * 60 * 60 * 24 * -i));
                SimpleDateFormat dSdf = new SimpleDateFormat("yyMMdd", Locale.KOREA);
                String key_yesterday = dSdf.format(dDate.getTime());

                String yesterday_waterUsageStr = preferenceManager.getString(key_yesterday + "-water-usage", "");
                Log.d("jay", "week_waterUsageStr" + yesterday_waterUsageStr);

                if (yesterday_waterUsageStr.equals("")) {
                    waterUsageList.add(new WaterUsage());
                } else {
                    WaterUsage yesterday_waterUsage = gson.fromJson(yesterday_waterUsageStr, WaterUsage.class);
                    waterUsageList.add(yesterday_waterUsage);
                }
            } else {
                String key_week = Integer.toString(Integer.parseInt(picked_date_key) - i) ; // 220621, 220620 ...
                Log.d("jay", "key_week" + key_week);
                String week_waterUsageStr = preferenceManager.getString(key_week + "-water-usage", "");
                Log.d("jay", "week_waterUsageStr" + week_waterUsageStr);

                if (week_waterUsageStr.equals("")) {
                    waterUsageList.add(new WaterUsage());
                } else {
                    WaterUsage week_waterUsage = gson.fromJson(week_waterUsageStr, WaterUsage.class);
                    waterUsageList.add(week_waterUsage);
                }
            }
        }

        // 값 만들기
        ArrayList<BarEntry> values = new ArrayList<>();
        Collections.reverse(waterUsageList);
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
                    new float[]{val1, val2, val3, val4, val5, val6},
                    getResources().getDrawable(R.drawable.ic_launcher_foreground)));
            dayTotalList.add(Float.toString(waterUsage.getWaterTotal()));
            Log.d("hun","dayTotal : " + dayTotalList.get(i));
        }

        // ui
        setChart(values, "물 사용량");
        day1.setText(dayTotalList.get(0) + " ml");
        day2.setText(dayTotalList.get(1) + " ml");
        day3.setText(dayTotalList.get(2) + " ml");
        day4.setText(dayTotalList.get(3) + " ml");
        day5.setText(dayTotalList.get(4) + " ml");
        day6.setText(dayTotalList.get(5) + " ml");
        day7.setText(dayTotalList.get(6) + " ml");
    }

    private void setChart(ArrayList<BarEntry> values, String label) {
        // <-- chart 그리기 - (2) draw
        BarDataSet set1 = new BarDataSet(values, label);
        set1.setDrawIcons(false);

        if (label.equals("쓰레기 배출량")){
            set1.setColors(
                    ContextCompat.getColor(this, R.color.paper),
                    ContextCompat.getColor(this, R.color.plastic),
                    ContextCompat.getColor(this, R.color.pla_bag),
                    ContextCompat.getColor(this, R.color.can),
                    ContextCompat.getColor(this, R.color.bottle),
                    ContextCompat.getColor(this, R.color.etc)
            );
            set1.setStackLabels(new String[]{"", "", ""});
        } else if (label.equals("물 사용량")){
            set1.setColors(
                    ContextCompat.getColor(this, R.color.tooth),
                    ContextCompat.getColor(this, R.color.hand),
                    ContextCompat.getColor(this, R.color.face),
                    ContextCompat.getColor(this, R.color.dish),
                    ContextCompat.getColor(this, R.color.shower),
                    ContextCompat.getColor(this, R.color.etc)
            );
            set1.setStackLabels(new String[]{"", "", ""});
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextColor(Color.WHITE);

        bcChart.setData(data);

        bcChart.setFitBars(true);
        bcChart.invalidate();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DATE:
                DatePickerDialog dpd = new DatePickerDialog(Statistic.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        selectedDate = calendar.getTime();

                        monthOfYear = monthOfYear + 1;

                        String new_monthOfYear = "";

                        if (monthOfYear < 10) {
                            new_monthOfYear = "0" + monthOfYear;
                        }

                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE", Locale.KOREA);
                        String dayName = simpledateformat.format(calendar.getTime());
                        Log.d("jay", "dayName" + dayName);

                        Txt_pickDate.setText(year + "." + new_monthOfYear + "." + dayOfMonth + " (" + dayName + ")");

                        String pick_date = year + "" + new_monthOfYear + "" + dayOfMonth; //20220614
                        picked_date_key = pick_date.substring(2, 8);    // 220614

                        setupUiUsage();
                    }
                },
                        2022, 05, 18);
                return dpd;
        }
        return super.onCreateDialog(id);
    }


    // 하단 ui 세팅하기
    public void setupUiUsage() {
        if (picked_date_key.equals("")) {
            // 오늘 날짜로 데이트 피커 날짜 세팅
            Txt_pickDate.setText(application.todayDate);
            Txt_pickDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(DIALOG_DATE);
                }
            });

            // 오늘 날짜로 데이터 모델링 값 세팅
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat ymd = new SimpleDateFormat("yyMMdd", Locale.KOREA);
            String today_key = ymd.format(date);    // 220621
            Log.d("jay", "today_key : " + today_key);

            if (trashTypeColor_View.getVisibility() == VISIBLE) {
                String today_key_trashUsageStr = preferenceManager.getString(today_key + "-trash-usage", "");

                if (today_key_trashUsageStr.equals("")) {
                    Txt_item_all.setText("총 쓰레기 배출량 : 0 g");
                    Txt_item1.setText("종이 : 0 g");
                    Txt_item2.setText("플라스틱 : 0 g");
                    Txt_item3.setText("비닐 : 0 g");
                    Txt_item4.setText("캔 : 0 g");
                    Txt_item5.setText("공병 : 0 g");
                    Txt_item6.setText("기타 : 0 g");
                } else { // 데이터 꺼내오기
                    // String을 데이터 모델로 변경
                    TrashUsage today_key_trashUsage = gson.fromJson(today_key_trashUsageStr, TrashUsage.class);
                    float today_total_trash = today_key_trashUsage.getPaper() + today_key_trashUsage.getPlastic() + today_key_trashUsage.getPlastic_bag()
                            + today_key_trashUsage.getCan() + today_key_trashUsage.getEmpty_bottle() + today_key_trashUsage.getTrashEtc();
                    Txt_item_all.setText("총 쓰레기 배출량 : " + today_total_trash + " g");
                    Txt_item1.setText("종이 : " + today_key_trashUsage.getPaper() + " g");
                    Txt_item2.setText("플라스틱 : " + today_key_trashUsage.getPlastic() + " g");
                    Txt_item3.setText("비닐 : " + today_key_trashUsage.getPlastic_bag() + " g");
                    Txt_item4.setText("캔 : " + today_key_trashUsage.getCan() + " g");
                    Txt_item5.setText("공병 : " + today_key_trashUsage.getEmpty_bottle() + " g");
                    Txt_item6.setText("기타 : " + today_key_trashUsage.getTrashEtc() + " g");
                }

            } else if (waterTypeColor_View.getVisibility() == VISIBLE) {
                String today_key_waterUsageStr = preferenceManager.getString(today_key + "-water-usage", "");

                if (today_key_waterUsageStr.equals("")) {
                    Txt_item_all.setText("총 물 사용량 : 0 ml");
                    Txt_item1.setText("양치 : 0 ml");
                    Txt_item2.setText("손 씻기 : 0 ml");
                    Txt_item3.setText("세수 : 0 ml");
                    Txt_item4.setText("샤워 : 0 ml");
                    Txt_item5.setText("설거지 : 0 ml");
                    Txt_item6.setText("기타 : 0 ml");
                } else {
                    WaterUsage today_key_waterUsage = gson.fromJson(today_key_waterUsageStr, WaterUsage.class);

                    Txt_item_all.setText("총 물 사용량 : " + today_key_waterUsage.getWaterTotal() + " ml");
                    Txt_item1.setText("양치 : " + today_key_waterUsage.getTooth() + " ml");
                    Txt_item2.setText("손 씻기 : " + today_key_waterUsage.getHand() + " ml");
                    Txt_item3.setText("세수 : " + today_key_waterUsage.getFace() + " ml");
                    Txt_item4.setText("샤워 : " + today_key_waterUsage.getShower() + " ml");
                    Txt_item5.setText("설거지 : " + today_key_waterUsage.getDish() + " ml");
                    Txt_item6.setText("기타 : " + today_key_waterUsage.getEtcWater() + " ml");

                    Log.d("hun","doing");
                }
            }
        }

        // 데이트 피커에 선택된 날짜가 있다면
        else {
            if(trashTypeColor_View.getVisibility() == VISIBLE){
                setupTrashUsage();

                // 선택한 날짜의 저장된 값 가져와서 찍어주기
                String picked_date_trashUsageStr = preferenceManager.getString(picked_date_key + "-trash-usage", "");
                Log.d("jay", "picked_date_trashUsageStr : " + picked_date_trashUsageStr);

                if (picked_date_trashUsageStr.equals("")) {
                    Txt_item_all.setText("총 쓰레기 배출량 : 0 g");
                    Txt_item1.setText("종이 : 0 g");
                    Txt_item2.setText("플라스틱 : 0 g");
                    Txt_item3.setText("비닐 : 0 g");
                    Txt_item4.setText("캔 : 0 g");
                    Txt_item5.setText("공병 : 0 g");
                    Txt_item6.setText("기타 : 0 g");
                } else {
                    TrashUsage picked_trashUsage = gson.fromJson(picked_date_trashUsageStr, TrashUsage.class);
                    float picked_total_trash = picked_trashUsage.getPaper() + picked_trashUsage.getPlastic() + picked_trashUsage.getPlastic_bag()
                            + picked_trashUsage.getCan() + picked_trashUsage.getEmpty_bottle() + picked_trashUsage.getTrashEtc();
                    Txt_item_all.setText("총 쓰레기 배출량 : " + picked_total_trash + " g");
                    Txt_item1.setText("종이 : " + picked_trashUsage.getPaper() + " g");
                    Txt_item2.setText("플라스틱 : " + picked_trashUsage.getPlastic() + " g");
                    Txt_item3.setText("비닐 : " + picked_trashUsage.getPlastic_bag() + " g");
                    Txt_item4.setText("캔 : " + picked_trashUsage.getCan() + " g");
                    Txt_item5.setText("공병 : " + picked_trashUsage.getEmpty_bottle() + " g");
                    Txt_item6.setText("기타 : " + picked_trashUsage.getTrashEtc() + " g");
                }

            } else if (waterTypeColor_View.getVisibility() == VISIBLE){
                setupWaterUsage();

                // 선택한 날짜의 저장된 값 가져와서 찍어주기
                String picked_date_waterUsageStr = preferenceManager.getString(picked_date_key + "-water-usage", "");

                Log.d("jay", "picked_date_waterUsageStr : " + picked_date_waterUsageStr);

                if (picked_date_waterUsageStr.equals("")) {
                    Txt_item_all.setText("총 물 사용량 : 0 ml");
                    Txt_item1.setText("양치 : 0 ml");
                    Txt_item2.setText("손 씻기 : 0 ml");
                    Txt_item3.setText("세수 : 0 ml");
                    Txt_item4.setText("샤워 : 0 ml");
                    Txt_item5.setText("설거지 : 0 ml");
                    Txt_item6.setText("기타 : 0 ml");
                } else {
                    WaterUsage picked_waterUsage = gson.fromJson(picked_date_waterUsageStr, WaterUsage.class);
                    Txt_item_all.setText("총 물 사용량 : " + picked_waterUsage.getWaterTotal() + " ml");
                    Txt_item1.setText("양치 : " + picked_waterUsage.getTooth() + " ml");
                    Txt_item2.setText("손 씻기 : " + picked_waterUsage.getHand() + " ml");
                    Txt_item3.setText("세수 : " + picked_waterUsage.getFace() + " ml");
                    Txt_item4.setText("샤워 : " + picked_waterUsage.getShower() + " ml");
                    Txt_item5.setText("설거지 : " + picked_waterUsage.getDish() + " ml");
                    Txt_item6.setText("기타 : " + picked_waterUsage.getEtcWater() + " ml");
                }
            }
        }

    }
}   // end of class
