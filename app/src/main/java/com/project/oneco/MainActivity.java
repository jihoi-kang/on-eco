package com.project.oneco;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SoundMeter soundMeter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        soundMeter = new SoundMeter();
        TextView tvOutput = findViewById(R.id.tv_output);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soundMeter == null) {
                    return;
                }
                soundMeter.start();
            }
        });
        findViewById(R.id.btn_amplitude).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soundMeter == null) {
                    return;
                }
                double amplitude = soundMeter.getAmplitude();
                float db = 20 * (float) (Math.log10(amplitude));
                tvOutput.setText(db + "db");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (soundMeter != null) {
            soundMeter.stop();
        }
        super.onDestroy();
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
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check();
    }

}