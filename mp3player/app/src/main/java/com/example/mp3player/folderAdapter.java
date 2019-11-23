package com.example.mp3player;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class folderAdapter extends RecyclerView.Adapter<folderAdapter.ViewHolder>

        implements OnPersonItemClickListenerd  {
    LinkedList<folderInfo> folder = new LinkedList<folderInfo>();
    OnPersonItemClickListenerd listener;

    @NonNull
    @Override
    public folderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.folder_layot, parent, false);

        return new folderAdapter.ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull folderAdapter.ViewHolder viewHolder, int position) {
        folderInfo aa = folder.get(position);
        viewHolder.setItem(aa);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public void addItem(folderInfo item) {//아이템을 추가
        folder.add(item);

    }
    public int getPosition(folderInfo item) {
        folder.indexOf(item);
        return folder.indexOf(item);
    }

    @Override
    public void onItemClick(folderAdapter.ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemname;
        ImageView itemImage;

        public ViewHolder(View itemView, final OnPersonItemClickListenerd listener) {
            super(itemView);
            itemname = itemView.findViewById(R.id.image4);
            itemImage = itemView.findViewById(R.id.foldertit);
        }
        public void setItem(final folderInfo item){
            itemname.setText(item.getTitle());
        }

    }
}
