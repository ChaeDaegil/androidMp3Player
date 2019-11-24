package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    RecyclerView musicList = null;
    folderAdapter folderadp = new folderAdapter();
    ArrayList<MusicInfo> albums = new ArrayList<MusicInfo>();
    Intent intent;
    Context context;
    String[] fold = {"전체","앨범","아티스트"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicList = (RecyclerView)findViewById(R.id.musicList);
        context = getApplicationContext();
        getMusicList();

        for(int a=0 ; a < fold.length ; a++) {
            folderInfo folder = new folderInfo();
            folder.setTitle(fold[a]);
            folderadp.additem(folder);
        }

        musicList.setLayoutManager(new LinearLayoutManager(this)) ;
        musicList.setAdapter(folderadp);
        folderadp.setOnItemClickListener(new folderAdapter.OnPersonItemClickListenerd() {
            @Override
            public void onItemClick(folderAdapter.ViewHolder holder, View view, int position) {
                folderInfo name = folderadp.getItem(position);
                if(name.getTitle().equals("전체")){
                    intent = new Intent(context, MusicListActivity.class);
                    intent.putExtra("list", albums); /*송신*/

                    startActivity(intent);
                }
                else {
                    intent = new Intent(context, CategoriActivity.class);
                    intent.putExtra("name", name.getTitle()); /*송신*/

                    startActivity(intent);
                }
            }
        });

    }
    public void getMusicList(){

        //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM
        };

        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

        while(cursor.moveToNext()){
            MusicInfo musicDto = new MusicInfo();
            musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            musicDto.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            System.out.println(musicDto.getVloume());
            albums.add(musicDto);
        }
    }
}
