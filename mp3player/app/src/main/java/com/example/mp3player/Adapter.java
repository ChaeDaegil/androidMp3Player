package com.example.mp3player;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    ArrayList<MusicInfo> items = new ArrayList<MusicInfo>();
    Context context;
    private OnPersonItemClickListener listener = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.musicinfo_layout, viewGroup, false);
        Adapter.ViewHolder vh = new Adapter.ViewHolder(view) ;

        return vh;
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
    public void setContext(Context context) {//아이템을 추가
        this.context =context;
    }
    public MusicInfo getItem(int position) {
        return items.get(position);
    }

    public int getPosition(MusicInfo item) {
        items.indexOf(item);
        return items.indexOf(item);
    }

    interface OnPersonItemClickListener {
        public void onItemClick(Adapter.ViewHolder holder, View view, int position);
    }
    public void setItem(int position, MusicInfo item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnPersonItemClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemname;
        ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);

            itemname = itemView.findViewById(R.id.itemName);
            itemImage = itemView.findViewById(R.id.ItemimageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(listener!=null)
                        {
                            listener.onItemClick(Adapter.ViewHolder.this,v,pos);
                        }
                    }
                }
            });
        }
        public void setItem(final MusicInfo item){
                itemname.setText(item.getTitle());
                Bitmap bitmap = BitmapFactory.decodeFile(getCoverArtPath(Long.parseLong(item.getAlbumId()),context));
                if(bitmap ==null){
                    itemImage.setImageResource(R.drawable.player);
                }else{
                    itemImage.setImageBitmap(bitmap);
                }
        }
        private String getCoverArtPath(long albumId, Context context) {

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

}
