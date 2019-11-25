package com.example.mp3player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        if(intent == null){
            return Service.START_STICKY;
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
        playMusic(musicInfo);
    }
    public void playMusic(MusicInfo musicDto) {
        try {
            seekBar.setProgress(0);
            Uri musicURI = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+musicDto.getId());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, musicURI);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            title.setText(musicInfo.getTitle());
            Bitmap bitmap = BitmapFactory.decodeFile(getCoverArtPath(Long.parseLong(musicDto.getAlbumId()),getApplication()));
            if(bitmap ==null){
                musicart.setImageResource(R.drawable.player);
            }else{
                musicart.setImageBitmap(bitmap);
            }


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
