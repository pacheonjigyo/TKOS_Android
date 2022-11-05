package com.example.tkos;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.media.MediaPlayer;

public class MusicService extends Service {

    MediaPlayer player; // 음악 재생을 위한 객체

    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.background1); // MediaPlayer 객체 초기화
        player.setLooping(true); //true:무한반복
        player.setVolume(100,100);

    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start(); // 노래 재생 시작
        return super.onStartCommand(intent,flags,startId);
    }

    public void onStart(Intent intent, int startId) {}

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {}

    public void onPause() {}

    @Override
    public void onDestroy() {
        player.stop(); // 멈춤
        player.release(); // 자원 해제
    }

    @Override
    public void onLowMemory() {}
}