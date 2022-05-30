package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.TrashUsage;

public class WriteTrash extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // todo: 쓰레기 종류
    private String trashType;
    private OnEcoApplication application;

    // todo: 데이터 정의(오늘 배출한 쓰레기, 전일 대비 절약한 쓰레기)
    private String todayTrash;
    private String removedTrash;
    private int currentItemWeight = 0;

    EditText etItemWeight;

    ListView lvList;
    ArrayAdapter<String> adapter;

    // todo: listView에 들어갈 item들 정의(9개)
    String[] tissueItems = { "물티슈", "각티슈", "손 닦는 휴지", "두루말이 휴지" };
    String[] disposableCupItems = { "종이 정수기컵", "종이 자판기컵", "종이 Tall 사이즈(355ml)", "종이 Grande 사이즈(473ml)", "종이 Venti 사이즈(591ml)",
            "플라스틱 Tall 사이즈(355ml)", "플라스틱 Grande 사이즈(473ml)", "플라스틱 Venti 사이즈(591ml)"};
    String[] disposableSpoonItems = {"일회용 수저", "일회용 그릇"};
    String[] paperItems = {"A4", "B4", "택배박스 1호(50cm)", "택배박스 2호(60cm)", "택배박스 3호(80cm)", "택배박스 4호(100cm)", "택배박스 5호(120cm)"};
    String[] plasticItems = {"250ml", "500ml", "1L", "2L"};
    String[] plasticBagItems = {"3L", "5L", "10L", "20L"};
    String[] canItems = {"250ml", "355ml", "500ml", "750ml", "참치캔"};
    String[] emptyBottleItems = {"100ml", "180ml"};
    String[] etcItems = {"기타"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trash);

        application = (OnEcoApplication) getApplication();

        // todo: listview bind & item click listener 달기(setOnItemClickListener)
        lvList = findViewById(R.id.lv_list);
        lvList.setOnItemClickListener(this);

        etItemWeight = findViewById(R.id.UserInput_TodayT);
        TextView tvTodayTrash = findViewById(R.id.TXT_today_water_input);

        // todo: Button 9개 bind
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


        // 이전 버튼
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
            }
        });

        // todo: Button을 눌렀을 때 trashType에 저장.(9개 모두 구현)
        Btn_tissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: trashType에 쓰레기 유형 저장
                trashType = "tissue";
                // todo: item들을 riteTrash 셋해준다.
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, tissueItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_disposable_cup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "disposable_cup";
                // todo: item들을 riteTrash 셋해준다.
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

                tvTodayTrash.setText(currentItemWeight + "g");
            }
        });



        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                    case R.id.Btn_tissue:
                        application.trashType = "tissue";
                        break;

                    case R.id.Btn_disposable_cup:
                        application.trashType = "disposable cup";
                        break;

                    case R.id.Btn_disposable_spoon:
                        application.trashType = "disposable spoon";
                        break;

                    case R.id.Btn_paper:
                        application.trashType = "paper";
                        break;

                    case R.id.Btn_plastic:
                        application.trashType = "plastic";
                        break;

                    case R.id.Btn_plastic_bag:
                        application.trashType = "plastic bag";
                        break;

                    case R.id.Btn_can:
                        application.trashType = "can";
                        break;

                    case R.id.Btn_empty_bottle:
                        application.trashType = "empty bottle";
                        break;

                    case R.id.Btn_etc:
                        application.trashType = "etc";
                        break;
                }
            }
        };

        Btn_tissue.setOnClickListener(onClickListener);
        Btn_disposable_cup.setOnClickListener(onClickListener);
        Btn_disposable_spoon.setOnClickListener(onClickListener);
        Btn_paper.setOnClickListener(onClickListener);
        Btn_plastic.setOnClickListener(onClickListener);
        Btn_plastic_bag.setOnClickListener(onClickListener);
        Btn_can.setOnClickListener(onClickListener);
        Btn_empty_bottle.setOnClickListener(onClickListener);
        Btn_etc.setOnClickListener(onClickListener);

        PreferenceManager manager = PreferenceManager.getInstance(WriteTrash.this);
        Gson gson = new Gson();

        TrashUsage usage = new TrashUsage();

        // todo: 측정값 or 사용자가 직접 입력한 값이 들어가도록
        usage.setTissue(1f);
        usage.setDisposable_cup(1f);
        usage.setDisposable_spoon(1f);
        usage.setPaper(1f);
        usage.setPlastic(1f);
        usage.setPlastic_bag(1f);
        usage.setCan(1f);
        usage.setEmpty_bottle(1f);
        usage.setEtc(1f);

        // data를 String화 시키기
        String json = gson.toJson(usage);

        // 변환한 String 값을 SharedPreference에 저장
        manager.putString("0508", json);

        // 데이터 꺼내오기
        String data = manager.getString("0508", "");

        // String을 데이터 모델로 변경
        TrashUsage trashUsage = gson.fromJson(data, TrashUsage.class);
        Log.d("jay", "tissue: " + trashUsage.getTissue());
        Log.d("jay", "disposable_cup: " + trashUsage.getDisposable_cup());
        Log.d("jay", "disposable_spoon: " + trashUsage.getDisposable_spoon());
        Log.d("jay", "paper: " + trashUsage.getPaper());
        Log.d("jay", "plastic: " + trashUsage.getPlastic());
        Log.d("jay", "plastic_bag: " + trashUsage.getPlastic_bag());
        Log.d("jay", "can: " + trashUsage.getCan());
        Log.d("jay", "empty_bottle: " + trashUsage.getEmpty_bottle());
        Log.d("jay", "etc: " + trashUsage.getEtc());

    } // end of onClick



    // todo: onItemClick 리스너 구현
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
                currentItemWeight = 2;
            } else if (itemName.equals("B4")) {
                currentItemWeight = 3;
            } else if (itemName.equals("택배박스 1호(50cm)")) {
                currentItemWeight = 6;
            } else if (itemName.equals("택배박스 2호(60cm)")) {
                currentItemWeight = 8;
            } else if (itemName.equals("택배박스 3호(80cm)")) {
                currentItemWeight = 10;
            } else if (itemName.equals("택배박스 4호(100cm)")) {
                currentItemWeight = 12;
            } else if (itemName.equals("택배박스 4호(120cm)")) {
                currentItemWeight = 14;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("plastic")) {
            itemName = plasticItems[position];
        }

        if (trashType.equals("plastic")) {
            itemName = plasticItems[position];
            if (itemName.equals("250ml")) {
                currentItemWeight = 2;
            } else if (itemName.equals("500ml")) {
                currentItemWeight = 5;
            } else if (itemName.equals("1L")) {
                currentItemWeight = 10;
            } else if (itemName.equals("2L")) {
                currentItemWeight = 20;
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
                currentItemWeight = 5;
            } else if (itemName.equals("355ml")) {
                currentItemWeight = 7;
            } else if (itemName.equals("500ml")) {
                currentItemWeight = 10;
            } else if (itemName.equals("750ml")) {
                currentItemWeight = 13;
            } else if (itemName.equals("참치캔")) {
                currentItemWeight = 15;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("empty_bottle")) {
            itemName = emptyBottleItems[position];
        }

        if (trashType.equals("empty_bottle")) {
            itemName = canItems[position];
            if (itemName.equals("100ml")) {
                currentItemWeight = 10;
            } else if (itemName.equals("180ml")) {
                currentItemWeight = 20;
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("etc")) {
            itemName = etcItems[position];
        }

        // todo: 나머지 item들도 구현 해야함.
        Log.d("jay", "itemName: " + itemName);
    }


    @Override
    public void onBackPressed(); {
        //super.onBackPressed();
    }


}   // end of class