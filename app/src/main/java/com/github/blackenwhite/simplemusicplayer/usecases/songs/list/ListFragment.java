package com.github.blackenwhite.simplemusicplayer.usecases.songs.list;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

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
 * Created by alexbel on 3/13/16.
 */
public class ListFragment extends Fragment {

    private ProgressBar mProgressBar;
    private boolean mLoadingMore = false;

    private SongsAdapter mAdapter;
    private List<Song> mSongList;
    private  ListView mListView;
    private View mView;
    private LayoutInflater mInflater;

    private int mOrientation;
    private int mRowLayout;
    private MainView mMainView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        initProgressBar();
        initOrientation();
        initSongList();
        initListView();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String mode = getResources().getString(R.string.mode_list);
        mMainView.updateMode(mode);
        mAdapter.setMainView(mMainView);
    }

    public void setMainView(MainView view) {
        mMainView = view;
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        if (mInflater == null) {
            mInflater = inflater;
        }
        if (mView == null) {
            mView = mInflater.inflate(R.layout.list, container, false);
        }
    }

    private void initProgressBar() {
        if (mProgressBar == null) {
            if (mView != null) {
                mProgressBar = (ProgressBar) mView.findViewById(R.id.progress_bar);
            }
        }
    }

    private void initOrientation() {
        final String orientTag = MainActivity.getContext().getResources().getString(R.string.tag_orientation);
        mOrientation = getArguments().getInt(orientTag);
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRowLayout = R.layout.item_list_landscape;
        } else {
            mRowLayout = R.layout.item_list_portrait;
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initSongList() {
        if (mSongList == null) {
            mSongList = new ArrayList<>();
            mAdapter = new SongsAdapter(getActivity(), mRowLayout, mSongList);
        }
    }

    private void initListView() {
        if (mListView == null) {
            if (mView != null) {
                mListView = (ListView) mView.findViewById(R.id.list);
                mListView.setAdapter(mAdapter);
                mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        int lastInScreen = firstVisibleItem + visibleItemCount;
                        if ((lastInScreen == totalItemCount) && !(mLoadingMore)) {
                            loadMoreSongs(lastInScreen);
                        }
                    }
                });
            }
        }
    }

    // Logic
    private void loadMoreSongs(int from) {
        mLoadingMore = true;
        mProgressBar.setVisibility(View.VISIBLE);
        SongsData.getInstance().getSongs(from, new Callback<List<Song>>() {
            @Override
            public void call(List<Song> songs) {
                mLoadingMore = false;
                final List<Song> newSongs = SongsData.getInstance().filterIncoming(mSongList, songs);
                mSongList.addAll(newSongs);

                if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    new ImagesLoader(new Callback<Void> (){
                        @Override
                        public void call(Void aVoid) {
                            mProgressBar.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    }).execute(newSongs);
                } else {
                    mAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
