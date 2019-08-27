package com.munin.music.utils;

import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.munin.library.image.ImageLoadUtils;

public class DataBindingAdapterUtils {
    private static final String TAG = "DataBindingAdapterUtils";
    @BindingAdapter("rvAdapter")
    public static void bindAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        if (adapter == null || recyclerView == null) {
            return;
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("rvLayoutManager")
    public static void bindLayoutManager(RecyclerView recyclerView, RecyclerView.LayoutManager manager) {
        if (manager == null || recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(manager);
    }

    @BindingAdapter("netImg")
    public static void loadImage(ImageView imageView, String url) {
        ImageLoadUtils.loadImg(imageView.getContext(), url, imageView);
    }
}
