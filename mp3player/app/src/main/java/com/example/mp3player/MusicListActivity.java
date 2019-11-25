package com.example.mp3player;

import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class MusicListActivity extends AppCompatActivity {
    RecyclerView musicview;
    Intent intent;
    Adapter adapter;
    folderAdapter folderadp;
    ArrayList<MusicInfo> albums = new ArrayList<MusicInfo>();
    Context context = this;
    String foldername;
    String foldertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);
        musicview = (RecyclerView)findViewById(R.id.ReMusicView);
        intent = getIntent();
        albums = (ArrayList<MusicInfo>) intent.getSerializableExtra("list");
        foldername = intent.getExtras().getString("folderName");// 앨범이나 아티스트의 이름
        foldertype = intent.getExtras().getString("folderType");// 앨범 아티스트 둘중 하나
        adapter = new Adapter();
        folderadp= new folderAdapter();
        if(albums != null){
            getMusicList();
        }
        else if(foldername != null){
            getMusicListfolder(foldertype,foldername);
        }

    }
    public void getMusicList(){
        for(int a=0 ; a < albums.size() ; a++) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setId(albums.get(a).getId());
            musicInfo.setTitle(albums.get(a).getTitle());
            musicInfo.setAlbum(albums.get(a).getAlbum());
            musicInfo.setAlbumId(albums.get(a).getAlbumId());
            musicInfo.setArtist(albums.get(a).getArtist());
            adapter.addItem(musicInfo);
        }

        adapter.setOnItemClickListener(new Adapter.OnPersonItemClickListener() {
            @Override
            public void onItemClick(Adapter.ViewHolder holder, View view, int position) {
                MusicInfo musicInfo= adapter.getItem(position);
                intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("music",musicInfo);
                intent.putExtra("musiclist", albums); /*송신*/
                startActivity(intent);
            }
        });
        adapter.setContext(getApplication());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        musicview.setLayoutManager(gridLayoutManager);
        musicview.setAdapter(adapter);
    }
    public void getMusicListfolder(String musictype,String MusictypeName){

        //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM
        };

        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

        while(cursor.moveToNext()){
            MusicInfo musicDto = new MusicInfo();

            String nameChck = "";
            musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            musicDto.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));


            if(musictype.equals("앨범")){
                nameChck = musicDto.getAlbum();
                foldername = "앨범";
            }
            else if(musictype.equals("아티스트")){
                nameChck = musicDto.getArtist();
                foldername ="아티스트";
            }

            if(MusictypeName.equals(nameChck)){
                adapter.addItem(musicDto);
            }
        }
        adapter.setOnItemClickListener(new Adapter.OnPersonItemClickListener() {
            @Override
            public void onItemClick(Adapter.ViewHolder holder, View view, int position) {
                MusicInfo musicInfo= adapter.getItem(position);
                intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("music",musicInfo);
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3);
        adapter.setContext(getApplication());
        musicview.setLayoutManager(gridLayoutManager);
        musicview.setAdapter(adapter);
        cursor.close();

    }
}
