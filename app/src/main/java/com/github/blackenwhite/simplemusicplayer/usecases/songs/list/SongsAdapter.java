package com.github.blackenwhite.simplemusicplayer.usecases.songs.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.blackenwhite.simplemusicplayer.R;
import com.github.blackenwhite.simplemusicplayer.model.Song;
import com.github.blackenwhite.simplemusicplayer.usecases.MainView;

import java.util.List;

/**
 * Created by alexbel on 3/12/16.
 */
public class SongsAdapter extends ArrayAdapter<Song> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private final Context mContext;
    private final List<Song> mSongList;
    private final int mResource;
    private MainView mMainView;

    public SongsAdapter(Context context, int resource, List<Song> songList) {
        super(context, resource, songList);
        mContext = context;
        mSongList = songList;
        mResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(mResource, parent, false);


        // If orientation is landscape, then we need an imageview for cover image
        if (mResource == R.layout.item_list_landscape) {
            ImageView imgView = (ImageView) rowView.findViewById(R.id.cover_view);
            imgView.setImageBitmap(mSongList.get(position).getCoverImg());
        }

        TextView titleView = (TextView) rowView.findViewById(R.id.title_view);
        TextView artistView = (TextView) rowView.findViewById(R.id.artist_view);
        titleView.setText(mSongList.get(position).getTitle());
        artistView.setText(mSongList.get(position).getArtist());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainView.showPlayer(mSongList.get(position));
            }
        });

        return rowView;
    }

    public void setMainView(MainView mainView) {
        mMainView = mainView;
    }
}
