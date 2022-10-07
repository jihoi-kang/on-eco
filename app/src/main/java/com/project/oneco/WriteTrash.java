package com.project.oneco;

import android.Manifest;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.TrashUsage;
import com.project.oneco.tensorflow.ClassifierActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WriteTrash extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText ET_UserInputTrash;
    TextView TXT_today_trash_input;
    TextView TXT_compare_trash;
    TextView TXT_today_date;

    private final static String TAG = "WriteTrash";
    // 쓰레기 종류
    private String trashType;
    private OnEcoApplication application;

    private int currentItemWeight = 0;

    private int touchCount1, touchCount2, touchCount3, touchCount4, touchCount5, touchCount6 = 0;

    private PreferenceManager preferenceManager;
    private Gson gson;

    ListView lvList;
    ArrayAdapter<String> adapter;

    // listView에 들어갈 item들 정의(9개 >> 6개)
    String[] normalTrashItems = {"물티슈", "각티슈", "손 닦는 휴지", "두루말이 휴지"};
    String[] glassItems = {"100ml", "180ml"};
    String[] canItems = {"250ml", "355ml", "500ml", "750ml", "참치캔(100g)"};
    String[] paperItems = {"A4", "B4", "종이 정수기컵", "종이 자판기컵", "종이컵 Tall 사이즈(355ml)", "종이컵 Grande 사이즈(473ml)", "종이컵 Venti 사이즈(591ml)","택배박스 1호(50cm)", "택배박스 2호(60cm)", "택배박스 3호(80cm)", "택배박스 4호(100cm)", "택배박스 5호(120cm)"};
    String[] plasticItems = {"플라스틱컵 Tall 사이즈(355ml)", "플라스틱컵 Grande 사이즈(473ml)", "플라스틱컵 Venti 사이즈(591ml)", "250ml", "500ml", "1L", "2L"};
    String[] plasticBagItems = {"3L", "5L", "10L", "20L"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trash);

        // listview bind & item click listener 달기(setOnItemClickListener)
        lvList = findViewById(R.id.lv_list);
        lvList.setOnItemClickListener(this);

        ET_UserInputTrash = findViewById(R.id.UserInput_TodayT);
        TXT_today_trash_input = findViewById(R.id.TXT_today_trash_input);
        TXT_today_date = findViewById(R.id.TXT_today_date);
        TXT_compare_trash = findViewById(R.id.TXT_compare_trash);

        // Button 9개 bind >> 6로 수정
        Button Btn_normal_trash = findViewById(R.id.Btn_normal_trash);
        Button Btn_glass = findViewById(R.id.Btn_glass);
        Button Btn_can = findViewById(R.id.Btn_can);
        Button Btn_paper = findViewById(R.id.Btn_paper);
        Button Btn_plastic = findViewById(R.id.Btn_plastic);
        Button Btn_plastic_bag = findViewById(R.id.Btn_plastic_bag);

        Button Btn_add = findViewById(R.id.Btn_add);
        Button Btn_sub = findViewById(R.id.Btn_sub);

        application = (OnEcoApplication) getApplication();
        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();

        TXT_today_date.setText(application.todayDate);

        application.statisticType = "trash-usage";

        checkPermission();
        checkPermission_camera();

        // 사용자 입력 텍스트를 인트 변수 currentItemWeight에 저장
        ET_UserInputTrash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("jay", "charSequence: " + charSequence);
                if (charSequence.equals("")) return;
                // todo: 값을 완전히 지우면 오류 발생
                currentItemWeight = Integer.parseInt(charSequence.toString());
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

        // Button을 눌렀을 때 trashType에 저장.(9개 모두 구현)
        Btn_normal_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("jay", "passed");
                // trashType에 쓰레기 유형 저장
                trashType = "normal_trash";
                // item들을 riteTrash 셋해준다.
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, normalTrashItems);
                lvList.setAdapter(adapter);
                touchCount1++;
                ListVisible();
            }
        });

        Btn_glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "glass";
                // item들을 riteTrash 셋해준다.
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, glassItems);
                lvList.setAdapter(adapter);
                touchCount2++;
                ListVisible();
            }
        });


        Btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "can";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, canItems);
                lvList.setAdapter(adapter);
                touchCount3++;
                ListVisible();
            }

        });

        Btn_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "paper";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, paperItems);
                lvList.setAdapter(adapter);
                touchCount4++;
                ListVisible();
            }
        });

        Btn_plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "plastic_bag";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, plasticItems);
                lvList.setAdapter(adapter);
                touchCount5++;
                ListVisible();
            }
        });

        Btn_plastic_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "plastic";
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, plasticBagItems);
                lvList.setAdapter(adapter);
                touchCount6++;
                ListVisible();
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
                        trashUsage.setNormalTrash(normal_trash + currentItemWeight);
                    } else if (trashType.equals("glass")) {
                        float glass = trashUsage.getGlass();
                        trashUsage.setGlass(glass + currentItemWeight);
                    } else if (trashType.equals("can")) {
                        float can = trashUsage.getCan();
                        trashUsage.setCan(can + currentItemWeight);
                    } else if (trashType.equals("paper")) {
                        float paper = trashUsage.getPaper();
                        trashUsage.setPaper(paper + currentItemWeight);
                    } else if (trashType.equals("plastic")) {
                        float plastic = trashUsage.getPlastic();
                        trashUsage.setPlastic(plastic + currentItemWeight);
                    } else if (trashType.equals("plastic_bag")) {
                        float plastic_bag = trashUsage.getPlastic_bag();
                        trashUsage.setPlastic_bag(plastic_bag + currentItemWeight);
                    }

                    // localStorage에 저장
                    String updatedTrashUsage = gson.toJson(trashUsage);
                    preferenceManager.putString(key + "-trash-usage", updatedTrashUsage);

                    // 쓰레기 전체 g 구하기
                    float total = trashUsage.getNormalTrash() + trashUsage.getGlass() + trashUsage.getCan()
                            + trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag();

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
                    if (trashType.equals("normal_trash")) {
                        float normal_trash = trashUsage.getNormalTrash();
                        trashUsage.setNormalTrash(normal_trash - currentItemWeight);
                    } else if (trashType.equals("glass")) {
                        float glass = trashUsage.getGlass();
                        trashUsage.setGlass(glass - currentItemWeight);
                    } else if (trashType.equals("can")) {
                        float can = trashUsage.getCan();
                        trashUsage.setCan(can - currentItemWeight);
                    } else if (trashType.equals("paper")) {
                        float paper = trashUsage.getPaper();
                        trashUsage.setPaper(paper - currentItemWeight);
                    } else if (trashType.equals("plastic")) {
                        float plastic = trashUsage.getPlastic();
                        trashUsage.setPlastic(plastic - currentItemWeight);
                    } else if (trashType.equals("plastic_bag")) {
                        float plastic_bag = trashUsage.getPlastic_bag();
                        trashUsage.setPlastic_bag(plastic_bag - currentItemWeight);
                    }

                    // localStorage에 저장
                    String updatedTrashUsage = gson.toJson(trashUsage);
                    preferenceManager.putString(key + "-trash-usage", updatedTrashUsage);

                    // 쓰레기 전체 g 구하기
                    float total = trashUsage.getNormalTrash() + trashUsage.getGlass() + trashUsage.getCan()
                            + trashUsage.getPaper() + trashUsage.getPlastic() + trashUsage.getPlastic_bag();
                    TXT_today_trash_input.setText(total + "g");
                    setPreSavedTrash(total);
                }
            }
        });

        // 저장되어 있는 trash 값 화면에 반영
        SetFirstBottomUI();

    }   /** end of onCreate **/



    // Button을 눌렀을 때 리스트 보이고 안보이기
    private void ListVisible() {
        if (lvList.getVisibility() == View.GONE) {
            lvList.setVisibility(View.VISIBLE);
        } else {
            if (touchCount1 == 2 || touchCount2 == 2 || touchCount3 == 2 || touchCount4 == 2 || touchCount5 == 2 || touchCount6 == 2) {
                lvList.setVisibility(View.GONE);
                touchCount1 = 0;
                touchCount2 = 0;
                touchCount3 = 0;
                touchCount4 = 0;
                touchCount5 = 0;
                touchCount6 = 0;
            }
        }
    }


    // onItemClick 리스너 구현
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
        // trashType이 뭔지 알아야되
        String itemName = null;
        if (trashType.equals("normal_trash")) {
            itemName = normalTrashItems[position];
            if (itemName.equals("물티슈")) {
                currentItemWeight = 2;
            } else if (itemName.equals("각티슈")){
                currentItemWeight = 1;
            } else if (itemName.equals("손 닦는 휴지")){
                currentItemWeight = 2;
            } else if (itemName.equals("두루말이 휴지")){
                currentItemWeight = 1;
            }

            ET_UserInputTrash.setText("" + currentItemWeight);
        } else if (trashType.equals("glass")) {
            itemName = glassItems[position];
        }

        Log.d(TAG, "trashType: " + trashType);
        if (trashType.equals("glass")) {
            itemName = glassItems[position];
            if (itemName.equals("100ml")) {
                currentItemWeight = 150;
            } else if (itemName.equals("180ml")) {
                currentItemWeight = 190;
            }
            ET_UserInputTrash.setText("" + currentItemWeight);
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
            ET_UserInputTrash.setText("" + currentItemWeight);
        } else if (trashType.equals("paper")) {
            itemName = paperItems[position];
        }


        if (trashType.equals("paper")) {
            itemName = paperItems[position];
            if (itemName.equals("A4")) {
                currentItemWeight = 5;
            } else if (itemName.equals("B4")) {
                currentItemWeight = 10;
            } else if (itemName.equals("종이 정수기컵")) {
                    currentItemWeight = 3;
            } else if (itemName.equals("종이 자판기컵")) {
                    currentItemWeight = 5;
            } else if (itemName.equals("종이컵 Tall 사이즈(355ml)")) {
                    currentItemWeight = 7;
            } else if (itemName.equals("종이컵 Grande 사이즈(473ml)")) {
                    currentItemWeight = 9;
            } else if (itemName.equals("종이컵 Venti 사이즈(591ml)")) {
                    currentItemWeight = 11;
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
            ET_UserInputTrash.setText("" + currentItemWeight);
        } else if (trashType.equals("plastic")) {
            itemName = plasticItems[position];
        }

        if (trashType.equals("plastic")) {
            itemName = plasticItems[position];
            if (itemName.equals("플라스틱컵 Tall 사이즈(355ml)")) {
                currentItemWeight = 7;
            } else if (itemName.equals("플라스틱컵 Grande 사이즈(473ml)")) {
                currentItemWeight = 9;
            } else if (itemName.equals("플라스틱컵 Venti 사이즈(591ml)")) {
                currentItemWeight = 11;
            } else if (itemName.equals("250ml")) {
                currentItemWeight = 10;
            } else if (itemName.equals("500ml")) {
                currentItemWeight = 15;
            } else if (itemName.equals("1L")) {
                currentItemWeight = 20;
            } else if (itemName.equals("2L")) {
                currentItemWeight = 35;
            }
            ET_UserInputTrash.setText("" + currentItemWeight);
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
            ET_UserInputTrash.setText("" + currentItemWeight);
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
        float savedTrash = todayTotal - preTotal;

        // UI 변경
        if (savedTrash < 0) {
            TXT_compare_trash.setText("- " + savedTrash + " g");
        } else {
            TXT_compare_trash.setText("+ " + savedTrash + " g");
        }
    }

    public void SetFirstBottomUI(){
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
        TXT_today_trash_input.setText(total + " g");

        setPreSavedTrash(total);
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
}   /** end of Class **/