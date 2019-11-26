package com.example.mp3player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicService extends Service {
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer = new MediaPlayer();
    MusicInfo musicInfoservice;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MusicInfo musicInfo = (MusicInfo) intent.getSerializableExtra("musicinfo");
        playMusic(musicInfo);
        return mBinder;
    }

    public class MyBinder extends Binder {
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public void onCreate(){
        System.out.println("oncreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        System.out.println("onStartCommand");
        if(intent == null){
            return Service.START_NOT_STICKY;
        }else{
            processCommans(intent);
        }
        return super.onStartCommand(intent,flags,startId);
    }

    private void processCommans(Intent intent) {

    }

    @Override
    public void onDestroy() {
        System.out.println("Destory");
        super.onDestroy();
    }

    public void nextMusic(ArrayList<MusicInfo> allmusic, MusicInfo musicInfo){

        for(int a=0 ; a < allmusic.size() ; a++) {
            System.out.println("마직막 번호" + allmusic.size());
            if(musicInfo.getId().equals(allmusic.get(allmusic.size()-1).getId())){
                musicInfo.setId(allmusic.get(0).getId());
                musicInfo.setTitle(allmusic.get(0).getTitle());
                musicInfo.setAlbum(allmusic.get(0).getAlbum());
                musicInfo.setAlbumId(allmusic.get(0).getAlbumId());
                musicInfo.setArtist(allmusic.get(0).getArtist());
                break;
            }
            else if(allmusic.get(a).getId().equals(musicInfo.getId())) {
                musicInfo.setId(allmusic.get( a + 1 ).getId());
                musicInfo.setTitle(allmusic.get(a + 1).getTitle());
                musicInfo.setAlbum(allmusic.get(a + 1).getAlbum());
                musicInfo.setAlbumId(allmusic.get(a + 1).getAlbumId());
                musicInfo.setArtist(allmusic.get(a + 1).getArtist());
                break;
            }
        }
        musicInfoservice=musicInfo;
        playMusic(musicInfo);
    }
    public void beforeMusic(ArrayList<MusicInfo> allmusic,MusicInfo musicInfo){
        for(int a=0 ; a < allmusic.size() ; a++) {
            System.out.println(musicInfo.getId() + " :::::: " + allmusic.get(a).getId());
            if(musicInfo.getId().equals(allmusic.get(0).getId())){
                musicInfo.setId(allmusic.get(allmusic.size()).getId());
                musicInfo.setTitle(allmusic.get(allmusic.size()).getTitle());
                musicInfo.setAlbum(allmusic.get(allmusic.size()).getAlbum());
                musicInfo.setAlbumId(allmusic.get(allmusic.size()).getAlbumId());
                musicInfo.setArtist(allmusic.get(allmusic.size()).getArtist());
                break;
            }
            if(allmusic.get(a).getId().equals(musicInfo.getId())) {
                musicInfo.setId(allmusic.get( a -1).getId());
                musicInfo.setTitle(allmusic.get(a - 1).getTitle());
                musicInfo.setAlbum(allmusic.get(a - 1).getAlbum());
                musicInfo.setAlbumId(allmusic.get(a - 1).getAlbumId());
                musicInfo.setArtist(allmusic.get(a - 1).getArtist());
                break;
            }
        }
        musicInfoservice=musicInfo;
        playMusic(musicInfo);
    }
    public void playMusic(MusicInfo musicDto) {
        try {
            Uri musicURI = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+musicDto.getId());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, musicURI);
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch (Exception e){

        }
    }

    private static String getCoverArtPath(long albumId, Context context) {

        Cursor albumCursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }
}
