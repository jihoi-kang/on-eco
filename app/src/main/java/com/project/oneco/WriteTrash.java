package com.project.oneco;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.project.oneco.data.MyTrash;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.TrashUsage;
import com.project.oneco.tensorflow.ClassifierActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WriteTrash extends AppCompatActivity implements OnTrashItemClickListener {

    androidx.appcompat.widget.AppCompatButton BTN_trash_me;
    androidx.appcompat.widget.AppCompatButton BTN_trash_us;

    LinearLayout LO_myTrash;
    LinearLayout LO_ourTrash;
    LinearLayout LO_write_detail;

    Button BTN_my_trash;
    Button BTN_our_trash;

    Button Btn_normal_trash;
    Button Btn_glass;
    Button Btn_can;
    Button Btn_paper;
    Button Btn_plastic;
    Button Btn_plastic_bag;

    Button Btn_add;
    Button Btn_sub;
    Button Btn_add_me;
    Button Btn_cancel_me;

    EditText ET_my_weight;
    EditText ET_trash_memo;
    EditText ET_us_weight;
    EditText ET_family_num;

    TextView TXT_myTrash_num;
    TextView TXT_myTrash_weight;
    TextView TXT_compare_trash_num;
    TextView TXT_compare_trash_weight;

    TextView TXT_usTrash_weight;
    TextView TXT_mean_family_weight;
    TextView TXT_today_date;

    RecyclerView rvList;
    RecyclerView rvMyTrashList;

    private final static String TAG = "WriteTrash";
    // 쓰레기 종류
    private String trashType;
    private OnEcoApplication application;

    float us_total = 0;
    private float us_trash_weight = 0;
    private float my_trash_weight = 0;
    private int family_num = 0;
    private float mean_family_weight = 0f;

    private int touchCount1, touchCount2, touchCount3, touchCount4, touchCount5, touchCount6 = 0;

    private PreferenceManager preferenceManager;
    private Gson gson;

    //    ListView lvList;
//    ArrayAdapter<String> adapter;
    TrashAdapter trashAdapter;
    WriteTrashAdapter writeTrashAdapter;
    ArrayList<MyTrash> myTrashList;

    // listView에 들어갈 item들 정의(9개 >> 6개)
    String[] normalTrashItems = {"물티슈", "각티슈", "손 닦는 휴지", "두루말이 휴지"};
    String[] glassItems = {"100ml", "180ml"};
    String[] canItems = {"250ml", "355ml", "500ml", "750ml", "참치캔(100g)"};
    String[] paperItems = {"A4", "B4", "종이 정수기컵", "종이 자판기컵", "종이컵 Tall 사이즈(355ml)", "종이컵 Grande 사이즈(473ml)", "종이컵 Venti 사이즈(591ml)", "택배박스 1호(50cm)", "택배박스 2호(60cm)", "택배박스 3호(80cm)", "택배박스 4호(100cm)", "택배박스 5호(120cm)"};
    String[] plasticItems = {"플라스틱컵 Tall 사이즈(355ml)", "플라스틱컵 Grande 사이즈(473ml)", "플라스틱컵 Venti 사이즈(591ml)", "250ml", "500ml", "1L", "2L"};
    String[] plasticBagItems = {"3L", "5L", "10L", "20L"};

    //todo: 검색엔진 미스매칭 아이콘, 밑줄 안나옴
    //todo: 리스트 스크롤 안됨
    // todo: 수민님 나-> 무게, 메모 포커스 아웃

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trash);

        // listview bind & item click listener 달기(setOnItemClickListener)
