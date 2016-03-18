package com.github.blackenwhite.simplemusicplayer.model;

import android.graphics.Bitmap;

/**
 * Created by alexbel on 3/12/16.
 */
public class Song {

    private String title;
    private String artist;
    private String picUrl;
    private String demoUrl;
    private Bitmap coverImg;

    public Song(){}

    public Song(String title, String artist) {
        this.artist = artist;
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by \"%s\" | %s | %s", title, artist, picUrl, demoUrl);
    }

    @Override
    public boolean equals(Object other) {
        return title.equals(((Song)other).getTitle()) &&
                artist.equals(((Song)other).getArtist());
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public Bitmap getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(Bitmap coverImg) {
        this.coverImg = coverImg;
    }

}
