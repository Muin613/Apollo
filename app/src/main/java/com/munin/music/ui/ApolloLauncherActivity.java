package com.munin.music.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.munin.music.R;
import com.munin.music.model.ad.AdModel;
import com.munin.music.ad.AdFactory;
import com.munin.music.ad.controller.base.AdType;
import com.munin.music.ad.controller.base.IAdController;
import com.munin.music.ad.controller.base.IAdListener;
import com.munin.music.ui.main.MainActivity;

/**
 * @author M
 */
public class ApolloLauncherActivity extends BaseActivity {
    private IAdController mAdController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        AdModel model = new AdModel();
        model.setType(AdType.IMG);
        model.setAdUrl("https://hbimg.huabanimg.com/bf4ce7ab7d88412ef24ac3c5d5cd09dc4c3b991ab227-VigMt4_fw658");
        model.setBeginTime("");
        model.setEndTime("");
        model.setShowTime("4000");
        mAdController = AdFactory.createAdController(this, model.getType());
        if (mAdController == null) {
            return;
        }
        mAdController.setAdListener(new IAdListener() {
            @Override
            public void onAdFinish() {
                MainActivity.startMainAct(ApolloLauncherActivity.this);
                finish();
            }
        });
        mAdController.bind(model);
        mAdController.show(findViewById(R.id.flash_body));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdController.destroy();
    }
}
