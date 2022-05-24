package com.project.oneco;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class SoundMeter {

    /* 기능 정리
    사용자가 시작 버튼을 누르면, 그 후부터 종료 버튼을 누를 때까지의 데시벨을 측정하여 기록하고, 기록된 데이터를 다음과 같이 분석한다.
        1) 평균 데시벨 => 물 사용 여부를 판가름하는 기준
        2) 평균 데시벨 이상 => 물 사용
        3) 평균 데시벨 미만 => 물 미사용

           x축 = 4)의 시간, y축 = 데시벨
        4) 시작 버튼을 누른 후부터 종료 버튼을 누를 때까지의 시간 => 물 사용/미사용 활동에 걸린 시간
        5) 평균 데시벨 이상으로 측정된 구간/시간 => 물을 사용한 때/시간
        6) 평균 데시벨 미만으로 측정된 구간/시간 => 물을 사용하지 않은 때/시간

        7) 4, 5, 6)의 정보를 막대 그래프로 변환하여 보여준다.
    */

    private AudioRecord ar = null;
    private int minSize;

    @SuppressLint("MissingPermission")
    public void start() {
        minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
            // AudioRecord 라이브러리를 import한 후, AudioRecord 클래스로 ar 객체를 생성
            // 안드로이드 기기 MIC에서 데시벨 값을 가져옴
        ar.startRecording();
    }

    public void stop() {
        if (ar != null) {
            ar.stop();
        }
    }

    // 진폭 가져오기 매서드
    public double getAmplitude() {
        short[] buffer = new short[minSize];
        ar.read(buffer, 0, minSize);    // ar에 넣어둔 데시벨 측정 값을 버퍼에서 read?
        int max = 0;
        for (short s : buffer) {
            if (Math.abs(s) > max) {
                max = Math.abs(s);
            }
        }
        return max;
    }

}