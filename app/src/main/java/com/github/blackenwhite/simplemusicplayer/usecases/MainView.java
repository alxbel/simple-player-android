package com.github.blackenwhite.simplemusicplayer.usecases;

import com.github.blackenwhite.simplemusicplayer.model.Song;

/**
 * Created by alexbel on 3/16/16.
 */
public interface MainView {
    void showPlayer(Song song);
    void updateMode(String mode);
    void showToast(String msg);
}
