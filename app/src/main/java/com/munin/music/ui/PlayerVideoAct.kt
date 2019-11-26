package com.munin.music.ui

import android.os.Bundle
import com.munin.music.R
import kotlinx.android.synthetic.main.act_ffmpeg_video.*

class PlayerVideoAct : BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_ffmpeg_video)
        video_view.play("http://video.maxxipoint.com/Video/Family-60S-0411.mp4")
    }
}
