package com.munin.music.ui.music.controller;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.munin.library.utils.ViewUtils;

public class MusicTitleController {
    public AppCompatImageView mBackImageView, mShareImageView;
    public AppCompatTextView mTitleView, mAuthorView;

    public void changeTitle(String titleContent, String authorContent) {
        ViewUtils.setText(mTitleView, titleContent);
        ViewUtils.setText(mAuthorView, authorContent);
    }
}