//        lvList = findViewById(R.id.lv_list);
//        lvList.setOnItemClickListener(this);
        rvList = findViewById(R.id.rv_list);

        LO_myTrash = findViewById(R.id.LO_myTrash);
        LO_ourTrash = findViewById(R.id.LO_ourTrash);
        LO_write_detail = findViewById(R.id.LO_write_detail);

        BTN_my_trash = findViewById(R.id.BTN_my_trash);
        BTN_our_trash = findViewById(R.id.BTN_our_trash);

        ET_my_weight = findViewById(R.id.ET_my_weight);
        ET_trash_memo = findViewById(R.id.ET_trash_memo);
        ET_us_weight = findViewById(R.id.ET_us_weight);
        ET_family_num = findViewById(R.id.ET_family_num);

        TXT_myTrash_num = findViewById(R.id.TXT_myTrash_num);
        TXT_myTrash_weight = findViewById(R.id.TXT_myTrash_weight);
        TXT_compare_trash_num = findViewById(R.id.TXT_compare_trash_num);
        TXT_compare_trash_weight = findViewById(R.id.TXT_compare_trash_weight);

        TXT_usTrash_weight = findViewById(R.id.TXT_usTrash_weight);
        TXT_mean_family_weight = findViewById(R.id.TXT_mean_family_weight);
        TXT_today_date = findViewById(R.id.TXT_today_date);

        rvMyTrashList = findViewById(R.id.rv_my_trash_list);

        // Button 9개 bind >> 6로 수정
        Btn_normal_trash = findViewById(R.id.Btn_normal_trash);
        Btn_glass = findViewById(R.id.Btn_glass);
        Btn_can = findViewById(R.id.Btn_can);
        Btn_paper = findViewById(R.id.Btn_paper);
        Btn_plastic = findViewById(R.id.Btn_plastic);
        Btn_plastic_bag = findViewById(R.id.Btn_plastic_bag);

        Btn_add = findViewById(R.id.Btn_add);
        Btn_sub = findViewById(R.id.Btn_sub);
        Btn_add_me = findViewById(R.id.Btn_add_me);
        Btn_cancel_me = findViewById(R.id.Btn_cancel_me);

        application = (OnEcoApplication) getApplication();
        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();

        TXT_today_date.setText(application.todayDate);

        application.statisticType = "trash-usage";

        BTN_my_trash.setSelected(true);
        BTN_our_trash.setSelected(false);

        checkPermission();
        checkPermission_camera();

        writeTrashAdapter = new WriteTrashAdapter();
        rvMyTrashList.setAdapter(writeTrashAdapter);

        myTrashList = new ArrayList();

        // trash-amount 값 가져오기
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String key = simpledateformat.format(calendar.getTime()); // 220530
        String trashAmountListStr = preferenceManager.getString(key + "-trash-amount", "");
        Log.d("jay", "trashAmountListStr:" + trashAmountListStr);
        try {
            JSONArray jsonArray = new JSONArray(trashAmountListStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                MyTrash trash = gson.fromJson(object.toString(), MyTrash.class);
                myTrashList.add(trash);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        writeTrashAdapter.updateItems(myTrashList);
        setTrashAmount();

        trashAdapter = new TrashAdapter(this);
        rvList.setAdapter(trashAdapter);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        // 사용자 입력 텍스트를 인트 변수 currentItemWeight에 저장
        ET_us_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("jay", "charSequence: " + charSequence);
                if (charSequence.toString().equals("")) return;
                us_trash_weight = Float.parseFloat(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // todo:저장이 안됨?
        // 사용자 입력 텍스트를 인트 변수 family_num에 저장
        ET_family_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("jay", "charSequence: " + charSequence);
                if (charSequence.toString().equals("")) return;
                family_num = Integer.parseInt(charSequence.toString());
                mean_family_weight = family_num * 0.893f * 30;
                TXT_mean_family_weight.setText(mean_family_weight + " kg");

                compareTrashDischarge(us_total, mean_family_weight);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // family_num 인 가구 월 평균 쓰레기 배출량
        mean_family_weight = family_num * 0.893f * 30;

        ET_my_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("jay", "charSequence: " + charSequence);
                if (charSequence.toString().equals("")) return;
                my_trash_weight = Float.parseFloat(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // 검색화면으로 넘어가기
        ImageButton Btn_search = findViewById(R.id.btn_search);
        Btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        // 쓰레기 스캔
        Button Btn_scan_trash = findViewById(R.id.Btn_scan_trash);
        Btn_scan_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);
            }
        });

        // 샤워 타이머 게임
        Button Btn_measure_shower = findViewById(R.id.Btn_measure_shower);
        Btn_measure_shower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.waterType = "shower";
                application.bf_activity = "WriteWater";
                Intent intent = new Intent(getApplicationContext(), WaterStopGame.class);
                startActivity(intent);
            }
        });

        // 내 쓰레기
        BTN_my_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.whoseTrash = "my";
                setupMyTrash();
            }
        });

        // 우리집 쓰레기
        BTN_our_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.whoseTrash = "our";
                setupOurTrash();
            }
        });

        // todo : 내 쓰레기와 우리집 쓰레기 상태에서 누를 때 if문으로 구분
        // Button을 눌렀을 때 trashType에 저장.(9개 모두 구현)
        Btn_normal_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.Btn_normal_trash:
                        application.trashType = "normal_trash";
                        Btn_normal_trash.setSelected(true);
                        Btn_glass.setSelected(false);
                        Btn_can.setSelected(false);
                        Btn_paper.setSelected(false);
                        Btn_plastic.setSelected(false);
                        Btn_plastic_bag.setSelected(false);
                }
                Log.d("jay", "passed");
                // trashType에 쓰레기 유형 저장
                trashType = "normal_trash";

                // todo : 아마도 아래 부분은 우리집 쓰레기 상태에서 누를 때 동작할 필요 없음

                trashAdapter.updateItems(Arrays.asList(normalTrashItems));
                touchCount1++;
                setVisible_WriteDetail();

            }
        });

        Btn_glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "glass";
                // item들을 riteTrash 셋해준다.
                switch (view.getId()) {
                    case R.id.Btn_glass:
                        application.trashType = "glass";
                        Btn_normal_trash.setSelected(false);
                        Btn_glass.setSelected(true);
                        Btn_can.setSelected(false);
                        Btn_paper.setSelected(false);
                        Btn_plastic.setSelected(false);
                        Btn_plastic_bag.setSelected(false);
                }
                trashAdapter.updateItems(Arrays.asList(glassItems));
                touchCount2++;
                setVisible_WriteDetail();
            }
        });


        Btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "can";
                switch (view.getId()) {
                    case R.id.Btn_can:
                        application.trashType = "can";
                        Btn_normal_trash.setSelected(false);
                        Btn_glass.setSelected(false);
                        Btn_can.setSelected(true);
                        Btn_paper.setSelected(false);
                        Btn_plastic.setSelected(false);
                        Btn_plastic_bag.setSelected(false);
                }
                trashAdapter.updateItems(Arrays.asList(canItems));
                touchCount3++;
                setVisible_WriteDetail();
            }

        });

        Btn_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "paper";
                switch (view.getId()) {
                    case R.id.Btn_paper:
                        application.trashType = "paper";
                        Btn_normal_trash.setSelected(false);
                        Btn_glass.setSelected(false);
                        Btn_can.setSelected(false);
                        Btn_paper.setSelected(true);
                        Btn_plastic.setSelected(false);
                        Btn_plastic_bag.setSelected(false);
                }
                trashAdapter.updateItems(Arrays.asList(paperItems));
                touchCount4++;
                setVisible_WriteDetail();
            }
        });

        Btn_plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "plastic";
                switch (view.getId()) {
                    case R.id.Btn_plastic:
                        application.trashType = "plastic";
                        Btn_normal_trash.setSelected(false);
                        Btn_glass.setSelected(false);
                        Btn_can.setSelected(false);
                        Btn_paper.setSelected(false);
                        Btn_plastic.setSelected(true);
                        Btn_plastic_bag.setSelected(false);
                }
                trashAdapter.updateItems(Arrays.asList(plasticItems));
                touchCount5++;
                setVisible_WriteDetail();
            }
        });

        Btn_plastic_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "plastic_bag";
                switch (view.getId()) {
                    case R.id.Btn_plastic_bag:
                        application.trashType = "plastic_bag";
                        Btn_normal_trash.setSelected(false);
                        Btn_glass.setSelected(false);
                        Btn_can.setSelected(false);
                        Btn_paper.setSelected(false);
                        Btn_plastic.setSelected(false);
                        Btn_plastic_bag.setSelected(true);
                }
                trashAdapter.updateItems(Arrays.asList(plasticBagItems));
                touchCount6++;
                setVisible_WriteDetail();
            }
        });

        // 나 -> 추가 버튼
        Btn_add_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trashType == null) {
                    Toast.makeText(getApplicationContext(), "쓰레기 종류를 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // todo: 오늘 배출한 쓰레기 개수 and 무게 and 메모 저장
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
                    String key = simpledateformat.format(calendar.getTime()); // 220530

                    Log.d("jay", "key: " + key);

                    // trash-usage
                    String trashUsageStr = preferenceManager.getString(key + "-trash-usage", "");
                    TrashUsage trashUsage;

                    // 프리퍼런스에 저장된 값이 없을 시
                    if (trashUsageStr.equals("")) {
                        trashUsage = new TrashUsage();
                    } else {
                        trashUsage = gson.fromJson(trashUsageStr, TrashUsage.class);
                    }

                    Log.d("jay", "trashUsageStr: " + trashUsageStr);

                    // 각각의 쓰레기 타입에 저장
                    if (trashType.equals("normal_trash")) {
                        float normal_trash = trashUsage.getNormalTrash();
                        trashUsage.setNormalTrash(normal_trash + my_trash_weight);
                        application.count_normal_trash++;
                        Btn_normal_trash.setText("일반 / 기타\n" + application.count_normal_trash);
                    } else if (trashType.equals("glass")) {
                        float glass = trashUsage.getGlass();
                        trashUsage.setGlass(glass + my_trash_weight);
                        application.count_glass++;
                        Btn_glass.setText("유리\n" + application.count_glass);
                    } else if (trashType.equals("can")) {
                        float can = trashUsage.getCan();
                        trashUsage.setCan(can + my_trash_weight);
                        application.count_can++;
                        Btn_can.setText("캔\n" + application.count_can);
                    } else if (trashType.equals("paper")) {
                        float paper = trashUsage.getPaper();
                        trashUsage.setPaper(paper + my_trash_weight);
                        application.count_paper++;
                        Btn_paper.setText("종이\n" + application.count_paper);
                    } else if (trashType.equals("plastic")) {
                        float plastic = trashUsage.getPlastic();
                        trashUsage.setPlastic(plastic + my_trash_weight);
                        application.count_plastic++;
                        Btn_plastic.setText("플라스틱\n" + application.count_plastic);
                    } else if (trashType.equals("plastic_bag")) {
                        float plastic_bag = trashUsage.getPlastic_bag();
                        trashUsage.setPlastic_bag(plastic_bag + my_trash_weight);
                        application.count_plastic_bag++;
                        Btn_plastic_bag.setText("비닐\n" + application.count_plastic_bag);
                    }

                    // localStorage에 저장
                    String updatedTrashUsage = gson.toJson(trashUsage);
                    preferenceManager.putString(key + "-trash-usage", updatedTrashUsage);

                    // trash-amount
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
                    String dateStr = dateFormat.format(calendar.getTime());
                    MyTrash myTrash = new MyTrash(dateStr, trashType, my_trash_weight, ET_trash_memo.getText().toString());
                    myTrashList.add(myTrash);
                    preferenceManager.putString(key + "-trash-amount", gson.toJson(myTrashList));

                    writeTrashAdapter.updateItems(myTrashList);
                    ET_trash_memo.setText("");

                    // todo: 내 쓰레기와 우리집 쓰레기 g 구분
                    // 쓰레기 전체 g 구하기
                    float total = trashUsage.getNormalTrash() + trashUsage.getGlass() + trashUsage.getCan()
                            + trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag();

                    TXT_myTrash_weight.setText(total + "g");
                    setPreSavedTrash(total);
                }

                touchCount1++;
                touchCount2++;
                touchCount3++;
                touchCount4++;
                touchCount5++;
                touchCount6++;
                setVisible_WriteDetail();

                // todo: 통계는 개수로 변경

                // todo: 앱 나가도 저장되도록
                application.count_trash_total = application.count_paper + application.count_plastic +
                        application.count_plastic_bag + application.count_can + application.count_glass + application.count_normal_trash;
                TXT_myTrash_num.setText(application.count_trash_total + " 개");

                // 키보드 내리기
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(ET_my_weight.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(ET_trash_memo.getWindowToken(), 0);
            }
        });


        //todo: 전일 대비 배출한 쓰레기 개수

        // 우리집 -> + 버튼
        Btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trashType == null) {
                    Toast.makeText(getApplicationContext(), "쓰레기 종류를 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 오늘 배출한 쓰레기 값 저장
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
                    String key = simpledateformat.format(calendar.getTime()); // 220530

                    Log.d("jay", "key: " + key);

                    String trashUsageStr = preferenceManager.getString(key + "-trash-usage", "");
                    TrashUsage trashUsage;

                    // 프리퍼런스에 저장된 값이 없을 시
                    if (trashUsageStr.equals("")) {
                        trashUsage = new TrashUsage();
                    } else {
                        trashUsage = gson.fromJson(trashUsageStr, TrashUsage.class);
                    }

                    Log.d("jay", "trashUsageStr: " + trashUsageStr);

                    // 각각의 쓰레기 타입에 저장
                    if (trashType.equals("normal_trash")) {
                        float normal_trash = trashUsage.getNormalTrash();
                        trashUsage.setNormalTrash(normal_trash + us_trash_weight);
                    } else if (trashType.equals("glass")) {
                        float glass = trashUsage.getGlass();
                        trashUsage.setGlass(glass + us_trash_weight);
                    } else if (trashType.equals("can")) {
                        float can = trashUsage.getCan();
                        trashUsage.setCan(can + us_trash_weight);
                    } else if (trashType.equals("paper")) {
                        float paper = trashUsage.getPaper();
                        trashUsage.setPaper(paper + us_trash_weight);
                    } else if (trashType.equals("plastic")) {
                        float plastic = trashUsage.getPlastic();
                        trashUsage.setPlastic(plastic + us_trash_weight);
                    } else if (trashType.equals("plastic_bag")) {
                        float plastic_bag = trashUsage.getPlastic_bag();
                        trashUsage.setPlastic_bag(plastic_bag + us_trash_weight);
                    }

                    // localStorage에 저장
                    String updatedTrashUsage = gson.toJson(trashUsage);
                    preferenceManager.putString(key + "-trash-usage", updatedTrashUsage);


                    // 쓰레기 전체 g 구하기
                    us_total = trashUsage.getNormalTrash() + trashUsage.getGlass() + trashUsage.getCan()
                            + trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag();

                    TXT_usTrash_weight.setText(us_total + " kg");
                    setPreSavedTrash(us_total);


                    // 키보드 내리기
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(ET_us_weight.getWindowToken(), 0);
                }
            }
        });

        Btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trashType == null) {
                    Toast.makeText(getApplicationContext(), "쓰레기 종류를 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 오늘 배출한 쓰레기 값 저장
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
                    String key = simpledateformat.format(calendar.getTime()); // 220530

                    Log.d("jay", "key: " + key);

                    String trashUsageStr = preferenceManager.getString(key + "-trash-usage", "");
                    TrashUsage trashUsage;

                    // 저장된 값이 없을 시
                    if (trashUsageStr.equals("")) {
                        trashUsage = new TrashUsage();
                    } else {
                        trashUsage = gson.fromJson(trashUsageStr, TrashUsage.class);
                    }

                    Log.d("jay", "trashUsageStr: " + trashUsageStr);

                    // 각각의 쓰레기 타입에 저장
                    if (trashType.equals("normal_trash")) {
                        float normal_trash = trashUsage.getNormalTrash();
                        trashUsage.setNormalTrash(normal_trash - us_trash_weight);
                    } else if (trashType.equals("glass")) {
                        float glass = trashUsage.getGlass();
                        trashUsage.setGlass(glass - us_trash_weight);
                    } else if (trashType.equals("can")) {
                        float can = trashUsage.getCan();
                        trashUsage.setCan(can - us_trash_weight);
                    } else if (trashType.equals("paper")) {
                        float paper = trashUsage.getPaper();
                        trashUsage.setPaper(paper - us_trash_weight);
                    } else if (trashType.equals("plastic")) {
                        float plastic = trashUsage.getPlastic();
                        trashUsage.setPlastic(plastic - us_trash_weight);
                    } else if (trashType.equals("plastic_bag")) {
                        float plastic_bag = trashUsage.getPlastic_bag();
                        trashUsage.setPlastic_bag(plastic_bag - us_trash_weight);
                    }

                    // localStorage에 저장
                    String updatedTrashUsage = gson.toJson(trashUsage);
                    preferenceManager.putString(key + "-trash-usage", updatedTrashUsage);

                    // 쓰레기 전체 g 구하기
                    float total = trashUsage.getNormalTrash() + trashUsage.getGlass() + trashUsage.getCan()
                            + trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag();
                    TXT_usTrash_weight.setText(total + "g"); // // todo: 개수로 변경 TXT_today_trash_input는 개수로 변경
                    TXT_usTrash_weight.setText("이번 달 우리 집 쓰레기 배출량 (" + total + "kg)");
                    setPreSavedTrash(total);

                    // 키보드 내리기
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(ET_us_weight.getWindowToken(), 0);
                }
            }
        });

        TXT_mean_family_weight.setText(mean_family_weight + " kg");

        // 저장되어 있는 trash 값 화면에 반영
        SetFirstBottomUI();

    }

    /**
     * end of onCreate
     **/


    private void setWhoseTrash() {
        if (application.whoseTrash.equals("my")) {
            LO_myTrash.setVisibility(View.VISIBLE);
            LO_ourTrash.setVisibility(View.GONE);
            BTN_my_trash.setSelected(true);
            BTN_our_trash.setSelected(false);

            setTrashAmount();

        } else if (application.whoseTrash.equals("our")) {
            LO_myTrash.setVisibility(View.GONE);
            LO_ourTrash.setVisibility(View.VISIBLE);
            BTN_my_trash.setSelected(false);
            BTN_our_trash.setSelected(true);

            // 버튼 이름만 표시하도록
            Btn_normal_trash.setText("일반 / 기타");
            Btn_glass.setText("유리");
            Btn_can.setText("캔");
            Btn_paper.setText("종이");
            Btn_plastic.setText("플라스틱");
            Btn_plastic_bag.setText("비닐");
        }
    }

    private void setupMyTrash() {
        setWhoseTrash();
    }

    private void setupOurTrash() {
        setWhoseTrash();
    }

    // Button을 눌렀을 때 레이아웃 LO_write_detail 보이고 안보이기
    private void setVisible_WriteDetail() {
        if (LO_write_detail.getVisibility() == View.GONE) {
            LO_write_detail.setVisibility(View.VISIBLE);
        } else {
            if (touchCount1 == 2 || touchCount2 == 2 || touchCount3 == 2 || touchCount4 == 2 || touchCount5 == 2 || touchCount6 == 2) {
                LO_write_detail.setVisibility(View.GONE);
                touchCount1 = 0;
                touchCount2 = 0;
                touchCount3 = 0;
                touchCount4 = 0;
                touchCount5 = 0;
                touchCount6 = 0;
                Btn_normal_trash.setSelected(false);
                Btn_glass.setSelected(false);
                Btn_can.setSelected(false);
                Btn_paper.setSelected(false);
                Btn_plastic.setSelected(false);
                Btn_plastic_bag.setSelected(false);
            }
        }
    }


    // onItemClick 리스너 구현
    @Override
    public void onItemClick(int position) {
        // trashType이 뭔지 알아야되
        String itemName = null;
        if (trashType.equals("normal_trash")) {
            itemName = normalTrashItems[position];
            if (itemName.equals("물티슈")) {
                my_trash_weight = 2;
            } else if (itemName.equals("각티슈")) {
                my_trash_weight = 1;
            } else if (itemName.equals("손 닦는 휴지")) {
                my_trash_weight = 2;
            } else if (itemName.equals("두루말이 휴지")) {
                my_trash_weight = 1;
            }
            ET_my_weight.setText("" + my_trash_weight);
        } else if (trashType.equals("glass")) {
            itemName = glassItems[position];
        }

        Log.d(TAG, "trashType: " + trashType);
        if (trashType.equals("glass")) {
            itemName = glassItems[position];
            if (itemName.equals("100ml")) {
                my_trash_weight = 150;
            } else if (itemName.equals("180ml")) {
                my_trash_weight = 190;
            }
            ET_my_weight.setText("" + my_trash_weight);
        } else if (trashType.equals("can")) {
            itemName = canItems[position];
        }

        if (trashType.equals("can")) {
            itemName = canItems[position];
            if (itemName.equals("250ml")) {
                my_trash_weight = 60;
            } else if (itemName.equals("355ml")) {
                my_trash_weight = 85;
            } else if (itemName.equals("500ml")) {
                my_trash_weight = 100;
            } else if (itemName.equals("750ml")) {
                my_trash_weight = 145;
            } else if (itemName.equals("참치캔(100g)")) {
                my_trash_weight = 100;
            }
            ET_my_weight.setText("" + my_trash_weight);
        } else if (trashType.equals("paper")) {
            itemName = paperItems[position];
        }


        if (trashType.equals("paper")) {
            itemName = paperItems[position];
            if (itemName.equals("A4")) {
                my_trash_weight = 5;
            } else if (itemName.equals("B4")) {
                my_trash_weight = 10;
            } else if (itemName.equals("종이 정수기컵")) {
                my_trash_weight = 3;
            } else if (itemName.equals("종이 자판기컵")) {
                my_trash_weight = 5;
            } else if (itemName.equals("종이컵 Tall 사이즈(355ml)")) {
                my_trash_weight = 7;
            } else if (itemName.equals("종이컵 Grande 사이즈(473ml)")) {
                my_trash_weight = 9;
            } else if (itemName.equals("종이컵 Venti 사이즈(591ml)")) {
                my_trash_weight = 11;
            } else if (itemName.equals("택배박스 1호(50cm)")) {
                my_trash_weight = 145;
            } else if (itemName.equals("택배박스 2호(60cm)")) {
                my_trash_weight = 185;
            } else if (itemName.equals("택배박스 3호(80cm)")) {
                my_trash_weight = 310;
            } else if (itemName.equals("택배박스 4호(100cm)")) {
                my_trash_weight = 500;
            } else if (itemName.equals("택배박스 5호(120cm)")) {
                my_trash_weight = 1200;
            }
            ET_my_weight.setText("" + my_trash_weight);
        } else if (trashType.equals("plastic")) {
            itemName = plasticItems[position];
        }

        if (trashType.equals("plastic")) {
            itemName = plasticItems[position];
            if (itemName.equals("플라스틱컵 Tall 사이즈(355ml)")) {
                us_trash_weight = 7;
            } else if (itemName.equals("플라스틱컵 Grande 사이즈(473ml)")) {
                us_trash_weight = 9;
            } else if (itemName.equals("플라스틱컵 Venti 사이즈(591ml)")) {
                us_trash_weight = 11;
            } else if (itemName.equals("250ml")) {
                us_trash_weight = 10;
            } else if (itemName.equals("500ml")) {
                us_trash_weight = 15;
            } else if (itemName.equals("1L")) {
                us_trash_weight = 20;
            } else if (itemName.equals("2L")) {
                us_trash_weight = 35;
            }
            ET_my_weight.setText("" + us_trash_weight);
        } else if (trashType.equals("plastic_bag")) {
            itemName = plasticBagItems[position];
        }

        if (trashType.equals("plastic_bag")) {
            itemName = plasticBagItems[position];
            if (itemName.equals("3L")) {
                us_trash_weight = 2;
            } else if (itemName.equals("5L")) {
                us_trash_weight = 2;
            } else if (itemName.equals("10L")) {
                us_trash_weight = 5;
            } else if (itemName.equals("20L")) {
                us_trash_weight = 7;
            }
            ET_my_weight.setText("" + us_trash_weight);
        } else if (trashType.equals("plastic_bag")) {
            itemName = canItems[position];
        }

    }

    // 오늘 사용한 전체 쓰레기의 양과 비교하여 UI도 변경해주는
    public void setPreSavedTrash(float todayTotal) {
        // 전일 사용한 전체 양 구하기
        float preTotal = 0f;
        // 전일 대비 절약한 쓰레기의 양.
        // 전날 데이터가 null이 아닐 때까지 데이터 불러오기. 조건?
        for (int i = 1; i < 10; i++) {
            // 어제 쓰레기 전체 사용량(trashTotal) 불러오기
            Date dDate = new Date();
            dDate = new Date(dDate.getTime() + (1000 * 60 * 60 * 24 * -i));
            SimpleDateFormat dSdf = new SimpleDateFormat("yyMMdd", Locale.KOREA);
            String key_yesterday = dSdf.format(dDate.getTime());
            Log.d("jay", "key_yesterday: " + key_yesterday);

            String yesterday_trashUsageStr = preferenceManager.getString(key_yesterday + "-trash-usage", "");
            TrashUsage yesterday_trashUsage;

            Log.d("jay", "yesterday_trashUsageStr : " + yesterday_trashUsageStr);

            // 만약 어제 데이터가 없으면 그 전날 데이터 데이터 불러오기
            if (yesterday_trashUsageStr.equals("")) {
                continue;
                // yesterday_trashUsage = new TrashUsage();
            } else {
                yesterday_trashUsage = gson.fromJson(yesterday_trashUsageStr, TrashUsage.class);
            }

            preTotal = yesterday_trashUsage.getTrashTotal();

            if (preTotal > 0) {
                break;
            }

            Log.d("jay", "key_yesterday: " + key_yesterday);
            Log.d("jay", "yesterday_trashUsageStr: " + yesterday_trashUsageStr);

        }

        // 오늘과 전일 비교
        float comparedTrash = todayTotal - preTotal;

        // UI 변경
        if (comparedTrash < 0) {
            TXT_compare_trash_weight.setText("- " + comparedTrash + " g");
        } else {
            TXT_compare_trash_weight.setText("+ " + comparedTrash + " g");
        }
    }

    public void SetFirstBottomUI() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String key = simpledateformat.format(calendar.getTime()); // 0530

        String trashUsageStr = preferenceManager.getString(key + "-trash-usage", "");
        TrashUsage todayTrashUsage;
        if (trashUsageStr.equals("")) {
            todayTrashUsage = new TrashUsage();
        } else {
            todayTrashUsage = gson.fromJson(trashUsageStr, TrashUsage.class);
        }

        // 쓰레기 전체 g 구하기
        float total = todayTrashUsage.getNormalTrash() + todayTrashUsage.getGlass() + todayTrashUsage.getCan()
                + todayTrashUsage.getPaper() + todayTrashUsage.getPlastic() + todayTrashUsage.getPlastic_bag();
        TXT_compare_trash_weight.setText(total + " g");

        setPreSavedTrash(total);
    }

    private void setTrashAmount() {
        application.count_normal_trash = 0;
        application.count_glass = 0;
        application.count_can = 0;
        application.count_paper = 0;
        application.count_plastic = 0;
        application.count_plastic_bag = 0;

        for (int index = 0; index < myTrashList.size(); index++) {
            MyTrash myTrash = myTrashList.get(index);
            if (myTrash.getType().equals("normal_trash")) {
                application.count_normal_trash++;
            } else if (myTrash.getType().equals("glass")) {
                application.count_glass++;
            } else if (myTrash.getType().equals("can")) {
                application.count_can++;
            } else if (myTrash.getType().equals("paper")) {
                application.count_paper++;
            } else if (myTrash.getType().equals("plastic")) {
                application.count_plastic++;
            } else if (myTrash.getType().equals("plastic_bag")) {
                application.count_plastic_bag++;
            }
        }

        Btn_normal_trash.setText("일반 / 기타\n" + application.count_normal_trash);
        Btn_glass.setText("유리\n" + application.count_glass);
        Btn_can.setText("캔\n" + application.count_can);
        Btn_paper.setText("종이\n" + application.count_paper);
        Btn_plastic.setText("플라스틱\n" + application.count_plastic);
        Btn_plastic_bag.setText("비닐\n" + application.count_plastic_bag);
    }

    private void compareTrashDischarge(float myTrashWeight, float averageTrashWeight) {
        int myTrashDp;
        int averageTrashDp;
        if (myTrashWeight > averageTrashWeight) {
            myTrashDp = 300;
            averageTrashDp = (int) ((averageTrashWeight * 300) / myTrashWeight);
        } else {
            averageTrashDp = 300;
            myTrashDp = (int) ((myTrashWeight * 300) / averageTrashWeight);
        }

        View myTrashView = new View(this);
        LinearLayout.LayoutParams myTrashParams = new LinearLayout.LayoutParams(toPx(myTrashDp), toPx(48));
        myTrashParams.topMargin = toPx(16);
        myTrashView.setLayoutParams(myTrashParams);
        myTrashView.setBackgroundColor(getResources().getColor(R.color.us_trash));

        TextView myTrashTextView = new TextView(this);
        LinearLayout.LayoutParams myTrashTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myTrashTextView.setText("이번달 쓰레기 배출량");
        myTrashTextView.setLayoutParams(myTrashTextParams);

        View averageTrashView = new View(this);
        LinearLayout.LayoutParams averageTrashParams = new LinearLayout.LayoutParams(toPx(averageTrashDp), toPx(48));
        averageTrashParams.topMargin = toPx(16);
        averageTrashView.setLayoutParams(averageTrashParams);
        averageTrashView.setBackgroundColor(getResources().getColor(R.color.avg_trash));

        TextView averageTrashTextView = new TextView(this);
        LinearLayout.LayoutParams averageTrashTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        averageTrashTextView.setText("월 평균 쓰레기 배출량");
        averageTrashTextView.setLayoutParams(averageTrashTextParams);

        LO_ourTrash.addView(myTrashView);
        LO_ourTrash.addView(myTrashTextView);
        LO_ourTrash.addView(averageTrashView);
        LO_ourTrash.addView(averageTrashTextView);
    }

    private int toPx(int dp) {
        return (int) (dp * (getResources().getDisplayMetrics().densityDpi / 160f));
    }

    /**
     * CAMERA 권한이 있는지 확인합니다.
     */
    private void checkPermission_camera() {
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(WriteTrash.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(WriteTrash.this, "Camera Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    /**
     * 데시벨을 측정하기 위해선 RECORD_AUDIO라는 권한을 사용자로부터 받아야 합니다.
     * RECORD_AUDIO 권한이 있는지 확인합니다.
     */
    private void checkPermission() {
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(WriteTrash.this, "Audio Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(WriteTrash.this, "Audio Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check();
    }
}   /**
 * end of Class
 **/