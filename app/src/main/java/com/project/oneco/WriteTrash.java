package com.project.oneco;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.TrashUsage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WriteTrash extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final static String TAG = "WriteTrash";
    // 쓰레기 종류
    private String trashType;
    private OnEcoApplication application;

    // 데이터 정의(오늘 배출한 쓰레기, 전일 대비 절약한 쓰레기)
    private String todayTrash;
    private String removedTrash;
    TextView TXT_today_trash_input;
    TextView TXT_saved_trash;
    private int currentItemWeight = 0;


    EditText etItemWeight;
    EditText ET_UserInputTrash;

    private PreferenceManager preferenceManager;
    private Gson gson;

    ListView lvList;
    ArrayAdapter<String> adapter;

    // listView에 들어갈 item들 정의(9개)
    String[] tissueItems = {"물티슈", "각티슈", "손 닦는 휴지", "두루말이 휴지"};
    String[] disposableCupItems = {"종이 정수기컵", "종이 자판기컵", "종이 Tall 사이즈(355ml)", "종이 Grande 사이즈(473ml)", "종이 Venti 사이즈(591ml)",
            "플라스틱 Tall 사이즈(355ml)", "플라스틱 Grande 사이즈(473ml)", "플라스틱 Venti 사이즈(591ml)"};
    String[] disposableSpoonItems = {"일회용 수저", "일회용 그릇"};
    String[] paperItems = {"A4", "B4", "택배박스 1호(50cm)", "택배박스 2호(60cm)", "택배박스 3호(80cm)", "택배박스 4호(100cm)", "택배박스 5호(120cm)"};
    String[] plasticItems = {"250ml", "500ml", "1L", "2L"};
    String[] plasticBagItems = {"3L", "5L", "10L", "20L"};
    String[] canItems = {"250ml", "355ml", "500ml", "750ml", "참치캔(100g)"};
    String[] emptyBottleItems = {"100ml", "180ml"};
    String[] etcItems = {"기타"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trash);

        application = (OnEcoApplication) getApplication();
        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();


        // listview bind & item click listener 달기(setOnItemClickListener)
        lvList = findViewById(R.id.lv_list);
        lvList.setOnItemClickListener(this);

        etItemWeight = findViewById(R.id.UserInput_TodayT);

        TXT_today_trash_input = findViewById(R.id.TXT_today_trash_input);
        ET_UserInputTrash = findViewById(R.id.UserInput_TodayT);
        TXT_saved_trash = findViewById(R.id.TXT_saving_trash);

        // Button 9개 bind
        Button Btn_tissue = findViewById(R.id.Btn_tissue);
        Button Btn_disposable_cup = findViewById(R.id.Btn_disposable_cup);
        Button Btn_disposable_spoon = findViewById(R.id.Btn_disposable_spoon);
        Button Btn_paper = findViewById(R.id.Btn_paper);
        Button Btn_plastic = findViewById(R.id.Btn_plastic);
        Button Btn_plastic_bag = findViewById(R.id.Btn_plastic_bag);
        Button Btn_can = findViewById(R.id.Btn_can);
        Button Btn_empty_bottle = findViewById(R.id.Btn_empty_bottle);
        Button Btn_etc = findViewById(R.id.Btn_etc);
        Button Btn_add = findViewById(R.id.Btn_add);
        Button Btn_sub = findViewById(R.id.Btn_sub);

        etItemWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("jay", "charSequence: " + charSequence);
                if (charSequence.equals("")) return;

                currentItemWeight = Integer.parseInt(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // 이전 버튼
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                application.active_activity = "";
                application.statisticType = "";
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

        // 통계 화면으로 넘어가기
        ImageButton Tstatistic = findViewById(R.id.Tstatistic);
        Tstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);

                application.active_activity = "statistic";
                application.statisticType = "trash-usage";
            }
        });

        // Button을 눌렀을 때 trashType에 저장.(9개 모두 구현)
        Btn_tissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("jay", "passed");
                // trashType에 쓰레기 유형 저장
                trashType = "tissue";
                // item들을 riteTrash 셋해준다.
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, tissueItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_disposable_cup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "disposable_cup";
                // item들을 riteTrash 셋해준다.
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, disposableCupItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_disposable_spoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "disposable_spoon";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, disposableSpoonItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "paper";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, paperItems);
                lvList.setAdapter(adapter);
            }

        });

        Btn_plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "plastic";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, plasticItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_plastic_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "plastic_bag";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, plasticBagItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "can";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, canItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_empty_bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "empty_bottle";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, emptyBottleItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "etc";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, etcItems);
                lvList.setAdapter(adapter);
            }
        });

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

                    // 저장된 값이 없을 시
                    if (trashUsageStr.equals("")) {
                        trashUsage = new TrashUsage();
                    } else {
                        trashUsage = gson.fromJson(trashUsageStr, TrashUsage.class);
                    }

                    Log.d("jay", "trashUsageStr: " + trashUsageStr);

                    // 각각의 쓰레기 타입에 저장
                    if (trashType.equals("tissue")) {
                        float tissue = trashUsage.getTissue();
                        trashUsage.setTissue(tissue + currentItemWeight);
                    } else if (trashType.equals("disposable_cup")) {
                        float disposableCup = trashUsage.getDisposable_cup();
                        trashUsage.setDisposable_cup(disposableCup + currentItemWeight);
                    } else if (trashType.equals("disposable_spoon")) {
                        float disposableSpoon = trashUsage.getDisposable_spoon();
                        trashUsage.setDisposable_spoon(disposableSpoon + currentItemWeight);
                    } else if (trashType.equals("paper")) {
                        float paper = trashUsage.getPaper();
                        trashUsage.setPaper(paper + currentItemWeight);
                    } else if (trashType.equals("plastic")) {
                        float plastic = trashUsage.getPlastic();
                        trashUsage.setPlastic(plastic + currentItemWeight);
                    } else if (trashType.equals("plastic_bag")) {
                        float plastic_bag = trashUsage.getPlastic_bag();
                        trashUsage.setPlastic_bag(plastic_bag + currentItemWeight);
                    } else if (trashType.equals("can")) {
                        float can = trashUsage.getCan();
                        trashUsage.setCan(can + currentItemWeight);
                    } else if (trashType.equals("empty_bottle")) {
                        float empty_bottle = trashUsage.getEmpty_bottle();
                        trashUsage.setEmpty_bottle(empty_bottle + currentItemWeight);
                    } else if (trashType.equals("etc")) {
                        float etc = trashUsage.getTrashEtc();
                        trashUsage.setTrashEtc(etc + currentItemWeight);
                    }

                    // localStorage에 저장
                    String updatedTrashUsage = gson.toJson(trashUsage);
                    preferenceManager.putString(key + "-trash-usage", updatedTrashUsage);

                    // 쓰레기 전체 g 구하기
                    float total = trashUsage.getTissue() + trashUsage.getDisposable_cup() + trashUsage.getDisposable_spoon()
                            + trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag() + trashUsage.getCan()
                            + trashUsage.getEmpty_bottle() + trashUsage.getTrashEtc();

                    TXT_today_trash_input.setText(total + "g");
                    setPreSavedTrash(total);
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
                    if (trashType.equals("tissue")) {
                        float tissue = trashUsage.getTissue();
                        trashUsage.setTissue(tissue - currentItemWeight);
                    } else if (trashType.equals("disposable_cup")) {
                        float disposableCup = trashUsage.getDisposable_cup();
                        trashUsage.setDisposable_cup(disposableCup - currentItemWeight);
                    } else if (trashType.equals("disposable_spoon")) {
                        float disposableSpoon = trashUsage.getDisposable_spoon();
                        trashUsage.setDisposable_spoon(disposableSpoon - currentItemWeight);
                    } else if (trashType.equals("paper")) {
                        float paper = trashUsage.getPaper();
                        trashUsage.setPaper(paper - currentItemWeight);
                    } else if (trashType.equals("plastic")) {
                        float plastic = trashUsage.getPlastic();
                        trashUsage.setPlastic(plastic - currentItemWeight);
                    } else if (trashType.equals("plastic_bag")) {
                        float plastic_bag = trashUsage.getPlastic_bag();
                        trashUsage.setPlastic_bag(plastic_bag - currentItemWeight);
                    } else if (trashType.equals("can")) {
                        float can = trashUsage.getCan();
                        trashUsage.setCan(can - currentItemWeight);
                    } else if (trashType.equals("empty_bottle")) {
                        float empty_bottle = trashUsage.getEmpty_bottle();
                        trashUsage.setEmpty_bottle(empty_bottle - currentItemWeight);
                    } else if (trashType.equals("etc")) {
                        float etc = trashUsage.getTrashEtc();
                        trashUsage.setTrashEtc(etc - currentItemWeight);
                    }

                    // localStorage에 저장
                    String updatedTrashUsage = gson.toJson(trashUsage);
                    preferenceManager.putString(key + "-trash-usage", updatedTrashUsage);

                    // 쓰레기 전체 g 구하기
                    float total = trashUsage.getTissue() + trashUsage.getDisposable_cup() + trashUsage.getDisposable_spoon()
                            + trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag() + trashUsage.getCan()
                            + trashUsage.getEmpty_bottle() + trashUsage.getTrashEtc();

                    TXT_today_trash_input.setText(total + "g");
                    setPreSavedTrash(total);
                }
            }
        });


        // 저장되어 있는 trash 값 반영
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
//        todayTrashUsage.getTrashTotal();
        float total = todayTrashUsage.getTissue() + todayTrashUsage.getDisposable_cup() + todayTrashUsage.getDisposable_spoon()
                + todayTrashUsage.getPaper() + todayTrashUsage.getPlastic() + todayTrashUsage.getPlastic_bag() + todayTrashUsage.getCan()
                + todayTrashUsage.getEmpty_bottle() + todayTrashUsage.getTrashEtc();
        TXT_today_trash_input.setText(total + "g");

        setPreSavedTrash(total);

    }   // end of onCreate


    // onItemClick 리스너 구현
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
        // trashType이 뭔지 알아야되
        String itemName = null;
        if (trashType.equals("tissue")) {
            itemName = tissueItems[position];
            // 물티슈, 각티슈 등등 에 따라 값을 넣어주는거에요(editTextView에)
            if (itemName.equals("물티슈")) {
                currentItemWeight = 2;
            } else if (itemName.equals("각티슈")) {
                currentItemWeight = 1;
            } else if (itemName.equals("손 닦는 휴지")) {
                currentItemWeight = 2;
            } else if (itemName.equals("두루말이 휴지")) {
                currentItemWeight = 1;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("disposable_cup")) {
            itemName = disposableCupItems[position];
        }

        if (trashType.equals("disposable_cup")) {
            itemName = disposableCupItems[position];
            if (itemName.equals("종이 정수기컵")) {
                currentItemWeight = 3;
            } else if (itemName.equals("종이 자판기컵")) {
                currentItemWeight = 5;
            } else if (itemName.equals("종이 Tall 사이즈(355ml)")) {
                currentItemWeight = 7;
            } else if (itemName.equals("종이 Grande 사이즈(473ml)")) {
                currentItemWeight = 9;
            } else if (itemName.equals("종이 Venti 사이즈(591ml)")) {
                currentItemWeight = 11;
            } else if (itemName.equals("플라스틱 Tall 사이즈(355ml)")) {
                currentItemWeight = 7;
            } else if (itemName.equals("플라스틱 Grande 사이즈(473ml)")) {
                currentItemWeight = 9;
            } else if (itemName.equals("플라스틱 Venti 사이즈(591ml)")) {
                currentItemWeight = 11;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("disposable_spoon")) {
            itemName = disposableSpoonItems[position];
        }

        if (trashType.equals("disposable_spoon")) {
            itemName = disposableSpoonItems[position];
            if (itemName.equals("일회용 수저")) {
                currentItemWeight = 3;
            } else if (itemName.equals("일회용 그릇")) {
                currentItemWeight = 2;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("paper")) {
            itemName = paperItems[position];
        }

        if (trashType.equals("paper")) {
            itemName = paperItems[position];
            if (itemName.equals("A4")) {
                currentItemWeight = 5;
            } else if (itemName.equals("B4")) {
                currentItemWeight = 10;
            } else if (itemName.equals("택배박스 1호(50cm)")) {
                currentItemWeight = 145;
            } else if (itemName.equals("택배박스 2호(60cm)")) {
                currentItemWeight = 185;
            } else if (itemName.equals("택배박스 3호(80cm)")) {
                currentItemWeight = 310;
            } else if (itemName.equals("택배박스 4호(100cm)")) {
                currentItemWeight = 500;
            } else if (itemName.equals("택배박스 5호(120cm)")) {
                currentItemWeight = 1200;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("plastic")) {
            itemName = plasticItems[position];
        }

        if (trashType.equals("plastic")) {
            itemName = plasticItems[position];
            if (itemName.equals("250ml")) {
                currentItemWeight = 10;
            } else if (itemName.equals("500ml")) {
                currentItemWeight = 15;
            } else if (itemName.equals("1L")) {
                currentItemWeight = 20;
            } else if (itemName.equals("2L")) {
                currentItemWeight = 35;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("plastic_bag")) {
            itemName = plasticBagItems[position];
        }

        if (trashType.equals("plastic_bag")) {
            itemName = plasticBagItems[position];
            if (itemName.equals("3L")) {
                currentItemWeight = 2;
            } else if (itemName.equals("5L")) {
                currentItemWeight = 2;
            } else if (itemName.equals("10L")) {
                currentItemWeight = 5;
            } else if (itemName.equals("20L")) {
                currentItemWeight = 7;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("can")) {
            itemName = canItems[position];
        }

        if (trashType.equals("plastic_bag")) {
            itemName = plasticBagItems[position];
            if (itemName.equals("3L")) {
                currentItemWeight = 2;
            } else if (itemName.equals("5L")) {
                currentItemWeight = 2;
            } else if (itemName.equals("10L")) {
                currentItemWeight = 5;
            } else if (itemName.equals("20L")) {
                currentItemWeight = 7;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("can")) {
            itemName = canItems[position];
        }

        if (trashType.equals("can")) {
            itemName = canItems[position];
            if (itemName.equals("250ml")) {
                currentItemWeight = 60;
            } else if (itemName.equals("355ml")) {
                currentItemWeight = 85;
            } else if (itemName.equals("500ml")) {
                currentItemWeight = 100;
            } else if (itemName.equals("750ml")) {
                currentItemWeight = 145;
            } else if (itemName.equals("참치캔(100g)")) {
                currentItemWeight = 100;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("empty_bottle")) {
            itemName = emptyBottleItems[position];
        }

        Log.d(TAG, "trashType: " + trashType);
        if (trashType.equals("empty_bottle")) {
            itemName = emptyBottleItems[position];
            if (itemName.equals("100ml")) {
                currentItemWeight = 150;
            } else if (itemName.equals("180ml")) {
                currentItemWeight = 190;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("etc")) {
            itemName = etcItems[position];
        }
    }

    // 오늘 사용한 전체 쓰레기의 양과 비교하여 UI도 변경해주는
    public void setPreSavedTrash(float todayTotal) {
        // 전일 사용한 전체 양 구하기
        float preTotal = 0f;
        // 전일 대비 절약한 쓰레기의 양.
        // 전날 데이터가 null이 아닐 때까지 데이터 불러오기. 조건?
        for(int i = 1; i<10; i++){
            // 어제 쓰레기 전체 사용량(trashTotal) 불러오기
            Date dDate = new Date();
            dDate = new Date(dDate.getTime()+(1000*60*60*24*-i));
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
        float savedTrash = todayTotal - preTotal;

        // UI 변경
        if (savedTrash < 0) {
            TXT_saved_trash.setText(savedTrash + "g");
        } else {
            TXT_saved_trash.setText("+" + savedTrash + "g");
        }
    }


}   // end of class