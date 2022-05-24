package com.project.oneco.data;

import android.Manifest;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.project.oneco.WaterStopWatch;

import java.util.List;

public class CheckPermission {

//    /**
//     * 데시벨을 측정하기 위해선 RECORD_AUDIO라는 권한을 사용자로부터 받아야 합니다.
//     * RECORD_AUDIO 권한이 있는지 확인합니다.
//     */
//    private void checkPermission() {
//
//        TedPermission.create()
//                .setPermissionListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        Toast.makeText(WaterStopWatch.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions) {
//                        Toast.makeText(WaterStopWatch.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
//                .setPermissions(Manifest.permission.RECORD_AUDIO)
//                .check();
//    }

}
