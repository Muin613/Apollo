package com.munin.music.ui.music.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.munin.library.view.widget.AspectImageView;
import com.munin.music.R;
import com.munin.music.data.MusicData;


/**
 * @author M
 */
public class MusicItemView extends FrameLayout {
    private MusicData mData;
    private AspectImageView mCoverImageView;

    public MusicItemView(@NonNull Context context) {
        this(context, null);
    }

    public MusicItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, null);
        inflate(getContext(), R.layout.layout_music_player_cover, this);
        mCoverImageView = findViewById(R.id.music_cover_disc);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public MusicItemView setData(MusicData data) {
        mData = data;
        return this;
    }

    public AspectImageView getCoverImage() {
        return mCoverImageView;
    }
}
