package com.github.blackenwhite.simplemusicplayer.usecases.songs.table;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by alexbel on 3/16/16.
 */
public class TableScrollView extends ScrollView {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private ScrollViewListener scrollViewListener = null;

    public TableScrollView(Context context) {
        super(context);
    }

    public TableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
