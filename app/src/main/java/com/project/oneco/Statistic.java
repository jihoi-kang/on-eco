package com.project.oneco;

import static android.view.View.GONE;
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
import android.widget.Toast;

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

    OnEcoApplication application;
    final int DIALOG_DATE = 1;

    private PreferenceManager preferenceManager;
    private Gson gson;

    private ArrayList<TrashUsage> trashUsageList;
    private ArrayList<WaterUsage> waterUsageList;
    private ArrayList<String> dayTotalList;

    String picked_date_key = "";

    public static int displayDate = 7; // 7, 30, 365

    private long backpressedTime = 0;

    // 차트에서 요일 변경해줌
    public static Date selectedDate;

    Button Btn_graph_trash;
    Button Btn_graph_water;
    Button Btn_week;
    Button Btn_month;
    Button Btn_year;

    BarChart bcChart;
    TextView Txt_pickDate;
    TextView Txt_item_all;
    TextView Txt_item1;
    TextView Txt_item2;
    TextView Txt_item3;
    TextView Txt_item4;
    TextView Txt_item5;
    TextView Txt_item6;
    TextView Txt_item_all01;
    TextView Txt_item01;
    TextView Txt_item02;
    TextView Txt_item03;
    TextView Txt_item04;
    TextView Txt_item05;
    TextView Txt_item06;

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
        Btn_graph_trash = findViewById(R.id.Btn_graph_trash);
        Btn_graph_water = findViewById(R.id.Btn_graph_water);
        Btn_week = findViewById(R.id.Btn_week);
        Btn_month = findViewById(R.id.Btn_month);
        Btn_year = findViewById(R.id.Btn_year);

        Txt_pickDate = findViewById(R.id.Txt_pickDate);
        Txt_item_all = findViewById(R.id.Txt_item_all);
        Txt_item1 = findViewById(R.id.Txt_item1);
        Txt_item2 = findViewById(R.id.Txt_item2);
        Txt_item3 = findViewById(R.id.Txt_item3);
        Txt_item4 = findViewById(R.id.Txt_item4);
        Txt_item5 = findViewById(R.id.Txt_item5);
        Txt_item6 = findViewById(R.id.Txt_item6);
        Txt_item_all01 = findViewById(R.id.Txt_item_all01);
        Txt_item01 = findViewById(R.id.Txt_item01);
        Txt_item02 = findViewById(R.id.Txt_item02);
        Txt_item03 = findViewById(R.id.Txt_item03);
        Txt_item04 = findViewById(R.id.Txt_item04);
        Txt_item05 = findViewById(R.id.Txt_item05);
        Txt_item06 = findViewById(R.id.Txt_item06);
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
        l.setEnabled(false);
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

        onBackPressed();

        Btn_week.setSelected(true);
        Btn_month.setSelected(false);
        Btn_year.setSelected(false);

        if (application.statisticType.equals("water-usage")) {
            setupWaterUsage();
        } else if (application.statisticType.equals("trash-usage")) {
            setupTrashUsage();
        }

        if (application.statisticType.equals("water-usage")){

        } else if (application.statisticType.equals("trash-usage")){
            Btn_graph_trash.setSelected(true);
            Btn_graph_water.setSelected(false);
        }

        // 쓰레기 배출량 버튼
        Btn_graph_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Btn_graph_trash.setSelected(true);
                Btn_graph_water.setSelected(false);
                application.statisticType = "trash-usage";
                setupTrashUsage();
                setupUiUsage();
            }
        });

        // 물 사용량 버튼
        Btn_graph_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Btn_graph_trash.setSelected(false);
                Btn_graph_water.setSelected(true);
                application.statisticType = "water-usage";
                setupWaterUsage();
                setupUiUsage();
            }
        });

        Btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Btn_week.setSelected(true);
                Btn_month.setSelected(false);
                Btn_year.setSelected(false);
                displayDate = 7;
                setGraphTotalVisibility(true);
                if (application.statisticType.equals("water-usage")) {
                    setupWaterUsage();
                } else if (application.statisticType.equals("trash-usage")) {
                    setupTrashUsage();
                }
            }
        });

        Btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Btn_week.setSelected(false);
                Btn_month.setSelected(true);
                Btn_year.setSelected(false);
                displayDate = 30;
                setGraphTotalVisibility(false);
                Log.d("jay", "statisticType: " + application.statisticType);
                if (application.statisticType.equals("water-usage")) {
                    setupWaterUsage();
                } else if (application.statisticType.equals("trash-usage")) {
                    setupTrashUsage();
                }
            }
        });

        Btn_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Btn_week.setSelected(false);
                Btn_month.setSelected(false);
                Btn_year.setSelected(true);
                displayDate = 365;
                setGraphTotalVisibility(false);
                if (application.statisticType.equals("water-usage")) {
                    setupWaterUsage();
                } else if (application.statisticType.equals("trash-usage")) {
                    setupTrashUsage();
                }
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
    } // end of setUpUi()

    @Override
    protected void onDestroy() {
        application.statisticType = "";
        super.onDestroy();
    }

    private void setupTrashUsage() {
        // 리스트 초기화
        trashUsageList.clear();
        dayTotalList.clear();

        trashTypeColor_View.setVisibility(VISIBLE);
        waterTypeColor_View.setVisibility(View.GONE);

        Btn_graph_trash.setSelected(true);
        Btn_graph_water.setSelected(false);

        // 1주일치 데이터 가져오기
        for (int i = 0; i < displayDate; i++) {
            // 오늘을 기준으로 -> 하단의 데이트피커에서 날짜를 선택하지 않았을 때 (picked_date_key값이 없을 때)
            if (picked_date_key.equals("")) {
                setupUiUsage();

                Date dDate = new Date();
                dDate = new Date(dDate.getTime() - 1000L * 60 * 60 * 24 * i);
                SimpleDateFormat dSdf = new SimpleDateFormat("yyMMdd", Locale.KOREA);
                String key_yesterday = dSdf.format(dDate.getTime());

                String yesterday_trashUsageStr = preferenceManager.getString(key_yesterday + "-trash-usage", "");

                if (yesterday_trashUsageStr.equals("")) {
                    trashUsageList.add(new TrashUsage());
                } else {
                    TrashUsage yesterday_trashUsage = gson.fromJson(yesterday_trashUsageStr, TrashUsage.class);
                    trashUsageList.add(yesterday_trashUsage);
                }
            }

            // 특정 날짜를 기준으로 -> 데이트피커에서 날짜를 선택했을 때
            else {
                String key_week = Integer.toString(Integer.parseInt(picked_date_key) - i); // 220621, 220620 ...

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
            float val1 = trashUsage.getNormalTrash();
            float val2 = trashUsage.getGlass();
            float val3 = trashUsage.getCan();
            float val4 = trashUsage.getPaper();
            float val5 = trashUsage.getPlastic();
            float val6 = trashUsage.getPlastic_bag();

            values.add(new BarEntry(
                    i,
                    new float[]{val1, val2, val3, val4, val5, val6},
                    getResources().getDrawable(R.drawable.ic_launcher_foreground)));

            dayTotalList.add(Float.toString(
                    trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag()
                            + trashUsage.getCan() + trashUsage.getGlass() + trashUsage.getNormalTrash()));
            Log.d("hun", "dayTotal_trash : " + dayTotalList.get(i));
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

        Btn_graph_trash.setSelected(false);
        Btn_graph_water.setSelected(true);

        // 1주일치 데이터 가져오기
        for (int i = 0; i < displayDate; i++) {
            if (picked_date_key.equals("")) {
                setupUiUsage();

                Date dDate = new Date();
                dDate = new Date(dDate.getTime() - 1000L * 60 * 60 * 24 * i);
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
                String key_week = Integer.toString(Integer.parseInt(picked_date_key) - i); // 220621, 220620 ...
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
            Log.d("hun", "dayTotal : " + dayTotalList.get(i));
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
        set1.setDrawValues(false);
        set1.setDrawIcons(false);

        if (label.equals("쓰레기 배출량")) {
            set1.setColors(
                    ContextCompat.getColor(this, R.color.normal_trash),
                    ContextCompat.getColor(this, R.color.glass),
                    ContextCompat.getColor(this, R.color.can),
                    ContextCompat.getColor(this, R.color.paper),
                    ContextCompat.getColor(this, R.color.plastic),
                    ContextCompat.getColor(this, R.color.pla_bag)
            );
            set1.setStackLabels(new String[]{"", "", ""});
        } else if (label.equals("물 사용량")) {
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
                    Txt_item_all.setText("총 쓰레기 배출량");
                    Txt_item1.setText("종이");
                    Txt_item2.setText("플라스틱");
                    Txt_item3.setText("비닐");
                    Txt_item4.setText("캔");
                    Txt_item5.setText("공병");
                    Txt_item6.setText("기타");

                    Txt_item_all01.setText("0 g");
                    Txt_item01.setText(": 0 g");
                    Txt_item02.setText(": 0 g");
                    Txt_item03.setText(": 0 g");
                    Txt_item04.setText(": 0 g");
                    Txt_item05.setText(": 0 g");
                    Txt_item06.setText(": 0 g");
                } else { // 데이터 꺼내오기
                    // String을 데이터 모델로 변경
                    TrashUsage today_key_trashUsage = gson.fromJson(today_key_trashUsageStr, TrashUsage.class);
                    float today_total_trash = today_key_trashUsage.getPaper() + today_key_trashUsage.getPlastic() + today_key_trashUsage.getPlastic_bag()
                            + today_key_trashUsage.getCan() + today_key_trashUsage.getGlass() + today_key_trashUsage.getNormalTrash();

                    Txt_item_all.setText("총 쓰레기 배출량");
                    Txt_item1.setText("종이");
                    Txt_item2.setText("플라스틱");
                    Txt_item3.setText("비닐");
                    Txt_item4.setText("캔");
                    Txt_item5.setText("공병");
                    Txt_item6.setText("기타");

                    Txt_item_all01.setText(": " + today_total_trash + " g");
                    Txt_item01.setText(": " + today_key_trashUsage.getNormalTrash() + " g");
                    Txt_item02.setText(": " + today_key_trashUsage.getGlass() + " g");
                    Txt_item03.setText(": " + today_key_trashUsage.getCan() + " g");
                    Txt_item04.setText(": " + today_key_trashUsage.getPaper() + " g");
                    Txt_item05.setText(": " + today_key_trashUsage.getPlastic() + " g");
                    Txt_item06.setText(": " + today_key_trashUsage.getPlastic_bag() + " g");

                }


            } else if (waterTypeColor_View.getVisibility() == VISIBLE) {
                String today_key_waterUsageStr = preferenceManager.getString(today_key + "-water-usage", "");

                if (today_key_waterUsageStr.equals("")) {
                    Txt_item_all.setText("총 물 사용량");
                    Txt_item1.setText("양치");
                    Txt_item2.setText("손 씻기");
                    Txt_item3.setText("세수");
                    Txt_item4.setText("샤워");
                    Txt_item5.setText("설거지");
                    Txt_item6.setText("기타");

                    Txt_item_all01.setText("0 ml");
                    Txt_item01.setText(": 0 ml");
                    Txt_item02.setText(": 0 ml");
                    Txt_item03.setText(": 0 ml");
                    Txt_item04.setText(": 0 ml");
                    Txt_item05.setText(": 0 ml");
                    Txt_item06.setText(": 0 ml");


                } else {
                    WaterUsage today_key_waterUsage = gson.fromJson(today_key_waterUsageStr, WaterUsage.class);

                    Txt_item_all.setText("총 물 사용량");
                    Txt_item1.setText("양치");
                    Txt_item2.setText("손 씻기");
                    Txt_item3.setText("세수");
                    Txt_item4.setText("샤워");
                    Txt_item5.setText("설거지");
                    Txt_item6.setText("기타");

                    Txt_item_all01.setText(": " + today_key_waterUsage.getWaterTotal() + " ml");
                    Txt_item01.setText(": " + today_key_waterUsage.getTooth() + " ml");
                    Txt_item02.setText(": " + today_key_waterUsage.getHand() + " ml");
                    Txt_item03.setText(": " + today_key_waterUsage.getFace() + " ml");
                    Txt_item04.setText(": " + today_key_waterUsage.getShower() + " ml");
                    Txt_item05.setText(": " + today_key_waterUsage.getDish() + " ml");
                    Txt_item06.setText(": " + today_key_waterUsage.getEtcWater() + " ml");

                    Log.d("hun", "doing");
                }
            }
        }

        // 데이트 피커에 선택된 날짜가 있다면
        else {
            if (trashTypeColor_View.getVisibility() == VISIBLE) {
                setupTrashUsage();

                // 선택한 날짜의 저장된 값 가져와서 찍어주기
                String picked_date_trashUsageStr = preferenceManager.getString(picked_date_key + "-trash-usage", "");
                Log.d("jay", "picked_date_trashUsageStr : " + picked_date_trashUsageStr);

                if (picked_date_trashUsageStr.equals("")) {
                    Txt_item_all.setText("총 쓰레기 배출량");
                    Txt_item1.setText("종이");
                    Txt_item2.setText("플라스틱");
                    Txt_item3.setText("비닐");
                    Txt_item4.setText("캔");
                    Txt_item5.setText("공병");
                    Txt_item6.setText("기타");

                    Txt_item_all01.setText("0 g");
                    Txt_item01.setText(": 0 g");
                    Txt_item02.setText(": 0 g");
                    Txt_item03.setText(": 0 g");
                    Txt_item04.setText(": 0 g");
                    Txt_item05.setText(": 0 g");
                    Txt_item06.setText(": 0 g");
                } else {
                    TrashUsage picked_trashUsage = gson.fromJson(picked_date_trashUsageStr, TrashUsage.class);
                    float picked_total_trash = picked_trashUsage.getPaper() + picked_trashUsage.getPlastic() + picked_trashUsage.getPlastic_bag()
                            + picked_trashUsage.getCan() + picked_trashUsage.getGlass() + picked_trashUsage.getNormalTrash();
                    Txt_item_all.setText("총 쓰레기 배출량");
                    Txt_item1.setText("종이");
                    Txt_item2.setText("플라스틱");
                    Txt_item3.setText("비닐");
                    Txt_item4.setText("캔");
                    Txt_item5.setText("공병");
                    Txt_item6.setText("기타");

                    Txt_item_all01.setText(": " + picked_trashUsage + " g");
                    Txt_item01.setText(": " + picked_trashUsage.getNormalTrash() + " g");
                    Txt_item02.setText(": " + picked_trashUsage.getGlass() + " g");
                    Txt_item03.setText(": " + picked_trashUsage.getCan() + " g");
                    Txt_item04.setText(": " + picked_trashUsage.getPaper() + " g");
                    Txt_item05.setText(": " + picked_trashUsage.getPlastic() + " g");
                    Txt_item06.setText(": " + picked_trashUsage.getPlastic_bag() + " g");
                }

            } else if (waterTypeColor_View.getVisibility() == VISIBLE) {
                setupWaterUsage();

                // 선택한 날짜의 저장된 값 가져와서 찍어주기
                String picked_date_waterUsageStr = preferenceManager.getString(picked_date_key + "-water-usage", "");

                Log.d("jay", "picked_date_waterUsageStr : " + picked_date_waterUsageStr);

                if (picked_date_waterUsageStr.equals("")) {
                    Txt_item_all.setText("총 물 사용량");
                    Txt_item1.setText("양치");
                    Txt_item2.setText("손 씻기");
                    Txt_item3.setText("세수");
                    Txt_item4.setText("샤워");
                    Txt_item5.setText("설거지");
                    Txt_item6.setText("기타");

                    Txt_item_all01.setText(": 0 ml");
                    Txt_item01.setText(": 0 ml");
                    Txt_item02.setText(": 0 ml");
                    Txt_item03.setText(": 0 ml");
                    Txt_item04.setText(": 0 ml");
                    Txt_item05.setText(": 0 ml");
                    Txt_item06.setText(": 0 ml");
                } else {
                    WaterUsage picked_waterUsage = gson.fromJson(picked_date_waterUsageStr, WaterUsage.class);
                    Txt_item_all.setText("총 물 사용량");
                    Txt_item1.setText("양치");
                    Txt_item2.setText("손 씻기");
                    Txt_item3.setText("세수");
                    Txt_item4.setText("샤워");
                    Txt_item5.setText("설거지");
                    Txt_item6.setText("기타");

                    Txt_item_all01.setText(picked_waterUsage.getWaterTotal() + " ml");
                    Txt_item01.setText(picked_waterUsage.getTooth() + " ml");
                    Txt_item02.setText(picked_waterUsage.getHand() + " ml");
                    Txt_item03.setText(picked_waterUsage.getFace() + " ml");
                    Txt_item04.setText(picked_waterUsage.getShower() + " ml");
                    Txt_item05.setText(picked_waterUsage.getDish() + " ml");
                    Txt_item06.setText(picked_waterUsage.getEtcWater() + " ml");
                }
            }
        }

    }

    private void setGraphTotalVisibility(boolean enable) { // true / false
        int visibility;
        if (enable) {
            visibility = VISIBLE;
        } else {
            visibility = GONE;
        }
        day1.setVisibility(visibility);
        day2.setVisibility(visibility);
        day3.setVisibility(visibility);
        day4.setVisibility(visibility);
        day5.setVisibility(visibility);
        day6.setVisibility(visibility);
        day7.setVisibility(visibility);
    }

    // todo: 처음에 trash에서 statistic으로 가면 백키 동작?
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }

    }

}   // end of class
