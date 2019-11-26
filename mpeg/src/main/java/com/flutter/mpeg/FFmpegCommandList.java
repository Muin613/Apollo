package com.flutter.mpeg;

import java.util.ArrayList;

/**
 * 指令集合
 * Created by Super on 2019/4/5.
 */
public class FFmpegCommandList extends ArrayList<String> {

    public FFmpegCommandList() {
        super();
        this.add("ffmpeg");
        this.add("-y");
    }

    /**
     * 清除命令集合
     */
    public void clearCommands() {
        this.clear();
    }

    public FFmpegCommandList append(String s) {
        this.add(s);
        return this;
    }

    public String[] build() {
        String[] command = new String[this.size()];
        for (int i = 0; i < this.size(); i++) {
            command[i] = this.get(i);
        }
        return command;
    }

}
