package com.github.blackenwhite.simplemusicplayer.usecases.player;

import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.github.blackenwhite.simplemusicplayer.R;
import com.github.blackenwhite.simplemusicplayer.model.Song;

import java.io.IOException;

/**
 * Created by alexbel on 3/17/16.
 */
public class PlayerImpl implements Player {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private MediaPlayer mMediaPlayer;
    private double mStartTime = 0;
    private double mFinalTime = 0;
    private Handler mHandler = new Handler();

    private Song mSong;
    private PlayerView mPlayerView;

    public PlayerImpl(PlayerView playerView, Song song) {
        mPlayerView = playerView;
        mSong = song;

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mSong.getDemoUrl());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated() {
        mPlayerView.setViewContent(mSong);
    }

    @Override
    public void reset() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void setOnButtonClickListener(ImageButton button) {
        switch (button.getId()) {
            case R.id.btn_play:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mMediaPlayer.isPlaying()) {
                            mMediaPlayer.start();
                            mFinalTime = mMediaPlayer.getDuration();
                            mStartTime = mMediaPlayer.getCurrentPosition();
                            mPlayerView.setSeekBarMax((int) mFinalTime);
                            mPlayerView.setSeekBarProgress((int)mStartTime);
                            mPlayerView.showToast("Playing");
                            mHandler.postDelayed(updateSongTime, 100);
                        }
                    }
                });
                break;
            case R.id.btn_stop:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMediaPlayer.reset();
                        try {
                            mMediaPlayer.setDataSource(mSong.getDemoUrl());
                            mMediaPlayer.prepare();
                            mPlayerView.showToast("Stopped");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.btn_pause:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMediaPlayer.pause();
                        mPlayerView.showToast("Paused");
                    }
                });
                break;
        }
    }

    @Override
    public void setOnSeekBarClickListener(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (!mMediaPlayer.isPlaying()) {
                        seekBar.setProgress(0);
                        mPlayerView.showToast("Press `play`");
                        return;
                    }
                    mMediaPlayer.seekTo(progress);
                    Integer secs = progress / 1000;
                    String msg = String.format("@%ds", secs);
                    mPlayerView.showToast(msg);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private Runnable updateSongTime = new Runnable() {
        public void run() {
            mStartTime = mMediaPlayer.getCurrentPosition();
            mPlayerView.setSeekBarProgress((int)mStartTime);
            mHandler.postDelayed(this, 100);
        }
    };
}
