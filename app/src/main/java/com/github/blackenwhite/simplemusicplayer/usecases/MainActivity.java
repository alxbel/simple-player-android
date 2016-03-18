package com.github.blackenwhite.simplemusicplayer.usecases;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.blackenwhite.simplemusicplayer.R;
import com.github.blackenwhite.simplemusicplayer.common.Callback;
import com.github.blackenwhite.simplemusicplayer.data.images.ImageLoader;
import com.github.blackenwhite.simplemusicplayer.model.Song;
import com.github.blackenwhite.simplemusicplayer.usecases.player.PlayerImpl;
import com.github.blackenwhite.simplemusicplayer.usecases.player.PlayerViewImpl;
import com.github.blackenwhite.simplemusicplayer.usecases.songs.list.ListFragment;
import com.github.blackenwhite.simplemusicplayer.usecases.songs.table.TableFragment;

public class MainActivity extends AppCompatActivity implements MainView {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private Toast mToast;

    private static Context mContext;
    private String mCurrentMode;
    private Song mCurrentSong;
    private Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mCurrentMode = getResources().getString(R.string.mode_list);
        mBundle = new Bundle();

        initActionbar();

        // Check that the activity is using the layout version with
        // the songs_container FrameLayout
        if (findViewById(R.id.main_frame) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            setupOrientation();
            setupFragment();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getResources().getConfiguration().updateFrom(newConfig);
        setupOrientation();
        setupFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_as_table:
                mCurrentMode = getResources().getString(R.string.mode_table);
                break;

            case R.id.action_as_list:
                mCurrentMode = getResources().getString(R.string.mode_list);
                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        setupFragment();
        return true;
    }

    @Override
    public void showPlayer(Song song) {
        setActionbarTitle(mCurrentMode);
        mCurrentSong = song;

        Callback<Void> setupPlayer = new Callback<Void>() {
            @Override
            public void call(Void aVoid) {
                PlayerViewImpl playerViewImpl = new PlayerViewImpl();
                playerViewImpl.setMainView(MainActivity.this);
                PlayerImpl playerImpl = new PlayerImpl(playerViewImpl, mCurrentSong);
                playerViewImpl.setPlayer(playerImpl);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, playerViewImpl);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        if (mCurrentSong.getCoverImg() == null) {
            new ImageLoader(setupPlayer).execute(mCurrentSong);
        } else {
            setupPlayer.call(null);
        }
    }

    @Override
    public void updateMode(String mode) {
        mCurrentMode = mode;
        setActionbarTitle(mode);
    }

    @Override
    public void showToast(String msg) {
        try {
            mToast.getView().isShown();
            mToast.setText(msg);
        } catch (Exception e) {
            mToast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static Context getContext() {
        return mContext;
    }

    private void initActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setActionbarTitle(String mode) {
        String title = String.format("%s ( %s )", getResources().getString(R.string.app_name), mode);
        getSupportActionBar().setTitle(title);
    }

    private void setupOrientation() {
        setupOrientation(getResources().getConfiguration().orientation);
    }

    private void setupOrientation(final int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBundle.putInt(getResources().getString(R.string.tag_orientation), Configuration.ORIENTATION_LANDSCAPE);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mBundle.putInt(getResources().getString(R.string.tag_orientation), Configuration.ORIENTATION_PORTRAIT);
        }
    }

    private void setupFragment() {
        if (mCurrentMode.equals(getResources().getString(R.string.mode_table))) {
            setupTableFragment();
        } else if (mCurrentMode.equals(getResources().getString(R.string.mode_list))) {
            setupListFragment();
        } else if(mCurrentMode.equals(getResources().getString(R.string.mode_player))) {
            showPlayer(mCurrentSong);
        }
    }

    private void setupTableFragment() {
        TableFragment fragment = new TableFragment();
        fragment.setArguments(mBundle);
        fragment.setMainView(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupListFragment() {
        ListFragment fragment = new ListFragment();
        fragment.setMainView(this);
        fragment.setArguments(mBundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
