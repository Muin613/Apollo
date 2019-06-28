package com.munin.music.ad.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import com.munin.library.common.WeakHandler;
import com.munin.library.image.ImageLoadUtils;
import com.munin.library.utils.StringUtils;
import com.munin.library.utils.ViewUtils;
import com.munin.music.R;
import com.munin.music.model.ad.AdModel;
import com.munin.music.ad.controller.base.BaseAdController;

/**
 * @author M
 */
public class ImgAdController extends BaseAdController {

    private AppCompatImageView mImgView;


    public ImgAdController(Context context) {
        super(context);
        View view = inflate(context, R.layout.layout_img_ad, null);
        this.mImgView = view.findViewById(R.id.ad_img_view);
    }

    @Override
    public void bind(AdModel model) {
        super.bind(model);
        if (mModel == null) {
            return;
        }
        if (StringUtils.isNotBlank(model.getShowTime())) {
            try {
                setContinueTime(Integer.valueOf(model.getShowTime()));
            } catch (Exception e) {
                setContinueTime(getDefaultContinueTime());
            }
        }
    }

    @Override
    public void show(ViewGroup showView) {
        if (mModel == null) {
            return;
        }
        ViewUtils.addView(showView, mImgView);
        ImageLoadUtils.loadImg(mContext, mModel.getAdUrl(), mImgView);
        super.show(showView);
    }

    @Override
    public void onAdFinish() {
        super.onAdFinish();
        ViewUtils.removeView(mImgView);
        ImageLoadUtils.clearImage(mContext, mImgView);
    }


    @Override
    public Runnable getShowRunnable() {
        return null;
    }

    @Override
    public int getDefaultContinueTime() {
        return 3000;
    }

    @Override
    public WeakHandler getWeakHandler() {
        return null;
    }

    @Override
    public void destroy() {
        super.onAdFinish();
    }
}
