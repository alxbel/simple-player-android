package com.github.blackenwhite.simplemusicplayer.usecases.player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.blackenwhite.simplemusicplayer.R;
import com.github.blackenwhite.simplemusicplayer.model.Song;
import com.github.blackenwhite.simplemusicplayer.usecases.MainView;

/**
 * Created by alexbel on 3/16/16.
 */
public class PlayerViewImpl extends Fragment implements PlayerView {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private MainView mMainView;

    private Player mPlayer;

    private View mView;
    private ImageView mSongCover;
    private TextView mSongTitle;
    private TextView mSongArtist;
    private ImageButton mPlayBtn;
    private ImageButton mStopBtn;
    private ImageButton mPauseBtn;
    private SeekBar mSeekBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        initContent();
        initButtons();
        initSeekBar();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPlayer.onViewCreated();
        final String mode = getResources().getString(R.string.mode_player);
        mMainView.updateMode(mode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlayer.reset();
    }

    @Override
    public void setMainView(MainView mainView) {
        mMainView = mainView;
    }

    public void setPlayer(Player player) {
        mPlayer = player;
    }

    @Override
    public void setViewContent(Song song) {
        mSongCover.setImageBitmap(song.getCoverImg());
        mSongTitle.setText(song.getTitle());
        mSongArtist.setText(song.getArtist());
    }

    @Override
    public void setSeekBarProgress(int progress) {
        if (mSeekBar != null) {
            mSeekBar.setProgress(progress);
        }
    }

    @Override
    public void setSeekBarMax(int max) {
        if (mSeekBar != null) {
            mSeekBar.setMax(max);
        }
    }

    @Override
    public void showToast(String msg) {
        mMainView.showToast(msg);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.player, container, false);
        }
    }

    private void initContent() {
        if (mView != null) {
            if (mSongCover == null) {
                mSongCover = (ImageView) mView.findViewById(R.id.song_cover);
            }
            if (mSongTitle == null) {
                mSongTitle = (TextView) mView.findViewById(R.id.song_title);
            }
            if (mSongArtist == null) {
                mSongArtist = (TextView) mView.findViewById(R.id.song_artist);
            }
        }
    }

    private void initButtons() {
        if (mView != null) {
            if (mPlayBtn == null) {
                mPlayBtn = (ImageButton) mView.findViewById(R.id.btn_play);
                mPlayer.setOnButtonClickListener(mPlayBtn);
            }
            if (mPauseBtn == null) {
                mPauseBtn = (ImageButton) mView.findViewById(R.id.btn_pause);
                mPlayer.setOnButtonClickListener(mPauseBtn);
            }
            if (mStopBtn == null) {
                mStopBtn = (ImageButton) mView.findViewById(R.id.btn_stop);
                mPlayer.setOnButtonClickListener(mStopBtn);
            }
        }
    }

    private void initSeekBar() {
        if (mView != null) {
            if (mSeekBar == null) {
                mSeekBar = (SeekBar) mView.findViewById(R.id.song_seekbar);
                mPlayer.setOnSeekBarClickListener(mSeekBar);
            }
        }
    }
}
