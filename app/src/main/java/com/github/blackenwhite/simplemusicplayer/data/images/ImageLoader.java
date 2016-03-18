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

/**
 * Created by alexbel on 3/14/16.
 */
public class ImageLoader extends AsyncTask<Song, Void, Void> {

    private Callback mCallback;

    public ImageLoader(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(Song... song) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(song[0].getPicUrl());
            bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
            bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            song[0].setCoverImg(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mCallback.call(null);
    }
}
