package com.example.mp3player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>

        implements OnPersonItemClickListener {
    Bitmap bitmap;
    LinkedList<MusicInfo> items = new LinkedList<MusicInfo>();

    OnPersonItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.musicinfo_layout, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MusicInfo item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {//아이템의 사이즈를 리턴한다
        return items.size();
    }

    public void addItem(MusicInfo item) {//아이템을 추가
        items.add(item);

    }
    public void removeitem(int index) {//아이템을 삭제
        items.remove(index);

    }
    public void setItems(LinkedList<MusicInfo> items) {//
        this.items = items;
    }

    public MusicInfo getItem(int position) {
        return items.get(position);
    }

    public int getPosition(MusicInfo item) {
        items.indexOf(item);
        return items.indexOf(item);
    }

    public void setItem(int position, MusicInfo item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnPersonItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemname;
        ImageView itemImage;

        public ViewHolder(View itemView, final OnPersonItemClickListener listener) {
            super(itemView);


            itemname = itemView.findViewById(R.id.itemName);
            itemImage = itemView.findViewById(R.id.ItemimageView);



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                    return false;
                }
            });
        }
        public void setItem(final MusicInfo item){
                itemname.setText(item.getTitle());
                itemImage.setImageResource(R.drawable.player);
        }

    }

}
