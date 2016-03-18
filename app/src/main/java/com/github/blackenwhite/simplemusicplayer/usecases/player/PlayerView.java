package com.github.blackenwhite.simplemusicplayer.usecases.player;

import com.github.blackenwhite.simplemusicplayer.model.Song;
import com.github.blackenwhite.simplemusicplayer.usecases.MainView;

/**
 * Created by alexbel on 3/17/16.
 */
public interface PlayerView {
    void setMainView(MainView mainView);
    void setPlayer(Player listener);
    void setViewContent(Song song);
    void setSeekBarProgress(int progress);
    void setSeekBarMax(int max);
    void showToast(String msg);
}
