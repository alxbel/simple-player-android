package com.github.blackenwhite.simplemusicplayer.usecases.songs.table;

/**
 * Created by alexbel on 3/16/16.
 */
public interface ScrollViewListener {
    void onScrollChanged(TableScrollView scrollView,
                         int x, int y, int oldx, int oldy);
}
