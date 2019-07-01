package com.munin.music.ui.music.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.munin.library.log.Logger;
import com.munin.library.utils.ListUtils;
import com.munin.music.data.MusicData;
import com.munin.music.ui.music.view.MusicTinyControlView;

import java.util.ArrayList;
import java.util.List;

public class MusicTinyItemChangeViewAdapter extends PagerAdapter {
    private static final String TAG = "MusicTinyItemChangeViewAdapter";
    private List<MusicTinyControlView> mViewList = new ArrayList<>();
    private List<MusicData> mDataList = new ArrayList<>();

    public Context mContext;

    public MusicTinyItemChangeViewAdapter(Context context) {
        mContext = context;
    }

    public MusicTinyItemChangeViewAdapter initData(List<MusicData> dataList) {
        if (mContext == null) {
            Logger.e(TAG, "initData: context is null!");
            return this;
        }
        if (ListUtils.isEmpty(dataList)) {
            return this;
        }
        mDataList.addAll(dataList);
        for (MusicData data : dataList) {
            MusicTinyControlView view = new MusicTinyControlView(mContext);
            mViewList.add(view);
        }
        notifyDataSetChanged();
        return this;
    }

    public List<MusicData> getDataList() {
        return mDataList;
    }

    public List<MusicTinyControlView> getViews() {
        return mViewList;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViewList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViewList.get(position);
        container.addView(view);
        return view;
    }
}
