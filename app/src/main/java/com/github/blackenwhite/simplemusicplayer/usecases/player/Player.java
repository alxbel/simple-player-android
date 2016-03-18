package com.github.blackenwhite.simplemusicplayer.usecases.player;

import android.widget.ImageButton;
import android.widget.SeekBar;

/**
 * Created by alexbel on 3/17/16.
 */
public interface Player {
    void onViewCreated();
    void reset();
    void setOnButtonClickListener(ImageButton button);
    void setOnSeekBarClickListener(SeekBar seekBar);
}
