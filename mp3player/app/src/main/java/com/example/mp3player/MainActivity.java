package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView musicList = null;
    Adapter adapter;
    Context context = this;
    ArrayList<MusicInfo> albums;
    boolean check =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new Adapter();
        albums = new ArrayList<MusicInfo>();
        getMusicList();
    }
    public void getMusicList(){

        //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);


            while(cursor.moveToNext()){
                MusicInfo musicDto = new MusicInfo();
                musicList = (RecyclerView)findViewById(R.id.musicList) ;
                musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                adapter.addItem(musicDto);

                if(albums.isEmpty()){
                    albums.add(musicDto);
                }

                for(int i=0;i<albums.size();i++){
                    String albumid = albums.get(i).getAlbumId();
                    if(albumid.equals(musicDto.getAlbumId())){
                        check = true;
                        break;
                    }
                }
                if(check){
                    check = false;
                }else{
                    albums.add(musicDto);
                }
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
                musicList.setLayoutManager(gridLayoutManager);
                musicList.setAdapter(adapter);
            }
        System.out.println("사이즈 " + albums.size());
            cursor.close();

    }

}
