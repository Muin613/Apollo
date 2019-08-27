package com.munin.music.model.pictureplaza;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.munin.music.data.PictureData;

import java.util.List;

public class PicturePlazaViewModel extends AndroidViewModel {
    MutableLiveData<List<PictureData.RecommendsBean>> mObservableRecommends;

    public PicturePlazaViewModel(@NonNull Application application) {
        super(application);

    }

    public MutableLiveData<List<PictureData.RecommendsBean>> getRecomends() {
        if (mObservableRecommends == null) {
            mObservableRecommends = new MutableLiveData<>();
        }
        return mObservableRecommends;
    }

}
