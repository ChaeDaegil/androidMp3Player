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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);
        musicview = (RecyclerView)findViewById(R.id.ReMusicView);
        intent = getIntent();
        albums = (ArrayList<MusicInfo>) intent.getSerializableExtra("list");
        adapter = new Adapter();
        folderadp= new folderAdapter();

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
                startActivity(intent);
            }
        });
        adapter.setContext(getApplication());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        musicview.setLayoutManager(gridLayoutManager);
        musicview.setAdapter(adapter);
    }

}
