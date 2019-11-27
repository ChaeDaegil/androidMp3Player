package com.example.mp3player;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyApplication extends Application
{
    MusicService ms; // 서비스 객체
    MediaPlayer mediaPlayer;
    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MusicService.MyBinder mb = (MusicService.MyBinder) service;
            ms = mb.getService(); // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴

        }
        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
        }
    };
    void createService(){
        Intent servi = new Intent(
                getApplicationContext(), // 현재 화면
                MusicService.class); // 다음넘어갈 컴퍼넌트

        bindService(servi, // intent 객체
                conn, // 서비스와 연결에 대한 정의
                Context.BIND_AUTO_CREATE);
    }
    public MusicService getService(){
        return ms;
    }
    public void setMediaPlayer(MediaPlayer media){ this.mediaPlayer = media;}
    public MediaPlayer getMediaPlayer(){ return this.mediaPlayer;}
}