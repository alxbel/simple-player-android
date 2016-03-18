package com.github.blackenwhite.simplemusicplayer.usecases.songs.table;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.blackenwhite.simplemusicplayer.data.images.ImagesLoader;
import com.github.blackenwhite.simplemusicplayer.common.Callback;
import com.github.blackenwhite.simplemusicplayer.R;
import com.github.blackenwhite.simplemusicplayer.data.songs.SongsData;
import com.github.blackenwhite.simplemusicplayer.model.Song;
import com.github.blackenwhite.simplemusicplayer.usecases.MainActivity;
import com.github.blackenwhite.simplemusicplayer.usecases.MainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexbel on 3/11/16.
 */
public class TableFragment extends Fragment implements ScrollViewListener {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private MainView mMainView;

    private View mView;
    private ProgressBar mProgressBar;
    private Integer mOrientation;
    private List<Song> mSongList;
    private TableLayout mTableLayout;
    private LayoutInflater mInflater;
    private TableScrollView mTableScrollableContainer;
    private boolean mLoadingMore = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        initProgressBar();
        initSongList();
        initOrientation();
        initTable();
        loadMoreSongs(0);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String mode = getResources().getString(R.string.mode_table);
        mMainView.updateMode(mode);
    }

    @Override
    public void onScrollChanged(TableScrollView scrollView, int x, int y, int oldx, int oldy) {
        // We take the last son in the scrollview
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff == 0 && !mLoadingMore) {
            mLoadingMore = true;
            loadMoreSongs(mSongList.size());
        }
    }

    public void setMainView(MainView view) {
        mMainView = view;
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        if (mInflater == null) {
            mInflater = inflater;
        }
        if (mView == null) {
            mView = mInflater.inflate(R.layout.table, container, false);
        }
    }

    private void initSongList() {
        if (mSongList == null) {
            mSongList = new ArrayList<>();
        }
    }

    private void initOrientation() {
        final String orientTag = MainActivity.getContext().getResources().getString(R.string.tag_orientation);
        mOrientation = getArguments().getInt(orientTag);
    }

    private void initTable() {
        if (mTableLayout == null) {
            if (mView != null) {
                mTableLayout = (TableLayout) mView.findViewById(R.id.table);
            }
        }

        if (mTableScrollableContainer == null) {
            if (mView != null) {
                mTableScrollableContainer = (TableScrollView) mView.findViewById(R.id.table_container);
                mTableScrollableContainer.setScrollViewListener(this);
            }
        }
    }

    private void initProgressBar() {
        if (mProgressBar == null) {
            if (mView != null) {
                mProgressBar = (ProgressBar) mView.findViewById(R.id.progress_bar);
            }
        }
    }


    // Business logic
    private void loadMoreSongs(int from) {
        mProgressBar.setVisibility(View.VISIBLE);
        SongsData.getInstance().getSongs(from, new Callback<List<Song>>() {
            @Override
            public void call(List<Song> songs) {
                mLoadingMore = false;
                final List<Song> newSongs = SongsData.getInstance().filterIncoming(mSongList, songs);
                mSongList.addAll(newSongs);

                if (mOrientation.equals(Configuration.ORIENTATION_PORTRAIT)) {
                    mProgressBar.setVisibility(View.GONE);
                    addRows(newSongs);
                } else {
                    new ImagesLoader(new Callback<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            addRows(newSongs);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }).execute(newSongs);
                }
            }
        });
    }

    private void addRows(final List<Song> songs) {
        for (final Song song : songs) {
            View tableRow;
            if (mOrientation.equals(Configuration.ORIENTATION_LANDSCAPE)) {
                tableRow = mInflater.inflate(R.layout.item_table_landscape, null, false);
                ImageView imgView = (ImageView) tableRow.findViewById(R.id.cover_view);
                imgView.setImageBitmap(song.getCoverImg());
            } else {
                tableRow = mInflater.inflate(R.layout.item_table_portrait, null, false);
            }
            TextView titleView = (TextView) tableRow.findViewById(R.id.title_view);
            titleView.setText(song.getTitle());
            TextView artistView = (TextView) tableRow.findViewById(R.id.artist_view);
            artistView.setText(song.getArtist());

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMainView.showPlayer(song);
                }
            });
            mTableLayout.addView(tableRow);
        }
    }
}