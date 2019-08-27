package com.munin.library.mvvm;

import android.content.Context;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BindingViewAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {
    protected final LayoutInflater mLayoutInflater;
    protected List<T> mData = new ArrayList<>();
    private SparseIntArray mViewType = new SparseIntArray();

    public BindingViewAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setLayoutAndViewType();
    }

    @Override
    public int getItemViewType(int position) {
        return bindViewType(position, mData.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        bindViewHolder(holder, position, getItemViewType(position), mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(mLayoutInflater, mViewType.get(viewType), parent, false);
        return new BindingViewHolder(dataBinding);
    }

    public void binData(List<T> data) {
        mData = data;
    }

    protected abstract void setLayoutAndViewType();

    public void bindLayoutByType(int type, int layoutId) {
        mViewType.put(type, layoutId);
    }

    public abstract int bindViewType(int position, @NonNull T data);

    public abstract void bindViewHolder(@NonNull BindingViewHolder holder, int position, int type, @NonNull T data);
}