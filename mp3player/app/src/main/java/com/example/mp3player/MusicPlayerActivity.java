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
        intent = getIntent();
        musicInfo = (MusicInfo) intent.getSerializableExtra("music");
        playMusic(musicInfo);

    }

    public void playMusic(MusicInfo musicDto) {
        try {
            mediaPlayer = new MediaPlayer();
            Uri musicURI = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+musicDto.getId());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, musicURI);
            mediaPlayer.prepare();
            mediaPlayer.start();

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
