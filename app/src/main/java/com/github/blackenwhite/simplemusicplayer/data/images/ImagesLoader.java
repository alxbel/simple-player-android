package com.github.blackenwhite.simplemusicplayer.data.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.github.blackenwhite.simplemusicplayer.common.Callback;
import com.github.blackenwhite.simplemusicplayer.model.Song;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by alexbel on 3/14/16.
 */
public class ImagesLoader extends AsyncTask<List<Song>, Void, Void> {

    private Callback mCallback;

    public ImagesLoader(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(List<Song>... songs) {
        for (Song song : songs[0]) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(song.getPicUrl());
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
                bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                song.setCoverImg(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mCallback != null) {
            mCallback.call(null);
        }
    }
}
