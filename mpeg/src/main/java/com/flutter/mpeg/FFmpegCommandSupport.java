package com.flutter.mpeg;

/**
 * RxFFmpeg 命令支持
 * Created by Super on 2019/4/5.
 */
public class FFmpegCommandSupport {

    public static String[] getBoxblur() {
        FFmpegCommandList cmdlist = new FFmpegCommandList();
        cmdlist.append("-i");
        cmdlist.append("/storage/emulated/0/1/input.mp4");
        cmdlist.append("-vf");
        cmdlist.append("boxblur=5:1");
        cmdlist.append("-preset");
        cmdlist.append("superfast");
        cmdlist.append("/storage/emulated/0/1/result.mp4");
        return cmdlist.build();
    }


}
