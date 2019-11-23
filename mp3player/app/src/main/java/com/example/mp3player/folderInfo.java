package com.example.mp3player;

public class folderInfo {

    String album_id;
    String title;
    public folderInfo(){

    }
    public folderInfo( String albumId, String title) {
        this.album_id = albumId;
        this.title = title;
    }
    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
