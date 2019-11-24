package com.example.mp3player;

import android.provider.BaseColumns;

public class favoriteMemo {
    public favoriteMemo() {}
    public static abstract class MemoEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_ALBUMID = "albumId";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_ARTIST = "artist";

        public static final String COLUMN_ALBUM = "album";

    }
}
