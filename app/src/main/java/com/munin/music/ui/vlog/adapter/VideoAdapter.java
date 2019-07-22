package com.munin.music.ui.vlog.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.munin.music.ui.common.VideoPlayerView;

import java.util.Arrays;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoVH> {

    private List<String> mData = Arrays.asList(new String[]{"标题一", "标题二"});

    @NonNull
    @Override
    public VideoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoVH(new VideoPlayerView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoVH holder, int position) {
//        ((VideoPlayerView) holder.itemView).setTitle(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class VideoVH extends RecyclerView.ViewHolder {
        public VideoVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
