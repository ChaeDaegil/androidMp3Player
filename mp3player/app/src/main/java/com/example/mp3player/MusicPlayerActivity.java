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
    MusicInfo musicInfo;
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
        musicInfo = (MusicInfo) intent.getSerializableExtra("music");
        BtnOnClickListener onClickListener = new BtnOnClickListener() ;
        mediaPlayer = new MediaPlayer();
        playMusic(musicInfo);
        play.setOnClickListener(onClickListener);
        before.setOnClickListener(onClickListener);
        next.setOnClickListener(onClickListener);
        stop.setOnClickListener(onClickListener);
    }

    public void playMusic(MusicInfo musicDto) {
        try {
            Uri musicURI = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+musicDto.getId());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, musicURI);
            mediaPlayer.prepare();
            mediaPlayer.start();

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
                    break ;
                case R.id.next :
                    break ;
            }
        }
    }

}
