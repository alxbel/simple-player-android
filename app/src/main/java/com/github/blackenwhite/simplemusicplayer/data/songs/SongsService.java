package com.github.blackenwhite.simplemusicplayer.data.songs;

import com.github.blackenwhite.simplemusicplayer.model.SongsContainer;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by alexbel on 3/13/16.
 */
public interface SongsService {
    @GET("melodies")
    Call<SongsContainer> getSongs(@Query("limit") Integer limit, @Query("from") Integer from);
}
