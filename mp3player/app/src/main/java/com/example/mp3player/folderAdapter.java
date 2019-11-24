package com.example.mp3player;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class folderAdapter extends RecyclerView.Adapter<folderAdapter.ViewHolder> {

    private ArrayList<folderInfo> folder = new ArrayList<folderInfo>();
    private OnPersonItemClickListenerd listener =null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.folder_layota, parent, false) ;

        folderAdapter.ViewHolder vh = new folderAdapter.ViewHolder(view) ;

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        folderInfo text = folder.get(position) ;
        holder.title.setText(text.getTitle()) ;
    }

    @Override
    public int getItemCount() {
        return folder.size();
    }

    public folderInfo getItem(int position){
        return folder.get(position);
    }
    public void additem(folderInfo item) {
        folder.add(item) ;
    }

    public void setOnItemClickListener(OnPersonItemClickListenerd listener){
        this.listener = listener;
    }
    public interface OnPersonItemClickListenerd {
        public void onItemClick(folderAdapter.ViewHolder holder, View view, int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.foldertit);
            img = itemView.findViewById(R.id.image4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(listener!=null)
                        {
                            listener.onItemClick(ViewHolder.this,v,pos);
                        }
                    }
                }
            });
        }

    }
}
