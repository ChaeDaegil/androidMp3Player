package com.example.mp3player;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class CategoriActivity extends AppCompatActivity {
    RecyclerView musicList;
    folderAdapter folderAdp;
    Context context;
    ArrayList<MusicInfo> albums = new ArrayList<MusicInfo>();
    boolean check = false;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categori_layout);
        Intent intent = getIntent();
        folderAdp = new folderAdapter();
        context = getApplicationContext();
        if(intent != null){
            String Bigfolder = intent.getExtras().getString("name");
            getMusicList(Bigfolder);
        }

    }
    public void getMusicList(String bigfolder){

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
            folderInfo folder = new folderInfo();
            musicList = (RecyclerView)findViewById(R.id.list) ;
            String nameChck = "";
            musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            musicDto.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));


            if(bigfolder.equals("앨범")){
                nameChck = musicDto.getAlbum();
            }
            else if(bigfolder.equals("아티스트")){
                nameChck = musicDto.getArtist();
            }

            for(int i=0;i<albums.size();i++){

                String albumid = "";
                if(bigfolder.equals("앨범")){
                    albumid = albums.get(i).getAlbum();
                }
                else if(bigfolder.equals("아티스트")){
                    albumid = albums.get(i).getArtist();
                }

                if(albumid.equals(nameChck)){
                    check = true;
                    break;
                }
            }
            if(check){
                check = false;
            }else{
                albums.add(musicDto);
                folder.setTitle(nameChck);
                folder.setAlbum_id(musicDto.getAlbumId());
                folderAdp.additem(folder);
            }

        }
        folderAdp.setOnItemClickListener(new folderAdapter.OnPersonItemClickListenerd() {
            @Override
            public void onItemClick(folderAdapter.ViewHolder holder, View view, int position) {

                intent = new Intent(context, MusicListActivity.class);
                intent.putExtra("list",albums);

                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,1);
        musicList.setLayoutManager(gridLayoutManager);

        musicList.setAdapter(folderAdp);
        System.out.println("사이즈 " + albums.size());
        cursor.close();

    }
}
