package com.example.mp3player;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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
        mediaPlayer = new MediaPlayer();

        if(musicInfo != null) {
            playMusic(musicInfo);
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
    public void nextMusic(ArrayList<MusicInfo> allmusic,MusicInfo musicInfo){

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
    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.play :
                    stop.setVisibility(View.VISIBLE);
                    play.setVisibility(View.GONE);
                    System.out.println("play");
                    mediaPlayer.start();
                    break ;
                case R.id.stop:
                    stop.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                    System.out.println("stop");
                    mediaPlayer.pause();
                    break;
                case R.id.before :
                    beforeMusic(allmusic,musicInfo);
                    seekBar.setProgress(0);
                    break ;
                case R.id.next :
                    nextMusic(allmusic,musicInfo);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
