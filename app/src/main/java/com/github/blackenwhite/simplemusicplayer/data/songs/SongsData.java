package com.github.blackenwhite.simplemusicplayer.data.songs;

import android.util.Log;

import com.github.blackenwhite.simplemusicplayer.R;
import com.github.blackenwhite.simplemusicplayer.data.BaseService;
import com.github.blackenwhite.simplemusicplayer.model.Song;
import com.github.blackenwhite.simplemusicplayer.model.SongsContainer;
import com.github.blackenwhite.simplemusicplayer.usecases.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by alexbel on 3/13/16.
 */
public class SongsData {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final SongsService service = BaseService.createSongsService();
    private static SongsData instance = null;

    private SongsData(){}

    public static final SongsData getInstance() {
        if (instance == null) {
            instance = new SongsData();
        }
        return instance;
    }

    public List<Song> getSongs(final int from, final com.github.blackenwhite.simplemusicplayer.common.Callback<List<Song>> viewCallback) {
        final List<Song> songs = new ArrayList<>();

        final int limit = MainActivity.getContext().getResources().getInteger(R.integer.songs_limit);
        Call<SongsContainer> call = service.getSongs(limit, from);
        call.enqueue(new Callback<SongsContainer>() {
            @Override
            public void onResponse(Response<SongsContainer> response, Retrofit retrofit) {
                songs.addAll(response.body().getMelodies());
                viewCallback.call(songs);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
            }
        });

        return songs;
    }

    public List<Song> filterIncoming(final List<Song> storage, final List<Song> incoming) {
        final List<Song> filtered = new ArrayList<>();
        for (Song song : incoming) {
            if (storage.contains(song)) continue;
            filtered.add(song);
        }
        return filtered;
    }
}
