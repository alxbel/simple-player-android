package com.github.blackenwhite.simplemusicplayer.data;

import com.github.blackenwhite.simplemusicplayer.data.songs.SongsService;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by alexbel on 3/13/16.
 */
public class BaseService {
    private static final String BASE_URL = "https://api-content-beeline.intech-global.com/public/marketplaces/1/tags/4/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static SongsService createSongsService() {
        return retrofit.create(SongsService.class);
    }
}
