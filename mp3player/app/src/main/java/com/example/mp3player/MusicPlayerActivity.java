package com.example.mp3player;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport.RunningServiceInfo;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.mp3player.MusicService.MyBinder;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {
    SeekBar seekBar;
    TextView title;
    MediaPlayer mediaPlayer;
    ImageView musicart;
    Button play;
    Button before;
    Button next;
    Button stop;
    Intent intent;
    ProgressUpdate progressUpdate;
    MusicInfo musicInfo;
    ArrayList<MusicInfo> allmusic;
    boolean isPlaying = true;
    MusicService ms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplayer_layout);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        title = (TextView)findViewById(R.id.musictitle);
        musicart = (ImageView)findViewById(R.id.musicart);
        play = (Button)findViewById(R.id.play);
        before =(Button)findViewById(R.id.before);
        next =(Button)findViewById(R.id.next);
        stop =(Button)findViewById(R.id.stop);
        intent = getIntent();
        progressUpdate = new ProgressUpdate();
        progressUpdate.start();

        musicInfo = (MusicInfo) intent.getSerializableExtra("music");
        allmusic = (ArrayList<MusicInfo>) intent.getSerializableExtra("musiclist");
        BtnOnClickListener onClickListener = new BtnOnClickListener() ;
        MyApplication myApp = (MyApplication) getApplication();
        ms = myApp.getService();

        if(musicInfo != null) {
            mediaPlayer = ms.mediaPlayer;
            ms.playMusic(musicInfo);
            playMusic(musicInfo);
            ms.setAllMusicService(allmusic);
        }

        play.setOnClickListener(onClickListener);
        before.setOnClickListener(onClickListener);
        next.setOnClickListener(onClickListener);
        stop.setOnClickListener(onClickListener);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                if (seekBar.getProgress() > 0 && play.getVisibility() == View.GONE) {
                    mediaPlayer.start();
                }
            }
        });
    }

    public void playMusic(MusicInfo musicDto) {
        try {

            seekBar.setProgress(0);
            Uri musicURI = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+musicDto.getId());
            seekBar.setMax(mediaPlayer.getDuration());
            title.setText(musicInfo.getTitle());
            Bitmap bitmap = BitmapFactory.decodeFile(getCoverArtPath(Long.parseLong(musicDto.getAlbumId()),getApplication()));
            if(bitmap ==null){
                musicart.setImageResource(R.drawable.player);
            }else{
                musicart.setImageBitmap(bitmap);
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    ms.nextMusic(allmusic,musicInfo);
                    musicInfo = ms.musicInfoservice;
                    playMusic(musicInfo);
                }
            });

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
    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.play :
                    stop.setVisibility(View.VISIBLE);
                    play.setVisibility(View.GONE);
                    mediaPlayer.start();
                    break ;
                case R.id.stop:
                    stop.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                    mediaPlayer.pause();
                    break;
                case R.id.before :
                    ms.beforeMusic(allmusic,musicInfo);
                    musicInfo = ms.musicInfoservice;
                    playMusic(musicInfo);
                    seekBar.setProgress(0);
                    break ;
                case R.id.next :
                    ms.nextMusic(allmusic,musicInfo);
                    musicInfo = ms.musicInfoservice;
                    playMusic(musicInfo);
                    seekBar.setProgress(0);
                    break ;
            }
        }
    }
    class ProgressUpdate extends Thread{
        @Override
        public void run() {
            while(isPlaying){
                try {
                    Thread.sleep(500);
                    if(mediaPlayer!=null){
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                } catch (Exception e) {
                    Log.e("ProgressUpdate",e.getMessage());
                }

            }
        }
    }

}
